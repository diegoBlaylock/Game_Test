package utils.ds;

/**
 * Matrix data structures are represented as flat arrays in column major order, the following
 * class provides utility functions to generate and modify these arrays 
 * 
 * @author diego
 */
public class Mat4f {

	public static final float[] IDENTITY4 = new float[] {1f,0f,0f,0f,
														 0f,1f,0f,0f,
														 0f,0f,1f,0f,
														 0f,0f,0f,1f};
	public static final float[] IDENTITY3 = new float[] {1f,0f,0f,
														 0f,1f,0f,
														 0f,0f,1f};
	
	public static final float[] IDENTITY2 = new float[] {1f,0f,
														 0f,1f};
												
	
	/**
	 * generate a translation matrix where upon multiplying with, will modify a vectors x,y,z appropriatly
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static float[] translate(float x, float y, float z) {
		float[] output = copy(IDENTITY4);
		translate(output,x,y, z);
		return output;
		
	}
	
	/**
	 * modifies existing matrix to include translation components
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void translate(float[] id, float x, float y, float z) {
		id[12] = x;
		id[13] = y;
		id[14] = z;
	}
	
	/**
	 * Modifies existing matrix to include rotation components around the Z axis
	 * 
	 * @param id
	 * @param theta
	 */
	public static void rotateZ(float[] id, double theta) {
		id[0] = (float) Math.cos(theta);
		id[1] = (float) Math.sin(theta);
		id[4] = - id[1];
		id[5] = id[0];
	}
	
	/**
	 * Modifies existing matrix to include rotation components around the Z axis
	 * 
	 * @param id
	 * @param theta
	 */
	public static void rotateY(float[] id, double theta) {
		id[0] = (float) Math.cos(theta);
		id[2] = (float) Math.sin(theta);
		id[8] = - id[1];
		id[10] = id[0];
	}
	
	/**
	 * Generates rotateZ matrix
	 * 
	 * @param theta
	 * @return
	 */
	public static float[] rotate(double theta) {
		float[] output = copy(IDENTITY4);
		rotateZ(output,theta);
		return output;
	}
	
	/**
	 * Modifies existing array to include scale xy components
	 * 
	 * @param id
	 * @param scale
	 */
	public static void scale(float[] id, float scale) {
		id[0] = scale;
		id[5] = scale;
		//id[10] = scale;
	}
	
	/**
	 * generates a matrix with scale xy components
	 * 
	 * @param scale
	 * @return
	 */
	public static float[] scale(float scale) {
		float[] output = copy(IDENTITY4);
		scale(output,scale);
		return output;	
	}
	
	/**
	 * returns a new Matrix containing the result of multiplying the matrices
	 */
	public static float[] mul(float[] a, float[] b){
		float[] out = new float[16];
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				for(int k = 0; k < 4; k++) {
					out[i*4+j] += a[k+4*i]*b[4*k+j];
				}
			}
		}
		return out;
	}
	
	/**
	 * returns copy of matrix
	 * 
	 * @param orig
	 * @return
	 */
	public static float[] copy(float[] orig) {
		float[] c = new float[orig.length];
		System.arraycopy(orig, 0, c, 0, orig.length);
		return c;
	}
	
	/**
	 * returns copy of identity matrix
	 * 
	 * @return
	 */
	public static float[] identity() {
		return Mat4f.copy(IDENTITY4);
	}
	
	/**
	 * copies the identity matrix onto an existing array
	 * 
	 * @param value
	 */
	public static void identity(float[] value) {
		System.arraycopy(IDENTITY4, 0, value, 0, value.length);
	}
	
	/**
	 * create Projection Matrix
	 * 
	 * @param fov
	 * @param w
	 * @param h
	 * @param n
	 * @param f
	 * @return
	 */
	public  static float[] createProjectionMatrix(float fov, float w, float h, float n, float f){
		float[] projectionMatrix = new float[16];
	      
		return projectionMatrix(projectionMatrix, fov, w, h, n, f);
	}
	
	/**
	 * Copies Projection matrix onto array
	 * 
	 * @param projectionMatrix
	 * @param fov
	 * @param w
	 * @param h
	 * @param n
	 * @param f
	 * @return
	 */
	public  static float[] projectionMatrix(float[] projectionMatrix, float fov, float w, float h, float n, float f){
		float aspectRatio = w / h;
		float yScale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustumLength = f-n;
		
		projectionMatrix[0] = xScale;
		projectionMatrix[5] = yScale;
		projectionMatrix[10] = -((f+n) / frustumLength);
		projectionMatrix[11] = -1;
		projectionMatrix[14] = -((2 * f*n) / frustumLength);
		projectionMatrix[15] = 0;
		 
		return (projectionMatrix);
	}
	
	/**
	 * returns ortho projection
	 * 
	 * TODO
	 * 
	 * @param projectionMatrix
	 * @param fov
	 * @param w
	 * @param h
	 * @param n
	 * @param f
	 * @return
	 */
	public  static float[] orthoMatrix(float[] projectionMatrix, float fov, float w, float h, float n, float f){	     
	    /*float aspectRatio = w / h;
	    float yScale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
	    float xScale = yScale / aspectRatio;
	    float frustumLength = f-n;
	
	      
	    projectionMatrix[0] = xScale;
	    projectionMatrix[5] = yScale;
	    projectionMatrix[10] = -((f+n) / frustumLength);
	    projectionMatrix[11] = -1;
	    projectionMatrix[14] = -((2 * f*n) / frustumLength);
	    projectionMatrix[15] = 0;
	     
	    return (projectionMatrix);*/
		//TODO
		
		return null;
	}
	
	//Very unpretty to string method
	public static String toString(float[] mat) {
		Float[] arr = new Float[mat.length];
		for(int i = 0;i < mat.length; i++) {
			arr[i] = Float.valueOf(mat[i]);
		}
		
		return String.format("⎡%1$6.4f, %5$6.4f, %9$6.4f, %13$6.4f\n⎢%2$6.4f, %6$6.4f, %10$6.4f, %14$6.4f\n⎢%3$6.4f, %7$6.4f, %11$6.4f, %15$6.4f\n⎣%4$6.4f, %8$6.4f, %12$6.4f, %16$6.4f", (Object[]) arr);
	}
	
}
