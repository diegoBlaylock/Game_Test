package test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import engine.world.World;
import engine.world.tile.TileMap;
import engine.world.tile.types.ITileType;

@ExtendWith(MockitoExtension.class)
public class WorldMocks {
	
	public static World alwaysColliding = new World();
	
	public static World neverColliding = new World();
	
	public static World collisionAt00 = new World();
	
	static {
		
		setupWorldMocks();
	}
	
	static void setupWorldMocks() {
		ITileType collidable = Mockito.mock(ITileType.class);
		Mockito.when(collidable.isCollision()).thenReturn(true);
		
		ITileType noncollidable = Mockito.mock(ITileType.class);
		Mockito.when(noncollidable.isCollision()).thenReturn(false);
		
		TileMap always = Mockito.mock(TileMap.class);
		Mockito.when(always.getTile(Mockito.anyInt(), Mockito.anyInt())).thenReturn(collidable);
		Mockito.when(always.getTile(Mockito.anyFloat(), Mockito.anyFloat())).thenReturn(collidable);

		TileMap never = Mockito.mock(TileMap.class);
		Mockito.when(never.getTile(Mockito.anyInt(), Mockito.anyInt())).thenReturn(noncollidable);
		Mockito.when(never.getTile(Mockito.anyFloat(), Mockito.anyFloat())).thenReturn(noncollidable);
		
		TileMap at00 = Mockito.mock(TileMap.class);
		Mockito.when(at00.getTile(Mockito.anyInt(), Mockito.anyInt())).then(inv -> ((Integer)inv.getArgument(0) == 0 && (Integer) inv.getArgument(1) == 0? collidable: noncollidable));
		Mockito.when(at00.getTile(Mockito.anyFloat(), Mockito.anyFloat())).then(inv -> (((Float)inv.getArgument(0)).intValue() == 0 && ((Float) inv.getArgument(1)).intValue() == 0? collidable: noncollidable));

		
		
		alwaysColliding.loadMap(always);
		neverColliding.loadMap(never);
		collisionAt00.loadMap(at00);
		
	}
}
