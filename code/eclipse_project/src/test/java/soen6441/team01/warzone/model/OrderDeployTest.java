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
public class OrderDeployTest {

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
	 * Test if the desired countries added to the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_deploy_valid_1() throws Exception {
		Player l_player = new Player("player", d_model_factory);
		l_player .setReinforcements(7);
		Continent l_america = new Continent(1, "America", 10);
		Country l_canada = new Country(1, "Canada", l_america, 250, 250, d_model_factory);
		l_player.addPlayerCountry(l_canada);

		OrderDeploy l_deploy = new OrderDeploy("Canada", 7, l_player);
		l_deploy.execute();
		assertTrue( l_canada.getArmies() == 7);

		l_player .setReinforcements(3);
		l_deploy = new OrderDeploy("Canada", 3, l_player);
		l_deploy.execute();
		assertTrue( l_canada.getArmies() == 10);
	}
}
