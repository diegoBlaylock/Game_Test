package engine.ecs.components;

public class Speed extends Component{
	
	float speed = 0;
	
	public Speed(float init) {
		speed = init;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void set(float speed2) {
		this.speed = speed2;
	}
}
