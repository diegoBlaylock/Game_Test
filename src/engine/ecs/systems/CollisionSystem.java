package engine.ecs.systems;


import java.util.Collection;
import java.util.Iterator;
import engine.ecs.IEntityFetcher;
import engine.ecs.entities.Entity;
import engine.events.PostOffice;
import engine.events.events.CollisionEvent;
import engine.physics.BoundingBox;
import engine.physics.Collider;
import engine.physics.Location;
import engine.physics.RigidBody;
import utils.ds.Vec2f;

/**
 * detects and resolves collisions. Runs through RigidBody components and checks channel by channel
 * 
 * @author diego
 *
 */
public class CollisionSystem implements ISystem{

	@SuppressWarnings("unchecked")
	@Override
	public void processEntities(IEntityFetcher fetcher) {
		Collection<? extends Entity> entities = fetcher.getEntitiesContaining(Location.class, RigidBody.class);
		
		
		int index = 1;
		Iterator<? extends Entity> outer = entities.iterator();
		
		while(outer.hasNext()) {
			Iterator<? extends Entity> inner = entities.iterator();
			
			for(int i = 0; i < index; i++) {
				inner.next();
			}
			
			Entity a = outer.next();
			RigidBody body_a = a.getComponent(RigidBody.class);			
			
			// COLLISION WITH TILES
			BoundingBox tile_box = body_a.getChannel(-1);
			
			if(tile_box != null) {
				Location l = a.getComponent(Location.class);
				for(int i = (int) tile_box.left(); i < tile_box.right(); i++) {
					for(int j = (int) tile_box.bottom(); j < tile_box.right(); j++) {
						if(a.getWorld().getTileMap().getTile(i, j).isCollision()) {
							float ax1 = tile_box.getX1() + l.getX();
							float ax2 = tile_box.getX2() + l.getX();
							float ay1 = tile_box.getY1() + l.getY();
							float ay2 = tile_box.getY2() + l.getY();
							
							float bx1 = i;
							float bx2 = 1 + i;
							float by1 = j;
							float by2 = 1 + j;
							
							if(ax1 < bx2 && ax2 > bx1) {
								if(ay1 < by2 && ay2 > by1) {
									
									// RESOLVE Collision
									
									Vec2f a_center = new Vec2f((ax1+ax2)/2, (ay1+ay2)/2);
									Vec2f b_center = new Vec2f((bx1+bx2)/2, (by1+by2)/2);

									Vec2f center = new Vec2f(b_center.getX() - a_center.getX(), b_center.getY() - a_center.getY());

									if(Math.abs(center.getX()) > Math.abs(center.getY())) {
										if(center.getX() > 0) {
											l.set(new float[] {bx1 - tile_box.getX2() ,l.getY()});
										} else {
											l.set(new float[] {bx2 - tile_box.getX1() , l.getY()});

										}
									} else {
										if(center.getY() > 0) {
											l.set( new float[] {l.getX(), bx1 - tile_box.getY2()});
										} else {
											l.set(new float[] {l.getX(), bx2 - tile_box.getY1() });

										}		}
									
									
								}
							}
						}
					}
				}
			}
		
			// COLLISION WITH ENTITIES ON THE SAME CHANNEL
			while(inner.hasNext()) {
				Entity b = inner.next();
				
				
				RigidBody body_b= b.getComponent(RigidBody.class);
				
				for(BoundingBox bb_a : body_a.getBoundingBoxes()) {
					
					BoundingBox bb_b = body_b.getChannel(bb_a.getChannel());
					
					if(bb_b == null || bb_a.getChannel() == (byte) -1) {
						continue;
					}
					
					Collider a_coll = bb_a.getType();
					Collider b_coll = bb_b.getType();
					
					if(a_coll == Collider.ICEBERG && b_coll == Collider.ICEBERG) {
						continue;
					}
					
					if(intersects(bb_a.getChannel(), a,b)) {
						
						//Send Event
						PostOffice.mail(new CollisionEvent(a.EID(), bb_a.getChannel()), b.EID());
						PostOffice.mail(new CollisionEvent(b.EID(), bb_a.getChannel()), a.EID());
						
						
						
						if(b_coll == Collider.PUSHOVER && a_coll == Collider.ICEBERG) {
							resolveCollision(bb_a.getChannel(), b, a);
						}
						
						if(a_coll == Collider.PUSHOVER && b_coll == Collider.ICEBERG) {
							resolveCollision(bb_a.getChannel(), a,b);
						}	
					}
				}
			}
				
			index++;
			
		}
		
	}
	
	public void resolveCollision(byte channel, Entity error, Entity constraint){
		Location a = error.getComponent(Location.class);
		Location b = constraint.getComponent(Location.class);
		
		BoundingBox a_box = error.getComponent(RigidBody.class).getChannel(channel);
		BoundingBox b_box = constraint.getComponent(RigidBody.class).getChannel(channel);
		
		//( (x1+x2) /2 + xloc)
		
		Vec2f a_center = new Vec2f((a_box.getX1()+a_box.getX2())/2 + a.getX(), (a_box.getY1()+a_box.getY2())/2 + a.getY());
		Vec2f b_center = new Vec2f((b_box.getX1()+b_box.getX2())/2 + b.getX(), (b_box.getY1()+b_box.getY2())/2 + b.getY());

		Vec2f center = new Vec2f(b_center.getX() - a_center.getX(), b_center.getY() - a_center.getY());

		if(Math.abs(center.getX()) > Math.abs(center.getY())) {
			if(center.getX() > 0) {
				a.set(new float[] {b.getX() + b_box.getX1() - a_box.getX2() ,a.getY()});
			} else {
				a.set(new float[] {b.getX() + b_box.getX2() - a_box.getX1() ,a.getY()});

			}
		} else {
			if(center.getY() > 0) {
				a.set( new float[] {a.getX(), b.getY() + b_box.getY1() - a_box.getY2()});
			} else {
				a.set(new float[] {a.getX(), b.getY() + b_box.getY2() - a_box.getY1() });

			}		}
		
	}
	
	public boolean intersects(byte channel, Entity a, Entity b) {
		BoundingBox a_bbox = a.getComponent(RigidBody.class).getChannel(channel);
		Location a_loc = a.getComponent(Location.class);
		BoundingBox b_bbox = b.getComponent(RigidBody.class).getChannel(channel);
		Location b_loc = b.getComponent(Location.class);
		
		float ax1 = a_bbox.getX1() + a_loc.getX();
		float ax2 = a_bbox.getX2() + a_loc.getX();
		float ay1 = a_bbox.getY1() + a_loc.getY();
		float ay2 = a_bbox.getY2() + a_loc.getY();
		
		float bx1 = b_bbox.getX1() + b_loc.getX();
		float bx2 = b_bbox.getX2() + b_loc.getX();
		float by1 = b_bbox.getY1() + b_loc.getY();
		float by2 = b_bbox.getY2() + b_loc.getY();
		
		if(ax1 < bx2 && ax2 > bx1) {
			if(ay1 < by2 && ay2 > by1) {
				return true;
			}
		}
		
		return false;
	}
		
	

	
	
}
