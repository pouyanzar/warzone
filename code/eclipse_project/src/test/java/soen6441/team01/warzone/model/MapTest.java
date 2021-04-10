package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.entities.DominationMap;
import soen6441.team01.warzone.model.entities.SaveMapFormat;

/**
 * Tests for the Map model class
 *
 */
public class MapTest {
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
	 * Test the loadmap command. Simple test to load an existing valid map file
	 */
	@Test
	public void test_loadmap_command_1() {
		IMapModel l_map = new Map(d_model_factory);
		try {
			l_map = Map.loadMapFromFile(d_MAP_DIR + "canada/canada.map", d_model_factory);
		} catch (Exception e) {
			e.printStackTrace();
			fail("failure loading an existing valid map");
		}

		ArrayList<IContinentModel> l_continents = l_map.getContinents();
		assertTrue(l_continents.size() == 6);
		assertTrue(l_continents.get(5).getName().equals("Northwestern_Territories"));

		ArrayList<ICountryModel> l_countries = l_map.getCountries();
		assertTrue(l_countries.size() == 31);
		assertTrue(l_countries.get(5).getName().equals("Quebec-North"));
		assertTrue(l_countries.get(5).getNeighbors().size() == 4);

		assertTrue(l_map.getNeighbors(6).size() == 4);
	}

	/**
	 * Test the loadmap command. Load a non-existing map file. Should throw an
	 * exception.
	 */
	@Test
	public void test_loadmap_command_2() {
		Boolean l_assert_result = true;
		IMapModel l_map = new Map(d_model_factory);
		try {
			l_map = Map.loadMapFromFile(d_MAP_DIR + "canada/quebec.map", d_model_factory);
			l_assert_result = false;
		} catch (Exception e) {
			if (!e.getMessage().contains("Error loading map file")) {
				l_assert_result = false;
			}
		}
		if (!l_assert_result) {
			fail("expecting an exception trying to load a non-existing map file");
		} else {
			assertTrue(true);
		}
	}

	/**
	 * checks that the add continent is working as expected
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_continent_1() throws Exception {
		Map l_map = new Map(d_model_factory);
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4, null);
		IContinentModel l_europe = l_map.addContinent(2, "Europe", 4, null);
		ArrayList<IContinentModel> l_continents = l_map.getContinents();
		assertTrue(l_continents.size() == 2);
		assertTrue(l_continents.get(0).getId() == 1);
		assertTrue(l_continents.get(1).getId() == 2);
	}

	/**
	 * checks that cannot add duplicate continent
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_add_dup_continent_1() throws Exception {
		Map l_map = new Map(d_model_factory);
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4, null);
		IContinentModel l_europe = l_map.addContinent(1, "Europe", 4, null);
	}

	/**
	 * checks that the add country is working as expected
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_country_1() throws Exception {
		Map l_map = new Map(d_model_factory);
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4, null);
		ICountryModel l_canada = l_map.addCountry("Canada", l_north_america, 0, 0);
		ICountryModel l_usa = l_map.addCountry("USA", l_north_america, 0, 0);
		ArrayList<ICountryModel> l_countries = l_map.getCountries();
		assertTrue(l_countries.size() == 2);
		assertTrue(l_countries.get(0).getId() == 1);
		assertTrue(l_countries.get(1).getId() == 2);
	}

	/**
	 * checks that cannot add duplicate continent
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_add_dup_country_1() throws Exception {
		Map l_map = new Map(d_model_factory);
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4, null);
		ICountryModel l_canada = l_map.addCountry("Canada", l_north_america, 0, 0);
		ICountryModel l_usa = l_map.addCountry("Canada", l_north_america, 0, 0);
	}

	/**
	 * checks addNeighbor valid tests
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_neighbor_valid() throws Exception {
		Map l_map = new Map(d_model_factory);
		ICountryModel l_canada = l_map.addCountry("Canada", null, 0, 0);
		ICountryModel l_usa = l_map.addCountry("USA", null, 0, 0);
		l_canada.addNeighbor(l_usa);
		ArrayList<ICountryModel> l_countries = l_map.getCountries();
		assertTrue(l_countries.size() == 2);
		ArrayList<ICountryModel> l_neighbors = l_canada.getNeighbors();
		assertTrue(l_neighbors.size() == 1);
		l_neighbors = l_usa.getNeighbors();
		assertTrue(l_neighbors.size() == 0);
	}

	/**
	 * checks addNeighbor invalid test
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_add_neighbor_invalid_1() throws Exception {
		Map l_map = new Map(d_model_factory);
		l_map.addNeighbor("Canada", "USA");
	}

	/**
	 * checks addNeighbor invalid test
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_add_neighbor_invalid_2() throws Exception {
		Map l_map = new Map(d_model_factory);
		ICountryModel l_canada = l_map.addCountry("Canada", null, 0, 0);
		l_map.addNeighbor("Canada", "USA");
	}

	/**
	 * checks removeNeighbor valid tests
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_remove_neighbor_valid() throws Exception {
		Map l_map = new Map(d_model_factory);
		ICountryModel l_canada = l_map.addCountry("Canada", null, 0, 0);
		ICountryModel l_usa = l_map.addCountry("USA", null, 0, 0);
		l_canada.addNeighbor(l_usa);
		ArrayList<ICountryModel> l_neighbors = l_canada.getNeighbors();
		assertTrue(l_neighbors.size() == 1);
		assertTrue(l_neighbors.get(0).getName().equals("USA"));
		l_map.removeNeighbor("Canada", "USA");
		l_neighbors = l_canada.getNeighbors();
		assertTrue(l_neighbors.size() == 0);
	}

	/**
	 * checks removeNeighbor invalid tests
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_remove_neighbor_invalid_1() throws Exception {
		Map l_map = new Map(d_model_factory);
		l_map.removeNeighbor("Canada", "USA");
	}

	/**
	 * checks if validatemap function works properly for valid map
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_validatemap_true() throws Exception {
		Map l_map = new Map(d_model_factory);
		l_map = (Map) Map.loadMapFromFile(d_MAP_DIR + "canada/canada.map", d_model_factory);

		assertTrue(l_map.validatemap());
	}

	/**
	 * checks if validatemap function works properly for invalid map // (1) map
	 * validation – map is a connected graph; (2) continent validation – continent
	 * is a connected subgraph;
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_validatemap_false() throws Exception {
		Map l_map = new Map(d_model_factory);
		l_map = (Map) Map.loadMapFromFile(d_MAP_DIR + "canada_incomplete/canada_incomplete.map", d_model_factory);

		assertTrue(!l_map.validatemap());
	}

	/**
	 * checks if validatemap function works properly for invalid map. // (1) map
	 * validation – map is a connected graph; (2) continent validation – continent
	 * is a connected subgraph;
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_validatemap_false2() throws Exception {
		Map l_map = new Map(d_model_factory);
		l_map = (Map) Map.loadMapFromFile(d_MAP_DIR + "canada_incomplete2/canada_incomplete.map", d_model_factory);
		assertTrue(!l_map.validatemap());
	}

	/**
	 * checks if validatemap function works properly for invalid map. // (1) map
	 * validation – map is a connected graph; (2) continent validation – continent
	 * is a connected subgraph;
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_validatemap_false3() throws Exception {
		Map l_map = new Map(d_model_factory);
		l_map = (Map) Map.loadMapFromFile(d_MAP_DIR + "canada_incomplete3/canada.map", d_model_factory);
		assertTrue(!l_map.validatemap());
	}

	/**
	 * tests the saveMap function
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_saveMap_1() throws Exception {
		Map l_map = new Map(d_model_factory);
		File myObj = new File("\\tmp\\test_1_map.map");
		myObj.delete();
		l_map = (Map) Map.loadMapFromFile(d_MAP_DIR + "canada/canada.map", d_model_factory);
		l_map.saveMap("\\tmp\\test_1_map.map", SaveMapFormat.Domination);
		l_map = new Map(d_model_factory);
		d_model_factory.setMapModel(l_map);
		l_map = (Map) Map.loadMapFromFile("\\tmp\\test_1_map.map", d_model_factory);
		assertTrue(l_map.validatemap());
	}

	/**
	 * tests loading a domination style map
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_loadmap_domination_file_1() throws Exception {
		IMapModel l_map = Map.loadMapFromFile(d_MAP_DIR + "canada/canada.map", d_model_factory);
		assertTrue(l_map.getContinents().size() == 6);
		assertTrue(l_map.getCountries().size() == 31);

		l_map = Map.loadMapFromFile(d_MAP_DIR + "world_small/world_small.map", d_model_factory);
		assertTrue(l_map.getContinents().size() == 4);
		assertTrue(l_map.getCountries().size() == 11);
	}

	/**
	 * tests loading a conquest style map
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_loadmap_conquest_file_1() throws Exception {
		IMapModel l_map = Map.loadMapFromFile(d_MAP_DIR + "conquest_maps/Earth.map", d_model_factory);
		assertTrue(l_map.getContinents().size() == 7);
		assertTrue(l_map.getCountries().size() == 42);

		l_map = Map.loadMapFromFile(d_MAP_DIR + "conquest_maps/Europe.map", d_model_factory);
		assertTrue(l_map.getContinents().size() == 7);
		assertTrue(l_map.getCountries().size() == 50);

		l_map = Map.loadMapFromFile(d_MAP_DIR + "conquest_maps/USA.map", d_model_factory);
		assertTrue(l_map.getContinents().size() == 7);
		assertTrue(l_map.getCountries().size() == 58);

		l_map = Map.loadMapFromFile(d_MAP_DIR + "conquest_maps/world_small_conquest.map", d_model_factory);
		assertTrue(l_map.getContinents().size() == 4);
		assertTrue(l_map.getCountries().size() == 11);
	}

}
