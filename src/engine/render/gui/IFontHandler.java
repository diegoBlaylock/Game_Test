package engine.render.gui;

import engine.render.Texture;
import engine.render.shapes.Shape;
import engine.render.utils.Font;

public interface IFontHandler {

	public Shape generateShape(String text);
	public float getSize();
	public Texture getTexture();
	public void setFont(Font f);

}
