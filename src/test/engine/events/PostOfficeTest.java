package test.engine.events;

import java.util.Deque;
import java.util.LinkedList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import engine.events.Event;
import engine.events.PostOffice;
import engine.events.PostOffice.Subscription;

public class PostOfficeTest {
	
	static class AbstractEvent extends Event{
		public String message;
				
		public AbstractEvent(String message) {
			this.message = message;
		}
		
		public String toString() {
			return "[" + this.getClass().getTypeName() + "-\"" + message;
		}
	}

	static class A extends AbstractEvent{

		public A(String message) {
			super(message);
		}}
	static class B extends AbstractEvent{

		public B(String message) {
			super(message);
		}}

	Deque<AbstractEvent> receivedEvents = new LinkedList<AbstractEvent>();
	
	@Test
	void TestBroadcast() {
		Subscription<A> pub = PostOffice.subscribe(A.class, 0, PostOffice.Scope.PUBLIC, this::queue);
		Subscription<A> priv = PostOffice.subscribe(A.class, 1, PostOffice.Scope.PRIVATE, this::queue);

		PostOffice.broadcast(new A("broadcast"));
		PostOffice.pollEvents();
		PostOffice.pollEvents();
		
		Assertions.assertInstanceOf(A.class, receivedEvents.poll());
		
		Assertions.assertTrue(receivedEvents.isEmpty());
		


		
		PostOffice.broadcast(new A("broadcast2"),1);
		PostOffice.pollEvents();
		PostOffice.pollEvents();	
		
		Assertions.assertInstanceOf(A.class, receivedEvents.poll());
		Assertions.assertInstanceOf(A.class, receivedEvents.poll());
		
		Assertions.assertTrue(receivedEvents.isEmpty());
		
		pub.cancel();
		priv.cancel();
	}
	
	@Test
	void TestMail() {
		Subscription<A> pub = PostOffice.subscribe(A.class, 0, PostOffice.Scope.PUBLIC, this::queue);
		Subscription<A> priv = PostOffice.subscribe(A.class, 1, PostOffice.Scope.PRIVATE, this::queue);

		PostOffice.mail(new A("broadcast"));
		PostOffice.pollEvents();
		
		Assertions.assertTrue(receivedEvents.isEmpty());
		


		
		PostOffice.mail(new A("broadcast2"),0,1);
		PostOffice.pollEvents();
		
		Assertions.assertInstanceOf(A.class, receivedEvents.poll());
		Assertions.assertInstanceOf(A.class, receivedEvents.poll());
		
		Assertions.assertTrue(receivedEvents.isEmpty());
		
		pub.cancel();
		priv.cancel();
	}
	
	
	public void queue(AbstractEvent e) {
		receivedEvents.push(e);
	}
}
