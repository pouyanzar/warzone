package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.controller.GamePlayController;
import soen6441.team01.warzone.controller.ControllerFactory;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.view.ViewFactory;

/**
 * Tests for the Country model class
 *
 */
public class CountryTest {
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
	 * test that the constructor is setting the core attributes properly and that
	 * the core getters and setters are working with valid data
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_getters_setters_1() throws Exception {
		Continent l_continent = new Continent(1, "North-America", 2);
		Country l_country = new Country(1, "Canada", l_continent, 0, 0, d_model_factory);
		assertEquals(1, l_country.getId());
		assertEquals("Canada", l_country.getName());
		assertEquals(l_continent, l_country.getContinent());

		l_country.setId(-1);
		l_country.setName("United_States");
		l_continent = new Continent(1, "Europe", 2);
		l_country.setContinent(l_continent);
		assertEquals(-1, l_country.getId());
		assertEquals("United_States", l_country.getName());
		assertEquals(l_continent, l_country.getContinent());

		l_country.setContinent(null);
		assertEquals(null, l_country.getContinent());
	}

	/**
	 * test id setter is validating properly
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_id_setter_1() throws Exception {
		Country l_country = new Country(1, "Canada", null, 0, 0, d_model_factory);
		l_country.setId(-2);
	}

	/**
	 * test name setter is validating properly
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_name_setter_1() throws Exception {
		Country l_country = new Country(1, "Canada", null, 0, 0, d_model_factory);
		l_country.setName("United States");
	}

	/**
	 * simple add neighbor tests
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_addNeighbor() throws Exception {
		Country l_country_1 = new Country(1, "Canada", null, 0, 0, d_model_factory);
		Country l_country_2 = new Country(2, "United_States", null, 0, 0, d_model_factory);
		l_country_1.addNeighbor(l_country_2);
		l_country_2.addNeighbor(l_country_1);
		assertTrue(l_country_1.getNeighbors().size() == 1);
		assertSame(l_country_1.getNeighbors().get(0), l_country_2);
		assertTrue(l_country_2.getNeighbors().size() == 1);
		assertSame(l_country_2.getNeighbors().get(0), l_country_1);
	}

	/**
	 * add existing neighbor
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_addNeighbor_existing() throws Exception {
		Country l_country_1 = new Country(1, "Canada", null, 0, 0, d_model_factory);
		Country l_country_2 = new Country(2, "United_States", null, 0, 0, d_model_factory);
		l_country_1.addNeighbor(l_country_2);
		l_country_1.addNeighbor(l_country_2);
		assertTrue(l_country_1.getNeighbors().size() == 1);
		assertSame(l_country_1.getNeighbors().get(0), l_country_2);
	}

	/**
	 * add yourself as neighbor
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_addNeighbor_yourself_1() throws Exception {
		Country l_country_1 = new Country(1, "Canada", null, 0, 0, d_model_factory);
		Country l_country_2 = new Country(1, "Canada", null, 0, 0, d_model_factory);
		l_country_1.addNeighbor(l_country_2);
	}

	/**
	 * add yourself as neighbor
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_addNeighbor_yourself_2() throws Exception {
		Country l_country_1 = new Country(1, "Canada", null, 0, 0, d_model_factory);
		l_country_1.addNeighbor(l_country_1);
	}

	/**
	 * simple remove neighbor tests
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_removeNeighbor() throws Exception {
		Country l_country_1 = new Country(1, "Canada", null, 0, 0, d_model_factory);
		Country l_country_2 = new Country(2, "United_States", null, 0, 0, d_model_factory);
		l_country_1.addNeighbor(l_country_2);
		l_country_2.addNeighbor(l_country_1);
		assertTrue(l_country_1.getNeighbors().size() == 1);
		assertSame(l_country_1.getNeighbors().get(0), l_country_2);
		assertTrue(l_country_2.getNeighbors().size() == 1);
		assertSame(l_country_2.getNeighbors().get(0), l_country_1);
		l_country_1.removeNeighbor("United_States");
		assertTrue(l_country_1.getNeighbors().size() == 0);
		assertTrue(l_country_2.getNeighbors().size() == 1);
	}

	/**
	 * test static function findCountry
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_findCountry_1() throws Exception {
		ArrayList<ICountryModel> l_countries = new ArrayList<ICountryModel>();
		l_countries.add(new Country(1, "Canada", null, 0, 0, d_model_factory));
		l_countries.add(new Country(2, "USA", null, 0, 0, d_model_factory));
		ICountryModel l_result = Country.findCountry(2, l_countries);
		if (l_result.getName() != "USA") {
			fail("problem finding existing country");
		}
		l_result = Country.findCountry(3, l_countries);
		if (l_result != null) {
			fail("problem finding non-existing country");
		}
	}
	
	/**
	 * test static function findCountry
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_findCountry_by_name() throws Exception {
		ArrayList<ICountryModel> l_countries = new ArrayList<ICountryModel>();
		l_countries.add(new Country(1, "Canada", null, 0, 0, d_model_factory));
		l_countries.add(new Country(2, "USA", null, 0, 0, d_model_factory));
		ICountryModel l_result = Country.findCountry("USA", l_countries);
		if (l_result.getName() != "USA") {
			fail("problem finding existing country");
		}
		l_result = Country.findCountry("Canada", l_countries);
		if (l_result.getName() != "Canada") {
			fail("problem finding existing country");
		}
		l_result = Country.findCountry("France", l_countries);
		if (l_result != null) {
			fail("problem finding non-existing country");
		}
	}
}
