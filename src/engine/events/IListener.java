package engine.events;

/**
 * handler for events
 * 
 * @author diego
 *
 * @param <T>
 */
@FunctionalInterface
public interface IListener<T extends Event> {

	public void handle(T event);
	
}
