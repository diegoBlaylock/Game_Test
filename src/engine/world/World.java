package engine.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import engine.ILogic;
import engine.ecs.EntityFetcher;
import engine.ecs.IEntityFetcher;
import engine.ecs.components.Component;
import engine.ecs.entities.Entity;
import engine.ecs.entities.living.Jim;
import engine.events.PostOffice;
import engine.events.events.EntityEvent;
import engine.world.tile.TileMap;

public class World implements ILogic{

	EntityFetcher fetcher = new EntityFetcher(this);
	TileMap map;
	public Jim player;
	
	
	public World() {
		
	}
	
	public void cleanup() {
        // Nothing to be done here yet
    }
	
	public IEntityFetcher getFetcher() {
		return this.new Fetcher();
	}
	
	public void addEntity(Entity e) {
		
		fetcher.add(e);
		e.updateWorld(this);
		EntityEvent event = new EntityEvent(e, true, this);
		PostOffice.broadcast(event, e.EID());
	}

	public void update(long dTick) {
		for(Entity e : fetcher.getEntities()) {
			e.update(dTick);
		
		}
		map.update(dTick);
	}

	public void loadMap(TileMap load) {
		if(map != null) {
			map.unload();
		}
		map = load;
	}
	
	class Fetcher implements IEntityFetcher{
		List< Entity> output = new ArrayList<Entity>();
		
		@Override
		public Collection<? extends Entity> getEntities() {
			output.clear();
			output.addAll(map.getEntities());

			output.addAll(fetcher.getEntities());
			return output;
		}

		@Override
		public Collection<Entity> getEntities(Class<? extends Component> queryClass) {
			output.clear();
			output.addAll(map.getEntities(queryClass));

			output.addAll(fetcher.getEntities(queryClass));
			return output;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Collection<Entity> getEntitiesContaining(Class<? extends Component>... classes) {
			output.clear();
			output.addAll(map.getEntitiesContaining(classes));

			
			output.addAll(fetcher.getEntitiesContaining(classes));
			return output;
		}

		@Override
		public World getWorld() {
			return World.this;
		}
		
		
		
	}

	public TileMap getTileMap() {
		return map;
	}

	public void remove(Entity e) {
		this.fetcher.remove(e);	
		EntityEvent event = new EntityEvent(e, false, this);
		PostOffice.broadcast(event, e.EID());
	}


	
}
