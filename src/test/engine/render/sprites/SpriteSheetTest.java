package test.engine.render.sprites;

import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;
import engine.render.Texture;
import engine.render.gui.Window;
import engine.render.sprites.SpriteSheet;

public class SpriteSheetTest {

	static SpriteSheet sheet;
	
	
	static Window w;
	@BeforeAll
	static void setup() {
		w = Window.createWindow("bob", 1, 1, false);
		GL.createCapabilities();
		sheet = new SpriteSheet(0, 4,4, 2, Texture.DEFAULT);
	}
	
	@AfterAll
	static void teardown() {
		w.cleanup();
		GL.destroy();
	}
	
	
	@Test
	void TestUpdate_givenRot0() {
		sheet.update(0);
		Assertions.assertArrayEquals(new float[] {0.0625f, 0.1875f, 0.1875f, 0.1875f, 0.1875f, 0.0625f, 0.0625f, 0.0625f}, getLoad());
		
		sheet.update(3);
		Assertions.assertArrayEquals(new float[] {0.8125f, 0.1875f, 0.9375f, 0.1875f, 0.9375f, 0.0625f, 0.8125f, 0.0625f}, getLoad());
		
		sheet.update(8);
		Assertions.assertArrayEquals(new float[] {0.0625f, 0.6875f, 0.1875f, 0.6875f, 0.1875f, 0.5625f, 0.0625f, 0.5625f}, getLoad());
		
		sheet.update(15);
		Assertions.assertArrayEquals(new float[] {0.8125f, 0.9375f, 0.9375f, 0.9375f, 0.9375f, 0.8125f, 0.8125f, 0.8125f}, getLoad());

		
		
		sheet.update(-1);
		Assertions.assertArrayEquals(new float[] {0.1875f, 0.1875f, 0.0625f, 0.1875f, 0.0625f, 0.0625f, 0.1875f, 0.0625f}, getLoad());
		
		sheet.update(-3);
		Assertions.assertArrayEquals(new float[] {0.6875f, 0.1875f, 0.5625f, 0.1875f, 0.5625f, 0.0625f, 0.6875f, 0.0625f}, getLoad());
		
		sheet.update(-16);
		Assertions.assertArrayEquals(new float[] {0.9375f, 0.9375f, 0.8125f, 0.9375f, 0.8125f, 0.8125f, 0.9375f, 0.8125f}, getLoad());
	}
	
	@Test
	void TestUpdate_givenRot1() {
		sheet.update(16);
		Assertions.assertArrayEquals(new float[] {0.0625f, 0.0625f, 0.0625f, 0.1875f, 0.1875f, 0.1875f, 0.1875f, 0.0625f}, getLoad());
		
		sheet.update(19);
		Assertions.assertArrayEquals(new float[] {0.8125f, 0.0625f, 0.8125f, 0.1875f, 0.9375f, 0.1875f, 0.9375f, 0.0625f}, getLoad());
		
		sheet.update(24);
		Assertions.assertArrayEquals(new float[] {0.0625f, 0.5625f, 0.0625f, 0.6875f, 0.1875f, 0.6875f, 0.1875f, 0.5625f}, getLoad());
		
		sheet.update(31);
		Assertions.assertArrayEquals(new float[] {0.8125f, 0.8125f, 0.8125f, 0.9375f, 0.9375f, 0.9375f, 0.9375f, 0.8125f}, getLoad());

		
		
		sheet.update(-17);
		Assertions.assertArrayEquals(new float[] {0.1875f, 0.0625f, 0.1875f, 0.1875f, 0.0625f, 0.1875f, 0.0625f, 0.0625f}, getLoad());
		
		sheet.update(-19);
		Assertions.assertArrayEquals(new float[] {0.6875f, 0.0625f, 0.6875f, 0.1875f, 0.5625f, 0.1875f, 0.5625f, 0.0625f}, getLoad());
		
		sheet.update(-32);
		Assertions.assertArrayEquals(new float[] {0.9375f, 0.8125f, 0.9375f, 0.9375f, 0.8125f, 0.9375f, 0.8125f, 0.8125f}, getLoad());
	}
	
	@Test
	void TestUpdate_givenRot2() {
		sheet.update(32);
		Assertions.assertArrayEquals(new float[] {0.1875f, 0.0625f, 0.0625f, 0.0625f, 0.0625f, 0.1875f, 0.1875f, 0.1875f}, getLoad());
		
		sheet.update(35);
		Assertions.assertArrayEquals(new float[] {0.9375f, 0.0625f, 0.8125f, 0.0625f, 0.8125f, 0.1875f, 0.9375f, 0.1875f}, getLoad());
		
		sheet.update(40);
		Assertions.assertArrayEquals(new float[] {0.1875f, 0.5625f, 0.0625f, 0.5625f, 0.0625f, 0.6875f, 0.1875f, 0.6875f}, getLoad());
		
		sheet.update(47);
		Assertions.assertArrayEquals(new float[] {0.9375f, 0.8125f, 0.8125f, 0.8125f, 0.8125f, 0.9375f, 0.9375f, 0.9375f}, getLoad());

		
		
		sheet.update(-33);
		Assertions.assertArrayEquals(new float[] {0.0625f, 0.0625f, 0.1875f, 0.0625f, 0.1875f, 0.1875f, 0.0625f, 0.1875f}, getLoad());
		
		sheet.update(-35);
		Assertions.assertArrayEquals(new float[] {0.5625f, 0.0625f, 0.6875f, 0.0625f, 0.6875f, 0.1875f, 0.5625f, 0.1875f}, getLoad());
		
		sheet.update(-48);
		Assertions.assertArrayEquals(new float[] {0.8125f, 0.8125f, 0.9375f, 0.8125f, 0.9375f, 0.9375f, 0.8125f, 0.9375f}, getLoad());
	}
	
	@Test
	void TestUpdate_givenRot3() {
		sheet.update(48);
		Assertions.assertArrayEquals(new float[] {0.1875f, 0.1875f, 0.1875f, 0.0625f, 0.0625f, 0.0625f, 0.0625f, 0.1875f}, getLoad());
		
		sheet.update(51);
		Assertions.assertArrayEquals(new float[] {0.9375f, 0.1875f, 0.9375f, 0.0625f, 0.8125f, 0.0625f, 0.8125f, 0.1875f}, getLoad());
		
		sheet.update(56);
		Assertions.assertArrayEquals(new float[] {0.1875f, 0.6875f, 0.1875f, 0.5625f, 0.0625f, 0.5625f, 0.0625f, 0.6875f}, getLoad());
		
		sheet.update(63);
		Assertions.assertArrayEquals(new float[] {0.9375f, 0.9375f, 0.9375f, 0.8125f, 0.8125f, 0.8125f, 0.8125f, 0.9375f}, getLoad());

		
		
		sheet.update(-49);
		Assertions.assertArrayEquals(new float[] {0.0625f, 0.1875f, 0.0625f, 0.0625f, 0.1875f, 0.0625f, 0.1875f, 0.1875f}, getLoad());
		
		sheet.update(-51);
		Assertions.assertArrayEquals(new float[] {0.5625f, 0.1875f, 0.5625f, 0.0625f, 0.6875f, 0.0625f, 0.6875f, 0.1875f}, getLoad());
		
		sheet.update(-64);
		Assertions.assertArrayEquals(new float[] {0.8125f, 0.9375f, 0.8125f, 0.8125f, 0.9375f, 0.8125f, 0.9375f, 0.9375f}, getLoad());
	}
	
	static float[] getLoad() {
		try {
			Field f = SpriteSheet.class.getDeclaredField("load");
			
			f.setAccessible(true);
			
			return (float[])f.get(sheet);
			
			
			
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
}
