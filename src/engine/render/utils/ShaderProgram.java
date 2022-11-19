package engine.render.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL46;

import utils.ds.Vec2f;

public class ShaderProgram {
	
	int id;
	
	List<Integer> shaders = new ArrayList<Integer>(5);
	
	Map<String, Integer> uniforms = new HashMap<String, Integer>(5);
	
	public ShaderProgram() {
		id = GL33.glCreateProgram();
		
	}
	
	public void attach(Shader shader) {
		GL33.glAttachShader(id, shader.shader_id);
		shaders.add(shader.shader_id);
		GL33.glLinkProgram(id);
		shader.delete();
	}
	
	public void deattach(Shader shader) {
		GL33.glDetachShader(id, shader.shader_id);
		shader.delete();
	}
	
	private int location(String name) {
		int loc = GL33.glGetUniformLocation(id, name);
		
		if(!uniforms.containsKey(name)) {
			uniforms.put(name, loc);
		}
		
		return loc;
	}
	
	public void uploadMatrix4f(String name, float[] matrix) {
		use();
		GL33.glUniformMatrix4fv(location(name), false, matrix);
	}
	
	public void uploadTexture(String name, int texID) {
		use();
		GL33.glUniform1i(location(name), texID);
	}
	
	public void uploadVec2f(String name, Vec2f vec) {
		use();
		GL33.glUniform2fv(location(name), vec.getVec(false));
	}
	
	public void uploadColor(String name, Color c) {
		use();
		GL33.glUniform4fv(location(name), c.rgb);
	}
	
	
	public void use() {
		GL33.glUseProgram(id);
	}
	
	public void delete() {
		GL33.glDeleteProgram(id);
	}

	public void cleanup() {
		
		this.delete();
	}

	public void upload(String name, float size) {
		use();
		GL46.glUniform1f(location(name), size);
	}
		
	
}
