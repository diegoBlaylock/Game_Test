package engine.ecs.systems;

import java.util.Collection;

import engine.ecs.IEntityFetcher;
import engine.ecs.components.ai.Controller;
import engine.ecs.entities.Entity;

public class ContollerSystem implements ISystem {

	@Override
	public void processEntities(IEntityFetcher fetcher) {
		Collection<? extends Entity> entities = fetcher.getEntities(Controller.class);
		
		for(Entity e : entities) {
			Controller c = e.getComponent(Controller.class);
			if(c.enabled()) {
				c.control(e);
			}
		}
	}
	
}