package engine.events.listeners;

import engine.ecs.entities.Entity;
import engine.events.IListener;
import engine.events.events.EntityEvent;

/**
 * Entity event wrapper for IListener
 * 
 * @author diego
 *
 */
public interface EntityListener extends IListener<EntityEvent>{
	
	@Override
	public default void handle(EntityEvent ee) {
		if(ee.isCreatedEvent()) {
			onSpawn(ee, ee.getEntity());
		} else {
			onDespawn(ee, ee.getEntity());
		}
		
	}
	
	public void onSpawn(EntityEvent event, Entity e);
	
	public void onDespawn(EntityEvent event, Entity e);

	
}
