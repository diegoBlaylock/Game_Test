package engine.ecs.entities;

public interface State {

	public void enter(Entity e);
	
	public void update(Entity e);
	
	public void exit(Entity e);
	
}
