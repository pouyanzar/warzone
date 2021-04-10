package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.entities.ConquestMap;
import soen6441.team01.warzone.model.entities.DominationMap;

/**
 * Tests for the Map model class
 *
 */
public class MapIoDominationTest {
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
	 * tests the getMapAsDominationMapFormat function
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_getMapAsDominationMapFormat_1() throws Exception {
		Map l_map = new Map(d_model_factory);
		l_map = (Map) Map.loadMapFromFile(d_MAP_DIR + "canada/canada.map", d_model_factory);
		l_map.validatemap();
		MapIoDomination l_dmap = new MapIoDomination();
		ArrayList<String> l_xmap = l_dmap.getMapAsTextArray(l_map);
		assertTrue(l_xmap.get(0).equals("[files]"));
		assertTrue(l_xmap.get(2).equals("[continents]"));
		assertTrue(l_xmap.get(3).equals("Atlantic_Provinces 3 white"));
		assertTrue(l_xmap.get(8).equals("Northwestern_Territories 2 white"));
		assertTrue(l_xmap.get(10).equals("[countries]"));
		assertTrue(l_xmap.get(11).equals("1 New_Brunswick 1 501 350"));
		assertTrue(l_xmap.get(20).equals("10 Ontario-West 2 345 337"));
		assertTrue(l_xmap.get(30).equals("20 Manitoba-North 4 296 277"));
		assertTrue(l_xmap.get(40).equals("30 Northwest_Territories-Continental 6 217 178"));
		assertTrue(l_xmap.get(41).equals("31 Yukon_Territory 6 147 145"));
		assertTrue(l_xmap.get(43).equals("[borders]"));
		assertTrue(l_xmap.get(44).equals("1 8 2 3"));
		assertTrue(l_xmap.get(54).equals("11 7 9 10 20 22"));
		assertTrue(l_xmap.get(58).equals("15 14 16 17"));
		assertTrue(l_xmap.get(64).equals("21 20 22 23 24 25 30"));
		assertTrue(l_xmap.get(73).equals("30 17 18 19 21 28 29 31"));
		assertTrue(l_xmap.get(74).equals("31 17 30"));
		Map l_map_model = (Map) Map.loadMap(l_xmap, d_model_factory);
		l_map_model.validatemap();
		ArrayList<String> l_ymap  = l_dmap.getMapAsTextArray(l_map_model);
		assertTrue(l_xmap.size() == l_ymap.size());
		for (int i = 0; i < l_xmap.size(); i++) {
			assertTrue(l_xmap.get(i).equals(l_ymap.get(i)));
		}
	}

	/**
	 * test isDominationFileFormat
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_isDominationFileFormat_1() throws Exception {
		List<String> l_records = Files.readAllLines(new File(d_MAP_DIR + "canada/canada.map").toPath(), Charset.defaultCharset());
		assertTrue(MapIoDomination.isDominationFileFormat(l_records));

		l_records = Files.readAllLines(new File(d_MAP_DIR + "solar/solar.map").toPath(), Charset.defaultCharset());
		assertTrue(MapIoDomination.isDominationFileFormat(l_records));

		l_records = Files.readAllLines(new File(d_MAP_DIR + "usa8/usa8regions.map").toPath(), Charset.defaultCharset());
		assertTrue(MapIoDomination.isDominationFileFormat(l_records));
	
		l_records = Files.readAllLines(new File(d_MAP_DIR + "world_small/world_small.map").toPath(), Charset.defaultCharset());
		assertTrue(MapIoDomination.isDominationFileFormat(l_records));
	}

	/**
	 * test isDominationFileFormat
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_isDominationFileFormat_2() throws Exception {
		List<String> l_records = Files.readAllLines(new File(d_MAP_DIR + "conquest_maps/Earth.map").toPath(), Charset.defaultCharset());
		assertFalse(MapIoDomination.isDominationFileFormat(l_records));
	}
}
