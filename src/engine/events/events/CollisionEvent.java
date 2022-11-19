package engine.events.events;

import engine.ecs.entities.Entity;
import engine.events.Event;

/**
 * Handed to entities upon a collision
 * 
 * @author diego
 *
 */
public final class CollisionEvent extends Event{
	
	final long ent_id;
	final byte channel;
	
	public CollisionEvent(long entity, byte channel) {
		this.ent_id = entity;
		this.channel = channel;
	}
	
	public Entity getCollidingEntity() {
		return Entity.fetch(ent_id);
	}
	
	public byte getChannel() {
		return channel;
	}
	
}
