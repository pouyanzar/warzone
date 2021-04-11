package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.entities.CardType;

import java.io.Serializable;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IOrder;

/**
 * Supports the definition and implementation of the 'airlift' order.
 *
 */
public class OrderAirlift implements IOrder, Serializable {
	private static final long serialVersionUID = 1L;
	private String d_source_country = "";
	private String d_target_country = "";
	private int d_reinforcements = 0;
	private IPlayerModel d_player = null;

	/**
	 * Constructor
	 * 
	 * @param p_source_country the country to move the reinforcements from
	 * @param p_target_country the country that will receive the reinforcements
	 * @param p_armies         the number of reinforcement armies to add to the
	 *                         specified country
	 * @param p_player         the player model needed to process this order
	 * @throws Exception order is not valid
	 */
	public OrderAirlift(String p_source_country, String p_target_country, int p_armies, IPlayerModel p_player)
			throws Exception {
		d_source_country = p_source_country;
		d_target_country = p_target_country;
		d_reinforcements = p_armies;
		d_player = p_player;
		isValid();
	}

	/**
	 * Execute the airlift order
	 * 
	 * @return informational message about what was done, ie if successfully
	 *         executed (ie no exceptions)
	 * @throws Exception unexpected error
	 */
	public String execute() throws Exception {
		isValid();
		String l_result = doAirlifting();
		d_player.removeCard(CardType.airlift);
		return l_result;
	}

	/**
	 * Checks if the condition for using airlift card does meet
	 * 
	 * @throws Exception when any of the conditions to use airlift card does not meet
	 */
	private void isValid() throws Exception {
		ICountryModel l_country;

		// check that the player was specified
		if (d_player == null) {
			throw new Exception("Internal error, player cannot be null in airlift order");
		}

		// validate that the player has the airlift card
		if (!d_player.hasCard(CardType.airlift)) {
			throw new Exception(d_player.getName() + " does not have an airlift card!");
		}

		// check that the source country is owned by the player
		l_country = Country.findCountry(d_source_country, d_player.getPlayerCountries());
		if (l_country == null) {
			throw new Exception(
					"airlift source country '" + d_source_country + "' does not belong to " + d_player.getName());
		}
		// check that there are enough armies to airlift
		if (d_reinforcements > l_country.getArmies()) {
			throw new Exception("not enough armies on " + d_source_country + " (" + l_country.getArmies()
					+ ") to airlift " + d_reinforcements + Utl.plural(d_reinforcements, " army", " armies") + " to "
					+ d_target_country);
		}

		// check that the target country is owned by the player
		l_country = Country.findCountry(d_target_country, d_player.getPlayerCountries());
		if (l_country == null) {
			throw new Exception(
					"airlift target country '" + d_target_country + "' does not belong to " + d_player.getName());
		}
	}

	/**
	 * destroys half of the armies on the opponent
	 * 
	 * @return a message to show half of the armies destroyed
	 */
	public String doAirlifting() {
		ICountryModel l_country;
		String l_msg = "";

		l_country = Country.findCountry(d_source_country, d_player.getPlayerCountries());
		l_country.removeArmies(d_reinforcements);

		l_country = Country.findCountry(d_target_country, d_player.getPlayerCountries());
		l_country.addArmies(d_reinforcements);

		l_msg = d_reinforcements + Utl.plural(d_reinforcements, " army", " armies")
				+ Utl.plural(d_reinforcements, " has", " have") + " been airlifted from " + d_source_country + " to "
				+ d_target_country;

		return l_msg;
	}

	/**
	 * Change the current player to the specified player
	 * 
	 * @param p_player the new player to assign this order to
	 */
	public void cloneToPlayer(IPlayerModel p_player) {
		d_player = p_player;
	}

	/**
	 * override the default toString() to describe what this order is
	 * 
	 * @return string describing what this order is
	 */
	@Override
	public String toString() {
		String l_str = "advance " + d_reinforcements + " from " + d_source_country + " to " + d_target_country;
		return l_str;
	}
}
