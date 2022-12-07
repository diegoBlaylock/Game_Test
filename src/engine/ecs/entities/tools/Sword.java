package engine.ecs.entities.tools;

import engine.ecs.entities.Entity;
import engine.ecs.entities.living.LivingEntity;
import engine.events.PostOffice;
import engine.events.PostOffice.Scope;
import engine.events.events.CollisionEvent;
import engine.events.listeners.CollisionListener;
import engine.physics.BoundingBox;
import engine.physics.Collider;
import engine.physics.Direction;
import engine.physics.Location;
import engine.physics.RigidBody;
import engine.render.Texture;
import engine.render.sprites.SpriteSheet;

/**
 * Sword class that jim uses
 * 
 * @author diego
 *
 */
public class Sword extends Entity implements CollisionListener {
	
	long owner;
	
	public Sword(LivingEntity j) {
		owner = j.EID();
		
		Direction dir = j.getComponent(Direction.class);
		
		this.addComponent(new SpriteSheet(0,4,7, 1, Texture.ITEMS16).setDepth(-0.5f));
		this.addComponent(new Location(j.getComponent(Location.class).getVec(true)));
		this.addComponent(dir);
		
		switch(dir.getDirection()) {
		case NORTH:
			this.addComponent(new RigidBody(new BoundingBox(0, Collider.TRIGGER, -0.09375f, -1, 0.09375f, 1)));
			this.getComponent(SpriteSheet.class).update(28);
			break;
		case EAST:
			this.addComponent(new RigidBody(new BoundingBox(0, Collider.TRIGGER, -1, -0.09375f, 1, 0.09375f)));
			this.getComponent(SpriteSheet.class).update(0);
			break;
		case SOUTH:
			this.addComponent(new RigidBody(new BoundingBox(0, Collider.TRIGGER, -0.09375f, -1, 0.09375f, 1)));
			this.getComponent(SpriteSheet.class).update(84);
			break;
		case WEST:
			this.addComponent(new RigidBody(new BoundingBox(0, Collider.TRIGGER, -1, -0.09375f, 1, 0.09375f)));
			this.getComponent(SpriteSheet.class).update(56);
			break;
		default:
			break;
		}
		
		PostOffice.subscribe(CollisionEvent.class, this.EID(), Scope.PRIVATE, this);
	}
	
	@Override
	public void onCollide(Entity e, byte channel) {
		if(!e.EID().equals(owner)) {
			e.delete();
		}
	}
}