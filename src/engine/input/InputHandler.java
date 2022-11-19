package engine.input;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import main.Main;

public class InputHandler implements GLFWKeyCallbackI{

	private Map<Long, Map<Integer, List<IAction>>> key_bindings = new HashMap<Long, Map<Integer, List<IAction>>>();
	
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if(!key_bindings.containsKey(window)) {
			key_bindings.put(window, new HashMap<Integer, List<IAction>>());
		}
		
		
		
		Map<Integer, List<IAction>> windowBindings = key_bindings.get(window);

		if(windowBindings.containsKey(key)) {
			if(windowBindings.get(key).isEmpty()) {
				windowBindings.remove(key);
				return;
			}
			
			
			for(IAction a : windowBindings.get(key)) {
				a.invoke(key, action, mods);
			}
		}
		
		if(window!=0) {
			invoke(0, key, scancode, action, mods);
		}
	}
	
	public void bind(long window, int keycode, IAction a) {
		if(!key_bindings.containsKey(window)) {
			key_bindings.put(window, new HashMap<Integer, List<IAction>>());
		}
		
		if(!key_bindings.get(window).containsKey(keycode)) {
			key_bindings.get(window).put(keycode, new LinkedList<IAction>());
		}
		
		
		key_bindings.get(window).get(keycode).add(a);
		

	}
	
	public void bind(int keycode, IAction a) {
		bind(0, keycode, a);
	}
	
	public void unbind(long window, int keycode, IAction a) {
		if(key_bindings.containsKey(window)) {
			
			if(key_bindings.get(window).containsKey(keycode)) {
				key_bindings.get(window).get(keycode).remove(a);
			}
		}
	}
	
	public void unbind(int keycode, IAction a) {
		unbind(0, keycode, a);
	}

	public boolean isKeyDown(int glfwKeyW) {
		return Main.ENGINE.getWindow().isKeyPressed(glfwKeyW);
	}
	
	
	
}