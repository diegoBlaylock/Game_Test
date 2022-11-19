package engine.render;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import utils.MathUtils;

public class Texture {
	public final static Map<String, Texture> textures= new HashMap<String, Texture>();

	public final static Texture PLAYER = new Texture("res/textures/link.png", false);
	public final static Texture ITEMS16 = new Texture("res/textures/items16.png", false);
	public final static Texture OCTOROK = new Texture("res/textures/octorok.png",false);
	public final static Texture DEFAULT = new Texture("res/textures/default.png", false);

	
	int pointer;
	int width;
	boolean initialized = false;
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getFilePath() {
		return filePath;
	}

	int height;
	String filePath;
	String identifier;
	
	
	
	
	public Texture(String path, String name, boolean init) {

		if(Files.notExists(Path.of(path))) {
				assert false : "Couldn't upload file!";
		}
		
		filePath = path;
		
		identifier = name;
		
		Texture.textures.put(name, this);
		
		if(init) {
			this.init();
		}
		
	}
	
	public Texture(String path, boolean init) {
		this(path, MathUtils.getUUID(), init);
		
	}
	
	public static void start() {
		for(Texture t : textures.values()) {
			if(!t.initialized) {
				t.init();
			}
		}
		
	}
	
	public void init() {
		initialized = true;
		pointer = GL46.glGenTextures();

		GL46.glBindTexture(GL46.GL_TEXTURE_2D, pointer);
		
		GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
		GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);
		GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);		
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		
		ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 0);
		
		if(image != null) {
			GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, (channels.get(0) == 4? GL46.GL_RGBA: GL46.GL_RGB), width.get(0),height.get(0) ,0 ,(channels.get(0) == 4? GL46.GL_RGBA: GL46.GL_RGB),GL46.GL_UNSIGNED_BYTE,image);
		}
		
		STBImage.stbi_image_free(image);
		
		this.width = width.get(0);
		this.height = height.get(0);

	}
	
	public void bind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, pointer);
	}
	
	public void unbind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
	}
	
}
