package engine.render;


import org.lwjgl.glfw.GLFW;

import engine.ILogic;
import engine.ecs.components.Rotation;
import engine.ecs.components.ai.BoundedDucklingController;
import engine.ecs.entities.Entity;
import engine.physics.Location;
import main.Main;
import utils.ds.Mat4f;

public class Camera extends Entity implements ILogic{
	
	
	
	public Location loc = new Location(0,0);
	public Rotation rot = new Rotation(0);
	public float zoom = 1.0f;
	public float depth = 16f;
	public transformation transform = new transformation();
	public BoundedDucklingController controller;

	public Camera() {
		this.addComponent(loc);
		this.addComponent(rot);
		controller = new BoundedDucklingController();
		this.addComponent(controller);
	}
	
	@Override
	public void update(long time) {
		controller.update(time);
	}
	


	public void cleanup() {
		
	}
	
	
	
	public class transformation{
		float[] translate = Mat4f.translate(0, 0, 0);
		float[] rotate = Mat4f.rotate(0);
		float[] scale = Mat4f.scale(1);
		
		public float[] getCameraMatrix() {
			
			Mat4f.translate(translate, -loc.getX(), -loc.getY(), -depth);
			Mat4f.rotateZ(rotate, -rot.getTheta());
			Mat4f.scale(scale, zoom);
			
			return Mat4f.mul(translate , Mat4f.mul(rotate, scale));
		}
	}


	
	
	/**
	 * Class that controls the location, depth, and rotation of the Camera
	 * The class should be extended to encapsulate more than just the camera,
	 * but rather all moving entity. AI or pathfinding in a sense.
	 * 
	 * @author diego
	 *
	 */
	public static class CController implements ILogic {
		Camera camera;
		
		public CController(Camera c) {
			camera = c;
		}

		@Override
		public void update(long time) {
			float speed = 0.1f;
			
			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				speed = 5f;
			}
			
			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_W)) {
				camera.loc.shift(0, speed);
			}
			
			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_A)) {
				camera.loc.shift(-speed, 0.0f);
			}

			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_S)) {
				camera.loc.shift(-0.0f, -speed);
			}
			
			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_D)) {
				camera.loc.shift(speed, 0.0f);
			}
			
			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_UP)) {
				camera.depth -= speed;
			}
			
			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
				camera.depth += speed;
			}
			
			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_Q)) {
				camera.rot.addEq(speed);
			}
			
			if(Main.ENGINE.getInputHandler().isKeyDown(GLFW.GLFW_KEY_E)) {
				camera.rot.addEq(-speed);
			}
			
			
		}
	}




}
