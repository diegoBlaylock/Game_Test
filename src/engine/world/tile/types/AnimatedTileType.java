package engine.world.tile.types;

import org.json.JSONArray;
import org.json.JSONObject;

import engine.world.tile.Atlas;
import engine.world.tile.Atlas.SubTex;

public class AnimatedTileType extends ITileType {

	public long interval;
	public long next;
	public boolean dirty = false;
	
	public int cycle_index = 0;
	
	SubTex[] cycles;
	
	public AnimatedTileType(JSONObject obj, Atlas a) {
		
		JSONObject args = obj.getJSONObject("args");
		
		this.collision = obj.getBoolean("collision");
		
		interval = args.getLong("interval");
		
		JSONArray array = args.getJSONArray("tiles");
		
		cycles = new SubTex[array.length()];
		
		for(int i = 0; i < cycles.length; i++) {
			
			cycles[i] = new SubTex(array.getInt(i), a.getSize(), a.getSheet());
			
		}
		
		next = interval + System.currentTimeMillis();
		System.out.println(next);
		
	}

	@Override
	public void update(long time) {
		if(System.currentTimeMillis() > next) {

			cycle_index++;
			cycle_index %= cycles.length;
			next = System.currentTimeMillis() + interval;
			dirty = true;
		} else {
			dirty = false;
		}
	}

	@Override
	public SubTex getLoc() {
		return cycles[cycle_index];
	}

	@Override
	public boolean isDirty() {

		return dirty;
	}

	
	
}
