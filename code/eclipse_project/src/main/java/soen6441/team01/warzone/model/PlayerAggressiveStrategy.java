package soen6441.team01.warzone.model;

import java.util.ArrayList;
import java.util.Random;

import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;

/**
 * Supports the aggressive player strategy. <br>
 * A computer based player that does not require user interaction to make decisions.
 *
 */
public class PlayerAggressiveStrategy implements IPlayerStrategy {
	// the map is available from within the player object
	IPlayerModel d_player = null;

	/**
	 * constructor
	 * 
	 * @param p_player    the cloned player to which to apply this order strategy 
	 * @param p_msg_model the message model
	 */
	public PlayerAggressiveStrategy(IPlayerModel p_player, IAppMsg p_msg_model) {
		d_player = p_player;
	}

	/**
	 * create the order
	 * 
	 * @return the next order
	 */
	public IOrder createOrder() throws Exception {
		IOrder l_order = null;
		Random rand = new Random();
		int l_order_number = rand.nextInt(2);
		int l_max_army = d_player.getPlayerCountries().get(0).getArmies();
		ICountryModel l_country = d_player.getPlayerCountries().get(0);

		// find the country with maximum number of armies
		for (ICountryModel l_country_it : d_player.getPlayerCountries()) {
			if (l_country.getArmies() >= l_max_army) {
				l_max_army = l_country.getArmies();
				l_country = l_country_it;
			}
		}
		ArrayList<ICountryModel> l_country_neighbors = l_country.getNeighbors();
		ICountryModel l_selected_neighbor = l_country_neighbors.get(rand.nextInt(l_country_neighbors.size()));

		// return an order randomly based on the number of order
		switch (l_order_number) {
		case 0:
			l_order = new OrderDeploy(l_country.getName(), d_player.getReinforcements(), d_player);
			return l_order;
		case 1:
			l_order = new OrderAdvance(d_player, l_country, l_selected_neighbor, l_country.getArmies());
			return l_order;
		default:
			return l_order;
		}

	}

	/**
	 * create a deep clone of the current strategy<br>
	 * required because we want the player to analyze based on recent changes to the
	 * map made by recent orders
	 * 
	 * @return a new strategy object cloned from this object
	 */
	public IPlayerStrategy cloneStrategy(IPlayerModel p_player) throws Exception {
		IAppMsg l_msg_model = p_player.getPlayerModelFactory().getUserMessageModel();
		IPlayerStrategy l_player_strategy = new PlayerAggressiveStrategy(p_player, l_msg_model);
		return l_player_strategy;
	}

	/**
	 * @return the name of this strategy
	 */
	@Override
	public String toString() {
		return "Aggressive";
	}
}
