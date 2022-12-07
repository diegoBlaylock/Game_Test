package test.utils.ds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import utils.ds.Vec2f;

public class Vec2fTest {

	@Test
	public void TestAdd_givenVectors_addVector() {
		Vec2f a = new Vec2f(1, -4.25f);
		Vec2f b = new Vec2f(-9.5f,16.75f);
		
		a.add(b);
		
		Assertions.assertEquals(a, new Vec2f(-8.5f, 12.5f));
		Assertions.assertEquals(b, new Vec2f(-9.5f,16.75f));
	}
	
	@Test
	public void TestOffset_givenVectors_returnNewOffsetVector() {
		Vec2f a = new Vec2f(1, -4.25f);
		
		Vec2f result = a.offset(-9.5f, 16.75f);
		
		Assertions.assertEquals(result, new Vec2f(-8.5f, 12.5f));
		Assertions.assertEquals(a, new Vec2f(1, -4.25f));
	}
	
	@Test
	public void TestHashIdentity_shouldCompareDataNotInstance(){
		Vec2f a = new Vec2f(1,1);
		Vec2f b = new Vec2f(1,1);
		
		Assertions.assertTrue(a.equals(a));
		Assertions.assertTrue(b.equals(b));
		
		Assertions.assertTrue(a.equals(b));
		Assertions.assertTrue(b.equals(a));
		
		Assertions.assertTrue(a.hashCode() == b.hashCode());
		
		b.setVec(-1, -1);
		
		Assertions.assertFalse(a.equals(b));
		Assertions.assertFalse(b.equals(a));
		
		Assertions.assertFalse(a.hashCode() == b.hashCode());
		
		
	}
}
