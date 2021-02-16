package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;

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
		Map l_map = new Map();
		try {
			l_map.loadMap(d_MAP_DIR + "canada/canada.map");
		} catch (Exception e) {
			e.printStackTrace();
			fail("failure loading an existing valid map");
		}
		assertTrue(true);
	}

	/**
	 * Test the loadmap command. Load a non-existing map file. Should throw an
	 * exception.
	 */
	@Test
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
	
	/**
	 * checks that the add continent is working as expected
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
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_country_1() throws Exception {
		Map l_map = new Map();
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4);
		ICountryModel l_canada = l_map.addCountry(2, "Canada", l_north_america, 0, 0);
		ICountryModel l_usa = l_map.addCountry(3, "USA", l_north_america, 0, 0);
		ArrayList<ICountryModel> l_countries = l_map.getCountries();
		assertTrue(l_countries.size() == 2);
		assertTrue(l_countries.get(0).getId() == 2);
		assertTrue(l_countries.get(1).getId() == 3);
	}

	/**
	 * checks that cannot add duplicate continent
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_add_dup_country_1() throws Exception {
		Map l_map = new Map();
		IContinentModel l_north_america = l_map.addContinent(1, "North-America", 4);
		ICountryModel l_canada = l_map.addCountry(2, "Canada", l_north_america, 0, 0);
		ICountryModel l_usa = l_map.addCountry(2, "Canada", l_north_america, 0, 0);
	}

	/**
	 * simple check of remove continent without countries or neighbors
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_remove_continent_1() throws Exception {
		Map l_map = new Map();
		l_map.addContinent(1, "North-America", 4);
		l_map.removeContinent(1);
		assertTrue(l_map.getContinents().size() == 0);
		l_map.addContinent(10, "North-America", 4);
		l_map.addContinent(11, "Europe", 4);
		l_map.removeContinent(10);
		assertTrue(l_map.getContinents().size() == 1);
		assertEquals("Europe", l_map.getContinents().get(0).getName());
	}
	
	/**
	 * simple check of remove continent error checking
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_remove_continent_2() throws Exception {
		Map l_map = new Map();
		l_map.addContinent(1, "North-America", 4);
		l_map.addContinent(2, "Europe", 4);
		IContinentModel l_continent = l_map.removeContinent(2);
		assertTrue(l_map.getContinents().size() == 1);
		assertEquals("North-America", l_map.getContinents().get(0).getName());
		assertFalse(l_continent.isActive());
		l_map.removeContinent(2);
	}
}
