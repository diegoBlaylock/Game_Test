package engine.render.shapes;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

/**
 * Abstraction of Opengl VBO,
 * Keeps track of vbo pointer and attributes, allowing creation of VBO and editing of attributes
 * @author diego
 *
 */
public class VBO {

	private int vbo;
	private AttrPointer[] pointers;
	int stride = 0;
	
	public VBO() {
		vbo = GL46.glGenBuffers();
	}
	
	public VBO(FloatBuffer buffer, boolean dynamic) {
		vbo = GL46.glGenBuffers();
		this.storeData(buffer, dynamic);
		
	}
	
	public VBO(float[] buffer, boolean dynamic) {
		this(createFloatBuffer(buffer), dynamic);
	}
	
	public void bind() {
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
	}
	
	public void unbind() {
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
	}
	
	public void bindAttr() {
		for(int i = 0; i < pointers.length; i++) {
			GL46.glEnableVertexAttribArray(i);
		}
	}
	
	public void bindAttr(int i) {
		GL46.glEnableVertexAttribArray(i);
	}
	
	public void unbindAttr() {
		for(int i = 0; i < pointers.length; i++) {
			GL46.glDisableVertexAttribArray(i);
		}
	}
	
	public void storeData(FloatBuffer buffer, boolean dynamic) {
		this.bind();
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buffer, (dynamic? GL46.GL_DYNAMIC_DRAW:GL46.GL_STATIC_DRAW));
		
		
		this.unbind();
	}
	
	public void storeData(FloatBuffer buffer, boolean dynamic, AttrPointer...  pointers) {
		this.bind();
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buffer, (dynamic? GL46.GL_DYNAMIC_DRAW:GL46.GL_STATIC_DRAW));
		
		setAttributes(pointers);		
	}
	
	
	public VBO setAttributes(AttrPointer... pointers) {
		int stride = 0;
		int offset = 0;
		
		this.bind();
		
		for(AttrPointer pointer:pointers) {
			stride+=pointer.length();
		}
		
		for(int i = 0; i < pointers.length; i++) {
			

			GL46.glVertexAttribPointer(i,pointers[i].dimensions, pointers[i].dataType, false, stride, offset);
			GL46.glEnableVertexAttribArray(i);
			pointers[i].i = i;
			pointers[i].offset = offset;
			offset+=pointers[i].length();
		}
		
		this.pointers = pointers;
		this.stride = stride;
		this.unbind();
		return this;
	}
	
	
	public void editAttribute(int pointer, float[] newData) {
		this.bind();
		AttrPointer attribute = this.pointers[pointer];
		
		float[] loader = new float[attribute.dimensions];
		for(int i = 0; i < newData.length; i+= attribute.dimensions) {
			System.arraycopy(newData, i, loader, 0, attribute.dimensions);
			
			GL46.glBufferSubData(GL46.GL_ARRAY_BUFFER, attribute.offset + i*stride/attribute.dimensions , loader);
			
		}
			
		this.unbind();
	}
	
	private static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static class AttrPointer {
		int i;
		int offset;
		int dataType;
		int dimensions;
		
		
		public static AttrPointer gen(int DataType, int dimensions) {
			AttrPointer attr = new AttrPointer();
			attr.dataType = DataType;
			attr.dimensions = dimensions;
			return attr;
		}
		
		
		
		public int length() {
			return size4Type(dataType) * dimensions;
		}



		static int size4Type(int dataType) {
			switch(dataType) {
			case(GL46.GL_FLOAT):
				return Float.BYTES;
			case(GL46.GL_INT):
				return Integer.BYTES;
			case(GL46.GL_DOUBLE):
				return Double.BYTES;
			
			}
			return 0;
		}
		
	}

	
	
}
