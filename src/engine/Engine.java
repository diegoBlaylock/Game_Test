package engine;

import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import engine.ecs.entities.Entity;
import engine.ecs.entities.living.Jim;
import engine.ecs.entities.living.Octorok;
import engine.ecs.systems.AnimationSystem;
import engine.ecs.systems.CollisionSystem;
import engine.ecs.systems.ContollerSystem;
import engine.ecs.systems.Render;
import engine.ecs.systems.StateSystem;
import engine.ecs.systems.SystemsManager;
import engine.events.PostOffice;
import engine.input.IAction;
import engine.input.InputHandler;
import engine.physics.Location;
import engine.render.Texture;
import engine.render.gui.Window;
import engine.render.gui.components.TextField;
import engine.render.utils.Color;
import engine.timer.Grandpa;
import engine.world.World;
import engine.world.tile.TileMap;

/**
 * Overarching class maintaining instances of core classes such as render, window, world, etc...
 * Runs the game loop and initializes the world and systems
 * 
 * @author diego
 *
 */
public class Engine {

	Render render = null;
	Window window;
	World world;
	InputHandler ih;
	SystemsManager systemsManager = new SystemsManager();
	
	long player_id;
	TextField fps = new TextField(-1f,1f, "FPS: ");
	
	static final int tps = 60; 
	
	public Engine(Window w, World world2, Render render2, InputHandler ih) {
		this.window = w;
		this.world = world2;
		this.render = render2;
		this.ih = ih;
		
		fps.setForeground(Color.WHITE);
		fps.getFont().scale = 0.05f;
	}

	public void init() {
		//Start textures, so this doesn't crash because of opengl
		Texture.start();
		
		//Spawn 4 Octoroks
		ih.bind(GLFW.GLFW_KEY_SPACE, new IAction() {
			@Override
			public void invoke(int key, int action, int mods) {
				if(action == GLFW.GLFW_PRESS) {
					for(int i = 0 ;i < 4; i++) {
						Octorok o = new Octorok();
						o.getComponent(Location.class).shift(16, 32);
						world.addEntity(o);
					}
				}
			}
		});
		
		//Set up Systems, order matters
		systemsManager.addSystem(new StateSystem());
		systemsManager.addSystem(new CollisionSystem());
		systemsManager.addSystem(new ContollerSystem());
		systemsManager.addSystem(new AnimationSystem());

		//Add Temporary fps counter
		window.add(fps);

		
		//Try and load Map along with player
		try {
			world.loadMap(TileMap.load(world ,"res/maps/0.dat"));				
			
			Jim player = new Jim();
			player_id = player.EID();
			world.addEntity(player);
			world.addEntity(render.getCamera());
			render.getCamera().controller.tether(player);
		} catch (IOException e) {
			assert false: "Couldn't load world file";
		}
		
		
		this.run();
	}
	
	/**
	 * Runs game at the specified ups (updates per second)
	 * 
	 * Sets up some maintance variables and the follows the following order of update:
	 * - Grab and distributes Events
	 * - update world and entities through update method
	 * - update entity data through systems
	 * - Render to screen
	 * 
	 * The Thread is put to sleep to maintain the ups
	 */
	public void run() {
		
		long milliPerRun = 1000 /tps;
		long lastTick = System.currentTimeMillis();
		int avg = 10;
		int index = 0;
		long sum = 0;
		
		while(!GLFW.glfwWindowShouldClose(getWindow().getID())) {
			
			long start = System.currentTimeMillis(); 
			
			getWindow().pollEvents();
			PostOffice.pollEvents();
			
			
			//Logic
			long dTick = System.currentTimeMillis() - lastTick;
			
			world.update(dTick);
			systemsManager.runSystems(world.getFetcher());
			
			lastTick = System.currentTimeMillis();
			
			//Render
			render.render(getWindow(), world);
			
			//Sleep
			try {
				Thread.sleep(Math.max(0, milliPerRun - (System.currentTimeMillis() - start)));
			} catch (InterruptedException e) {return;}
			
			sum+= ((System.currentTimeMillis() - start) ); //Used in ups average
			
			// Update fps counter
			if(index == 0) {
				fps.setText( String.format("FPS- %.2f\nPos- %s\nNum- %d", (1000.0 * avg) / sum, Entity.fetch(player_id).getComponent(Location.class), world.getFetcher().getEntities().size()));
				index = avg;
				sum = 0;
			}
			
			index--;
		}
		
		//Shut down all core classes
		getWindow().cleanup();
		world.cleanup();
		render.cleanup();
		Grandpa.cleanup();
		
	}
	
	
	public void resize(long window, int width, int height) {
		//TODO
	}
	
	public InputHandler getInputHandler() {
		return ih;
	}
	
	public Window getWindow() {
		return window;
	}

	public Jim getPlayer() {
		return (Jim) Entity.fetch(player_id);
	}
	
}
