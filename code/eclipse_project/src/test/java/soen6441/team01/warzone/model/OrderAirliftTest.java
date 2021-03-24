package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.entities.CardType;

/**
 * Tests for Player model class
 * 
 */
public class OrderAirliftTest {

	public ModelFactory d_model_factory = null;
	Player d_player;
	Country d_canada;
	Country d_usa;

	/**
	 * setup the environment for testing
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupGameEnviroment() throws Exception {
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		d_player = new Player("John", d_model_factory);
		Continent l_america = new Continent(1, "America", 10);
		d_canada = new Country(1, "Canada", l_america, 250, 250, d_model_factory);
		d_usa = new Country(2, "USA", l_america, 250, 250, d_model_factory);
	}

	/**
	 * Test a valid airlift order
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_airlift_valid_1() throws Exception {
		d_canada.setArmies(6);
		d_player.addPlayerCountry(d_canada);
		d_player.addPlayerCountry(d_usa);
		d_player.addCard(new Card(CardType.airlift));
		OrderAirlift l_airlift = new OrderAirlift("Canada", "USA", 4, d_player);
		assertTrue(d_player.getCards().size() == 1);
		String l_msg = l_airlift.execute();
		assertTrue(l_msg.contains("4 armies have been airlifted from Canada to USA"));
		assertTrue(d_player.getCards().size() == 0);
		assertTrue(d_canada.getArmies() == 2);
		assertTrue(d_usa.getArmies() == 4);
	}

	/**
	 * Test no airlift card
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_airlift_invalid_noairliftcard() throws Exception {
		OrderAirlift l_airlift = null;
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(6);
		d_player.addPlayerCountry(d_canada);

		String l_msg = "";
		try {
			l_airlift = new OrderAirlift("Canada", "USA", 3, d_player);
		} catch (Exception ex) {
			l_msg = ex.getMessage();
		}
		assertEquals(l_msg, "John does not have an airlift card!");
	}

	/**
	 * Test airlift to non owned country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_airlift_invalid_airliftmycountry_1() throws Exception {
		OrderAirlift l_airlift = null;
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(6);
		Card l_card = new Card(CardType.airlift);
		d_player.addCard(l_card);

		String l_msg = "";
		try {
			l_airlift = new OrderAirlift("Canada", "USA", 3, d_player);
		} catch (Exception ex) {
			l_msg = ex.getMessage();
		}
		assertTrue(l_msg.contains("airlift source country 'Canada' does not belong to John"));
	}

	/**
	 * Test airlift to non owned country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_airlift_invalid_airliftmycountry_2() throws Exception {
		OrderAirlift l_airlift = null;
		d_player.addPlayerCountry(d_canada);
		d_canada.setArmies(3);
		Card l_card = new Card(CardType.airlift);
		d_player.addCard(l_card);

		String l_msg = "";
		try {
			l_airlift = new OrderAirlift("Canada", "USA", 3, d_player);
		} catch (Exception ex) {
			l_msg = ex.getMessage();
		}
		assertTrue(l_msg.contains("airlift target country 'USA' does not belong to John"));
	}

	/**
	 * Test not enough armies
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_airlift_invalid_airliftmycountry_3() throws Exception {
		OrderAirlift l_airlift = null;
		d_player.addPlayerCountry(d_canada);
		d_canada.setArmies(2);
		Card l_card = new Card(CardType.airlift);
		d_player.addCard(l_card);
		
		String l_msg = "";
		try {
			l_airlift = new OrderAirlift("Canada", "USA", 3, d_player);
		} catch(Exception ex) {
			l_msg = ex.getMessage();
		}
		assertTrue(l_msg.contains("not enough armies on Canada (2) to airlift 3 armies to USA"));
	}
}
