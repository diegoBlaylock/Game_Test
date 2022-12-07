package test.utils.ds;

import org.junit.jupiter.api.Test;

import utils.ds.Mat4f;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.util.Arrays;

public class Mat4Test {

	@Test
	public void TestTranslate_givenInputs_shouldPass() {
		assertArrayEquals(new float[] {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 4, 9, 1} , Mat4f.translate(1, 4, 9));
	}
	
	@Test
	public void TestRotateZ_givenInputs_shouldPass() {

		assertArrayEquals(new float[] {6.123234E-17f, 1, 0, 0,
									   -1, 6.123234E-17f, 0, 0, 
									   0, 0, 1, 0, 
									   0, 0, 0, 1} , 
				Mat4f.rotate(1.5707963267948966192313216916398));
		
	}
	
	@Test
	public void TestScale_givenInputs_shouldPass() {
		assertArrayEquals(new float[] {4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1} , Mat4f.scale(4));
	}
	
	@Test
	public void TestprojectionMatrix_givenInputs_shouldPass() {

		assertArrayEquals(new float[] {0.75f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.2222222f, -1.0f, 0.0f, 0.0f, -2.2222223f, 0.0f} , Mat4f.createProjectionMatrix( 12, 9, 1f, 10));

	}
}
