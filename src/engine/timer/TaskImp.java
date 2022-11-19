package engine.timer;

import java.util.concurrent.Callable;

public class TaskImp<T> extends Task<T>{

	Callable<T> call;
	
	TaskImp(Callable<T> call){
		this.call = call;
	}
	
	@Override
	public boolean isDeferred() {
		return false;
	}

	@Override
	public T call() {
		// TODO Auto-generated method stub
		try {
			return call.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
