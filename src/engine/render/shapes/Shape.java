	package engine.render.shapes;

import org.lwjgl.opengl.GL46;

/**
 * Contains pointers to vao, vbo, number of indices, and depth of shape
 * 
 * @author diego
 *
 */
public class Shape {
	
	public static final Shape QUAD = ShapeLoader.createQuad(0, 1, 1, 0, 0);
	public static final Shape QUAD2 = ShapeLoader.createQuad(-1, 1, 1, -1, 0);


	private int vao;
	private int vertices;
	private VBO vbo;
	private float depth = 0;
	
	public Shape(int vao, int vertex, float depth, VBO vbo) {
		this.vao = vao;
		this.vertices = vertex;
		this.vbo = vbo;
		this.depth = depth;
	}

	public int getVaoID() {
		return vao;
	}

	public int getVertexCount() {
		return getVertices();
	}

	public int getVertices() {
		return vertices;
	}
	
	public void bind() {
		GL46.glBindVertexArray(vao);
		vbo.bindAttr();
	}
	
	public void bind(int... attr) {
		GL46.glBindVertexArray(vao);
		for(int i:attr) {
			vbo.bindAttr(i);
		}
	}
	
	public void unbind() {
		GL46.glBindVertexArray(0);
		vbo.unbindAttr();
	}

	public VBO getVBO() {
		return vbo;
	}

	public float getDepth() {
		return depth;
	}
	
}
