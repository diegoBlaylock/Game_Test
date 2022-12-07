package test.engine.ecs.system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import engine.ecs.entities.Entity;
import engine.ecs.systems.CollisionSystem;
import engine.physics.BoundingBox;
import engine.physics.Collider;
import engine.physics.Location;
import engine.physics.RigidBody;

public class CollisionTest {

	class PhysicsObject extends Entity{
		public PhysicsObject(float x, float y) {
			this.addComponent(new Location(x,y));
			this.addComponent(new RigidBody());
		}

		public PhysicsObject addBBox(BoundingBox bbox) {
			this.getComponent(RigidBody.class).addBoundingBox(bbox);
			return this;
		}
		
		public void setLoc(float x, float y) {
			this.getComponent(Location.class).set(x, y);
		}

		public void assertLoc(float i, float j) {
			Assertions.assertEquals(i, this.getComponent(Location.class).getX());
			Assertions.assertEquals(j, this.getComponent(Location.class).getY());
		}
	}
	
	PhysicsObject ent_a = new PhysicsObject(0,0).addBBox(new BoundingBox(0, Collider.ICEBERG, 0f, 0, 1, 2)).addBBox(new BoundingBox(1, Collider.PUSHOVER, -0f, 0, 1, 1));
	PhysicsObject ent_b = new PhysicsObject(0,0).addBBox(new BoundingBox(0, Collider.PUSHOVER, -1, 0, 0,1));

	
	@BeforeEach
	void setup() {
		ent_a.setLoc(0, 0);
		ent_b.setLoc(0, 0);

	}
	
	@Test
	void TestIntersects_givenCollsion_returnTrue() {
		ent_a.setLoc(-0.1f, 0);
		Assertions.assertTrue(CollisionSystem.intersects((byte) 0, ent_a, ent_b));	
		
		ent_a.setLoc(-0.1f, -1);
		Assertions.assertTrue(CollisionSystem.intersects((byte) 0, ent_a, ent_b));	
		
		ent_a.setLoc(-0.1f, -1.9f);
		Assertions.assertTrue(CollisionSystem.intersects((byte) 0, ent_a, ent_b));	
	}
	
	@Test
	void TestIntersects_givenNoCollsion_returnFalse() {
		ent_a.setLoc(1f, 0);
		Assertions.assertFalse(CollisionSystem.intersects((byte) 0, ent_a, ent_b));	
		
		ent_a.setLoc(-0.1f, -3);
		Assertions.assertFalse(CollisionSystem.intersects((byte) 1, ent_a, ent_b));	
		
		ent_a.setLoc(-3f, 0f);
		Assertions.assertFalse(CollisionSystem.intersects((byte) 0, ent_a, ent_b));	
	}
	
	@Test
	void TestIntersects_givenEdgeCase_returnFalse() {
		ent_a.setLoc(0f, 0);
		Assertions.assertFalse(CollisionSystem.intersects((byte) 0, ent_a, ent_b));	
		
		ent_a.setLoc(-1f, -2);
		Assertions.assertFalse(CollisionSystem.intersects((byte) 0, ent_a, ent_b));	
		
		ent_a.setLoc(-2f, 0f);
		Assertions.assertFalse(CollisionSystem.intersects((byte) 0, ent_a, ent_b));
		
		ent_a.setLoc(-1f, 1f);
		Assertions.assertFalse(CollisionSystem.intersects((byte) 0, ent_a, ent_b));
	}
	
	@Test
	void TestResolution () {
		//The desired behaviour is not decided, this works will for squares, but for rectangles, it can choose to resolve to strange places
		ent_b.setLoc(0.1f, 0);

		CollisionSystem.resolveCollision((byte) 0, ent_b, ent_a);
		ent_b.assertLoc(0,0);
		
		ent_b.setLoc(1.9f, 0);

		CollisionSystem.resolveCollision((byte) 0, ent_b, ent_a);
		ent_b.assertLoc(2,0);
	}
	
	
}
