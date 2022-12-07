package test.engine.ecs.components.ai;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import engine.ecs.components.ai.StateManager;
import engine.ecs.entities.Entity;
import engine.ecs.entities.State;

@ExtendWith(MockitoExtension.class)
public class StateManagerTest {
	
	static class TestState implements State { @Override public void enter(Entity e) {} @Override public void update(Entity e) {} @Override public void exit(Entity e) {}}
	
	State A = new TestState();
	State B = new TestState();
	State C = new TestState();
	State D = new TestState();
	
	@Test
	void TestNullStatePermenance_whenStackPopped_shouldStillHaveNULL() {
		StateManager sm = new StateManager();
		
		sm.popState(null);
		sm.popState(null);
		sm.popState(null);
		
		
		Assertions.assertSame(StateManager.NULL, sm.getState());
		
		sm.pushState(null, A);
		sm.pushState(null, B);
		sm.pushState(null, C);
		sm.pushState(null, D);
		sm.reset();
		
		Assertions.assertSame(StateManager.NULL, sm.getState());

	}
	
	@Test
	void TestCorrectStackBehavior() {
		StateManager sm = new StateManager();
		sm.pushState(null, A);
		Assertions.assertSame(A, sm.getState());
		sm.pushState(null, B);
		Assertions.assertSame(B, sm.getState());
		sm.pushState(null, C);
		Assertions.assertSame(C, sm.getState());
		sm.pushState(null, D);
		Assertions.assertSame(D, sm.getState());
		
		
		Assertions.assertSame(D, sm.popState(null));
		Assertions.assertSame(C, sm.popState(null));
		Assertions.assertSame(B, sm.popState(null));
		Assertions.assertSame(A, sm.popState(null));
		Assertions.assertSame(StateManager.NULL, sm.popState(null));

	}
	
	@Test
	void testUpdateState_shouldPopAndPush() {
		StateManager sm = new StateManager();
		sm.updateState(null, A);
		
		Assertions.assertSame(A, sm.getState());
		
		sm.popState(null);
		Assertions.assertSame(StateManager.NULL, sm.getState());
		
		sm.updateState(null, A);
		sm.updateState(null, B);
		Assertions.assertSame(B, sm.getState());

		
		sm.popState(null);
		Assertions.assertSame(StateManager.NULL, sm.getState());

	}
}
