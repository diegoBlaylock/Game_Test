package engine.world.tile.types;

import org.json.JSONObject;

import engine.world.tile.Atlas;
import engine.world.tile.Atlas.SubTex;

public class TileType extends ITileType{
	public SubTex loc;
	
	public TileType(JSONObject obj, Atlas a) {
		int where = obj.getInt("args");
		loc = new SubTex(where, a.getSize(), a.sheet);
		this.collision = obj.getBoolean("collision");
		
		
	}
	
	@Override
	public String toString() {
		return String.format("<%s: %s>", id, loc.toString());
	}

	@Override
	public void update(long time) {		
		
	}

	@Override
	public SubTex getLoc() {
		return loc;
	}

	@Override
	public boolean isDirty() {
		return false;
	}
	
}