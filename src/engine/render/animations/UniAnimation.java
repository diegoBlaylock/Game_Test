package engine.render.animations;

import engine.render.utils.sprites.SpriteSheet;

/**
 * Base Animation, holds one sprite location
 * 
 * @author diego
 *
 */
public class UniAnimation extends Animation{
	int frame;
	public UniAnimation(int frames) {
		frame = frames;
	}
	
	@Override
	public void cleanup() {}

	@Override
	public void update(SpriteSheet ss) {
		ss.update(frame);
	}

	@Override
	public void reset() {
	}
}
