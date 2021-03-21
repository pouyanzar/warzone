package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'deploy' order.
 *
 */
public class OrderAdvance implements IOrder {

	IPlayerModel d_player = null;
	ICountryModel d_country_from = null;
	ICountryModel d_country_to = null;
	private int d_num_armies = 0;

	/**
	 * Constructor
	 * 
	 * @param p_player       the player that owns this card
	 * @param p_country_from the country from which to move the armies
	 * @param p_country_to   the country to which to move the armies
	 * @param p_num_armies   the number of armies to advance
	 * @throws Exception failed validation
	 */
	public OrderAdvance(IPlayerModel p_player, ICountryModel p_country_from, ICountryModel p_country_to,
			int p_num_armies) throws Exception {
		d_player = p_player;
		d_country_from = p_country_from;
		d_country_to = p_country_to;
		d_num_armies = p_num_armies;
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
		return advance(d_country_from, d_country_to, d_num_armies);
	}

	/**
	 * is the order valid....
	 * 
	 * @throws Exception fails validation
	 */
	public void isValid() throws Exception {
		if( d_player == null ) {
			throw new Exception("Player cannot be null in 'advance' order.");
		}
		// todo: validate that the other required params are not null.... 
		
		
		// validate that the country is adjacent to one of the current player's
		// territories.
		boolean is_neighbor = false;
		for (ICountryModel l_county : d_country_from.getNeighbors()) {
			if (l_county.getName() == d_country_to.getName()) {
				is_neighbor = true;
				break;
			}
		}
		if( !is_neighbor) {
			throw new Exception("Countries " + d_country_from.getName() + " and " + d_country_to.getName()
			+ " are not neighbors.");
		}

		// todo: does the player have enough armies to move?
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
		String l_str = "advance " + d_country_from.getName() + " to " + d_country_to.getName();
		return l_str;
	}
	
	/**
	 * add the specified armies (reinforcements) to the specified owned country.
	 * 
	 * @param p_country_from     the name of the source country 
	 * @param p_country_to	     the name of the distination country to advance to
	 * @param p_number_of_armies the number of reinforcement armies to move to the
	 *                           specified country
	 * @return a message that describes the deployment done
	 * @throws Exception if destination country is not neighbors, or not enough reinforcements, or
	 *                   unexpected error
	 */
	private String advance(ICountryModel p_country_from, ICountryModel p_country_to, int p_number_of_armies) throws Exception {
		// execute the deployment
		p_country_to.setArmies(p_number_of_armies);
		d_player.addPlayerCountry(p_country_to);
		p_country_from.setArmies(p_country_from.getArmies()-p_number_of_armies);
		// prepare a returning message
		String l_xarmy = "army has";
		if (p_number_of_armies > 1) {
			l_xarmy = "armies have";
		}
		String l_msg = p_number_of_armies + " reinforcement " + l_xarmy + " been advance to " + p_country_to.getName();
		return l_msg;
	}
}
