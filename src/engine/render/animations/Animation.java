package engine.render.animations;

import engine.ecs.components.Component;
import engine.render.utils.sprites.SpriteSheet;

/**
 * This is used to update the SpriteSheet selection
 * 
 * @author diego
 */
public abstract class Animation extends Component{
	
	public abstract void update(SpriteSheet compenent);
	
	public abstract void reset();
	
}
