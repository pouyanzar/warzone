package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * Tests for the Map model class
 * @author John
 *
 */
public class MapTest {
	private String MAP_DIR = "./src/test/resources/maps/";

	@Test
	/**
	 * Test the editmap. Simple test to load an existing valid map file
	 */
	public void test_editmap_command_1() {
		Map map = new Map();
		try {
			map.loadMap(MAP_DIR + "canada/canada.map");
		} catch (Exception e) {
			e.printStackTrace();
			fail("failure loading an existing valid map");
		}
		assertTrue(true);
	}

	@Test
	/**
	 * Test the loadmap command. Simple test to load an existing valid map file
	 */
	public void test_loadmap_command_1() {
		Map map = new Map();
		try {
			map.loadMap(MAP_DIR + "canada/canada.map");
		} catch (Exception e) {
			e.printStackTrace();
			fail("failure loading an existing valid map");
		}
		assertTrue(true);
	}

	@Test
	/**
	 * Test the loadmap command. Load a non-existing map file. Should throw an
	 * exception.
	 */
	public void test_loadmap_command_2() {
		Map map = new Map();
		try {
			map.loadMap(MAP_DIR + "canada/quebec.map");
		} catch (Exception e) {
			assertTrue(true);
		}
		fail("expecting an exception trying to load a non-existing map file");
	}

}
