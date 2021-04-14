package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.entities.CardType;

/**
 * Tests for Player model class
 * 
 * @author pouyan
 *
 */
public class OrderBombTest {

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
	 * Test a valid bomb order
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_bomb_valid_1() throws Exception {
		d_canada.addNeighbor(d_us);
		d_us.setArmies(6);
		d_player.addPlayerCountry(d_canada);
		d_player.addCard(new Card(CardType.bomb));

		OrderBomb l_bomb = new OrderBomb(d_player, d_us);
		assertTrue(d_player.getCards().size() == 1);
		String l_msg = l_bomb.execute();
		assertTrue(l_msg.contains("US has been bombed. Armies reduced from 6 to 3"));
		assertTrue(d_player.getCards().size() == 0);
	}
	
	/**
	 * Test no bomb card
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_bomb_invalid_nobombcard() throws Exception {
		d_canada.addNeighbor(d_us);
		d_canada.setArmies(6);
		d_player.addPlayerCountry(d_canada);

		String l_msg = "";
		try {
			new OrderBomb(d_player, d_us);
		} catch(Exception ex) {
			l_msg = ex.getMessage();
		}
		assertEquals(l_msg, "John does not have a bomb card!");
		
		Card l_card = new Card(CardType.blockade);
		d_player.addCard(l_card);

		try {
			new OrderBomb(d_player, d_us);
		} catch(Exception ex) {
			l_msg = ex.getMessage();
		}
		assertEquals(l_msg, "John does not have a bomb card!");
	}
	
	/**
	 * Test invalid bomb order on non adjcent country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_bomb_invalid_notadjcent() throws Exception {
		d_canada.addNeighbor(d_us);
		d_canada.setArmies(6);
		d_player.addPlayerCountry(d_us);
		Card l_card = new Card(CardType.bomb);
		d_player.addCard(l_card);
		
		String l_msg = "";
		try {
			new OrderBomb(d_player, d_canada);
		} catch(Exception ex) {
			l_msg = ex.getMessage();
		}
		assertTrue(l_msg.contains("is not an adjacent country"));
	}
	
	/**
	 * Test cannot bomb own country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_bomb_invalid_bombmycountry() throws Exception {
		d_canada.addNeighbor(d_us);
		d_canada.setArmies(6);
		d_player.addPlayerCountry(d_canada);
		Card l_card = new Card(CardType.bomb);
		d_player.addCard(l_card);
		
		String l_msg = "";
		try {
			new OrderBomb(d_player, d_canada);
		} catch(Exception ex) {
			l_msg = ex.getMessage();
		}
		assertTrue(l_msg.contains("Cannot bomb your own country"));
	}
	
	/**
	 * Test bomb order - diplomactic country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_bomb_diplomacy_1() throws Exception {
		Player d_player2 = new Player("Player2", d_model_factory);
		d_player2.addPlayerCountry(d_us);

		d_canada.addNeighbor(d_us);
		d_us.setArmies(6);
		d_player.addPlayerCountry(d_canada);
		d_player.addCard(new Card(CardType.bomb));
		
		d_player.addDiplomacy(d_player2);

		OrderBomb l_bomb = new OrderBomb(d_player, d_us);
		assertTrue(d_player.getCards().size() == 1);
		String l_msg = l_bomb.execute();
		assertTrue(l_msg.contains("no bombing on US permitted since John and Player2 have diplomatic relations"));
		assertTrue(d_player.getCards().size() == 0);
	}
	

}
