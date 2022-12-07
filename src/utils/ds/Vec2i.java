package utils.ds;

import java.util.Arrays;

public class Vec2i implements IVec{

	int[] xy = new int[2];
	
	public Vec2i(int int1, int int2) {
		xy = new int[] {int1, int2};
	}

	@Override
	public int length() {
		return 2;
	}
	
	@Override
	public int hashCode() {
		return xy[0] * 256 - xy[1];
	}
	
	@Override
	public boolean equals (Object o) {
		if(o instanceof Vec2i) {
			return Arrays.equals(xy, ((Vec2i)o).xy);
			
		} 
		return false;
	}
	
	public int getX() {
		return xy[0];
	}

	public int getY() {
		return xy[1];
	}

	public int[] getXY() {
		return xy;
	}
	
	@Override
	public String toString() {
		return "<"+xy[0]  + ", " + xy[1] + ">";
	}

}
