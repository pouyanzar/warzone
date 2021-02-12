package soen6441.team01.warzone.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtlTest {

	@Test
	/**
	 * Simple test to see if the current working directory can be listed to the
	 * console. Also useful during the build (locally and remotely) to see what is
	 * the current working dorectory.
	 */
	public void test_printCurrentDirectory() {
		Utl.printCurrentDirectory();
		assertTrue(true);
	}

}
