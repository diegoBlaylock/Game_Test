package engine.ecs.entities.living;

import engine.ecs.components.Speed;
import engine.ecs.entities.Entity;
import engine.physics.Direction;
import engine.physics.Location;
import utils.Dir4;
import utils.ds.Vec2f;

/**
 * Defines components within a living entity
 * 
 * Components:
 * 	Location
 * 	Direction
 * 	Speed
 * 
 * @author diego
 */
public class LivingEntity extends Entity{

	public LivingEntity() {
		super();
		this.addComponent(new Location(0,0));
		this.addComponent(new Direction(Dir4.SOUTH));
		this.addComponent(new Speed(0.25f));
		
	}
	
	public Dir4 getDirection() {
		return this.getComponent(Direction.class).getDirection();
	}
	
	public float getSpeed() {
		return this.getComponent(Speed.class).getSpeed();
	}
	
	public Location getLocation() {
		return this.getComponent(Location.class);
	}
	
	public void setDirection(Dir4 newDir) {
		this.getComponent(Direction.class).setDirection(newDir);
	}
	
	public void setSpeed(float speed) {
		this.getComponent(Speed.class).set(speed);
	}
	
	public void setLocation(Vec2f newLoc) {
		this.getComponent(Location.class).set(newLoc.getVec(false));
	}
	
	public void setLocation(float x, float y) {
		this.getComponent(Location.class).set(x,y);
	}
	
}
