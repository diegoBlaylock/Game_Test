package engine.ecs.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.ILogic;
import engine.ecs.components.Component;
import engine.world.World;
import utils.MathUtils;

/**
 * Contains List of Components which can be fetched by class
 * Is identified by a long id and keeps a reference to its current world
 * 
 * @author diego
 *
 */
public abstract class Entity implements ILogic{
	
	static Map<Long, Entity> ID_ENTITY = new HashMap<Long, Entity>(100);
	
	Long id = Entity.register(this);
	
	List<Component> components = new ArrayList<Component>();
	
	World world;
	
	public <T extends Component> T getComponent(Class<T> component_class) {
		
		for (int i = 0; i < components.size(); i++) {
			Component comp = this.components.get(i);
			
			if(component_class.isAssignableFrom(comp.getClass())) {
				return component_class.cast(comp);
			}
		}
			
		return null;
	}
	
		
	public void addComponent(Component comp) {
		components.add(comp);
	}
	
	public void delComponent(Class<?> component_class) {
		for (int i = 0; i < components.size(); i++) {
			Component comp = this.components.get(i);
			
			if(component_class.isAssignableFrom(comp.getClass())) {
				components.remove(i);
				return;
			}
		}
			
	}
	
	public <T extends Component, V extends T> void updateComponent(Class<T> clazz1, V clazz2) {
		this.delComponent(clazz1);
		this.addComponent(clazz2);
	}
	
	public void update(long dTick) {
		for (int i = 0; i < components.size(); i++) {
			Component comp = this.components.get(i);
			
			if(ILogic.class.isAssignableFrom(comp.getClass())) {
				ILogic.class.cast(comp).update(dTick);
			}
		}
	}
	
	protected static long register(Entity ent) {
		long ent_id = MathUtils.getRandomID();
		ID_ENTITY.put(ent_id, ent);
		return ent_id;
	}
	
	public static Entity fetch(long ent_id) {
		if(ent_id == 0) {
			return null;
		}
		return ID_ENTITY.get(ent_id);
	}


	public boolean contains(Class<? extends Component> queryClass) {
		for (int i = 0; i < components.size(); i++) {
			Component comp = this.components.get(i);

			if(queryClass.isAssignableFrom(comp.getClass())) {
				return true;
			}
		}
		
		return false;
	}


	public Long EID() {
		return this.id;
	}
	
	public World getWorld() {
		return world;	
	}

	public void updateWorld(World world2) {
		world = world2;
	}
	
	public void delete() {
		if(world != null) {
			world.remove(this);
		}
		world = null;
		this.id = 0L;
		this.components.clear();
		ID_ENTITY.remove(EID());
	}

}
