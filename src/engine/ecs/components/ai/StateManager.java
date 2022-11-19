package engine.ecs.components.ai;

import java.util.Deque;
import java.util.LinkedList;
import engine.ecs.components.Component;
import engine.ecs.entities.Entity;
import engine.ecs.entities.State;

/**
 * Basic Pushdown automaton core for entities
 * 
 * @author diego
 */
public class StateManager extends Component{

	//The Initial State, used instead of null checks
	public static State NULL = new NullState();
	public static class NullState implements State{public void enter(Entity e) {} public void update(Entity e) {} public void exit(Entity e) {}}
	
	public Deque<State> state_record = new LinkedList<State>();
	
	public StateManager() {
		super();
		state_record.push(NULL);
	}
	
	public StateManager(Entity e, State... moving) {
		this();
		for(State s : moving) {
			this.pushState(e, s);
		}
	}

	public State getState() {
		return state_record.peek();
	}
	
	public void updateState(Entity e, State state) {
		this.popState(e);
		this.pushState(e, state);
	}
	
	public void pushState(Entity e,State state) {
		state.enter(e);
		state_record.push(state);
	}
	
	public State popState(Entity e) {
		if(NullState.class.isAssignableFrom(getState().getClass())) {
			return getState();
		}
		
		State s = state_record.pop();
		s.exit(e);
		return s;
	}

	public void update(Entity e) {
		this.getState().update(e);
	}

	public void reset() {
		state_record.clear();
		state_record.add(NULL);
	}
}
