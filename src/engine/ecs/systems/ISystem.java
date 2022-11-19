package engine.ecs.systems;

import engine.ecs.IEntityFetcher;

/**
 * A system will take the current list of entities with specified attributes and update them based on the system
 * for example a physics system will update location based on speed
 * 
 * @author diego
 *
 */
public interface ISystem {
	
	public void processEntities(IEntityFetcher fetcher);
	
}
