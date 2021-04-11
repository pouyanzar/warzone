package soen6441.team01.warzone.model;

import java.io.Serializable;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.entities.*;

/**
 * class Card to create cards for players to issue special orders during the
 * game
 * 
 */
public class Card implements Serializable {
	private static final long serialVersionUID = 1L;
	private CardType d_card_type;
	private String d_card_name;

	/**
	 * constructor - generates a random card
	 */
	public Card() {
		d_card_type = Utl.randomEnum(CardType.class);
		d_card_name = d_card_type.name();
	}

	/**
	 * constructor - initialized to a specific card
	 * 
	 * @param p_card_type the type of card this is
	 */
	public Card(CardType p_card_type) {
		d_card_type = p_card_type;
		d_card_name = d_card_type.name();
	}

	/**
	 * 
	 * @return the type of the current card
	 */
	public CardType getCardType() {
		return d_card_type;
	}

	/**
	 * Getter method for card name
	 * 
	 * @return d_card_name the name of current card
	 */
	public String getCardName() {
		return d_card_name;
	}
}
