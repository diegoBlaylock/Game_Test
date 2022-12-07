package engine.events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import engine.events.Channel.Entry;
import engine.events.Channel.Mode;

/**
 * keeps track of Suscribers and events. Fowarding events to suscribers on pollevents()
 * 
 * @author diego
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PostOffice {
	
	/**
	 * Keeps track of channels(queues) that accumulate event messages
	 */
	static final Map<Class, Channel> EVENT_BUS = new HashMap<Class, Channel>();
	
	/**
	 * Maintains a list of subscriptions. Structured as a double map, so broadcast messages
	 * can easily pull out all the subscriptions for an event while maintaining the quick access
	 * to private messages for specific subscribers
	 */
	static final Map<Class, Map<Long, Subscription>> SUBSCRIPTIONS = new HashMap<Class, Map<Long, Subscription>>();
	
	/**
	 * sends a private event to the suscibers given.
	 * 
	 * @param <T>
	 * @param event
	 * @param subscribers
	 */
	public static <T extends Event> void mail(T event, long... subscribers){
		send(Mode.MAIL, event, subscribers);
	}
	
	/**
	 * Sends a public event to all except to the suscribers listed. This will receive a private notification
	 * 
	 * @param <T>
	 * @param event
	 * @param ls
	 */
	public static <T extends Event> void broadcast(T event, long...ls){
		send(Mode.BROADCAST, event, ls);
	}
	
	public static <T extends Event> void send(Mode mode, T event, long...ls){
		if(!EVENT_BUS.containsKey(event.getClass())) {
			EVENT_BUS.put(event.getClass(), new Channel<T>());
		}
		
		Channel<T> channel = EVENT_BUS.get(event.getClass());
		channel.add(mode, event, ls);
	}
	
	public static <T extends Event> void pollEvents(){
		for(Class<T> clazz : EVENT_BUS.keySet()) {
			Channel<T> channel = EVENT_BUS.get(clazz);
			Map<Long, Subscription> subscriptions = SUBSCRIPTIONS.get(clazz);
			
			if(subscriptions == null || channel == null || channel.isEmpty()) {
				continue;
			}
			
			Iterator<Channel.Entry<T>> iterator = channel.iterator();
			
			while(iterator.hasNext()) {
				Entry<T> entry = iterator.next();
				
				if(entry.event.isConsumed()) {
					iterator.remove();
					continue;
				}
				
				
				if(entry.mode == Mode.BROADCAST) {
					Iterator<Subscription> sub_iter = subscriptions.values().iterator();
					
					while(sub_iter.hasNext()) {
						Subscription<T> sub = sub_iter.next();
							
						if(sub.cancelled) {
							sub_iter.remove();
							continue;
						}
						
						if(!((sub.scope == Scope.PUBLIC || (entry.subscribers!=null && Arrays.binarySearch(entry.subscribers, sub.subscriber) >= 0) ) && sub.enabled)) {
							continue;
						}
						
						sub.listener.handle(entry.event);
					}
				} else if(entry.mode == Mode.MAIL){
					for(long subscriber : entry.subscribers) {
						if(!subscriptions.containsKey(subscriber)) {
							continue;
						}
						
						Subscription sub = subscriptions.get(subscriber);
						
						if(sub.cancelled) {
							subscriptions.remove(subscriber);
							continue;
						}
						
						if(!sub.enabled) {
							continue;
						}
						
						sub.listener.handle(entry.event);
					}
				}
				
				iterator.remove();
			}
			
		}
	}
	
	/**
	 * This will be stored 
	 * 
	 * @author diego
	 *
	 * @param <T> EventType being addressed in subscription
	 */
	public static class Subscription<T extends Event>{
		boolean enabled = true;
		boolean cancelled = false;
		
		Scope scope = Scope.PUBLIC;
		
		final long subscriber;
		final IListener<T> listener;
		
		public Subscription(long owner, IListener<T> listener) {
			this.subscriber = owner;
			this.listener = listener;
		}
		
		public Subscription(long owner, Scope scope, IListener<T> listener) {
			this(owner, listener);
			this.scope = scope;
		}
		
		public void cancel() {
			cancelled = true;
		}
		
		public void setScope(Scope newScope) {
			scope = newScope;
		}
	}
	
	public enum Scope {
		PRIVATE,
		PUBLIC
	}
	
	/**
	 * Subscribes to PostOffice and starts receiving messages through Listener. Return subscription, 
	 * allowing for basic control such as cancelling or changing scope
	 * 
	 * @param <T>
	 * @param clazz			Event Class that the subscriptions deals with
	 * @param listener		The listener to foward messages to
	 * @param subscriber	The entity who owns the subscription. Used for addressed Messages
	 */
	public static <T extends Event> Subscription<T> subscribe(Class<T> clazz, long subscriber, Scope scope, IListener<T> listener) {
		Subscription<T> sub = new Subscription<T>(subscriber, scope, listener);
		
		if(!SUBSCRIPTIONS.containsKey(clazz)) {
			SUBSCRIPTIONS.put(clazz, new HashMap<Long, Subscription>());
		}
		
		Map<Long, Subscription> addresses = SUBSCRIPTIONS.get(clazz);
		
		addresses.put(subscriber, sub);
		
		return sub;
	}
	
}
