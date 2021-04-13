package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import soen6441.team01.warzone.model.entities.ConquestMap;

/**
 * Tests for the Map model class
 *
 */
public class MapIoConquestTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";
	public ModelFactory d_model_factory = null;

	/**
	 * setup the environment for testing
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupGameStartupController() throws Exception {
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
	}

	/**
	 * tests parseMap
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_parseMap_1() throws Exception {
		List<String> l_records = Files.readAllLines(new File(d_MAP_DIR + "conquest_maps/Earth.map").toPath(), Charset.defaultCharset());
		MapIoConquest l_mapio = new MapIoConquest();
		ConquestMap l_map = l_mapio.parseMap(l_records);
		assertTrue(l_map.d_continents.size() == 7);
		assertEquals(l_map.d_continents.get(0).d_name, "North America");
		assertEquals(l_map.d_continents.get(0).d_extra_armies, 5);
		assertEquals(l_map.d_continents.get(6).d_name, "Australia");
		assertEquals(l_map.d_continents.get(6).d_extra_armies, 2);
		
		assertEquals(l_map.d_territories.size(), 42);
		assertEquals(l_map.d_territories.get(0).d_name, "Alaska");
		assertEquals(l_map.d_territories.get(0).d_x, "90");
		assertEquals(l_map.d_territories.get(0).d_y, "101");
		assertEquals(l_map.d_territories.get(0).d_continent_name, "North America");
		assertEquals(l_map.d_territories.get(0).d_neighbors.size(), 3);
		assertEquals(l_map.d_territories.get(0).d_neighbors.get(0), "Northwest Territory");
		assertEquals(l_map.d_territories.get(0).d_neighbors.get(1), "Alberta");
		assertEquals(l_map.d_territories.get(0).d_neighbors.get(2), "Kamchatka");

		assertEquals(l_map.d_territories.get(41).d_name, "Eastern Australia");
		assertEquals(l_map.d_territories.get(41).d_x, "791");
		assertEquals(l_map.d_territories.get(41).d_y, "356");
		assertEquals(l_map.d_territories.get(41).d_continent_name, "Australia");
		assertEquals(l_map.d_territories.get(41).d_neighbors.size(), 2);
		assertEquals(l_map.d_territories.get(41).d_neighbors.get(0), "Western Australia");
		assertEquals(l_map.d_territories.get(41).d_neighbors.get(1), "New Guinea");

		assertEquals(l_map.d_territories.get(4).d_name, "Ontario");
		assertEquals(l_map.d_territories.get(4).d_x, "215");
		assertEquals(l_map.d_territories.get(4).d_y, "142");
		assertEquals(l_map.d_territories.get(4).d_continent_name, "North America");
		assertEquals(l_map.d_territories.get(4).d_neighbors.size(), 6);
		assertEquals(l_map.d_territories.get(4).d_neighbors.get(0), "Northwest Territory");
		assertEquals(l_map.d_territories.get(4).d_neighbors.get(1), "Alberta");
		assertEquals(l_map.d_territories.get(4).d_neighbors.get(2), "Greenland");
		assertEquals(l_map.d_territories.get(4).d_neighbors.get(3), "Quebec");
		assertEquals(l_map.d_territories.get(4).d_neighbors.get(4), "Western United States");
		assertEquals(l_map.d_territories.get(4).d_neighbors.get(5), "Eastern United States");

		assertEquals(l_map.d_territories.get(20).d_name, "Great Britain");
		assertEquals(l_map.d_territories.get(20).d_x, "433");
		assertEquals(l_map.d_territories.get(20).d_y, "141");
		assertEquals(l_map.d_territories.get(20).d_continent_name, "Europe");
		assertEquals(l_map.d_territories.get(20).d_neighbors.size(), 4);
		assertEquals(l_map.d_territories.get(20).d_neighbors.get(0), "Iceland");
		assertEquals(l_map.d_territories.get(20).d_neighbors.get(1), "Scandinavia");
		assertEquals(l_map.d_territories.get(20).d_neighbors.get(2), "Western Europe");
		assertEquals(l_map.d_territories.get(20).d_neighbors.get(3), "Northern Europe");
	}
	
	/**
	 * test isConquestFileFormat
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_isConquestFileFormat_1() throws Exception {
		List<String> l_records = Files.readAllLines(new File(d_MAP_DIR + "conquest_maps/Earth.map").toPath(), Charset.defaultCharset());
		assertTrue(MapIoConquest.isConquestFileFormat(l_records));

		l_records = Files.readAllLines(new File(d_MAP_DIR + "conquest_maps/Europe.map").toPath(), Charset.defaultCharset());
		assertTrue(MapIoConquest.isConquestFileFormat(l_records));

		l_records = Files.readAllLines(new File(d_MAP_DIR + "conquest_maps/USA.map").toPath(), Charset.defaultCharset());
		assertTrue(MapIoConquest.isConquestFileFormat(l_records));
	}

	/**
	 * test isConquestFileFormat
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_isConquestFileFormat_2() throws Exception {
		List<String> l_records = Files.readAllLines(new File(d_MAP_DIR + "canada/canada.map").toPath(), Charset.defaultCharset());
		assertFalse(MapIoConquest.isConquestFileFormat(l_records));
	}

}
