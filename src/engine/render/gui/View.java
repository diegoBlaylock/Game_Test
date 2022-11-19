package engine.render.gui;

import utils.ds.Mat4f;

public class View {

	float fov = 90f;
	
	int width, height;
	
	float xOffset, yOffset;
	
	float nearClip, farClip;
	
	float[] viewMatrix = new float[16];
	
	public View(float fov, int width, int height, float x, float y, float n, float f) {
		this.fov = fov;
		this.width = width;
		this.height = height;
		this.xOffset = x;
		this.yOffset = y;
		this.nearClip = n;
		this.farClip = f;
	}
	
	public float[] getViewMatrix() {
		return Mat4f.createProjectionMatrix(fov, width, height, nearClip, farClip);
	}

	public void resize(int width2, int height2) {
		this.width = width2;
		this.height = height2;
		Mat4f.projectionMatrix(viewMatrix, fov, width, height, nearClip, farClip);
	}
	
	
}
