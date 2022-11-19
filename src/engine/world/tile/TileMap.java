package engine.world.tile;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.BufferUtils;

import engine.ILogic;
import engine.ecs.IEntityFetcher;
import engine.ecs.components.Component;
import engine.ecs.components.RenderComp;
import engine.ecs.entities.Entity;
import engine.physics.Location;
import engine.render.shapes.Shape;
import engine.render.shapes.ShapeLoader;
import engine.world.World;
import engine.world.tile.types.ITileType;
import utils.ds.Vec2i;

/**
 * Update to hold entities
 * 
 * @author diego
 *
 */
public class TileMap implements IEntityFetcher, ILogic{
	public static final int BATCH_SIZE = 16;

	Atlas a;
	
	Map<Vec2i, Batch> batches = new HashMap<Vec2i, Batch>();
	
	List<Entity> load = new ArrayList<Entity>();
	
	public World world;
	
	
	
	
	public static TileMap load(World w, String input) throws IOException {
		String string = Files.readString(Paths.get(input));
		JSONObject dict = new JSONObject(string);
		if(!dict.has("legend")) {
			assert false: "dat file Needs legend";
		}
		
		if(!dict.has("data")) {
			assert false: "dat file needs data key";
		}
		
		TileMap tm = new TileMap();
		tm.world = w;

		tm.a = Atlas.read(dict.getJSONObject("legend"));
		
		
		JSONObject data = dict.getJSONObject("data");
		int width = data.getInt("width");
		int height = data.getInt("height");
		JSONArray array = data.getJSONArray("tiles");
		
		for(int i = 0 ; i < Math.ceil(width*1.0/BATCH_SIZE)  ; i++) {
			
			for(int j = 0 ; j <Math.ceil(height *1.0/ BATCH_SIZE) ; j++) {
				Vec2i loc = new Vec2i(j, (int) Math.ceil(width * 1.0/BATCH_SIZE - i));
				int[] batch_array = new int[BATCH_SIZE*BATCH_SIZE];
				
				for(int row = 0; row < BATCH_SIZE; row++) {
					int head_index = width * (BATCH_SIZE*i  +row ) +  BATCH_SIZE*j;
					
					
					for(int col = 0 ; col < BATCH_SIZE; col ++) {
					
						if((i * BATCH_SIZE + row) >= height || (j * BATCH_SIZE + col) >= width || head_index + col >= array.length() ) {
							batch_array[row*BATCH_SIZE+col] = Short.MAX_VALUE;
						}else {
							batch_array[row*BATCH_SIZE+col] = array.getInt(head_index + col);
						}
					}
				}
				
				tm.batches.put(loc, tm.new Batch(loc, batch_array));
			}
		}
		
		return tm;
		
	}
	
	public class Batch extends Entity{

		Vec2i pos;
		
		int[] cells = new int[BATCH_SIZE*BATCH_SIZE];
		//List<SolidTile> collision_tiles = new ArrayList<SolidTile>();
		
		
		int size = 0;
		
		Shape s;

		public Batch(Vec2i position, int[] data) {
			
			
			for(int i : data) {
				if(i!=Short.MAX_VALUE) {
					size++;
				}
			}
			this.cells = data;
			this.pos = position;
			
			
			s = genShape();
			
			
			this.addComponent(new RenderComp(s).appendTexture(a.sheet).setDepth(-1));
			this.addComponent(new Location((float) position.getX() * BATCH_SIZE, (float) position.getY() * BATCH_SIZE));
		}
		
		private Shape genShape() {
			
			FloatBuffer vertices = BufferUtils.createFloatBuffer(size*20);
			IntBuffer idxs = BufferUtils.createIntBuffer(size*6);
			int pos = 0;
			
			for(int i = 0; i < BATCH_SIZE; i++) {
				for(int j = 0; j < BATCH_SIZE; j++) {
					int net = BATCH_SIZE * BATCH_SIZE - BATCH_SIZE-(i*BATCH_SIZE - j);
					
					if(cells[net]!=Short.MAX_VALUE) {
						ITileType t = a.getTile(cells[net]);
						//Begining of Quad
						
						vertices.put(new float[] {j,i+1,0, t.getLoc().img_loc.getX(), t.getLoc().img_loc.getY()});
						vertices.put(new float[] {j,i,0, t.getLoc().img_loc.getX(), t.getLoc().img_stop.getY()});
						vertices.put(new float[] {j+1,i,0, t.getLoc().img_stop.getX(), t.getLoc().img_stop.getY()});
						vertices.put(new float[] {j+1,i+1,0, t.getLoc().img_stop.getX(), t.getLoc().img_loc.getY()});
						
						int idx_pos = pos*4;
						
						idxs.put(new int[] {idx_pos, idx_pos+1, idx_pos+2, idx_pos, idx_pos+2, idx_pos+3});
						
						pos+=1;
					}
				}
			}
			
			vertices.flip();
			idxs.flip();
			Shape shape = ShapeLoader.createTexShape(vertices, idxs);
			return shape;
		}
		
		private FloatBuffer genVertices() {
			
			FloatBuffer vertices = BufferUtils.createFloatBuffer(size*20);
			
			
			for(int i = 0; i < BATCH_SIZE; i++) {
				for(int j = 0; j < BATCH_SIZE; j++) {
					int net = BATCH_SIZE * BATCH_SIZE - BATCH_SIZE-(i*BATCH_SIZE - j);
					
					if(cells[net]!=Short.MAX_VALUE) {
						ITileType t = a.getTile(cells[net]);
						//Begining of Quad
						
						vertices.put(new float[] {j,i+1,0, t.getLoc().img_loc.getX(), t.getLoc().img_loc.getY()});
						vertices.put(new float[] {j,i,0, t.getLoc().img_loc.getX(), t.getLoc().img_stop.getY()});
						vertices.put(new float[] {j+1,i,0, t.getLoc().img_stop.getX(), t.getLoc().img_stop.getY()});
						vertices.put(new float[] {j+1,i+1,0, t.getLoc().img_stop.getX(), t.getLoc().img_loc.getY()});						
					}
				}
			}
			
			vertices.flip();
			
		
			return vertices;
		}
		
		
		@Override
		public void update(long time) {
			
		}

		public void refresh() {
			ShapeLoader.updateShape(s.getVaoID(), genVertices());
		}

		public boolean isDirty() {
			for(int i : cells) {
				if(i != Short.MAX_VALUE && a.getTile(i).isDirty()) {
					return true;
				}
			}
			
			return false;
		}
		
		/*public static class SolidTile extends Entity{
			public static final BoundingBox bbox = new BoundingBox(0,0,1,1);
			public static final Collider collider = new Collider(Collider.Type.ICEBERG);

			
			public SolidTile() {
				this.addComponent(bbox);
				this.addComponent(collider);
				
			}
		}*/

		public ITileType getTileLocal(int i, int j) {
			assert i < BATCH_SIZE : "WRONG INPUT";
			assert j < BATCH_SIZE : "WRONG INPUT";
			
			
			return a.getTile(cells[i + (BATCH_SIZE - j - 1)*BATCH_SIZE]);
		}
	}
	
	@Override
	public Collection<? extends Entity> getEntities() {
		load.clear();
		load.addAll(this.batches.values());
		//for(Batch b: this.batches.values()) {
			//load.addAll(b.collision_tiles);
		//}
		return load;
	}

	@Override
	public Collection<? extends Entity> getEntities(Class<? extends Component> queryClass) {
		
		return this.getEntities().stream().filter( new Predicate< Entity>() {

			@Override
			public boolean test(Entity t) {
				return t.contains(queryClass);
			}
			
		}).toList();
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Entity> getEntitiesContaining(Class<? extends Component>... classes) {
		return this.getEntities().stream().filter(new Predicate<Entity>() {

			@Override
			public boolean test(Entity t) {
				for(Class<? extends Component> clazz: classes) {
					if(!t.contains(clazz)) {
						return false;
					}
				}
				return true;
			}
			
		}).toList();
	}

	public Atlas getAtlas() {
		return a;
	}

	public void update(long dTick) {
		for(ITileType tt : a.legend.values()) {
			tt.update(dTick);
		}
		
		for(Batch b : batches.values()) {
			if(b.isDirty()) {
				b.refresh();
			}
		}
	}

	public void unload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public World getWorld() {
		
		return world ;
	}

	public ITileType getTile(int mapx, int mapy) {
		Vec2i loc = new Vec2i(mapx / BATCH_SIZE, mapy/BATCH_SIZE);
		
		Batch b = batches.get(loc);
		
		
		
		if(b == null) {
			return ITileType.NULL;
		}
		
		return b.getTileLocal(Math.abs(mapx % BATCH_SIZE), Math.abs(mapy % BATCH_SIZE));
		
	}

	public ITileType getTile(float x, float y) {
		return getTile((int) x, (int) y);
	}
	
}
