package engine.render.utils;

import org.lwjgl.opengl.GL33;

public class Uniform {

	public int id;
	
	public Uniform(ShaderProgram program, String attr) {
		id = GL33.glGetUniformLocation(program.id, attr);
	}
	
	public void set(float value) {
		GL33.glUniform1f(id, value);
		
	}

	public void setMat(float[] scale) {
		GL33.glUniformMatrix4fv(id, true, scale);
	}
	
		
}
