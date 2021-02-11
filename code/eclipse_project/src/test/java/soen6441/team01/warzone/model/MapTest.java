package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MapTest
{
	//private String MAP_DIR = ".\\target\\test-classes\\maps\\";
	private String MAP_DIR = "./src/test/resources/maps/";
	
	@Test
	/**
	 * Test the editmap command in the model Map class
	 * Simple test to load an existing valid map file
	 */
	public void test_editmap_command_1()
	{
		Map map = new Map();
		try
		{
			map.editMap(MAP_DIR + "canada/canada.map");
		} catch (IOException e)
		{
			e.printStackTrace();
			fail("failure loading an existing valid map");
		}
		assertTrue(true);
	}

}



