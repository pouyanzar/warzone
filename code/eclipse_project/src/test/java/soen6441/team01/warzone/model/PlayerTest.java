package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for Player model class
 * 
 * @author pouyan
 *
 */
public class PlayerTest {

	/**
	 * Tests the constructor to initialize the core attributes correctly
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_getter_setter_1() throws Exception {
		Player l_player = new Player("player");
		l_player.setReinforcements(10);
		assertEquals("player", l_player.getName());
		assertEquals(10, l_player.getReinforcements());
		assertTrue(l_player.getPlayerContinents().isEmpty());
		assertTrue(l_player.getPlayerCountries().isEmpty());

	}

	/**
	 * Test if the desired continent added to the list of continents that player
	 * controls
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_continent_player() throws Exception {
		Player l_player = new Player("player");
		Continent l_america = new Continent(1, "America", 10);
		l_player.addPlayerContinent(l_america);
		assertTrue(l_player.getPlayerContinents().contains(l_america));

	}

	/**
	 * Test if the desired continent removed from the list of continents that player
	 * controls
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_remove_continent_player() throws Exception {
		Player l_player = new Player("player");
		Continent l_america = new Continent(1, "America", 10);
		l_player.addPlayerContinent(l_america);
		l_player.removePlayerContinent(l_america);
		assertTrue(!l_player.getPlayerContinents().contains(l_america));

	}

	/**
	 * Test if the desired countries added to the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_country_player() throws Exception {
		Player l_player = new Player("player");
		Continent l_america = new Continent(1, "America", 10);
		Country l_canada = new Country(1, "America", l_america, 250, 250);
		l_player.addPlayerCountry(l_canada);
		assertTrue(l_player.getPlayerCountries().contains(l_canada));

	}

	/**
	 * Test if the desired countries removed from the list of countries player
	 * controls
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_remove_country_player() throws Exception {
		Player l_player = new Player("player");
		Continent l_america = new Continent(1, "America", 10);
		Country l_canada = new Country(1, "America", l_america, 250, 250);
		l_player.addPlayerCountry(l_canada);
		l_player.removePlayerCountry(l_canada);
		assertTrue(!l_player.getPlayerCountries().contains(l_canada));

	}

}
