package engine.render.sprites.animations;

import engine.render.sprites.SpriteSheet;

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
