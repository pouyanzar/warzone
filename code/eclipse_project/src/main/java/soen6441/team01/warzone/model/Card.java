package soen6441.team01.warzone.model;

/**
 * class Card to create cards for players to issue special orders during the
 * game
 * 
 * @author pouyan
 *
 */
public class Card {

	private String d_card_name;

	/**
	 * Issues a card randomly
	 */
	public Card() {
		String[] l_card_list = { "bomb", "reinforcement", "blockade", "airflit", "diplomacy" };
		int l_random_card = (int) Math.floor((Math.random() * l_card_list.length));
		setCardName(l_card_list[l_random_card]);
	}
	
	/**
	 * Getter method for card name
	 * 
	 * @return d_card_name the name of current card
	 */
	public String getCardName() {
		return d_card_name;
	}

	/**
	 * Setter method for card
	 * 
	 * @param l_cardName the name of current card
	 */
	public void setCardName(String l_cardName) {
		d_card_name = l_cardName;
	}
}