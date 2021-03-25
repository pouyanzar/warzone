package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.entities.CardType;

/**
 * Tests for Player model class
 * 
 */
public class OrderDiplomacyTest {

	public ModelFactory d_model_factory = null;
	Player d_player1;
	Player d_player2;
	Player d_player3;
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
		d_player1 = new Player("Player1", d_model_factory);
		d_player2 = new Player("Player2", d_model_factory);
		d_player3 = new Player("Player3", d_model_factory);
		Continent l_america = new Continent(1, "America", 10);
		d_canada = new Country(1, "Canada", l_america, 250, 250, d_model_factory);
		d_usa = new Country(2, "USA", l_america, 250, 250, d_model_factory);
	}

	/**
	 * Test a valid diplomacy order
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_diplomacy_valid_1() throws Exception {
		d_player1.addCard(new Card(CardType.diplomacy));
		OrderDiplomacy l_diplomacy = new OrderDiplomacy(d_player1, d_player2);
		assertTrue(d_player1.getCards().size() == 1);
		assertTrue(!d_player1.isDiplomatic(d_player2));
		assertTrue(!d_player1.isDiplomatic(d_player3));
		assertTrue(!d_player2.isDiplomatic(d_player1));
		assertTrue(!d_player2.isDiplomatic(d_player3));
		String l_msg = l_diplomacy.execute();
		assertTrue(l_msg.contains("diplomacy between Player1 and Player2 for this turn"));
		assertTrue(d_player1.getCards().size() == 0);
		assertTrue(d_player1.isDiplomatic(d_player2));
		assertTrue(d_player2.isDiplomatic(d_player1));
		assertTrue(!d_player1.isDiplomatic(d_player3));
		assertTrue(!d_player2.isDiplomatic(d_player3));
	}

	/**
	 * Test a diplomacy order with an attack
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_diplomacy_with_attack_1() throws Exception {
		String l_msg;
		d_player1.addCard(new Card(CardType.diplomacy));
		OrderDiplomacy l_diplomacy = new OrderDiplomacy(d_player1, d_player2);
		l_msg = l_diplomacy.execute();
		assertTrue(l_msg.contains("diplomacy between Player1 and Player2 for this turn"));
		assertTrue(d_player1.getCards().size() == 0);
		assertTrue(d_player1.isDiplomatic(d_player2));
		assertTrue(d_player2.isDiplomatic(d_player1));
		assertTrue(!d_player1.isDiplomatic(d_player3));
		assertTrue(!d_player2.isDiplomatic(d_player3));

		// try an attack
		OrderAdvance l_advance;
		d_canada.addNeighbor(d_usa);
		d_usa.addNeighbor(d_canada);
		d_canada.setArmies(3);
		d_usa.setArmies(3);
		d_player1.addPlayerCountry(d_canada);
		d_player2.addPlayerCountry(d_usa);

		l_advance = new OrderAdvance(d_player1, d_canada, d_usa, 1);
		l_msg = l_advance.execute();
		assertTrue(
				l_msg.contains("no advancement on USA permitted since Player1 and Player2 have diplomatic relations."));

		l_advance = new OrderAdvance(d_player2, d_usa, d_canada, 1);
		l_msg = l_advance.execute();
		assertTrue(l_msg
				.contains("no advancement on Canada permitted since Player2 and Player1 have diplomatic relations."));

		d_player3.addPlayerCountry(d_usa);
		l_advance = new OrderAdvance(d_player1, d_canada, d_usa, 1);
		l_msg = l_advance.execute();
		assertTrue(l_msg.contains("attack on"));
	}

	/**
	 * Test no diplomacy card
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_diplomacy_invalid_nodiplomacycard() throws Exception {
		String l_msg = "";
		try {
			new OrderDiplomacy(d_player1, d_player2);
		} catch (Exception ex) {
			l_msg = ex.getMessage();
		}
		assertEquals(l_msg, "Player1 does not have an diplomacy card!");
		assertTrue(d_player1.getCards().size() == 0);
		assertTrue(!d_player1.isDiplomatic(d_player2));
		assertTrue(!d_player1.isDiplomatic(d_player3));
		assertTrue(!d_player2.isDiplomatic(d_player1));
		assertTrue(!d_player2.isDiplomatic(d_player3));
	}
}
