package engine.ecs.systems;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import engine.ecs.components.RenderComp;
import engine.ecs.entities.Entity;
import engine.physics.Location;
import engine.render.Camera;
import engine.render.gui.Window;
import engine.render.utils.Graphics;
import engine.render.utils.Shader;
import engine.render.utils.ShaderProgram;
import engine.ecs.IEntityFetcher;
import engine.world.World;
import utils.ds.Mat4f;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.lwjgl.glfw.GLFW;

/**
 * Renders a world to screen
 * 
 * TODO needs simplifying and to be combined to use the Graphics class much more.
 * 
 * TODO the view needs to be modifiable, preferrably being render to GComponent. Perhaps use different buffer to hand to component
 * 
 * @author diego
 *
 */
public class Render implements ISystem{
	
	ShaderProgram program;
	
	Graphics g;
	
	Shader vs;
	Shader frag;
	
	Camera camera = new Camera();
	
	Callback glErrorCallback;
	
	float[] viewMatrix = Mat4f.identity();
	
    public Render(Window w) {
        GL.createCapabilities();
        
        vs = Shader.defaultVertex();
        frag = Shader.defaultFragment();
        
        program = new ShaderProgram();
        program.attach(vs);
        program.attach(frag);
        
        vs.delete();
        frag.delete();

        g = new Graphics(w);
        
        glErrorCallback = GLUtil.setupDebugMessageCallback();
     }
       

    public void cleanup() {
        program.cleanup();
        vs.delete();
        frag.delete();
        camera.cleanup();
    }
    
   

    @SuppressWarnings("unchecked")
	public void render(Window window, World scene) {
		
		List<Entity> renderables = new ArrayList<Entity>(scene.getFetcher().getEntitiesContaining(RenderComp.class, Location.class));
		
		renderables.sort(new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				return (int) Math.signum((o1.getComponent(RenderComp.class).getDepth() - o2.getComponent(RenderComp.class).getDepth()));
			}
			
		});
		
    	glClear(GL_COLOR_BUFFER_BIT);
    	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    	glEnable( GL46.GL_BLEND);
    	
    	program.use();
		program.uploadMatrix4f("worldMatrix", camera.transform.getCameraMatrix());
		program.uploadMatrix4f("projectionMatrix", window.mainView.getViewMatrix());
		
		
		
    	for(Entity e: renderables) {
    		RenderComp r = e.getComponent(RenderComp.class);
    		program.uploadMatrix4f("modelMatrix", r.getModelMatrix(e));


    		r.render(program, g);
	    	
    	}
    	
    	window.paint(g);
    	
		GLFW.glfwSwapBuffers(window.getID());
        
        
    }


	public Camera getCamera() {
		return camera;
	}


	@Override
	public void processEntities(IEntityFetcher fetcher) {
		
	}
}