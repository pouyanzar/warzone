package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.entities.CardType;

/**
 * Tests for Player model class
 * 
 */
public class OrderBlockadeTest {

	public ModelFactory d_model_factory = null;
	Player d_player;
	Country d_canada;
	Country d_us;


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
		d_us = new Country(2, "US", l_america, 250, 250, d_model_factory);
	}

	/**
	 * Test a valid blockade order
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_blockade_valid_1() throws Exception {
		d_canada.addNeighbor(d_us);
		d_canada.setArmies(6);
		d_player.addPlayerCountry(d_canada);
		d_player.addCard(new Card(CardType.blockade));
		assertTrue(d_canada.getOwner() != null);

		OrderBlockade l_blockade = new OrderBlockade(d_player, d_canada);
		assertTrue(d_player.getCards().size() == 1);
		String l_msg = l_blockade.execute();
		assertTrue(l_msg.contains("Canada has been blockaded and now has 18 armies and has become neutral"));
		assertTrue(d_player.getCards().size() == 0);
		assertTrue(d_canada.getOwner() == null);
	}
	
	/**
	 * Test no blockade card
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_blockade_invalid_noblockadecard() throws Exception {
		d_canada.addNeighbor(d_us);
		d_canada.setArmies(6);
		d_player.addPlayerCountry(d_canada);

		String l_msg = "";
		try {
			new OrderBlockade(d_player, d_us);
		} catch(Exception ex) {
			l_msg = ex.getMessage();
		}
		assertEquals(l_msg, "John does not have a blockade card!");
	}
	
	/**
	 * Test cannot blockade own country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_blockade_invalid_blockademycountry() throws Exception {
		d_canada.addNeighbor(d_us);
		d_canada.setArmies(6);
		d_player.addPlayerCountry(d_canada);
		Card l_card = new Card(CardType.blockade);
		d_player.addCard(l_card);
		
		String l_msg = "";
		try {
			new OrderBlockade(d_player, d_us);
		} catch(Exception ex) {
			l_msg = ex.getMessage();
		}
		assertTrue(l_msg.contains("Cannot blockade US since player John does not own it"));
	}
}
