package engine.ecs.systems;

import java.util.Collection;
import engine.ecs.IEntityFetcher;
import engine.ecs.entities.Entity;
import engine.render.animations.Animation;
import engine.render.utils.sprites.SpriteSheet;

/**
 * Updates sprite sheet from animation component
 * 
 * @author diego
 *
 */
public class AnimationSystem implements ISystem{
	
	@SuppressWarnings("unchecked")
	@Override
	public void processEntities(IEntityFetcher fetcher) {
		Collection<? extends Entity> entities = fetcher.getEntitiesContaining(Animation.class, SpriteSheet.class);
		for(Entity e : entities) {
			
			Animation a = e.getComponent(Animation.class);
					
			if(a.enabled()) {
				a.update(e.getComponent(SpriteSheet.class));
			}
		}
	}
	
	
	

}
