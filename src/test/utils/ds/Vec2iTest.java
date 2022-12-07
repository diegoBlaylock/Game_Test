package test.utils.ds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import utils.ds.Vec2i;

public class Vec2iTest {
	
	@Test
	public void TestHashIdentity_shouldCompareDataNotInstance(){
		Vec2i a = new Vec2i(1,1);
		Vec2i b = new Vec2i(1,1);
		
		Assertions.assertTrue(a.equals(a));
		Assertions.assertTrue(b.equals(b));
		
		Assertions.assertTrue(a.equals(b));
		Assertions.assertTrue(b.equals(a));
		
		Assertions.assertTrue(a.hashCode() == b.hashCode());
		
		b.getXY()[0]=-1;
		b.getXY()[1]=-1;
		
		
		Assertions.assertFalse(a.equals(b));
		Assertions.assertFalse(b.equals(a));
		
		Assertions.assertFalse(a.hashCode() == b.hashCode());
	}
}
