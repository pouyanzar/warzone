package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Player model class
 * 
 * @author pouyan
 *
 */
public class PlayerTest {

	public SoftwareFactoryModel d_model_factory = null;

	/**
	 * setup the environment for testing
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupGameStartupController() throws Exception {
		d_model_factory = SoftwareFactoryModel.createWarzoneBasicConsoleGameModels();
	}

	/**
	 * Test if the desired countries added to the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_country_player() throws Exception {
		SoftwareFactoryModel l_factory_model = SoftwareFactoryModel.createWarzoneBasicConsoleGameModels();
		Player l_player = new Player("player", l_factory_model);
		Continent l_america = new Continent(1, "America", 10);
		Country l_canada = new Country(1, "America", l_america, 250, 250, d_model_factory);
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
		SoftwareFactoryModel l_factory_model = SoftwareFactoryModel.createWarzoneBasicConsoleGameModels();
		Player l_player = new Player("player", l_factory_model);
		Continent l_america = new Continent(1, "America", 10);
		Country l_canada = new Country(1, "America", l_america, 250, 250, d_model_factory);
		l_player.addPlayerCountry(l_canada);
		l_player.removePlayerCountry(l_canada);
		assertTrue(!l_player.getPlayerCountries().contains(l_canada));
	}

}
