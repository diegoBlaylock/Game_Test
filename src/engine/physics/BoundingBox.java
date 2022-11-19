package engine.physics;

import utils.ds.Vec2f;

/**
 * Very Simple AABB that also defines type:
 * 	TRIGGER - No Collision resolution, sends collision events
 * 	PUSHOVER - Collision resolution with ICEBERGS, send collision events
 * 	ICEBERG - Immovable, cause resolution of PUSHOVERs, sends collision event
 * 
 *  and holds channel, where different Bounding boxes exist on different channels allowing for different behaviour and interaction depending on channel
 * 
 * @author diego
 *
 */
public class BoundingBox {
	
	Vec2f xy1, xy2;
	byte channel;
	Collider type = Collider.ICEBERG;
	
	public BoundingBox(int channel, Collider type,float x1, float y1, float x2, float y2) {
		xy1 = new Vec2f(x1,y1);
		xy2 = new Vec2f(x2,y2);
		
		this.channel = (byte) channel;
		this.type = type;
	}

	public float getX1() {
		return xy1.getX();
	}
	
	public float getY1() {
		return xy1.getY();
	}
	
	public float getX2() {
		return xy2.getX();
	}
	
	public float getY2() {
		return xy2.getY();
	}
	
	
	public float top() {
		return Math.max(getY2(), getY1());
	}
	
	public float bottom() {
		return Math.min(getY2(), getY1());

	}
	
	public float right() {
		return Math.max(getX2(), getX1());

	}
	
	public float left() {
		return Math.min(getX2(), getX1());

	}
	
	public Collider getType() {
		return type;
	}
	
	public byte getChannel() {
		return this.channel;
	}

	public float getWidth() {
		return this.right() - this.left();
	}
	
	public float getHeight() {
		return this.top() - this.bottom();
	}


	
}
