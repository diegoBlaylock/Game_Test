package engine.events.events;

import engine.ecs.entities.Entity;
import engine.events.Event;
import engine.world.World;

/**
 * Broadcast entity spawning and despawning in a world
 * 
 * @author diego
 *
 */
public class EntityEvent extends Event{
	final long ent_id;
	final World world;
	final boolean created;
	
	public EntityEvent(Entity e, boolean created, World w) {
		ent_id = e.EID();
		this.world = w;
		this.created = created;
	}
	
	public Entity getEntity() {
		return Entity.fetch(ent_id);
	}
	
	public boolean isCreatedEvent() {
		return created;
	}
	
	public World getWorld() {
		return world;
	}
}
