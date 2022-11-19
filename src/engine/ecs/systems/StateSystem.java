package engine.ecs.systems;

import java.util.Collection;

import engine.ecs.IEntityFetcher;
import engine.ecs.components.ai.StateManager;
import engine.ecs.entities.Entity;

public class StateSystem implements ISystem{

	@Override
	public void processEntities(IEntityFetcher fetcher) {
		Collection<? extends Entity> entities = fetcher.getEntities(StateManager.class);
		
		for(Entity e : entities) {
			StateManager states = e.getComponent(StateManager.class);
			
			states.update(e);
		}
		
	}

}
