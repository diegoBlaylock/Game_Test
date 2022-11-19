package utils.ds;

import java.util.Arrays;

public class Vec2f implements IVec{

	public static final Vec2f ZEROE = new Vec2f(0,0);
	public static final Vec2f FULL = new Vec2f(1,1);
	float[] xy = new float[] {0,0};

	public Vec2f(float x, float y) {
		this.xy[0] = x;
		this.xy[1]=y;
	}
	
	public Vec2f(float[] vecd) {
		System.arraycopy(vecd, 0, xy, 0, 2);
	}
	
	
	public float getX() {
		return this.xy[0];
	}
	
	public float getY() {
		return this.xy[1];
	}
	
	
	public float[] getVec(boolean copy) {
		if(!copy) {
			return this.xy;
		}
		return Arrays.copyOf(xy, 2);
	}

	@Override
	public int length() {
		return 2;
	}

	public void setVec(float[] vecd) {
		System.arraycopy(vecd, 0, xy, 0, 2);
	}

	public void setVec(float x, float y) {
		xy[0]=x;
		xy[1]=y;
	}
	
	@Override
	public String toString() {
		return String.format("<%.2f, %.2f>", xy[0], xy[1]);
	}

	public Vec2f offset(float x, float y) {
		return new Vec2f(this.getX() + x, this.getY() + y);
	}

	public void add(Vec2f scalar) {
		this.setVec(getX() + scalar.getX(), getY()+scalar.getY());
	}
	
}
