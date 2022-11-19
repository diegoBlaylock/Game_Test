package engine.ecs.entities.living;

import org.lwjgl.glfw.GLFW;

import engine.ecs.components.Speed;
import engine.ecs.components.ai.StateManager;
import engine.ecs.entities.Entity;
import engine.ecs.entities.State;
import engine.ecs.entities.tools.Sword;
import engine.events.PostOffice;
import engine.events.PostOffice.Scope;
import engine.events.events.EntityEvent;
import engine.events.listeners.EntityListener;
import engine.input.IAction;
import engine.physics.BoundingBox;
import engine.physics.Collider;
import engine.physics.Direction;
import engine.physics.Location;
import engine.physics.RigidBody;
import engine.render.Texture;
import engine.render.animations.Animation;
import engine.render.animations.Animations;
import engine.render.utils.sprites.SpriteSheet;
import main.Main;
import utils.Dir4;
import utils.MathUtils;

/**
 * Our Player Class. Don't ask me why it's named that
 * 
 * Component:
 * 	Location
 * 	Speed
 * 	Direction
 * 	RigidBody(-1, 0)
 * 	StateManager
 * 	Animation
 * 	SpriteSheet
 * 
 * States:
 * 	IdleState
 * 	Attack
 * 
 * @author diego
 *
 */
public class Jim extends LivingEntity implements EntityListener{
	
	public boolean walking = false;
	public int walking_counter = 0;
	public int counter = 0;
	
	public Jim() {
		super();
		this.addComponent(new SpriteSheet(0,14,16, 1, Texture.PLAYER));
		this.setLocation(16,32);
		this.setSpeed(0.25f);
		this.setDirection(Dir4.SOUTH);
		this.addComponent(new RigidBody(new BoundingBox(0, Collider.PUSHOVER, -1, -1, 1, 1), 
				new BoundingBox(-1, Collider.PUSHOVER, -1, -1, 1, 0)));
		this.addComponent(new StateManager(this, new Idle()));
		
		//Subscribe to EntityEvent TEST CASE
		PostOffice.subscribe(EntityEvent.class, this.EID(), Scope.PUBLIC, this);
	}
	
	@Override
	public void update(long time) {
		super.update(time);		
	}

	
	/**
	 * Mini key input handler. Mostly set up for making switching key layouts easier.
	 * 
	 * Receives keyBoard input and keeps track of what keys are pressed
	 *  
	 * @author diego
	 *
	 */
	static class KeyRecord implements IAction {
		
		static int[] keys = new int[] {
			GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D,
			GLFW.GLFW_KEY_SEMICOLON, GLFW.GLFW_KEY_APOSTROPHE
		};
		
		static KeyRecord record = new KeyRecord();
		
		
		boolean[] press_record = new boolean[keys.length];
		
		public KeyRecord() {
			for(int key : keys) {
				Main.ENGINE.getInputHandler().bind(key, this);
			}
		}
		
		@Override
		public void invoke(int key, int action, int mods) {
		
			
			switch(action) {
			case GLFW.GLFW_PRESS:
				press_record[indexOf(key)] = true;
				
				break;
			case GLFW.GLFW_RELEASE:
				press_record[indexOf(key)] = false;
				break;
			}
		}
		
		public boolean isPressed(int i) {
			return press_record[indexOf(i)];
		}

		private int indexOf(int keyCode) {
			
			for(int j = 0; j < keys.length; j++) {
				if(keys[j] ==keyCode) {
					return j;
				}
			}
			
			assert false:"Key Doesn't exist";
			return -1;
		}
	}
		
	/**
	 * This state will handle what happens when Idle and walking.
	 * 
	 * @author diego
	 *
	 */
	public class Idle implements State {
		@Override
		public void enter(Entity e) {
			
		}

		@Override
		public void update(Entity e) {
			Location loc = e.getComponent(Location.class);
			Direction dir = e.getComponent(Direction.class);
			float speed = e.getComponent(Speed.class).getSpeed();
						
			boolean changed = false;
			
			// Is attack button pressed
			if(KeyRecord.record.isPressed(KeyRecord.keys[4])) {
				//swap to Attack State
				e.getComponent(StateManager.class).pushState(e, new SwordAttack());
				return;
			}			
			
			// It is important to check for blocking, since jim will prefer up and down movement to left and right unless blocked
			boolean[] blocked = MathUtils.isTileBlocking(e);
			
			if((KeyRecord.record.isPressed(KeyRecord.keys[0]) && !blocked[0]) ^ (KeyRecord.record.isPressed(KeyRecord.keys[1]) && !blocked[2])) {
				if(KeyRecord.record.isPressed(KeyRecord.keys[0])) {
					
					if(dir.getDirection() != Dir4.NORTH) {
						changed = true;
						dir.setDirection(Dir4.NORTH);
					}
					
					loc.shift(0, speed);
				} 
				
				if(KeyRecord.record.isPressed(KeyRecord.keys[1])) {
				
					if(dir.getDirection() != Dir4.SOUTH) {
						changed = true;
						dir.setDirection(Dir4.SOUTH);
					}
					
					loc.shift(0, -speed);	
				}
			}else  {
				if(KeyRecord.record.isPressed(KeyRecord.keys[2])&& !blocked[3]) {
					
					if(dir.getDirection() != Dir4.WEST) {
						changed = true;
						dir.setDirection(Dir4.WEST);
					}
					
					loc.shift(-speed, 0);
				}
				
				if(KeyRecord.record.isPressed(KeyRecord.keys[3]) && !blocked[1]) {
					
					if(dir.getDirection() != Dir4.EAST) {
						changed = true;
						dir.setDirection(Dir4.EAST);
					}
					
					loc.shift(speed, 0);
				}
			}
			
			if(changed) {
				//Need to reset animation to new direction
				Animations.play(e, "walk");
				changed = false;
			}
			
			if(e.getComponent(Animation.class) != null){
				if((KeyRecord.record.isPressed(KeyRecord.keys[0]) ^ KeyRecord.record.isPressed(KeyRecord.keys[1])) || (KeyRecord.record.isPressed(KeyRecord.keys[2]) ^ KeyRecord.record.isPressed(KeyRecord.keys[3]))) {
					e.getComponent(Animation.class).enable();
				} else {
					e.getComponent(Animation.class).disable();
				}
			}
		}

		@Override
		public void exit(Entity e) {
			
		}
	}
	
	/**
	 * State creates sword and handles its and jim's animation
	 * 
	 * @author diego
	 *
	 */
	public static class SwordAttack implements State {
		
		static float speed = 0.3f;
		static float init = 4;
		static float stay = 9;
		static float close = 2;
		
		Sword sword;
		
		boolean isInit = true;
		boolean sheathing = false;
		
		float time = 0f;
		
		@Override
		public void enter(Entity e) {
			sword = new Sword((LivingEntity) e);
			
			if(e.contains(Animation.class)) {
				e.getComponent(Animation.class).disable();
			}
						
		
			Animations.play(e, "attack");		
		}

		@Override
		public void update(Entity e) {
			Location loc = sword.getComponent(Location.class);
			Location source = e.getComponent(Location.class);
			Direction d = sword.getComponent(Direction.class);
			
			if(isInit) {
				
				if(time == init) {
					isInit = false;
					setLoc(1, loc, source, d);
					e.getWorld().addEntity(sword);
					time = 0;
				}
				
				time++;			
			} else {
				
				if(sheathing) {	
					if(time == close) {
						e.getComponent(StateManager.class).popState(e);
						return;
					}
					
					setLoc(1-(time/(close+1)), loc, source, d);
					time++;
				}else {
					if(time == stay) {
						time=0;
						Animations.play(e, "attack_wane");
						sheathing = true;
						
					}
					
					time++;
				}
			}
		}

		@Override
		public void exit(Entity e) {
			sword.delete();
			Animations.play(e, "walk");
		}
		
		//Helper to set location of sword
		void setLoc(float num, Location loc, Location source, Direction d) {
			final float extent = 1.5f;
			
			switch(d.getDirection()) {
			case NORTH:
				loc.set(source.getX(), source.getY() + (extent * num));
				break;
			case EAST:
				loc.set(source.getX()+ (extent * num), source.getY() );
				break;
			case SOUTH:
				loc.set(source.getX(), source.getY() - (extent * num));
				break;
			case WEST:
				loc.set(source.getX()- (extent * num), source.getY());
				break;
			default:
				assert false: "Nope";
			}
		}
	}

	@Override
	public void onSpawn(EntityEvent event, Entity e) {
		System.out.println("Behold! entity " + e + " has entered the world " + e.EID());
	}

	@Override
	public void onDespawn(EntityEvent event, Entity e) {
		System.out.println("Behold! entity " + e + " has LEFT the world " + e.EID());
		
	}
	
}
