package engine.render.sprites.animations;

import java.util.HashMap;
import java.util.Map;

import engine.ecs.entities.Entity;
import engine.ecs.entities.living.Jim;
import engine.ecs.entities.living.Octorok;

/**
 * Helper class to animations. This will map Entities and Strings to a specific animation specified by ILibrary.
 * This can be especially helpful when dealing with lots of conditions such as direction, type, etc...
 * 
 * This is akin to Fetching an album of an Entity and fetching a track to play by name and current entity state
 * 
 * The library needs to be registered. Right now this is hard-coded, but the potential is there to generate ILibraries
 * from files.
 * 
 * @author diego
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Animations {
	
	public static Map<Class<? extends Entity>, ILibrary> LIBRARIES = new HashMap<Class<? extends Entity>, ILibrary>();
	
	// REGISTRATION
	static {
		register(Jim.class, new ILibrary.JimLibrary());
		register(Octorok.class, new ILibrary.OctorokLibrary());
	}
	
	public static <T extends Entity> void register(Class<T> clazz, ILibrary<T> library) {
		LIBRARIES.put(clazz, library);
	}

	/**
	 * play animation for entity identified by string with the following arguments
	 * 
	 * @param <T>
	 * @param entity
	 * @param string
	 * @param o
	 */
	public static <T extends Entity> void play(T entity, String string, Object... o) {
		Animation a = LIBRARIES.get(entity.getClass()).getAnimation(entity, string, o);
		Animation old = entity.getComponent(Animation.class);
		if(old != null) {
			old.reset();
		}
		entity.updateComponent(Animation.class, a);
	}
	
}
