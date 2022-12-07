package engine.render.sprites.animations;

import java.util.Map;

import engine.ecs.entities.Entity;
import engine.ecs.entities.living.Jim;
import engine.ecs.entities.living.Octorok;
import engine.physics.Direction;
import utils.Dir4;

/**
 * This interface simply returns an Animation Component for an Entity, string , and list of arguments.
 * 
 * Furthermore This will be the resting place of the implementations for all the Entities. Hopefully an implementation
 * can be coded to use files rather than hard coding
 * 
 * @author diego
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public interface ILibrary<T extends Entity> {
	
	public Animation getAnimation(T entity, String string, Object[] o);
	
	
	
	
	public class JimLibrary implements ILibrary<Jim> {
		static Map<String, ?> CDS = Map.of(
			"walk", Map.of(
				Dir4.NORTH, new CycleAnimation(8, 2, -3),
				Dir4.EAST, new CycleAnimation(8, 3, 4),
				Dir4.SOUTH, new CycleAnimation(8, 0, 1), 
				Dir4.WEST, new CycleAnimation(8, -4, -5)
			),
			
			"attack", Map.of(
				Dir4.NORTH, new UniAnimation(10),
				Dir4.EAST, new UniAnimation(11),
				Dir4.SOUTH, new UniAnimation(9), 
				Dir4.WEST, new UniAnimation(-12)
			),
			"attack_wane", Map.of(
				Dir4.NORTH, new RampAnimation(2, -2, -3),
				Dir4.EAST, new RampAnimation(2, 3,4),
				Dir4.SOUTH, new RampAnimation(2,0, 1), 
				Dir4.WEST, new RampAnimation(2,-4,-5)
			)
			
		);
		
		@Override
		public Animation getAnimation(Jim entity, String string, Object[] o) {
			Direction dir = entity.getComponent(Direction.class);
			
			switch(string) {
			case "walk":
				return ((Map<Dir4, Animation>) CDS.get(string)).get(dir.getDirection());
			case "attack":
				return ((Map<Dir4, Animation>) CDS.get(string)).get(dir.getDirection());
			case "attack_wane":
				return ((Map<Dir4, Animation>) CDS.get(string)).get(dir.getDirection());
			}
			
			assert false: "Couldn't play animation";
			return null;	
		}
	}
	
	public class OctorokLibrary implements ILibrary<Octorok> {

		@Override
		public Animation getAnimation(Octorok entity, String string, Object[] o) {
			Direction dir = entity.getComponent(Direction.class);
			int speed = (int) o[0];
			
			switch(string) {
			case "walk":
				switch(dir.getDirection()) {
				case NORTH:
					return new CycleAnimation(speed, 16, 17);
				case EAST:
					return new CycleAnimation(speed, -3, -4);
				case SOUTH:
					return new CycleAnimation(speed, 0, 1);
				case WEST:
					return new CycleAnimation(speed, 2, 3);
				default:
					break;
				}
			}
			
			assert false: "Couldn't play animation";
			return null;	
		}

	}
}