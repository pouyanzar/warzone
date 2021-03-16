package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'deploy' order.
 *
 */
public class OrderBomb implements IOrder {

	IPlayerModel d_player = null;
	ICountryModel d_country = null;

	/**
	 * Constructor
	 * 
	 * @param p_player the player that owns this card
	 * @param p_country the country to bomb
	 */
	public OrderBomb(IPlayerModel p_player, ICountryModel p_country) {
		d_player = p_player;
		d_country = p_country;
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
		throw new Exception("Bomb card execute method not yet implemented");
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
		String l_str = "bomb " + d_country.getName();
		return l_str;
	}
}
