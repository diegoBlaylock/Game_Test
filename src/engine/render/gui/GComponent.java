package engine.render.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import engine.render.utils.Color;
import engine.render.utils.Graphics;
import utils.MathUtils;
import utils.ds.Vec2f;

public abstract class GComponent implements IFocusable{
	static final GUILOCK LOCK = new GUILOCK();
	static class GUILOCK{}
	
	static Map<String, GComponent> GLOBAL_MAP = new HashMap<String, GComponent>();
	static String register(String name, GComponent comp) {
		GLOBAL_MAP.put(name, comp);
		return name;
	}
	
	protected GContainer parent;
	protected String id = register(MathUtils.getUUID(), this);
	protected Vec2f position = Vec2f.FULL;
	protected Vec2f size = Vec2f.ZEROE;
	protected Color foreground = Color.BLACK;
	protected Color background = Color.GREY;

	protected boolean enabled = true;
	protected boolean visible = true;
	
	
	public void paint(Graphics g) {
		g.setColor(background);
		g.drawRect(0, 0, this.getSize().getX(), -this.getSize().getY());
	}
	
	public Vec2f getPosition() {
		return position;
	}
	
	public Vec2f getSize() {
		return size;
	}
	
	public void setPosition(float x, float y) {
		this.position.setVec(x, y);
	}
	
	public void setForeground(Color c) {
		foreground = c;
	}
	
	@Override
	public void setFocus(boolean b) {
		
	}
	
	
	@Override
	public boolean hasFocus() {
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(GComponent.class.isAssignableFrom(o.getClass())) {
			return this == o;
		}
		
		if(String.class.isAssignableFrom(o.getClass())) {
			return this.id.equals(o);
		}
		
		if(UUID.class.isAssignableFrom(o.getClass())) {
			return this.id.equals(((UUID)o).toString());
		}
		
		return o == this;
		
	}
	
}
