package engine.events;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Event-queue. Wraps every event with its addressees and its mode
 * 
 * @author diego
 *
 * @param <T>
 */
public class Channel<T> implements Iterable<Channel.Entry<T>>{
	List<Entry<T>> events = new LinkedList<Entry<T>>();

	public void add(Mode mode, T event, long...subscribers) {
		events.add(new Entry<T>(mode, event, subscribers));
	}
	
	@Override
	public Iterator<Entry<T>> iterator() {
		return events.iterator();
	}
	
	protected static class Entry<T> {
		final Mode mode;
		final long[] subscribers;
		final T event;
		
		private Entry(Mode mode, T event, long... ls) {
			this.mode = mode;
			subscribers = ls;
			this.event = event;
			
			if(subscribers != null) {
				Arrays.sort(subscribers);
			}
		}
		
		
	}

	public boolean isEmpty() {
		return events.isEmpty();
	}

	public int size() {
		return events.size();
	}

	public static enum Mode{
		MAIL,
		BROADCAST
	}

}
