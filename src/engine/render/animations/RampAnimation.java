package engine.render.animations;

import engine.render.utils.sprites.SpriteSheet;

/**
 * This is a animation that cycles through sprites once and then holds
 * 
 * @author diego
 *
 */
public class RampAnimation extends Animation {
	int[] cycles;
	int interval;
	int index;
	int count;
	
	public RampAnimation(int interval, int... frames) {
		this.cycles = frames;
		this.interval = interval;
		this.index = 0;
		this.count = 0;
	}
	
	@Override
	public void cleanup() {
		
	}

	@Override
	public void update(SpriteSheet ss) {
		if(index == cycles.length-1) {
			return;
		}
		
		if(count <= 0) {
			index++;
			index%= cycles.length;
			
			ss.update(cycles[index]);
			
			count=interval;
		}
		count--;
	}

	@Override
	public void reset() {
		index = 0;
	}
}
