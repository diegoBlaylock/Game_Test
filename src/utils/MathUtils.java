package utils;

import java.util.PrimitiveIterator;
import java.lang.Math;
import java.util.Random;
import java.util.UUID;

import engine.ecs.entities.Entity;
import engine.physics.BoundingBox;
import engine.physics.Location;
import engine.physics.RigidBody;
import engine.world.World;
import engine.world.tile.TileMap;
import engine.world.tile.types.ITileType;
import utils.ds.Vec2f;

/**
 * Some misc. utility functions
 * 
 * @author diego
 *
 */
public class MathUtils {

	//Dir4 to vectors
	public static final Vec2f EAST = new Vec2f(1,0);
	public static final Vec2f WEST = new Vec2f(-1,0);
	public static final Vec2f NORTH = new Vec2f(0, 1);
	public static final Vec2f SOUTH = new Vec2f(0, -1);
	
	public static Random GLOBAL_RNG = new Random();
	public static PrimitiveIterator.OfLong ID_STREAM = GLOBAL_RNG.longs().iterator();
	
	
	public static Long getRandomID() {
		return ID_STREAM.nextLong();
	}
	
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * TODO
	 * 
	 * @param w
	 * @param origin
	 * @param direction
	 * @param distance
	 * @param fixed_step
	 * @return
	 */
	public static RayHit castRayEntity(World w, Location origin, Vec2f direction, int distance, float fixed_step) {
		return null;
	}
	
	/**
	 * Casts ray that interacts with tiles and checks if they are collidable. When the vector is on the edge 
	 * it will favor the positive side. e.g. 5.0, 4.0 will hit tile<5,4> but none of the surrounding tiles
	 * 
	 * @param w
	 * @param vec2f
	 * @param direction
	 * @param f
	 * @return
	 */
	public static RayHit castRayTiles(World w, Vec2f vec2f, Vec2f direction, float f) {
		
		TileMap tm = w.getTileMap();
		direction = normalize(direction);
		
		float iDistX = 0;
		float iDistY = 0;
		
		float ratioxy = (direction.getY() == 0? 0: direction.getX()/direction.getY());
		float ratioyx = (direction.getX() == 0? 0: direction.getY()/direction.getX());
		
		float dDistX = (direction.getX() == 0? 0 : length(1, ratioyx*ratioyx));
		float dDistY = (direction.getY() == 0? 0 : length(1, ratioxy*ratioxy));
		
		float stepx = 0;
		float stepy = 0;
		
		int mapx = (int) vec2f.getX();
		int mapy = (int) vec2f.getY();
		
		
		if(direction.getX() == 0) {
			stepx = 0;
			iDistX = 0;
		} else if(direction.getX() > 0) {
			stepx = 1;
			iDistX = (mapx  - vec2f.getX()) + dDistX;
		} else if(direction.getX() < 0) {
			stepx = -1;
			iDistX = (vec2f.getX() - vec2f.getX()) + dDistX;
		}
		
		if(direction.getY() == 0) {
			stepy = 0;
			iDistY = 0;
		} else if(direction.getY() > 0) {
			stepy = 1;
			iDistY = (mapy  - vec2f.getY()) + dDistY;
		} else if(direction.getY() < 0) {
			stepy = -1;
			iDistY = (vec2f.getY() - vec2f.getY()) + dDistY;
		}

		
		assert (dDistX != dDistY || dDistX != 0) : "RAYCAST for direction <0,0> not accepted";
		
		ITileType tile = tm.getTile(mapx, mapy);	

		
		if(tile != null && tile.isCollision()) {
			
			return new RayHit(Math.max(iDistX, iDistY), 0);
		}
		
		
		if(iDistX == iDistY) {
			mapy+= stepy;
			mapx+= stepx;
		} else if((dDistY != 0 && iDistX > iDistY) || dDistX == 0) {
			mapy+= stepy;
		} else {
			mapx+= stepx;
		}
		
		while(iDistY <= f && iDistX <= f) {
			tile = tm.getTile(mapx, mapy);	
			if(tile != null && tile.isCollision()) {
				
				return new RayHit(Math.max(iDistX, iDistY), 0);
			}
			
			if(iDistX == iDistY) {
				iDistY += dDistY;
				iDistX += dDistX;

				mapy+= stepy;
				mapx+= stepx;
			} else if((dDistY != 0 && iDistX > iDistY) || dDistX == 0) {
				iDistY += dDistY;
				mapy+= stepy;
			} else {
				iDistX += dDistX;
				mapx+= stepx;
			}
		}
		
		return null;
		
	}
	
	/**
	 * Result class of ray detection
	 * 
	 * @author diego
	 *
	 */
	public static class RayHit {
		float distance;
		long entity_id;
		Vec2f location;
		
		protected RayHit(float distance, long entity_id) {
			this.distance = distance;
			this.entity_id = entity_id;
		}
		
		public float get_distance() {
			return distance;
		}
		
		public Entity getCollider() {
			return Entity.fetch(entity_id);
		}
		
		@Override
		public String toString() {
			return String.format("Ray Cast: e.%d dist.%.2f", entity_id, distance);
		}
	}
	
	/**
	 * normalize vector
	 * 
	 * @param vector
	 * @return
	 */
	public static Vec2f normalize(Vec2f vector) {
		return normalize(vector.getVec(false));
	}
	
	/**
	 * normalize array
	 * 
	 * @param xy
	 * @return
	 */
	public static Vec2f normalize(float[] xy) {
		
		float length = length(xy);
		
		if(length == 1.0) {
			return new Vec2f(xy);
		} else if(length == 0){
			throw new ArithmeticException("Can't normalize vector <0,0>");
		}
		
		return new Vec2f( (xy[0]/ length),  (xy[1]/length));
		
	}
	
	/**
	 * return length of vector
	 * 
	 * @param vec
	 * @return
	 */
	public static float length(Vec2f vec) {
		return length(vec.getVec(false));
	}
	
	/**
	 * return length of vector
	 * 
	 * @param xy
	 * @return
	 */
	public static float length(float[] xy) {
		return (float) Math.sqrt((xy[0]*xy[0] + xy[1] * xy[1]));
	}
	
	/**
	 * return length of vector
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static float length(float x, float y) {
		return (float) Math.sqrt((x*x + y * y));
	}

	/**
	 * add new Vector result of operations
	 * 
	 * @param origin
	 * @param position
	 * @return
	 */
	public static Vec2f add(Vec2f origin, Vec2f position) {
		if(origin == null || position == null) {
			throw new NullPointerException("Can't add null vector");
		}
		return new Vec2f(origin.getX()+position.getX(), origin.getY()+position.getY());
	}

	/**
	 * Checks if Entity is touching a tile in the direction given
	 * 
	 * @param e
	 * @param north2
	 * @return
	 */
	public static boolean isTileBlocking(Entity e, Dir4 north2) {
		
		BoundingBox bb = e.getComponent(RigidBody.class).getChannel(-1);
		
		Location loc = e.getComponent(Location.class);
		
		switch (north2) {
		case NORTH:
			return MathUtils.castRayTiles(e.getWorld(), new Vec2f(bb.left() + loc.getX(), bb.top() + loc.getY()), MathUtils.EAST, bb.getWidth()-0.1f) != null;
		case EAST:
			return MathUtils.castRayTiles(e.getWorld(), new Vec2f(bb.right() + loc.getX(), bb.bottom() + loc.getY()), MathUtils.NORTH, bb.getHeight()-0.1f) != null;
		case SOUTH:
			return MathUtils.castRayTiles(e.getWorld(), new Vec2f(bb.left() + loc.getX(), bb.bottom() + loc.getY()-0.00001f), MathUtils.EAST, bb.getWidth()-0.1f) != null;
		case WEST:
			return MathUtils.castRayTiles(e.getWorld(), new Vec2f(bb.left() + loc.getX() - 0.0001f, bb.bottom() + loc.getY()), MathUtils.NORTH, bb.getHeight()-0.1f) != null;
		default:
			break;
		}
		
		return false;	
	}
	
	/**
	 * return array of result of checking whether entity is touching a tile in the respective directions
	 * 
	 * @param e
	 * @return
	 */
	public static boolean[] isTileBlocking(Entity e) {
		
		BoundingBox bb = e.getComponent(RigidBody.class).getChannel(-1);
		
		Location loc = e.getComponent(Location.class);
		
		boolean[] result = new boolean[4];
	
		result[0] = MathUtils.castRayTiles(e.getWorld(), new Vec2f(bb.left() + loc.getX(), bb.top() + loc.getY()), MathUtils.EAST, bb.getWidth()-0.1f) != null;
		result[1] = MathUtils.castRayTiles(e.getWorld(), new Vec2f(bb.right() + loc.getX(), bb.bottom() + loc.getY()), MathUtils.NORTH, bb.getHeight()-0.1f) != null;
		result[2] = MathUtils.castRayTiles(e.getWorld(), new Vec2f(bb.left() + loc.getX(), bb.bottom() + loc.getY()-0.00001f), MathUtils.EAST, bb.getWidth()-0.1f) != null;
		result[3] = MathUtils.castRayTiles(e.getWorld(), new Vec2f(bb.left() + loc.getX() - 0.0001f, bb.bottom() + loc.getY()), MathUtils.NORTH, bb.getHeight()-0.1f) != null;
		
		return result;	
	}
	
	
	public static float smootstep(float x) {
		if(x <= 0) {
			return 0;
		} else if (x>=1) {
			return 1;
		} else {
			return x*x * (3-2*x);
		}
	}

	/**
	 * returns the unit vector respresented by a direction
	 * 
	 * @param direction
	 * @return
	 */
	public static Vec2f getVec(Dir4 direction) {
		switch(direction) {
		case EAST:
			return EAST;
		case NONE:
			return Vec2f.ZEROE;
		case NORTH:
			return NORTH;
		case SOUTH:
			return SOUTH;
		case WEST:
			return WEST;
		}		
		return null;
	}

	/**
	 * returns new vector containing result of scalar multiplication
	 * 
	 * @param vec
	 * @param speed
	 * @return
	 */
	public static Vec2f scalar(Vec2f vec, float speed) {
		return new Vec2f(vec.getX()*speed, vec.getY()*speed);
	}
	
}
