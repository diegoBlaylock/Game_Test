package engine.timer;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class RecurringTask<T> extends Task<T> {
	
	Task<T> t;
	
	long interval;
	
	long timeout;
	
	public RecurringTask(float interval, Task<T> task) {
		this.interval = (long)(interval * 1000);
		this.t = task;
		
		this.timeout = System.currentTimeMillis() + this.interval;
	}
	
	public RecurringTask(float interval, Callable<T> task) {
		this(interval, new TaskImp<T>(task));
	}
	
	public void start() {
		if(!shouldCancel) {
			running = true;
			
			timeout = System.currentTimeMillis()+this.interval;
			result.result = this.call();
			
			
			for(Function<T,?> callback : callbacks) {
				
				
				callback.apply(result.result);
				
				if(shouldCancel)
					return;
			}
			
		}
	}
	
	@Override
	public boolean isDeferred() {
		// TODO Auto-generated method stub
		return System.currentTimeMillis() < timeout;
	}

	@Override
	public T call() {
		// TODO Auto-generated method stub
		try {
			return t.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
}
