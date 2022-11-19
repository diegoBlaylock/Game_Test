package engine.ecs.components.ai;

import engine.ecs.entities.Entity;
import engine.physics.Location;

/**
 * This controller will map the position of an entity to that of another entity
 * 
 * @author diego
 */
public class BoundedDucklingController extends Controller{

	Entity mother;
	
	/**
	 * Added to make custom repositioning functions easier to implement.
	 * 
	 * @param current
	 * @param target
	 */
	public void reposition_fn(Location current, Location target) {
		current.set(target.getVec(false));
	}

	/**
	 * Inherited Controller function
	 */
	@Override
	public void control(Entity e) {
		if(mother != null && mother.contains(Location.class)) {
			reposition_fn(e.getComponent(Location.class), mother.getComponent(Location.class));
		}
	}
	
	/**
	 * change the target of repositioning
	 * 
	 * @param newMother
	 */
	public void tether(Entity newMother) {
		mother = newMother;
	}

	@Override
	public void update(long time) {}
	
}
