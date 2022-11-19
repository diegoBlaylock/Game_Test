package engine.events;

/**
 * Base Event class, a consumed event is removed from the queue
 * 
 * @author diego
 *
 */
public abstract class Event {

	boolean consumed = false;
	
	public boolean isConsumed() {
		return consumed;
	}
	
	public void consume() {
		consumed = true;
	}
	
}
