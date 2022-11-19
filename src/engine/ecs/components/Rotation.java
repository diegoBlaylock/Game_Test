package engine.ecs.components;

public class Rotation extends Component{
	float theta = 0;
	
	public Rotation(float  i) {
		theta = i;
	}

	public void set(float newTheta) {
		theta = newTheta;
		cycle();
	}
	
	public Rotation addEq(float amount) {
		theta+= amount;
		cycle();
		return this;
	}
	
	void cycle() {
		theta %= 2*Math.PI;
	}

	public float getTheta() {
		return theta;
	}

	@Override
	public void cleanup() {
		
	}

}


