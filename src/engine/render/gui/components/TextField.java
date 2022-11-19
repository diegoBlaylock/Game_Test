package engine.render.gui.components;

import engine.render.gui.GComponent;
import engine.render.utils.Font;
import engine.render.utils.Graphics;
import utils.ds.Vec2f;

public class TextField extends GComponent{

	String text;
	
	Font font = new Font(1,0);
	
	public TextField(float x, float y, String text) {
		this.text = text;
		updateSize();

		this.setPosition(x, y);
	}



	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(this.foreground);
		g.setFont(font);
		g.drawText(text, 0, 0);
	}
	
	void updateSize() {
		float x = 0;
		float x_max = 0;
		float y = 1;
		for(char chr : text.toCharArray()) {
			if(chr == '\t') {
				x+=4;
			} else if(chr == '\n') {
				x_max = Math.max(x_max, x);
				x= 0;
				y+=1;
			} else if(chr == ' '){
				x+=1;
			} else if(!Character.isWhitespace(chr)) {
				x+=1;
			}
		}
		
		x += (font.spacing * (x-1));
		y += (font.spacing * (y-1));
		
		this.size= new Vec2f(font.scale * x_max, font.scale * y);
	}


	public void setText(String format) {
		text = format;
		updateSize();
	}



	public Font getFont() {
		return font;
	}
	
	
	
	
	
}
