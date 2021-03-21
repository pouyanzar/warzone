package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'deploy' order.
 *
 */
public class OrderDeploy implements IOrder {

	private String d_country_name;
	private int d_reinforcements;
	private IPlayerModel d_player;

	/**
	 * Constructor
	 * 
	 * @param p_country_name the country that will receive the reinforcements
	 * @param p_armies       the number of reinforcement armies to add to the
	 *                       specified country
	 * @param p_player       the player model needed to process this order
	 * @throws Exception fails validation
	 */
	public OrderDeploy(String p_country_name, int p_armies, IPlayerModel p_player) throws Exception {
		d_country_name = p_country_name;
		d_reinforcements = p_armies;
		d_player = p_player;
		isValid();
	}

	/**
	 * Execute the deploy order
	 * 
	 * @return informational message about what was done, ie if successfully
	 *         executed (ie no exceptions)
	 * @throws Exception unexpected error
	 */
	public String execute() throws Exception {
		isValid();
		return deploy(d_country_name, d_reinforcements);
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
	 * Checks if the condition for using bomb card does meet
	 * 
	 * @throws Exception when any of the conditions to use bomb card does not meet
	 */
	private void isValid() throws Exception {
		if (Utl.isEmpty(d_country_name)) {
			throw new Exception("No country specified for 'deploy order'");
		}

		if (d_player == null) {
			throw new Exception("No player specified for 'deploy order'");
		}

		if (d_reinforcements < 1) {
			throw new Exception("Invalid number of reinforcements specified for 'deploy order'");
		}

		ICountryModel l_country = Country.findCountry(d_country_name, d_player.getPlayerCountries());
		if (l_country == null) {
			throw new Exception("Country " + d_country_name + " is not owned by player " + d_player.getName());
		}

		int l_player_armies = d_player.getReinforcements();
		if (d_reinforcements > l_player_armies) {
			throw new Exception(d_player.getName() + " does not have enough reinforcements (" + l_player_armies
					+ ") to deploy " + d_reinforcements + " armies to " + d_country_name);
		}
	}

	/**
	 * override the default toString() to describe what this order is
	 * 
	 * @return string describing what this order is
	 */
	@Override
	public String toString() {
		String l_str = "deploy " + d_reinforcements + " to country " + d_country_name;
		return l_str;
	}

	/**
	 * add the specified armies (reinforcements) to the specified owned country.
	 * 
	 * @param p_country_name     the name of the country to deploy to
	 * @param p_number_of_armies the number of reinforcement armies to move to the
	 *                           specified country
	 * @return a message that describes the deployment done
	 * @throws Exception if country is not owned, or not enough reinforcements, or
	 *                   unexpected error
	 */
	private String deploy(String p_country_name, int p_number_of_armies) throws Exception {
		int l_player_armies = d_player.getReinforcements();

		// execute the deployment
		ICountryModel l_country = Country.findCountry(d_country_name, d_player.getPlayerCountries());
		int l_armies = l_country.getArmies();
		l_armies += p_number_of_armies;
		l_country.setArmies(l_armies);
		l_player_armies -= p_number_of_armies;
		d_player.setReinforcements(l_player_armies);

		// prepare a returning message
		String l_xarmy = "army has";
		if (p_number_of_armies > 1) {
			l_xarmy = "armies have";
		}

		String l_msg = p_number_of_armies + " reinforcement " + l_xarmy + " been deployed to " + p_country_name;
		return l_msg;
	}
}
