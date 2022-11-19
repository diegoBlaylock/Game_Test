package engine.ecs.systems;

import java.util.LinkedList;
import java.util.List;

import engine.ILogic;
import engine.ecs.IEntityFetcher;

public class SystemsManager implements ILogic{

	public List<ISystem> systems = new LinkedList<ISystem>();
	
	@Override
	public void update(long time) {
		systems.forEach(this::updateSystem);
	
	}
	
	public void runSystems(IEntityFetcher fetcher) {
		for(ISystem s : systems) {
			s.processEntities(fetcher);
		}
	}
	
	
	void updateSystem(ISystem system) {
		//system.update();
	}

	public void addSystem(ISystem contollerSystem) {
		systems.add(contollerSystem);
	}
	
	
}
