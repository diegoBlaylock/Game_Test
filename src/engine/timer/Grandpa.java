package engine.timer;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.Callable;

public class Grandpa {

	public enum state {
		IDLE,
		RUNNING,
		PAUSED
	}
	
	private static Thread running = new Thread(new Loop());
	private static state _state = state.IDLE;
	
	private static LinkedList<ITask> tasks = new LinkedList<ITask>();
	private static LinkedList<ITask> deferred = new LinkedList<ITask>();
	private static LinkedList<ITask> added = new LinkedList<ITask>();
	
	public static <T> void addTask(Task<T> t) {
		synchronized(added) {
			added.add(t);
		}
	}
	
	public static void start() {
		_state = state.RUNNING;
		running.start();
		
	}
	
	public static void pause() {
		_state = state.PAUSED;
	}
	
	public static void terminate(boolean force) {
		_state = state.IDLE;
		if(force) {
			running.interrupt();
		}
	}
	
	public static <T> Task<T> schedule(float time, Task<T> t) {
		Task<T> task = new DelayWrapperTask<T>(time, t);
		addTask(task);
		return task;
	}
	
	public static <T> Task<T> schedule(float time, Callable<T> t) {
		Task<T> task = new DelayWrapperTask<T>(time, new TaskImp<T>(t));
		addTask(task);
		return task;
	}
	
	public static <T> Task<T> timer(float time, Callable<T> t) {
		Task<T> task = new RecurringTask<T>(time, t);
		addTask(task);
		return task;
	}
	
	
	private static class Loop implements Runnable{
		@Override
		public void run() {
			
			while(_state != state.IDLE) {
				ListIterator<ITask> iter = deferred.listIterator();
				
				while(iter.hasNext()) {
					ITask task = iter.next();
					if(! task.isDeferred()) {
						iter.remove();
						tasks.add(task);
					}
					
					
				}
				
				
				iter = tasks.listIterator();
				go: while(iter.hasNext()) {
					ITask task = iter.next();
					switch(_state) {
						case PAUSED:
							while(_state == state.PAUSED) {
								Thread.yield();
							}
							if(_state == state.IDLE) {
								return;
							}
							break;
						case IDLE:
							return;
						case RUNNING:
							if(task.isDeferred()) {
								deferred.add(task);
								iter.remove();
								continue go;
								
							}
							
							task.start();
							
							if(task.isDone()) {
								iter.remove();
							}
					}
					
					
				}
				
				
				synchronized(added) {		
					
					tasks.addAll(added);
					added.clear();
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					return;
				}
				
			} 
		}
	}

	public static <T> Task<T> addTask(Callable<T> t) {
		Task<T> task = new TaskImp<T>(t);
		addTask(task);
		return task;
	}
	
	public static void cleanup() {
		_state = state.IDLE;
		running.interrupt();
	}
	public static void resume() {
		_state = state.RUNNING;
	}

}
