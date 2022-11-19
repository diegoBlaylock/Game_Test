package engine.render.utils;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Color {
	public static final Color BLACK = new Color(0,0,0);
	public static final Color WHITE = new Color(1,1,1);
	public static final Color TRANSPARENT = new Color(0,0,0,0);
	public static final Color RED = new Color(1,0,0);
	public static final Color GREEN = new Color(0,1,0);
	public static final Color BLUE = new Color(0,0,1);
	public static final Color GREY = new Color(0.5f,0.5f,0.5f);
	
	
	FloatBuffer rgb;
	
	public Color(float r, float g, float b) {
		rgb = BufferUtils.createFloatBuffer(4);
		rgb.put(r);
		rgb.put(g);
		rgb.put(b);
		rgb.put(1f);
		rgb.flip();
		
	}
	
	public Color(float r, float g, float b, float a) {
		rgb = BufferUtils.createFloatBuffer(4);
		rgb.put(r);
		rgb.put(g);
		rgb.put(b);
		rgb.put(a);
		rgb.flip();
		
	}
}
