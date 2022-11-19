package engine.render.utils.sprites;

import engine.ecs.components.RenderComp;
import engine.render.Texture;
import engine.render.shapes.ShapeLoader;

/**
 *	Subclass of RenderComp, allowing for rendering a specific sprite based on location(row major flattening) in spritesheet
 * 
 * @author diego
 *
 */
public class SpriteSheet extends RenderComp{
	
	int width, height, size; 
	int loc = Integer.MIN_VALUE;
	double padding_w = 0, padding_h = 0;
	float[] load = new float[8];
	
	public SpriteSheet(int loc, int width, int height, Texture atlas) {
		super(ShapeLoader.createDShape(new float[] {-1f,-1f, 0, 0, 0,1f, -1f, 0,0, 0, 1f, 1f, 0,0, 0, -1f, 1f, 0, 0,0}, new int[] {0,1,2,0,2,3}), atlas);
		this.width=width;
		this.height=height;
		this.size = width * height;
		this.update(loc);
		
	}
	
	public SpriteSheet(int loc, int width, int height, int padding, Texture atlas) {
		super(ShapeLoader.createDShape(new float[] {-1f,-1f, 0, 0, 0,1f, -1f, 0,0, 0, 1f, 1f, 0,0, 0, -1f, 1f, 0, 0,0}, new int[] {0,1,2,0,2,3}), atlas);
		this.width=width;
		this.height=height;
		this.size = width * height;
		this.update(loc);
		this.padding_w = padding * 1.0 / atlas.getWidth();
		this.padding_h = padding * 1.0 / atlas.getHeight();
	}

	/**
	 * This updates the current shape to include the uv coord of the current sprite.
	 * The location parameter allows for arguments to flip and rotate the sprite.
	 * 
	 * Negative numbers mean that there is a horizontal flip where -1 is 0 flipped.
	 * 
	 * 
	 * @param location of sprite
	 */
	public void update(int loc) {
		if(this.loc == loc) {
			return;
		}
		
		this.loc = loc;
		
		this.getVAO().bind();
		
		int uLoc = Math.abs(loc) - (loc<0?1:0);
		
		
		int col = (uLoc % (this.size)) % width;
		int row = (uLoc % this.size) / width;
		
		
		
		int rot = (uLoc / size) % 4;
		boolean hflipped = loc<0;
		
		float low_x; 
		float low_y;
		float high_x;
		float high_y;
		
		
		low_x = (float) (col * 1.0f / this.width + padding_w);
		low_y = (float) (row * 1.0f/ this.height + padding_h);
		high_x = (float) ((col+1.0f) / this.width - padding_w);
		high_y = (float) ((row+1f) / this.height - padding_h);
	
		
		
	
		if(hflipped) {
			this.loadArr(high_x, low_y, low_x, high_y, rot);

		}else {
			this.loadArr(low_x, low_y, high_x, high_y, rot);
		}
		
				
		this.getVAO().getVBO().editAttribute(1, load);
		
		this.getVAO().unbind();
	}
	
	void loadArr(float low_x, float low_y, float high_x, float high_y, int rotation) {
		
		switch(rotation) {
		case 0:
			load[0] = low_x;
			load[1] = high_y;
			load[2] = high_x;
			load[3] = high_y;
			load[4] = high_x;
			load[5] = low_y;
			load[6] = low_x;
			load[7] = low_y;
			break;
		case 3:
			load[6] = low_x;
			load[7] = high_y;
			load[0] = high_x;
			load[1] = high_y;
			load[2] = high_x;
			load[3] = low_y;
			load[4] = low_x;
			load[5] = low_y;
			break;
		case 2:
			load[4] = low_x;
			load[5] = high_y;
			load[6] = high_x;
			load[7] = high_y;
			load[0] = high_x;
			load[1] = low_y;
			load[2] = low_x;
			load[3] = low_y;
			break;
		case 1:
			load[2] = low_x;
			load[3] = high_y;
			load[4] = high_x;
			load[5] = high_y;
			load[6] = high_x;
			load[7] = low_y;
			load[0] = low_x;
			load[1] = low_y;
			break;
		}
		
	}

	public int getSize() {
		return size;
	}

	
		
	
}
