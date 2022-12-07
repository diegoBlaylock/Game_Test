package main;

import org.lwjgl.glfw.GLFW;

import engine.Engine;
import engine.ecs.systems.Render;
import engine.input.InputHandler;
import engine.render.gui.Window;
import engine.timer.Grandpa;
import engine.world.World;

public class Main {
	
	public static Engine ENGINE;
	
	public static void main(String[] args) {
		
		try {
		
			InputHandler ih = new InputHandler();
					
			
			Window w = Window.createWindow("Game Instance", 1000, 800, true);
			GLFW.glfwShowWindow(w.getID());
			
			Render r = new Render(w);
			
			ENGINE = new Engine(w, new World(), r, ih);
			
			w.setKeyCallback(ih);
			w.setResizeCallback(ENGINE::resize);
			
			Grandpa.start();
			ENGINE.init();
		
		} catch(Exception e) {
			System.err.println("Sorry and Error was Encountered");
			e.printStackTrace();
		}
		
	}
}
