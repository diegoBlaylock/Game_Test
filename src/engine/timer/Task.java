package engine.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public abstract class Task<T> implements Callable<T>, ITask{
	
	protected boolean shouldCancel;
	protected boolean running;
	protected boolean completed;
	protected Result<T> result = new Result<T>(this);

	protected List<Function<T, ?>> callbacks = new ArrayList<Function<T, ?>>(4);
	
	@Override
	public abstract T call();
	
	public void start() {
		if(!shouldCancel) {
			running = true;
			
			result.result = this.call();
			
			cancel();
			for(Function<T,?> callback : callbacks) {
				
				
				callback.apply(result.result);
				
				if(shouldCancel)
					return;
			}
			
		}
	}
	
	public boolean cancel() {
		shouldCancel = false;
		running = false;
		completed=true;
		result.isolate();
		
		return completed;
	}
	
	public Result<T> getResult() {
		return result;	
	}
	
	@Override
	public boolean isDone() {
		return completed;
	}
	
	
	protected static class Result<T> implements Future<T>{

		protected Task<T> task;
		
		protected T result = null;
		
		Result(Task<T> t){
			task = t;
			
			if(t != null && !t.completed) {
				task = t;
			}
		}
		
		protected void isolate() {
			task = null;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			if(isCancelled()) {
				return true;
			}
			
			this.task.shouldCancel = true;
			return isCancelled();
		}

		@Override
		public boolean isCancelled() {
			if(task == null) {
				return true;
			}
			
			return task.shouldCancel && task.completed;
		}

		@Override
		public boolean isDone() {
			if(task == null) {
				return true;
			}
			
			return task.completed;
		}

		@Override
		public T get() throws InterruptedException, ExecutionException {
			// TODO Auto-generated method stub
			return result;
		}

		@Override
		public T get(long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException, TimeoutException {
			long time = unit.toMillis(timeout)+System.currentTimeMillis();
			
			while(System.currentTimeMillis() < time) {
				if(this.isDone()) {
					return result;
				}
			}
			throw new TimeoutException("Task timed out");
		}
		
		public T await() {
			while(!this.isDone()) {}
			return this.result;
			
		}
		
	}
}
