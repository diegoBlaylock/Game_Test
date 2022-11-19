package engine.ecs.components.ai;

import engine.ILogic;
import engine.ecs.components.Component;
import engine.ecs.entities.Entity;

/**
 * An alternative to the repositioning done by FSMs. This does have the benefit of occuring after all updates
 * 
 * @author diego
 */
public abstract class Controller extends Component implements ILogic{

	public abstract void control(Entity e);
		
}
