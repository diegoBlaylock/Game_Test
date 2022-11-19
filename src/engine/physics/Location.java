package engine.physics;

import engine.ecs.components.Component;
import utils.ds.Vec2f;

/**
 * Stores location of an Entity
 * 
 * @author diego
 *
 */
public class Location extends Component{
	
	Vec2f xy;
	
	public Location(float x, float y) {
		xy = new Vec2f(x,y);
	}
	
	public Location(float[] vecd) {
		xy = new Vec2f(vecd);
	}
	
	
	public float getX() {
		return this.xy.getX();
	}
	
	public float getY() {
		return this.xy.getY();
	}
	
	
	public float[] getVec(boolean copy) {
		return xy.getVec(copy);
	}


	@Override
	public void cleanup() {
		
	}

	public void shift(float i, float j) {
		xy.getVec(false)[0]+=i;
		xy.getVec(false)[1]+=j;
		
	}
	
	public void shift(Vec2f vec) {
		xy.add(vec);
	}

	public void set(float[] vec) {
		xy.setVec(vec);
		
	}

	public void set(float x, float y) {
		xy.setVec(x, y);
		
	}
	@Override
	public String toString() {
		return xy.toString();
	}




}
