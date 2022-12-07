package engine.ecs.components;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import engine.ecs.entities.Entity;
import engine.physics.Location;
import engine.render.Texture;
import engine.render.shapes.Shape;
import engine.render.utils.Graphics;
import engine.render.utils.ShaderProgram;
import utils.ds.Mat4f;

/**
 * Contain a Shape and a Texture and depth.
 * 
 * @author diego
 *
 */
public class RenderComp extends Component{

	Shape shape;
	Texture tex = Texture.DEFAULT; 
	float depth;
	boolean outline = false;
	float[] modelMatrix = new float[16];
	
	public RenderComp(Shape shape) {
		this.shape = shape;
		this.depth = shape.getDepth();
	}

	public RenderComp(Shape quad, Texture texture) {
		this(quad);
		this.appendTexture(texture);
	}

	public void render(ShaderProgram program, Graphics g) {
		
		
		program.uploadTexture("texture", 0);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		tex.bind();
		GL30.glBindVertexArray(shape.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawElements(GL11.GL_TRIANGLES, shape.getVertexCount(), GL11.GL_UNSIGNED_INT,0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	@Override
	public void cleanup() {
		
	}

	public Shape getVAO() {
		return shape;
	}

	public RenderComp appendTexture(Texture sheet) {
		tex = sheet;
		
		return this;
	}

	public float[] getModelMatrix(Entity e) {
		Mat4f.identity(modelMatrix);
		Location loc = e.getComponent(Location.class);
		Rotation rot = e.getComponent(Rotation.class);
		
		if(rot != null) {
			Mat4f.rotateZ(modelMatrix, rot.theta);
		}
		
		if(loc != null) {
			modelMatrix = Mat4f.mul(Mat4f.translate(loc.getX(), loc.getY(), 0) , modelMatrix);
		}
		
		
		return modelMatrix;
	}

	public void updateShape(Shape s) {
		shape = s;
	}

	public float getDepth() {
		return depth;
	}

	public RenderComp setDepth(float i) {
		this.depth = i;
		return this;
	}

}
