package engine.render.gui;

import java.util.LinkedList;
import java.util.List;

import engine.render.utils.Graphics;
import utils.ds.Vec2f;

public abstract class GContainer extends GComponent{
	
	public List<GComponent> components = new LinkedList<GComponent>();

	public GContainer(float x, float y, float w, float h) {
		this.size = new Vec2f(w,h);
		this.position = new Vec2f(x,y);
	}
	
	public GContainer() {}
	
	public void add(GComponent component) {
		components.add(component);
		if(component.parent!=null) {
			component.parent.remove(component);
		}
		
		component.parent = this;
	}
	
	public void remove(GComponent component) {
		
		components.remove(component);
		component.parent = null;
	}

	@Override
	public void paint(Graphics g) {
		for(GComponent comp : components) {

			if(comp.enabled) {

				comp.paint(g.pushO(position));
				g.popO();
			}
		}
	}

	
	
}
