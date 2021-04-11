package soen6441.team01.warzone.model;

import java.io.Serializable;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'blockade' order.
 *
 */
public class OrderBlockade implements IOrder, Serializable {
	private static final long serialVersionUID = 1L;
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
		String l_result = doBlockade();
		d_player.removeCard(CardType.blockade);
		return l_result;

	}

	/**
	 * Checks if the condition for using bomb card does meet
	 * 
	 * @throws Exception when any of the conditions to use bomb card does not meet
	 */
	private void isValid() throws Exception {
		if (d_player == null) {
			throw new Exception("Must supply a player in 'blockade' order.");
		}
		if (d_country == null) {
			throw new Exception("Country cannot be null in 'blockade' order.");
		}

		// validate that the player has the blockade card
		boolean l_valid = d_player.hasCard(CardType.blockade);
		if (!l_valid) {
			throw new Exception(d_player.getName() + " does not have a blockade card!");
		}

		// can only blockade your own country
		String l_msg = "Cannot blockade " + d_country.getName() + " since player " + d_player.getName()
				+ " does not own it";
		IPlayerModel l_owner = d_country.getOwner();
		if (l_owner == null) {
			throw new Exception(l_msg);
		}
		if (!l_owner.getName().equals(d_player.getName())) {
			throw new Exception(l_msg);
		}
	}

	/**
	 * Change the current player to the specified player
	 * 
	 * @param p_other_player the new player to assign this order to
	 * @throws Exception unexpected error
	 */
	public void cloneToPlayer(IPlayerModel p_other_player) throws Exception {
		ModelFactory l_model_factory = p_other_player.getPlayerModelFactory();
		// find and set the country to blockade from the new players map
		ICountryModel l_country = Country.findCountry(d_country.getName(),
				l_model_factory.getMapModel().getCountries());
		if (l_country == null) {
			throw new Exception("Internal error, cannot find country to blockade in OrderBlockade");
		}
		d_country = l_country;
		d_player = p_other_player;
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
	 * @return message describing what was done
	 * @throws Exception unexpected error
	 */
	public String doBlockade() throws Exception {
		int l_armies = d_country.getArmies();
		int l_new_armies = l_armies * 3;
		if (l_new_armies < 0) {
			l_new_armies = 0;
		}
		d_country.setArmies(l_new_armies);
		d_player.removePlayerCountry(d_country);
		String l_msg = d_country.getName() + " has been blockaded and now has " + l_new_armies
				+ Utl.plural(l_new_armies, " armry", " armies") + " and has become neutral";
		return l_msg;
	}
}
