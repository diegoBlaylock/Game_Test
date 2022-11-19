package engine.physics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import engine.ecs.components.Component;

/**
 * This component lists all the Bounding Boxes for an entity. Each Channel can hold up one BoundingBox
 * 
 * @author diego
 *
 */
public class RigidBody extends Component{
	
	
	public Map<Byte, BoundingBox> boxes= new HashMap<Byte, BoundingBox>(8);
	
	public RigidBody(BoundingBox... boundingBox) {
		for(BoundingBox bb : boundingBox) {
			this.addBoundingBox(bb);
		}
	}
	public Collection<BoundingBox> getBoundingBoxes(){
		return boxes.values();
	}
	public void addBoundingBox(BoundingBox bb) {
		boxes.put(bb.getChannel(), bb);
	}
	
	public BoundingBox getChannel(int channel) {
		return boxes.get((byte) channel);
	}
	
	public BoundingBox getChannel(byte channel) {
		return boxes.get(channel);
	}
	
}
