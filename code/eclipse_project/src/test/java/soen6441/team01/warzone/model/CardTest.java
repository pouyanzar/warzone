package soen6441.team01.warzone.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Tests for Player model class
 * 
 */
public class CardTest {

	/**
	 * Test card creation
	 * 
	 * @throws Exception unexpected exception
	 */
	@Test
	public void test_card_creation() throws Exception {
		Card l_card;
		ArrayList<String> l_card_list = new ArrayList<String>();
		l_card_list.add("airlift");
		l_card_list.add("blockade");
		l_card_list.add("bomb");
		l_card_list.add("diplomacy");

		l_card = new Card(CardType.airlift);
		assertTrue(l_card.getCardName().equals("airlift"));

		l_card = new Card(CardType.blockade);
		assertTrue(l_card.getCardName().equals("blockade"));

		l_card = new Card(CardType.bomb);
		assertTrue(l_card.getCardName().equals("bomb"));

		l_card = new Card(CardType.diplomacy);
		assertTrue(l_card.getCardName().equals("diplomacy"));

		// check the random generator by going through some samples and making sure that
		// the random function is not generating an invalid card type
		for (int idx = 0; idx < 10; idx++) {
			l_card = new Card();
			assertTrue(l_card_list.contains(l_card.getCardName()));
		}

	}
}
