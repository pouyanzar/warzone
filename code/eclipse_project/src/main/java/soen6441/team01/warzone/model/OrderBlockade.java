package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'blockade' order.
 * @author pouyan
 *
 */
public class OrderBlockade {

	IPlayerModel d_player = null;
	ICountryModel d_country = null;

	/**
	 * Constructor
	 * 
	 * @param p_player  the player that owns this card
	 * @param p_country the country to bomb
	 * @throws Exception if there is an Exception
	 */
	public OrderBlockade(IPlayerModel p_player, ICountryModel p_country) throws Exception {
		d_player = p_player;
		d_country = p_country;
		isValid();
	}

	/**
	 * Execute the blockade order.<br>
	 * 
	 * @return informational message about what was done, <br>
	 *         e.g. blockading of country successful
	 * @throws Exception if not successfully executed
	 */
	public String execute() throws Exception {
		isValid();
		return blockade();
	}

	/**
	 * Checks if the condition for using bomb card does meet
	 * 
	 * @throws Exception when any of the conditions to use bomb card does not meet
	 */
	private void isValid() throws Exception {
		// validate that the player has the bomb card
		boolean l_valid;
		Card l_blockade = new Card(CardType.blockade);
		l_valid = d_player.hasCard(l_blockade);
		if (!l_valid) {
			throw new Exception(d_player + "does not have blockade card!");
		}
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
		String l_str = "blockade " + d_country.getName();
		return l_str;
	}
	
	/**
	 * blockade the target country of player's countries
	 * 
	 * @return a message to show blockade is done.
	 */
	public String blockade() {
		d_country.setArmies(d_country.getArmies() * 3);
		d_player.getPlayerCountries().remove(d_country);
		String l_msg = "Blockade is done";
		return l_msg;
	}
}
