package soen6441.team01.warzone.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtlTest {

	/**
	 * Simple test to see if the current working directory can be listed to the
	 * console. Also useful during the build (locally and remotely) to see what is
	 * the current working dorectory.
	 */
	@Test
	public void test_printCurrentDirectory() {
		Utl.PrintCurrentDirectory();
		assertTrue(true);
	}

	/**
	 * test the IsValidMapName method 
	 */
	@Test
	public void test_IsValidMapName() {
		assertTrue(Utl.IsValidMapName("Canada"));
		assertTrue(Utl.IsValidMapName("North-America"));
		assertTrue(Utl.IsValidMapName("North_America"));
		assertTrue(Utl.IsValidMapName("Planet1"));
		assertFalse(Utl.IsValidMapName(null));
		assertFalse(Utl.IsValidMapName(""));
		assertFalse(Utl.IsValidMapName(" PlantX"));
		assertFalse(Utl.IsValidMapName("PlantX "));
		assertFalse(Utl.IsValidMapName(" Plant X"));
		assertFalse(Utl.IsValidMapName("*PlantX"));
	}
	
}
