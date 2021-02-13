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
	private String d_MAP_DIR = "./src/test/resources/maps/";

	@Test
	/**
	 * Test the editmap. Simple test to load an existing valid map file
	 */
	public void test_editmap_command_1() {
		Map l_map = new Map();
		try {
			l_map.loadMap(d_MAP_DIR + "canada/canada.map");
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
		Map l_map = new Map();
		try {
			l_map.loadMap(d_MAP_DIR + "canada/canada.map");
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
		Boolean l_assert_result = true;
		Map l_map = new Map();
		try {
			l_map.loadMap(d_MAP_DIR + "canada/quebec.map");
			l_assert_result = false;
		} catch (Exception e) {
		}
		if( !l_assert_result ) {
			fail("expecting an exception trying to load a non-existing map file");
		}
		else {
			assertTrue(true);
		}
	}

}
