package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'bomb' order.
 *
 */
public class OrderBomb implements IOrder {

	IPlayerModel d_player = null;
	ICountryModel d_country_to_bomb = null;

	/**
	 * Constructor
	 * 
	 * @param p_player          the player that owns this card
	 * @param p_country_to_bomb the country to bomb
	 * @throws Exception if there is an Exception
	 */
	public OrderBomb(IPlayerModel p_player, ICountryModel p_country_to_bomb) throws Exception {
		d_player = p_player;
		d_country_to_bomb = p_country_to_bomb;
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
		String l_result = doBombing();
		d_player.removeCard(CardType.bomb);
		return l_result;
	}

	/**
	 * Checks if the condition for using bomb card does meet
	 * 
	 * @throws Exception when any of the conditions to use bomb card does not meet
	 */
	private void isValid() throws Exception {
		// validate that the player has the bomb card
		boolean l_valid = d_player.hasCard(CardType.bomb);
		if (!l_valid) {
			throw new Exception(d_player.getName() + " does not have a bomb card!");
		}

		// cannot bomb your own country
		IPlayerModel l_owner = d_country_to_bomb.getOwner();
		if (l_owner != null) {
			if (l_owner.getName().equals(d_player.getName())) {
				throw new Exception("Cannot bomb your own country " + d_country_to_bomb.getName());
			}
		}

		// validate that the country is adjacent to one of the current player’s
		// territories.
		l_valid = false;
		for (ICountryModel l_pcountry : d_player.getPlayerCountries()) {
			for (ICountryModel l_ncountry : l_pcountry.getNeighbors()) {
				if (l_ncountry.getName().equals(d_country_to_bomb.getName())) {
					l_valid = true;
					break;
				}
			}
		}
		if (!l_valid) {
			throw new Exception(d_country_to_bomb.getName()
					+ " is not an adjacent country (ie neighbor) to any of your countries.");
		}
	}

	/**
	 * Change the current player to the specified player
	 * 
	 * @param p_player the new player to assign this order to
	 * @throws Exception unexpected error
	 */
	public void cloneToPlayer(IPlayerModel p_player) throws Exception {
		ModelFactory l_model_factory = p_player.getPlayerModelFactory();
		// find and set the country to bomb from the new players map
		d_country_to_bomb = Country.findCountry(d_country_to_bomb.getName(),
				l_model_factory.getMapModel().getCountries());
		if (d_country_to_bomb == null) {
			throw new Exception("Internal error, cannot find country to bomb in OrderBomb");
		}
		d_player = p_player;
	}

	/**
	 * override the default toString() to describe what this card/order is
	 * 
	 * @return string describing what this card/order is
	 */
	@Override
	public String toString() {
		String l_str = "bomb " + d_country_to_bomb.getName();
		return l_str;
	}

	/**
	 * destroys half of the armies on the opponent
	 * 
	 * @return a message to show half of the armies destroyed
	 */
	public String doBombing() {
		int l_armies = d_country_to_bomb.getArmies();
		int l_new_armies = l_armies / 2;
		if (l_new_armies < 0) {
			l_new_armies = 0;
		}
		d_country_to_bomb.setArmies(l_new_armies);
		String l_msg = d_country_to_bomb.getName() + " has been bombed. Armies reduced from " + l_armies + " to "
				+ l_new_armies + " army/armies";
		return l_msg;
	}
}
