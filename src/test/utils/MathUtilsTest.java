package test.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.WorldMocks;
import utils.Dir4;
import utils.MathUtils;
import utils.MathUtils.RayHit;
import utils.ds.Vec2f;

public class MathUtilsTest {
	
	@Test
	public void TestGetVec_givenDirections_returnAppropriateVector() {
		Assertions.assertEquals( MathUtils.NORTH, MathUtils.getVec(Dir4.NORTH));
		Assertions.assertEquals(MathUtils.EAST, MathUtils.getVec(Dir4.EAST));
		Assertions.assertEquals(MathUtils.SOUTH, MathUtils.getVec(Dir4.SOUTH));
		Assertions.assertEquals(MathUtils.WEST, MathUtils.getVec(Dir4.WEST));

		Assertions.assertEquals(Vec2f.ZEROE, MathUtils.getVec(Dir4.NONE));
	}
	
	@Test
	public void TestAdd_givenNull_shouldThrowNullPointerException() {
		Assertions.assertThrows(NullPointerException.class, () -> MathUtils.add(null, null));
	}
	
	@Test
	public void TestAdd_givenVectors_returnResult() {
		Vec2f a = new Vec2f(1,3);
		Vec2f b = new Vec2f(0.2f, -0.1f);
		Vec2f c = new Vec2f(-1,-3);
		
		Assertions.assertEquals(new Vec2f(1.2f, 2.9f), MathUtils.add(a, b));
		Assertions.assertEquals(new Vec2f(0, 0), MathUtils.add(a, c));
		Assertions.assertEquals(new Vec2f(-0.8f, -3.1f), MathUtils.add(b, c));
		Assertions.assertEquals(new Vec2f(2, 6), MathUtils.add(a, a));
		
		Assertions.assertEquals(new Vec2f(1,3), a);
		Assertions.assertEquals(new Vec2f(0.2f, -0.1f), b);
		Assertions.assertEquals(new Vec2f(-1,-3), c);
	}
	
	@Test
	public void TestLength_givenZeroLengthVector_thenZero() {
		Assertions.assertEquals(0,MathUtils.length(Vec2f.ZEROE));
	}
	
	@Test
	public void TestLength_givenVector_returnResult() {
		Assertions.assertEquals(1.41421356237f,MathUtils.length(Vec2f.FULL));
		Assertions.assertEquals(1.41421356237f,MathUtils.length(new Vec2f(-1,-1)));
		Assertions.assertEquals(2f,MathUtils.length(new Vec2f(0,-2)));
		Assertions.assertEquals(1.41421356237f,MathUtils.length(new Vec2f(1,-1)));
	}
	
	@Test
	public void TestScalar_givenFloat_returnResult() {
		Assertions.assertEquals(Vec2f.ZEROE, MathUtils.scalar(Vec2f.FULL, 0));
		
		Assertions.assertEquals(new Vec2f(-5,-5), MathUtils.scalar(Vec2f.FULL, -5));

	}
	
	@Test
	public void TestNormalize_givenZero_shouldThrowArithmeticException() {
		
		Assertions.assertThrows(ArithmeticException.class, ()->MathUtils.normalize(Vec2f.ZEROE), "Can't normalize vector <0,0>");
		
	}
	
	@Test
	public void TestCastRayTiles() {
		RayHit result = MathUtils.castRayTiles(WorldMocks.neverColliding, Vec2f.ZEROE, MathUtils.NORTH, 100);
		Assertions.assertEquals(null, result);
		
		result = MathUtils.castRayTiles(WorldMocks.alwaysColliding, Vec2f.ZEROE, MathUtils.NORTH, 100);
		Assertions.assertNotEquals(null, result);
		
		result = MathUtils.castRayTiles(WorldMocks.collisionAt00, Vec2f.FULL, new Vec2f(-1, -1), 100);
		Assertions.assertNotEquals(null, result);
	}
}
