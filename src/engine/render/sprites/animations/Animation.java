package engine.render.sprites.animations;

import engine.ecs.components.Component;
import engine.render.sprites.SpriteSheet;

/**
 * This is used to update the SpriteSheet selection
 * 
 * @author diego
 */
public abstract class Animation extends Component{
	
	public abstract void update(SpriteSheet compenent);
	
	public abstract void reset();
	
}
