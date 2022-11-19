package engine.ecs.entities.living;

import java.util.ArrayList;

import engine.ecs.components.Speed;
import engine.ecs.components.ai.StateManager;
import engine.ecs.entities.Entity;
import engine.ecs.entities.State;
import engine.physics.BoundingBox;
import engine.physics.Collider;
import engine.physics.Direction;
import engine.physics.Location;
import engine.physics.RigidBody;
import engine.render.Texture;
import engine.render.animations.Animation;
import engine.render.animations.Animations;
import engine.render.utils.sprites.SpriteSheet;
import utils.Dir4;
import utils.MathUtils;

/**
 * Octorok class
 * 
 * Component:
 * 	Location
 * 	Direction
 * 	Speed
 * 	SpriteSheet
 * 	Rigidbody
 * 	StateManager
 * 
 * States:
 * 	Walking
 * 	Shooting
 * 
 * @author diego
 *
 */
public class Octorok extends LivingEntity{

	
	public Octorok() {
		this.addComponent(new SpriteSheet(0, 4, 2, 1, Texture.OCTOROK));
		this.setLocation(0,0);
		this.setDirection(Dir4.get(MathUtils.GLOBAL_RNG.nextInt(0, 4)));
		this.setSpeed(0.1f);
		this.addComponent(new RigidBody(new BoundingBox(-1, Collider.PUSHOVER, -1,-1,1,1), new BoundingBox(0, Collider.PUSHOVER, -1,-1,1,1)));
		this.addComponent(new StateManager(this, new Moving()));
		
	}
	
	public class Moving implements State{
		
		long movingSteps;
		long shootSteps;
		
		@Override
		public void enter(Entity e) {
			movingSteps = MathUtils.GLOBAL_RNG.nextLong(50, 500);
			shootSteps = MathUtils.GLOBAL_RNG.nextLong(600, 1000);
		}

		@Override
		public void update(Entity e) {
			if(shootSteps <= 0) {
				e.getComponent(StateManager.class).pushState(e, new Shoot());
				shootSteps = MathUtils.GLOBAL_RNG.nextLong(1000, 10000);

				return;
			}
			
			Direction d = e.getComponent(Direction.class);
			
			boolean changed = false;
			
			boolean[] blocked =  MathUtils.isTileBlocking(e);
			
			if(movingSteps <= 0 || blocked[d.getDirection().ordinal()]) {

				ArrayList<Dir4> viableDirections = new ArrayList<Dir4>(4);
				
				for(int i = 0; i < 4; i++) {
					if(!(blocked[i] || d.getDirection() == Dir4.get(i)) ) {
						viableDirections.add(Dir4.get(i));
					}
				}
				
				changed = true;
				
				d.setDirection(viableDirections.get(MathUtils.GLOBAL_RNG.nextInt(viableDirections.size())));
			}
			
			float speed = (float) (e.getComponent(Speed.class).getSpeed());
			Location loc = e.getComponent(Location.class);
			Animation anime = e.getComponent(Animation.class);
			
			
			if(anime == null) {
				changed = true;
			} else {
				anime.enable();
			}
			
			
			loc.shift(MathUtils.scalar(MathUtils.getVec(d.getDirection()), speed));
			
			if(changed) {
				Animations.play(e, "walk", 8);
			}
			
			if(movingSteps<= 0) {
				movingSteps = MathUtils.GLOBAL_RNG.nextLong(50, 500);
			}
			
			movingSteps --;
			shootSteps--;
		}

		@Override
		public void exit(Entity e) {
			
		}
	}
	
	/**
	 * 
	 * 
	 * @author diego
	 *
	 */
	public class Shoot implements State{

		int cooldown;
		
		@Override
		public void enter(Entity e) {
			cooldown = 100;
			e.getComponent(Animation.class).disable();
		}

		@Override
		public void update(Entity e) {
			
			if(cooldown <=0) {
				if(Math.random() > 0) {
					e.getComponent(StateManager.class).reset();
					e.delete();
					return;
				}
				e.getComponent(StateManager.class).popState(e);
			}
			cooldown--;
		}

		@Override
		public void exit(Entity e) {
			
		}
		
	}
	
	
	public static class Idle implements State{

		@Override
		public void enter(Entity e) {
			
		}

		@Override
		public void update(Entity e) {
			
		}

		@Override
		public void exit(Entity e) {
			
		}
		
	}
	
	
}
