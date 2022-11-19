package engine.world.tile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import engine.render.Texture;
import engine.world.tile.types.ITileType;
import utils.ds.Vec2f;
import utils.ds.Vec2i;

@SuppressWarnings("unchecked")
public class Atlas {
	
	static Map<String, Class<? extends ITileType>> tileTypes = new HashMap<String, Class<? extends ITileType>>();
	
	static {
		String reg;
		try {
			reg = Files.readString(Paths.get("src/engine/world/tile/types/type-reg.json"));
		
			JSONObject o = new JSONObject(reg);
			
			Iterator<String> iter = o.keys();
			
			while(iter.hasNext()) {
				String key = iter.next();
				tileTypes.put(key, (Class<? extends ITileType>) Class.forName(o.getString(key)));	
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public Texture sheet;
	
	public Map<Integer, ITileType> legend = new HashMap<Integer, ITileType>();
	
	Vec2i size;
	
	public ITileType getTile(int id) {
		
		return legend.get(id);
	
		
	}

		
	
	
	public static Atlas read(JSONObject lInfo) {
		
		Atlas a = new Atlas();
		JSONObject aInfo = lInfo.getJSONObject("atlas");
		
		a.sheet = new Texture(aInfo.getString("path"), true);
		a.size = new Vec2i(aInfo.getInt("width"), aInfo.getInt("height"));
		
		for(Object e : lInfo.getJSONArray("ids")) {
			JSONObject tileInfo = (JSONObject) e;
			
			int id = tileInfo.getInt("id");
			String type = tileInfo.getString("type");
			try {
				ITileType tt = tileTypes.get(type).getConstructor(JSONObject.class, Atlas.class).newInstance(tileInfo, a);
				tt.id = id;
				a.legend.put(id, tt);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1 ) {
				System.err.println("ERROR DECODING .DAT FILE");
				e1.printStackTrace();
			}
			
		}
		
		return a;
		
	}
	
	
	public static class SubTex{
		public int loc;
		public Vec2f img_loc;
		public Vec2f img_stop;
		
		public SubTex(int loc, Vec2i size, Texture t) {
			this.loc = loc;
			int w = t.getWidth()/ size.getX();
			int h = t.getHeight()/ size.getY();
			
			int loc_h = Math.floorDiv(this.loc, w);
			int loc_w = this.loc - (loc_h * w);
			assert loc_w < w && loc_h < h;
			
			float w_ratio = size.getX() * 1.0f / t.getWidth();
			float h_ratio = size.getY() * 1.0f / t.getHeight();
			
			img_loc = new Vec2f(loc_w * w_ratio, loc_h * h_ratio);
			
			img_stop = new Vec2f((loc_w + 1) * w_ratio, (loc_h+1) * h_ratio);
			
		}
		
		@Override
		public String toString() {
			return String.format("[%s: %.4f, %.4f]", loc, img_loc.getX(), img_loc.getY());
		}
		
	}

	public Texture getSheet() {
		return sheet;
	}




	public Vec2i getSize() {
		return size;
	}
	

	
}
