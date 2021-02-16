package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import soen6441.team01.warzone.model.contracts.IContinentModel;

/**
 * Tests for the Continent model class
 *
 */
public class ContinentTest {

	/**
	 * test that the constructor is setting the core attributes properly and that
	 * the core getters and setters are working with valid data
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_getters_setters_1() throws Exception {
		Continent l_continent = new Continent(1, "North-America", 2);
		assertEquals(1, l_continent.getId());
		assertEquals("North-America", l_continent.getName());
		assertEquals(2, l_continent.getExtraArmy());
		l_continent.setId(-1);
		l_continent.setName("Europe");
		l_continent.setExtraArmy(0);
		assertEquals(-1, l_continent.getId());
		assertEquals("Europe", l_continent.getName());
		assertEquals(0, l_continent.getExtraArmy());
	}

	/**
	 * test id setter is validating properly
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_id_setter_1() throws Exception {
		Continent l_continent = new Continent(0, "North-America", 0);
		l_continent.setId(-2);
	}

	/**
	 * test name setter is validating properly
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_name_setter_1() throws Exception {
		Continent l_continent = new Continent(0, "North-America", 0);
		l_continent.setName("North America");
	}

	/**
	 * test extra_army setter is validating properly
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_extra_army_setter_1() throws Exception {
		Continent l_continent = new Continent(0, "North-America", 0);
		l_continent.setExtraArmy(-1);
	}

	/**
	 * test static function findContinent
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_findContinent_1() throws Exception {
		ArrayList<IContinentModel> l_continents = new ArrayList<IContinentModel>();
		l_continents.add(new Continent(1, "North-America", 0));
		l_continents.add(new Continent(2, "Europe", 0));
		IContinentModel l_result = Continent.findContinent(2, l_continents);
		if (l_result.getName() != "Europe") {
			fail("problem finding existing continent");
		}
		l_result = Continent.findContinent(3, l_continents);
		if (l_result != null) {
			fail("problem finding non-existing continent");
		}
	}
}
