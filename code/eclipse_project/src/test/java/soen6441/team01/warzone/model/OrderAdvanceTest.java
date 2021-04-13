package soen6441.team01.warzone.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Player model class
 * 
 * @author Nazanin
 *
 */

public class OrderAdvanceTest {

	ModelFactory d_model_factory = null;
	Player d_player_1;
	Player d_player_2;
	Country d_canada;
	Country d_usa;

	/**
	 * setup the environment for testing
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupGameStartupController() throws Exception {
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		d_player_1 = new Player("Player1", d_model_factory);
		d_player_2 = new Player("Player2", d_model_factory);
		Continent l_america = new Continent(1, "America", 10);
		d_canada = new Country(1, "Canada", l_america, 250, 250, d_model_factory);
		d_usa = new Country(2, "US", l_america, 250, 250, d_model_factory);
	}

	/**
	 * Test advance order invalid scenarios - player not enough armies for the
	 * move/attack move
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_invalid_3() throws Exception {
		String l_msg = "";
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(1);
		d_usa.setArmies(1);
		d_player_1.addPlayerCountry(d_canada);
		d_player_2.addPlayerCountry(d_usa);

		try {
			new OrderAdvance(d_player_1, d_canada, d_usa, 2);
		} catch (Exception ex) {
			l_msg = ex.getMessage();
		}

		assertTrue(l_msg.contains("does not have enough armies on"));
	}

	/**
	 * Test advance order invalid scenarios - source_country is not adjacent to the
	 * destination_country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_invalid_4() throws Exception {
		String l_msg = "";
		d_canada.setArmies(1);
		d_player_1.addPlayerCountry(d_canada);

		try {
			new OrderAdvance(d_player_1, d_canada, d_canada, 1);
		} catch (Exception ex) {
			l_msg = ex.getMessage();
		}

		assertTrue(l_msg.contains("are not neighbors"));
	}
	
	/**
	 * Test advance order invalid scenarios - source_country is not adjacent to the
	 * destination_country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_invalid_2() throws Exception {
		String l_msg = "";
		d_canada.setArmies(1);
		d_player_1.addPlayerCountry(d_canada);

		try {
			new OrderAdvance(d_player_1, d_canada, d_usa, 1);
		} catch (Exception ex) {
			l_msg = ex.getMessage();
		}

		assertTrue(l_msg.contains("are not neighbors"));
	}

	/**
	 * Test advance order invalid scenarios - not players country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_invalid_1() throws Exception {
		String l_msg = "";
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(1);
		d_usa.setArmies(1);
		d_player_1.addPlayerCountry(d_canada);
		d_player_2.addPlayerCountry(d_usa);

		try {
			new OrderAdvance(d_player_1, d_usa, d_canada, 1);
		} catch (Exception ex) {
			l_msg = ex.getMessage();
		}

		assertTrue(l_msg.contains("is not owned by"));
	}

	/**
	 * Test advance order valid scenarios - attacker loses
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_attack_valid_2() throws Exception {
		String l_msg;
		OrderAdvance l_advance;
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(99);
		d_usa.setArmies(1);
		d_player_1.addPlayerCountry(d_canada);
		d_player_2.addPlayerCountry(d_usa);

		l_advance = new OrderAdvance(d_player_1, d_canada, d_usa, 98);
		l_advance.setOdds(-1, 100);
		l_msg = l_advance.execute();
		assertTrue(l_msg.equals("Player1 lost 98 armies from the attack on US"));
		assertTrue(d_canada.getOwner().getName().equals("Player1"));
		assertTrue(d_canada.getArmies() == 1);
		assertTrue(d_usa.getOwner().getName().equals("Player2"));
		assertTrue(d_usa.getArmies() == 1);
	}

	/**
	 * Test advance order valid scenarios - attacker wins
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_attack_valid_1() throws Exception {
		String l_msg;
		OrderAdvance l_advance;
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(3);
		d_usa.setArmies(99);
		d_player_1.addPlayerCountry(d_canada);
		d_player_2.addPlayerCountry(d_usa);

		l_advance = new OrderAdvance(d_player_1, d_canada, d_usa, 1);
		l_advance.setOdds(100, -1);
		l_msg = l_advance.execute();
		assertTrue(l_msg.contains("Player1 won the attack on US ["));
		assertTrue(d_canada.getOwner().getName().equals("Player1"));
		assertTrue(d_canada.getArmies() == 2);
		assertTrue(d_usa.getOwner().getName().equals("Player1"));
		assertTrue(d_usa.getArmies() == 1);
		// check that the player got a card
		assertTrue(d_player_1.getCards().size() == 1);
	}

	/**
	 * Test advance order valid scenarios - attacker wins
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_attack_valid_3() throws Exception {
		String l_msg;
		OrderAdvance l_advance;
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(99);
		d_usa.setArmies(20);
		d_player_1.addPlayerCountry(d_canada);
		d_player_2.addPlayerCountry(d_usa);

		l_advance = new OrderAdvance(d_player_1, d_canada, d_usa, 98);
		l_advance.setOdds(50, 50);
		l_msg = l_advance.execute();
		assertTrue(l_msg.contains("Player1 won the attack on US but lost"));
		assertTrue(d_canada.getOwner().getName().equals("Player1"));
		assertTrue(d_canada.getArmies() == 1);
		assertTrue(d_usa.getOwner().getName().equals("Player1"));
		// check that the player got a card
		assertTrue(d_player_1.getCards().size() == 1);
	}

	/**
	 * Test advance order valid scenario - target country is owner by attacker
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_move_valid_3() throws Exception {
		String l_msg;
		OrderAdvance l_advance;
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(5);
		d_usa.setArmies(0);
		d_player_1.addPlayerCountry(d_canada);
		d_player_1.addPlayerCountry(d_usa);

		l_advance = new OrderAdvance(d_player_1, d_canada, d_usa, 2);
		l_msg = l_advance.execute();
		assertTrue(l_msg.equals("Player1 has successfully moved 2 armies from Canada to US"));
		assertTrue(d_canada.getOwner().getName().equals("Player1"));
		assertTrue(d_canada.getArmies() == 3);
		assertTrue(d_usa.getOwner().getName().equals("Player1"));
		assertTrue(d_usa.getArmies() == 2);
	}

	/**
	 * Test advance order valid scenarios - target country has no armies
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_attack_no_armies_valid_2() throws Exception {
		String l_msg;
		OrderAdvance l_advance;
		d_canada.addNeighbor(d_usa);
		d_canada.setArmies(5);
		d_usa.setArmies(0);
		d_player_1.addPlayerCountry(d_canada);
		d_player_2.addPlayerCountry(d_usa);

		l_advance = new OrderAdvance(d_player_1, d_canada, d_usa, 2);
		l_msg = l_advance.execute();
		assertTrue(l_msg.equals("Player1 has successfully moved 2 armies from Canada to US"));
		assertTrue(d_canada.getOwner().getName().equals("Player1"));
		assertTrue(d_canada.getArmies() == 3);
		assertTrue(d_usa.getOwner().getName().equals("Player1"));
		assertTrue(d_usa.getArmies() == 2);
	}

	/**
	 * Test if advanced countries added to the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_move_valid_1() throws Exception {
		String l_msg;
		Player l_player = new Player("Nazanin", d_model_factory);
		l_player.setReinforcements(7);
		Continent l_america = new Continent(1, "America", 10);
		Country l_canada = new Country(1, "Canada", l_america, 250, 250, d_model_factory);
		Country l_usa = new Country(2, "USA", l_america, 250, 250, d_model_factory);
		l_canada.addNeighbor(l_usa);
		l_canada.setArmies(5);

		l_player.addPlayerCountry(l_canada);
		OrderAdvance l_advance = new OrderAdvance(l_player, l_canada, l_usa, 5);
		l_msg = l_advance.execute();
		assertTrue(l_msg.contains("Nazanin has successfully moved 5 armies from Canada to USA"));
		assertTrue(l_canada.getOwner().getName().equals("Nazanin"));
		assertTrue(l_canada.getArmies() == 0);
		assertTrue(l_usa.getOwner().getName().equals("Nazanin"));
		assertTrue(l_usa.getArmies() == 5);
	}
}
