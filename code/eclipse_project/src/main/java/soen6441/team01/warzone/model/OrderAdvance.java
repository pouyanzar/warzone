package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'deploy' order.
 *
 */
public class OrderAdvance implements IOrder {

	IPlayerModel d_player = null;
	ICountryModel d_country = null;

	/**
	 * Constructor
	 * 
	 * @param p_player  the player that owns this card
	 * @param p_country_from the country from which to move the armies
	 * @param p_country_to the country to which to move the armies
	 * @param p_num_armies the number of armies to advance
	 */
	public OrderAdvance(IPlayerModel p_player, ICountryModel p_country_from, ICountryModel p_country_to,
			int p_num_armies) {
		d_player = p_player;
		//d_country = p_country;
		isValid();
	}

	/**
	 * Execute the bomb order.<br>
	 * Note: a player cannot bomb their own country.
	 * 
	 * @return informational message about what was done, <br>
	 *         e.g. bombing of country successful
	 * @throws Exception if not successfully executed
	 */
	public String execute() throws Exception {
		isValid();
		throw new Exception("Advance order execute method not yet implemented");
	}

	/**
	 * is the order valid....
	 */
	private void isValid() {
		// validate that the country is adjacent to one of the current player’s
		// territories.
		// ...
	}

	/**
	 * Change the current player to the specified player
	 * 
	 * @param p_player the new player to assign this order to
	 */
	public void setPlayer(IPlayerModel p_player) {
		d_player = p_player;
	}

	/**
	 * override the default toString() to describe what this card/order is
	 * 
	 * @return string describing what this card/order is
	 */
	@Override
	public String toString() {
		String l_str = "advance " + d_country.getName();
		return l_str;
	}
}
