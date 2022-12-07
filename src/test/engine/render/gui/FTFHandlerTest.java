package test.engine.render.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import engine.render.gui.FTFHandler;



public class FTFHandlerTest {

	
	FTFHandler font = new FTFHandler("src/test/engine/render/gui/sample.ftf");
	
	@Test
	void TestIndexOf() {
		assertEquals(0, font.indexOf('0'));
		assertEquals(3, font.indexOf('3'));
		assertEquals(4, font.indexOf('4'));
		assertEquals(9, font.indexOf('9'));
		
		assertEquals(-1, font.indexOf('a'));

	}
	
	@Test
	void TestGetLoc_givenDefinedChar_returnMapping() {
		assertEquals(0, font.getLoc('0'));
		assertEquals(3, font.getLoc('3'));
		assertEquals(4, font.getLoc('4'));
		assertEquals(8, font.getLoc('8'));
	}
	
	@Test
	void TestGetLoc_givenUndefinedChar_returnDefaultLoc() {
		assertEquals(64, font.getLoc('\"'));
		assertEquals(64, font.getLoc('a'));
		assertEquals(64, font.getLoc('{'));
		assertEquals(64, font.getLoc('\b'));
		assertEquals(64, font.getLoc('\n'));
	}
	
	
}
