package engine.ecs;

import java.util.Collection;

import engine.ecs.components.Component;
import engine.ecs.entities.Entity;
import engine.world.World;

/**
 * Used to organize the entities and fetch them based on component composition
 * 
 * @author diego
 *
 */
public interface IEntityFetcher {

	public Collection<? extends Entity> getEntities();
	
	public Collection<? extends Entity> getEntities(Class<? extends Component> queryClass);
	
	public World getWorld();
	
	@SuppressWarnings("unchecked")
	public Collection<? extends Entity> getEntitiesContaining(Class<? extends Component>... classes );
}
