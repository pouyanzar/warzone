package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'bomb' order.
 *
 */
public class OrderBomb implements IOrder {

	IPlayerModel d_player = null;
	ICountryModel d_country = null;

	/**
	 * Constructor
	 * 
	 * @param p_player  the player that owns this card
	 * @param p_country the country to bomb
	 * @throws Exception if there is an Exception
	 */
	public OrderBomb(IPlayerModel p_player, ICountryModel p_country) throws Exception {
		d_player = p_player;
		d_country = p_country;
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
		return bomb();
	}

	/**
	 * Checks if the condition for using bomb card does meet
	 * 
	 * @throws Exception when any of the conditions to use bomb card does not meet
	 */
	private void isValid() throws Exception {
		// validate that the player has the bomb card
		boolean l_valid;
		Card l_bomb = new Card(CardType.bomb);
		l_valid = d_player.hasCard(l_bomb);
		if (!l_valid) {
			throw new Exception(d_player + "does not have bomb card!");
		}
		// validate that the country is adjacent to one of the current player’s
		// territories.
		ArrayList<ICountryModel> l_player_countries = new ArrayList<>();
		l_player_countries = d_player.getPlayerCountries();
		for (ICountryModel l_country : l_player_countries) {
			if (l_country.getNeighbors().contains(d_country)) {
				l_valid = true;
				break;
			}
		}
		if (!l_valid) {
			throw new Exception(d_country.getName() + "is a neighbor of none of your territories");
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
		String l_str = "bomb " + d_country.getName();
		return l_str;
	}
	
	/**
	 * destroys half of the armies on the opponent
	 * 
	 * @return a message to show half of the armies destroyed
	 */
	public String bomb() {
		d_country.setArmies(d_country.getArmies() / 2);
		String l_msg = "Half number of armies destroyed by bomb";
		return l_msg;
	}
}
