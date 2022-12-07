package engine.render.sprites.animations;

import engine.render.sprites.SpriteSheet;

/**
 * Keeps track of counters and cycles through list of sprite positions
 * @author diego
 *
 */
public class CycleAnimation extends Animation{

	int[] cycles;
	int interval;
	int index;
	int count;
	
	public CycleAnimation(int interval, int... frames) {
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
		count = 0;
	}

}
