package engine.timer;

public interface ITask {

	public void start();
	
	public boolean cancel();

	public boolean isDeferred();
	
	public boolean isDone();
	
}
