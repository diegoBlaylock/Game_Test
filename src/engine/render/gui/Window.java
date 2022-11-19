package engine.render.gui;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;

public class Window extends GContainer{
	private static boolean INIT = false;
	
	private final long handle;
	private int width;
	private int height;
	private GLFWFramebufferSizeCallbackI resizeCallback;
	public final View mainView = new View(90f, width, height,0,0, 0.01f, 100f);
	
	public Window(String title, Window.Options opts, GLFWFramebufferSizeCallbackI resizeFn,  GLFWKeyCallbackI keyFn) {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!Window.INIT){
			if(! GLFW.glfwInit()) {
				throw new IllegalStateException("Unable to initialize");
			}
			
			Window.INIT = true;
		}
		
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
				
		if (opts.width > 0 && opts.height > 0) {
            this.width = opts.width;
            this.height = opts.height;
        } else {
        	glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
           GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            width = vidMode.width();
            height = vidMode.height();
        }
		
		handle = glfwCreateWindow(this.width, this.height, "Hello World!", NULL, NULL);

		if (handle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
		
		GLFW.glfwSetFramebufferSizeCallback(handle, (window, w, h) -> this.onResize(w, h));
		GLFW.glfwSetKeyCallback(handle, keyFn);
		GLFW.glfwMakeContextCurrent(handle);
		
		if (opts.fps > 0) {
            GLFW.glfwSwapInterval(0);
        } else {
            GLFW.glfwSwapInterval(1);
        }

        GLFW.glfwShowWindow(handle);
		
		this.resizeCallback = resizeFn;
		this.mainView.resize(width, height);
		
	}
	
	public void setResizeCallback(GLFWFramebufferSizeCallbackI callback) {
		this.resizeCallback = callback;
	}
	
	public void setKeyCallback(GLFWKeyCallbackI callback) {
		GLFW.glfwSetKeyCallback(handle, callback);

	}
	
	protected void onResize(int width, int height) {
		this.width = width;
        this.height = height;
        GL11.glViewport(0,0, width, height);
        
        this.mainView.resize(width, height);
        
        try {
            this.resizeCallback.invoke(height, width, height);
        } catch (Exception excp) {
        	System.err.println("Error calling resize callback: "+ excp);
        }
        
        
	}
	
	public void cleanup() {
	
		Callbacks.glfwFreeCallbacks(handle);
		GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
	}
	public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(handle, keyCode) == GLFW.GLFW_PRESS;
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }
    
    public static Window createWindow(String title, int width, int height, boolean vsync) {
		Options o = new Options();
		o.width = width;
		o.height = height;
		if(!vsync) {
			o.fps = 30;
		}
    	
    	return new Window(title, o, null, null);
    	
    }
    
	public static class Options {
		public int fps;
		public int width;
		public int height;
	}
	public long getID() {
		return handle;
	}
}
