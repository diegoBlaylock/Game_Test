package engine.events.listeners;

import engine.ecs.entities.Entity;
import engine.events.IListener;
import engine.events.events.CollisionEvent;
/**
 * IListener wrapper for collision
 * 
 * @author diego
 *
 */
@FunctionalInterface
public interface CollisionListener extends IListener<CollisionEvent>{

	@Override
	public default void handle(CollisionEvent event) {
		onCollide(event.getCollidingEntity(), event.getChannel());
	}
	
	public void onCollide(Entity e, byte channel);
}
