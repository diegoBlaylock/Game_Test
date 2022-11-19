package engine.ecs.components;

import java.util.HashMap;
import java.util.Map;

import utils.MathUtils;

/**
 * Entities contain a list of components that function as attributes. These attributes are pulled by systems and modified accordingly
 * 
 * @author diego
 *
 */
public abstract class Component {
	
	static Map<Long, Component> ID_COMPONENT = new HashMap<Long, Component>();
	
	long id = MathUtils.getRandomID();
	boolean enable_flag = true;
	
	public void disable() {
		enable_flag = false;
	}
	public void enable() {
		enable_flag = true;
	}
	public boolean enabled() {
		return enable_flag;
	}
	
	public void cleanup() {
		
	}
	
	
	
	
	
}
