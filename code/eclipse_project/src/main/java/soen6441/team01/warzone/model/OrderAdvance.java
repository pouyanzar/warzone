package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;
import java.util.Random;

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
		if (d_player == null) {
			throw new Exception("Player cannot be null in 'advance' order.");
		}
		// todo: validate that the other required params are not null....
		if (d_country_from == null) {
			throw new Exception("Source country cannot be null in 'advance' order.");
		}
		if (d_country_to == null) {
			throw new Exception("Distination country cannot be null in 'advance' order.");
		}

		// validate that the country is adjacent to one of the current player's
		// territories.
		boolean is_neighbor = false;
		for (ICountryModel l_county : d_country_from.getNeighbors()) {
			if (l_county.getName() == d_country_to.getName()) {
				is_neighbor = true;
				break;
			}
		}
		if (!is_neighbor) {
			throw new Exception(
					"Countries " + d_country_from.getName() + " and " + d_country_to.getName() + " are not neighbors.");
		}

		// todo: does the player have enough armies to move?
		if (d_num_armies < d_country_to.getArmies()) {
			throw new Exception("Player has no enough army to advance.");
		}
		if (d_country_from.getArmies() < d_num_armies) {
			throw new Exception("Number of armies are greater than source country's armies.");
		}

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
	 * @param p_country_to       the name of the distination country to advance to
	 * @param p_number_of_armies the number of reinforcement armies to move to the
	 *                           specified country
	 * @return a message that describes the deployment done
	 * @throws Exception if destination country is not neighbors, or not enough
	 *                   reinforcements, or unexpected error
	 */
	private String advance(ICountryModel p_country_from, ICountryModel p_country_to, int p_number_of_armies)
			throws Exception {
		// execute the deployment
		// number of armies that will be killed by player from destination country
		// int l_dist_dies=p_country_to.getArmies()*60/100;
		// number of armies that will be killed by distination country from player's
		// country
		// int l_source_dies=p_number_of_armies*70/100;
		Random l_rand = new Random();
		String l_msg = "";
		int l_num_of_killed_dist = 0;
		int l_num_of_killed_source = 0;
		//System.out.println("\n"+p_country_from.getName()+"  " +p_country_from.getArmies());
		//System.out.println(p_country_to.getName()+"  " +p_country_to.getArmies());
		//System.out.println("Attack by   " +p_number_of_armies);
		
		
		for (int i = 0; i < p_number_of_armies; i++) {
			int l_src_killed_percent = l_rand.nextInt(70);
			int l_dst_killed_percent = l_rand.nextInt(60);
			if (l_src_killed_percent < l_dst_killed_percent) {
				l_num_of_killed_dist++;
			} else
				l_num_of_killed_source++;
			if (l_num_of_killed_dist > p_country_to.getArmies()) {
				break;
			}
		}
		
		//System.out.println("Killed in "+p_country_from.getName() +" " +l_num_of_killed_source+ " of " +p_country_from.getArmies());
		//System.out.println("Killed in  "+ p_country_to.getName()+" "+l_num_of_killed_dist+"  of " +p_country_to.getArmies());
		
		if ((l_num_of_killed_dist > p_country_to.getArmies())) {
			d_player.addPlayerCountry(p_country_to);
			p_country_from.setArmies(p_country_from.getArmies() - p_number_of_armies);
			// prepare a returning message
			String l_xarmy = "army has";
			if (p_number_of_armies > 1) {
				l_xarmy = "armies have";
			}
			l_msg = p_number_of_armies + " reinforcement " + l_xarmy + " been advance to " + p_country_to.getName();
			d_player.addCard(new Card());
		} else
			l_msg = "\nThe player failed to advance from " + p_country_from.getName() + " to " + p_country_to.getName()
					+ " because number of killed peaple in " + p_country_to.getName() + " was " + l_num_of_killed_dist
					+ " and number of killed peaple in " + p_country_from.getName() + " was " + l_num_of_killed_source;

		return l_msg;
	}
}
