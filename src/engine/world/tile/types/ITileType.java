package engine.world.tile.types;

import engine.ILogic;
import engine.world.tile.Atlas;
import engine.world.tile.Atlas.SubTex;

public abstract class ITileType implements ILogic {
	public static final ITileType NULL = new ITileType() {

		@Override
		public void update(long time) {
		}

		@Override
		public SubTex getLoc() {

			return null;
		}

		@Override
		public boolean isDirty() {

			return false;
		}
		
	};
	
	
	public int id;
	public boolean collision;
	
	public abstract Atlas.SubTex getLoc();

	public abstract boolean isDirty();

	public boolean isCollision() {
		return collision;
	}
	
}
