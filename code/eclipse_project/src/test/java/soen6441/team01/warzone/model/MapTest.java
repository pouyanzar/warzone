package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;

/**
 * Tests for the Map model class
 *
 */
public class MapTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";

	/**
	 * Test the loadmap command. Simple test to load an existing valid map file
	 */
	@Test
	public void test_loadmap_command_1() {
		IMapModel l_map = new Map();
		try {
			l_map = Map.loadMapFromFile(d_MAP_DIR + "canada/canada.map");
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
		IMapModel l_map = new Map();
		try {
			l_map = Map.loadMapFromFile(d_MAP_DIR + "canada/quebec.map");
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
		Map l_map = new Map();
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4);
		IContinentModel l_europe = l_map.addContinent(2, "Europe", 4);
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
		Map l_map = new Map();
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4);
		IContinentModel l_europe = l_map.addContinent(1, "Europe", 4);
	}

	/**
	 * checks that the add country is working as expected
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_country_1() throws Exception {
		Map l_map = new Map();
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4);
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
		Map l_map = new Map();
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4);
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
		Map l_map = new Map();
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
		Map l_map = new Map();
		l_map.addNeighbor("Canada", "USA");
	}

	/**
	 * checks addNeighbor invalid test
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_add_neighbor_invalid_2() throws Exception {
		Map l_map = new Map();
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
		Map l_map = new Map();
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
		Map l_map = new Map();
		l_map.removeNeighbor("Canada", "USA");
	}

	/**
	 * checks if validatemap function works properly for valid map
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_validatemap_true() throws Exception {
		IMapModel l_map = new Map();
		l_map = Map.loadMapFromFile(d_MAP_DIR + "canada/canada.map");

		assertTrue(l_map.validatemap(d_MAP_DIR + "canada/canada.map"));
	}

	/**
	 * checks if validatemap function works properly for invalid map
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_validatemap_false() throws Exception {
		IMapModel l_map = new Map();
		l_map = Map.loadMapFromFile(d_MAP_DIR + "canada_incomplete/canada_incomplete.map");

		assertTrue(!l_map.validatemap(d_MAP_DIR + "canada_incomplete/canada_incomplete.map"));
	}

	/**
	 * checks if validatemap function works properly for invalid map
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_validatemap_false2() throws Exception {
		IMapModel l_map = new Map();
		l_map = Map.loadMapFromFile(d_MAP_DIR + "canada_incomplete2/canada_incomplete.map");

		assertTrue(!l_map.validatemap(d_MAP_DIR + "canada_incomplete2/canada_incomplete.map"));
	}
	
	/**
	 * checks if validatemap function works properly for invalid map
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_validatemap_false3() throws Exception {
		IMapModel l_map = new Map();
		l_map = Map.loadMapFromFile(d_MAP_DIR + "canada_incomplete3/canada.map");

		assertTrue(!l_map.validatemap(d_MAP_DIR + "canada_incomplete3/canada.map"));
	}
	
	/**
	 * checks editmap functionality
	 * 
	 * @throws Exception when there is an exception
	 */
//	@Test
//	public void test_editmap() throws Exception {
//		Map l_map = new Map();
//		l_map.editmap(d_MAP_DIR + "germany");
//
//		assertTrue(true);
//	}
}
