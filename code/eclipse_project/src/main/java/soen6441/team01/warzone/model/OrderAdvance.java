package soen6441.team01.warzone.model;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.*;
import java.util.Random;

/**
 * Supports the definition and implementation of the 'deploy' order.
 *
 */
public class OrderAdvance implements IOrder {
	private IPlayerModel d_player = null;
	private ICountryModel d_country_from = null;
	private ICountryModel d_country_to = null;
	private int d_num_armies = 0;
	private int d_odds_attacker = 60;
	private int d_odds_defender = 70;

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
		String l_result = doAdvance();
		return l_result;
	}

	/**
	 * is the order valid....
	 * 
	 * @throws Exception fails validation
	 */
	public void isValid() throws Exception {
		if (d_player == null) {
			throw new Exception("Must supply a player in 'advance' order.");
		}
		if (d_country_from == null) {
			throw new Exception("Source country cannot be null in 'advance' order.");
		}
		if (d_country_to == null) {
			throw new Exception("Destination country cannot be null in 'advance' order.");
		}

		// validate that the from country belongs to the player
		ICountryModel l_xfrom_country = Country.findCountry(d_country_from.getName(), d_player.getPlayerCountries());
		if (l_xfrom_country == null) {
			throw new Exception("Cannot advance armies since source country " + d_country_from.getName()
					+ " is not owned by " + d_player.getName());
		}

		// validate that the source_country is adjacent to the destination_country
		boolean is_neighbor = false;
		for (ICountryModel l_county : d_country_from.getNeighbors()) {
			if (l_county.getName() == d_country_to.getName()) {
				is_neighbor = true;
				break;
			}
		}
		if (!is_neighbor) {
			throw new Exception("Cannot advance armies since source country " + d_country_from.getName()
					+ " and destination country " + d_country_to.getName() + " are not neighbors.");
		}

		// validate that the player has enough armies for the move/attack move
		if (d_num_armies > d_country_from.getArmies()) {
			throw new Exception(d_player.getName() + " does not have enough armies on country "
					+ d_country_from.getName() + " (" + d_country_from.getArmies() + ") to advance " + d_num_armies
					+ Utl.plural(d_num_armies, " army", " armies"));
		}
	}

	/**
	 * advance the specified number of armies (reinforcements) to the specified
	 * country. <br>
	 * attack if the destination country is owned by an opponent, otherwise simply
	 * move the armies.
	 * 
	 * @return a message that describes the advancement done
	 * @throws Exception unexpected error
	 */
	private String doAdvance() throws Exception {
		String l_msg = "";

		IPlayerModel l_dest_owner = d_country_to.getOwner();
		if (l_dest_owner == null) {
			// no one owns the destination country, so simply move on in
			l_msg = doMoveArmies();
		} else {
			if (l_dest_owner.getName().equals(d_player.getName())) {
				// we own the destination country, so simply move armies
				l_msg = doMoveArmies();
			} else {
				// the target country is owned by another player - attack!
				l_msg = doAttack();
			}
		}

		return l_msg;
	}

	/**
	 * Simply move (ie no attack required) armies to the target country.
	 * 
	 * @return a message that describes the advancement done
	 * @throws Exception unexpected error
	 */
	private String doMoveArmies() throws Exception {
		String l_msg = d_player.getName() + " has successfully moved " + d_num_armies
				+ Utl.plural(d_num_armies, " army", " armies") + " from " + d_country_from.getName() + " to "
				+ d_country_to.getName();

		int l_armies = d_country_from.getArmies();
		d_country_from.setArmies(l_armies - d_num_armies);

		l_armies = d_country_to.getArmies();
		d_country_to.setArmies(l_armies + d_num_armies);

		d_country_to.setOwner(d_player);

		return l_msg;
	}

	/**
	 * attack the destination country that is owned by another player.
	 * 
	 * An attack is simulated by the following battle simulation mechanism: <br>
	 * First, the attacking player decides how many armies are involved. <br>
	 * Then, each attacking army unit involved has 60% chances of killing one
	 * defending army. <br>
	 * At the same time, each defending army unit has 70% chances of killing one
	 * attacking army unit. <br>
	 * If all the defender's armies are eliminated, the attacker captures the
	 * territory.<br>
	 * The attacking army units that survived the battle then occupy the conquered
	 * territory. <br>
	 * A player receives a card at the end of his turn if they successfully
	 * conquered at least one territory during their turn
	 * 
	 * 
	 * @return a message that describes the advancement done
	 * @throws Exception unexpected error
	 */
	private String doAttack() throws Exception {
		String l_msg = "Internal error processing advance attack!";

		int l_army_from = d_num_armies;
		int l_army_target = d_country_to.getArmies();

		while (l_army_from > 0 && l_army_target > 0) {
			if (Utl.randomInt(100) <= d_odds_attacker) {
				l_army_target--;
			}
			if (l_army_target > 0) {
				if (Utl.randomInt(100) <= d_odds_defender) {
					l_army_from--;
				}
			}
		}

		int l_army_from_lost = d_num_armies - l_army_from;
		int l_army_target_lost = d_country_to.getArmies() - l_army_target;

		if (l_army_target > 0) {
			// lost the attack
			d_country_from.removeArmies(d_num_armies);
			d_country_to.removeArmies(l_army_target_lost);
			l_msg = d_player.getName() + " lost " + l_army_from_lost + Utl.plural(l_army_from_lost, " army", " armies")
					+ " from the attack on " + d_country_to.getName();
		}

		if (l_army_from > 0) {
			// won the attack!
			d_country_from.removeArmies(d_num_armies);
			d_country_to.setArmies(l_army_from);
			d_country_to.setOwner(d_player);
			if (l_army_from_lost == 0) {
				l_msg = d_player.getName() + " won the attack on " + d_country_to.getName();
			} else {
				l_msg = d_player.getName() + " won the attack on " + d_country_to.getName() + " but lost "
						+ l_army_from_lost + Utl.plural(l_army_from_lost, " army", " armies");
			}
		}

		return l_msg;
	}

	/**
	 * Change the current player to the specified player
	 * 
	 * @param p_player the new player to assign this order to
	 * @throws Exception unexpected error
	 */
	public void cloneToPlayer(IPlayerModel p_player) throws Exception {
		ModelFactory l_model_factory = d_player.getPlayerModelFactory();
		// find and set the countries from the new players map
		d_country_from = Country.findCountry(d_country_from.getName(), l_model_factory.getMapModel().getCountries());
		if (d_country_from == null) {
			throw new Exception("Internal error, cannot find country_from in OrderAdvance");
		}
		d_country_to = Country.findCountry(d_country_to.getName(), l_model_factory.getMapModel().getCountries());
		if (d_country_to == null) {
			throw new Exception("Internal error, cannot find country_to in OrderAdvance");
		}
		d_player = p_player;
	}

	/**
	 * set the odds of a battle. used mainly for testing.
	 * 
	 * @param p_odds_attacker new odds to assign to the attacker
	 * @param p_odds_defender new odds to assign to the defender
	 */
	public void setOdds(int p_odds_attacker, int p_odds_defender) {
		d_odds_attacker = p_odds_attacker;
		d_odds_defender = p_odds_defender;
	}

	/**
	 * override the default toString() to describe what this card/order is
	 * 
	 * @return string describing what this card/order is
	 */
	@Override
	public String toString() {
		String l_str = "advance " + d_num_armies + Utl.plural(d_num_armies, "army", "armies") + " from country "
				+ d_country_from.getName() + " to country " + d_country_to.getName();
		return l_str;
	}
}
