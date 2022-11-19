package engine.timer;

public class DelayWrapperTask<T> extends Task<T>{
	
	long timeout = 0l;
	Task<T> task;
	
	public DelayWrapperTask(float seconds, Task<T> task) {
		super();
		timeout = (long) (System.currentTimeMillis() + seconds*1000.0);

		this.task = task;
	}
	
	@Override
	public T call(){
		task.start();
		this.cancel();
		return task.getResult().await();
		
	}

	public boolean cancel() {
		super.cancel();
		return task.cancel();
	}

	@Override
	public boolean isDeferred() {


		return System.currentTimeMillis() < timeout;
	}

	
}
