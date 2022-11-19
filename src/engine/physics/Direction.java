package engine.physics;

import engine.ecs.components.Component;
import utils.Dir4;

/**
 * Direction of entity
 * 
 * @author diego
 *
 */
public class Direction extends Component {
	Dir4 direction;
	
	public Direction(Dir4 dir4) {
		direction = dir4;
	}
	
	public Direction() {
		direction = Dir4.NONE;
	}

	public Dir4 getDirection() {
		return direction;
	}
	
	public void setDirection(Dir4 direction) {
		this.direction = direction;
	}
	
}
