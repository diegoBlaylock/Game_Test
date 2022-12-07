package test.engine.ecs;

import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import engine.ecs.EntityFetcher;
import engine.ecs.components.Component;
import engine.ecs.entities.Entity;

public class EntityFetcherTest {
	static EntityFetcher template = new EntityFetcher(null);
	static EntityFetcher subject = new EntityFetcher(null);
	
	static abstract class Comp_Alpha extends Component {}
	static abstract class Comp_Bravo extends Component {}
	static abstract class Comp_Charlie extends Component {}
	static abstract class Comp_Delta extends Component {}
	
	static Entity e_abcd = createDummyEntity(Comp_Alpha.class, Comp_Bravo.class, Comp_Charlie.class, Comp_Delta.class);
	static Entity e_abc = createDummyEntity(Comp_Alpha.class, Comp_Bravo.class, Comp_Charlie.class);
	static Entity e_ab = createDummyEntity(Comp_Alpha.class, Comp_Bravo.class);
	static Entity e_a = createDummyEntity(Comp_Alpha.class);
	static Entity e_cd = createDummyEntity(Comp_Charlie.class, Comp_Delta.class);

	@SafeVarargs
	static Entity createDummyEntity(Class<? extends Component> ... comps) {
		Entity e = new Entity(){};
		
		for(int i = 0; i < comps.length; i++) {
			e.addComponent(Mockito.mock(comps[i]));
		}
		
		return e;
	}
	
	@BeforeAll
	static void beforeAll(){		
		template.add(e_abcd);
		template.add(e_abc);
		template.add(e_ab);
		template.add(e_a);
		template.add(e_cd);
		
	}
	
	@BeforeEach
	void beforeEach() {
		subject.removeAll();
		for(Entity e : template.getEntities()) {
			subject.add(e);
		}
	}
	
	@Test
	void TestFetcher_givenEmptySet_shouldPass() {
		subject.removeAll();
		
		Assertions.assertTrue(subject.getEntities().isEmpty());
		Assertions.assertTrue(subject.getEntities(Comp_Alpha.class).isEmpty());
		Assertions.assertTrue(subject.getEntitiesContaining(Comp_Alpha.class, Comp_Bravo.class).isEmpty());
	}
	
	@Test
	void TestGetEntities_givenAlpha_shouldReturn4Entities() {
		
		Collection<Entity> entities = subject.getEntities(Comp_Alpha.class);
		Assumptions.assumeTrue(entities!=null);
		
		Assertions.assertEquals(entities.size(), 4);
		Assertions.assertTrue(entities.contains(e_abcd));
		Assertions.assertTrue(entities.contains(e_abc));
		Assertions.assertTrue(entities.contains(e_ab));
		Assertions.assertTrue(entities.contains(e_a));
		
		Assertions.assertFalse(entities.contains(e_cd));
	}
	
	
}
