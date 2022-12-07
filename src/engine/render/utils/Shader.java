package engine.render.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.opengl.GL33;

public class Shader {
	static Shader default_vs;
	static Shader default_frag;
	
	int shader_id;
	
	String path;
	
	String code;
	
	public Shader(int type, String source) throws IOException {
		path = source;
		
		code = Files.readString(Paths.get(path));
		shader_id = GL33.glCreateShader(type);
		GL33.glShaderSource(shader_id, code);
		GL33.glCompileShader(shader_id);
	}

	public void delete() {
		GL33.glDeleteShader(shader_id);
	}

	public static Shader defaultVertex() {
		if(default_vs == null) {
			try {

				default_vs = new Shader(GL33.GL_VERTEX_SHADER, "src/engine/render/utils/shaders/vs/default.vs");
			} catch (IOException e) {
				assert false;
			}
		}
		
		return default_vs;
	}
	
	
	public static Shader defaultFragment() {
		if(default_frag == null) {
			try {

				default_frag = new Shader(GL33.GL_FRAGMENT_SHADER, "src/engine/render/utils/shaders/frag/default.frag");
			} catch (IOException e) {
				assert false;
			}
		}
		
		return default_frag;
	}

	public String getCode() {
		// TODO Auto-generated method stub
		return code;
	}
	
	
}
