package engine.render.utils;


import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import engine.render.Texture;
import engine.render.gui.FTFHandler;
import engine.render.gui.IFontHandler;
import engine.render.shapes.Shape;
import engine.render.shapes.ShapeLoader;
import utils.MathUtils;
import utils.ds.Vec2f;

/**
 * TODO organize shaders a bit to allow for texture, and projection
 * 
 * @author diego
 *
 */
public class Graphics {
	
	IFontHandler fontHandler;
	
	Color c = Color.BLACK;
		
	Deque<Vec2f> offset_stack = new LinkedList<Vec2f>();
	
	public Graphics() {
		offset_stack.push(Vec2f.ZEROE);
		fontHandler = new FTFHandler("res/fonts/nes.ftf");
	}
	
	public static ShaderProgram simple_shader;
	public static ShaderProgram text_shader;
	
	
	static {
		try {
		
			simple_shader = new ShaderProgram();			
			simple_shader.attach(new Shader(GL46.GL_VERTEX_SHADER, "src/engine/render/utils/shaders/graphics.vs"));
			simple_shader.attach(new Shader(GL46.GL_FRAGMENT_SHADER, "src/engine/render/utils/shaders/simplecolor.frag"));
			
			text_shader = new ShaderProgram();
			text_shader.attach(new Shader(GL46.GL_VERTEX_SHADER, "src/engine/render/utils/shaders/text.vs"));
			text_shader.attach(new Shader(GL46.GL_FRAGMENT_SHADER, "src/engine/render/utils/shaders/color_texture.frag"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void setColor(float r, float g, float b) {
		c = new Color(r,g,b);
	}
	
	public void setColor(float r, float g, float b, float a) {
		c = new Color(r,g,b, a);
	}
	
	public void setColor(Color colour) {
		c = colour;
	}
	
	public void drawLine(float x1, float y1, float x2, float y2) {
		setSimpleUniforms();

		Shape s = ShapeLoader.createSimpleLine(x1, y1, x2, y2);
		s.bind();
		GL46.glDrawElements(GL46.GL_LINES, s.getVertexCount(), GL46.GL_UNSIGNED_INT, 0);
		s.unbind();
	}
	
	public void drawRect(float x, float y, float width, float height) {
		setSimpleUniforms();

		Shape s = ShapeLoader.createSimpleQuad(x, y, width, height);
		s.bind();
		GL46.glDrawElements(GL46.GL_TRIANGLES, s.getVertexCount(), GL46.GL_UNSIGNED_INT, 0);
		s.unbind();
	}
	
	public void drawTexturedShape(Shape s, Texture t) {
		
	}
	

	public void drawText(String text, float x, float y) {
		text_shader.use();
		Shape s = fontHandler.generateShape(text);

		text_shader.uploadTexture("texture", 0);
		text_shader.uploadColor("replacementColor", c);
		text_shader.uploadVec2f("offset", getOffset().offset(x, y));
		text_shader.upload("scale", fontHandler.getSize());
		
		
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		fontHandler.getTexture().bind();
		GL30.glBindVertexArray(s.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawElements(GL11.GL_TRIANGLES, s.getVertexCount(), GL11.GL_UNSIGNED_INT,0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	public Vec2f getOffset() {
		return offset_stack.peek();
	}
	
	void setSimpleUniforms() {
		simple_shader.use();
		simple_shader.uploadColor("color", c);
		simple_shader.uploadVec2f("offset", getOffset());
	}

	public Graphics pushO(Vec2f position) {
		offset_stack.push(MathUtils.add(getOffset(), position));
		return this;
	}
	
	public Graphics popO() {
		if(offset_stack.size() > 1) {
			offset_stack.poll();
		}
		return this;
	}

	public void setFont(Font font) {
		fontHandler.setFont(font);
	}
	
}
