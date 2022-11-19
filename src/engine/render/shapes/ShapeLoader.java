package engine.render.shapes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import engine.render.shapes.VBO.AttrPointer;

/**
 * creates Shapes based on vertices
 * 
 * TODO abstract more, especially editing shape based on pointers, like in spritesheet
 * 
 * @author diego
 *
 */
public class ShapeLoader {

	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	
	private static IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private static void storeData(int attribute, int dimensions, FloatBuffer buffer, boolean dynamic) {
		int stride = (dimensions + 2) * Float.BYTES;
		
		int vbo = GL15.glGenBuffers(); //Creates a VBO ID
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo); //Loads the current VBO to store the data
		
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, (dynamic? GL46.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW));
		GL20.glVertexAttribPointer(attribute, dimensions, GL11.GL_FLOAT, false, stride, 0);
		GL46.glEnableVertexAttribArray(attribute);
		GL20.glVertexAttribPointer(attribute+1, 2, GL11.GL_FLOAT, false, stride, dimensions*Float.BYTES);
		GL46.glEnableVertexAttribArray(attribute+1);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unloads the current VBO when done.

	}
	
	private static void bindIndices(int[] data) {
		IntBuffer buffer = createIntBuffer(data);
		bindIndices(buffer);
		
	}
	
	private static void bindIndices(IntBuffer buffer) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
	}
	
	public static Shape createShape(float[] positions, int[] indices) {

		int vao = genVAO();

		VBO vbo = new VBO(positions, true).setAttributes(
				AttrPointer.gen(GL46.GL_FLOAT, 3), AttrPointer.gen(GL46.GL_FLOAT,  2));
		bindIndices(indices);
		GL30.glBindVertexArray(0);
		return new Shape(vao,indices.length, positions[2], vbo);
	}
	
	public static Shape createDShape(float[] positions, int[] indices) {

		int vao = genVAO();
		VBO vbo = new VBO(positions, true).setAttributes(
					AttrPointer.gen(GL46.GL_FLOAT, 3), AttrPointer.gen(GL46.GL_FLOAT,  2));
		bindIndices(indices);
		GL30.glBindVertexArray(0);
		return new Shape(vao,indices.length, positions[2], vbo);
	}
	
	public static Shape createQuad(float x1, float y1, float x2, float y2, float z) {
		return createShape(new float[] {x1,y1, z,    0f, 0f, 
										x1,y2, z,    0f, 1f, 
										x2,y2, z,    1f, 1f, 
										x2,y1, z,    1f, 0f}, new int[] {0,1,2,0,2,3});
	}
		
	private static int genVAO() {
			int vao = GL30.glGenVertexArrays();

			vaos.add(vao);
			GL30.glBindVertexArray(vao);
			return vao;
	}

	public static Shape createTexShape(FloatBuffer vertices, IntBuffer idxs) {
		int vao = genVAO();
		int length = idxs.limit();
		VBO vbo = new VBO(vertices, true).setAttributes(
				AttrPointer.gen(GL46.GL_FLOAT, 3), AttrPointer.gen(GL46.GL_FLOAT,  2));
		bindIndices(idxs);
		GL30.glBindVertexArray(0);
		return new Shape(vao,length, vertices.get(2), vbo);
	}
	
	public static Shape createShape2D(FloatBuffer vertices, IntBuffer idxs) {
		int vao = genVAO();
		int length = idxs.limit();
		VBO vbo = new VBO(vertices, true).setAttributes(
				AttrPointer.gen(GL46.GL_FLOAT, 2));
		bindIndices(idxs);
		GL30.glBindVertexArray(0);
		return new Shape(vao,length, 0,  vbo);
	}
	
	public static Shape createShape2D(float[] vertices, int[] idxs) {
		int vao = genVAO();
		int length = idxs.length;
		VBO vbo = new VBO(vertices, true).setAttributes(
				AttrPointer.gen(GL46.GL_FLOAT, 2));
		
		bindIndices(idxs);
		GL30.glBindVertexArray(0);
		return new Shape(vao,length,0, vbo);
	}

	public static void updateShape(int vao, FloatBuffer vertices) {
		GL46.glBindVertexArray(vao);
		storeData(0, 3, vertices, false);
		GL30.glBindVertexArray(0);

	}

	public static Shape createSimpleLine(float x1, float y1, float x2, float y2) {
		return createShape2D(new float[] {x1, y1, x2, y2}, new int[] {0,1});
	}

	public static Shape createSimpleQuad(float x, float y, float width, float height) {
		return createShape2D(new float[] {x, y + height, x, y, x + width, y, x+width, y + height}, new int[] {0,1,2,0,2,3});
	}
	

	
}
