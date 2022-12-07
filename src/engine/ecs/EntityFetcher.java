package engine.ecs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import engine.ecs.components.Component;
import engine.ecs.entities.Entity;
import engine.world.World;

/**
 * Inefficient implementation of IEntityFetcher
 * 
 * @author diego
 *
 */
public class EntityFetcher implements IEntityFetcher{
	
	public World world;
	
	List<Long> entities = new LinkedList<Long>();

	public EntityFetcher(World world) {
		this.world = world;
	}
	
	@Override
	public List<Entity> getEntities() {
		List<Entity> output = new LinkedList<Entity>();
		for(Long i:entities) {
			Entity e = Entity.fetch(i);
			output.add(e);
		}
		
		return output;
	}

	@Override
	public Collection<Entity> getEntities(Class<? extends Component> queryClass) {
		List<Entity> output = new LinkedList<Entity>();
		for(Long i:entities) {
			Entity e = Entity.fetch(i);
			
			if(e.contains(queryClass)){
				output.add(e);
			}
		}
		
		return output;
		
	}
	
	@SafeVarargs
	@Override
	public final Collection<Entity> getEntitiesContaining(Class<? extends Component>... classes) {
		
		List<Entity> output = new LinkedList<Entity>();
		ent: for(Long i:entities) {
			Entity e = Entity.fetch(i);
			
			for(Class<? extends Component> queryClass: classes) {
				if(!e.contains((Class<? extends Component>) queryClass)){
					continue ent;
				}
			}
			
			output.add(e);
		}

		return output;
	}
	
	public void add(Entity e) {
		entities.add(e.EID());

	}

	public void remove(Entity e) {
		entities.remove(e.EID());
	}
	
	public void removeAll() {
		entities.clear();
	}

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

}
