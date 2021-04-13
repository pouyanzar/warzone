package soen6441.team01.warzone.model;

import java.io.Serializable;
import java.util.ArrayList;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the aggressive player strategy. <br>
 * A computer based player that does not require user interaction to make
 * decisions.
 *
 */
public class PlayerAggressiveStrategy implements IPlayerStrategy, Serializable {
	private static final long serialVersionUID = 1L;
	private IPlayerModel d_player = null; // the map is available from within the player object
	private boolean d_advance_sw = false;

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
	 * create the order. <br>
	 * note that the order needs to be executed otherwise any changes to the
	 * players' state is not affected. e.g. if we don't do this then if a deploy
	 * order is given the number of reinforcements will not be subtracted from the
	 * player. However at this stage the player is a clone with a completely cloned
	 * map, so it's ok to execute the order as it won't affect the master (ie
	 * common) map, until the order is executed on the real player and the real map,
	 * which is done by the system automatically later on.
	 * 
	 * @return the next order, null = end turn
	 * @throws Exception an unexpected error
	 */
	public IOrder createOrder() throws Exception {
		IOrder l_order = null;
		IAppMsg l_msg = d_player.getPlayerModelFactory().getUserMessageModel();
		String l_msg_header = "Gameplay - computer player " + d_player.getName() + " [aggressive] issuing order> ";

		try {
			l_order = doDeploy();
			if (l_order == null) {
				l_order = doCards();
			}
			if (l_order == null) {
				l_order = doAdvance();
			}
			if (l_order != null) {
				l_order.execute(); // see why - method comment above
			}
		} catch (Exception ex) {
			l_msg.setMessage(MsgType.Warning,
					"exception encountered while creating [aggressive] order: " + ex.getMessage());
		}

		if (l_order != null) {
			l_msg.setMessage(MsgType.Informational, l_msg_header + l_order.toString());
		} else {
			l_msg.setMessage(MsgType.Informational, l_msg_header + "end turn");
		}

		return l_order;
	}

	/**
	 * if player has enough reinforcements deploy on strongest country
	 * 
	 * @return the order; otherwise null
	 * @throws Exception unexpected error
	 */
	private IOrder doDeploy() throws Exception {
		int l_rein = d_player.getReinforcements();
		if (l_rein < 1) {
			return null;
		}
		ICountryModel l_country = findStongestCountry();
		IOrder l_order = new OrderDeploy(l_country.getName(), d_player.getReinforcements(), d_player);
		return l_order;
	}

	/**
	 * play a card if one is available.<br>
	 * note that being aggressive the player will only play the bomb card if it's
	 * available.
	 * 
	 * @return the bomb order or null if cannot
	 * @throws Exception unexpected error
	 */
	private IOrder doCards() throws Exception {
		IOrder l_order = null;
		if (d_advance_sw) {
			return null;
		}
		if (d_player.hasCard(CardType.bomb)) {
			ICountryModel l_country_dest = findStrongestOpponentAdjacentCountry();
			if (l_country_dest != null) {
				l_order = new OrderBomb(d_player, l_country_dest);
			}
		}
		return l_order;
	}

	/**
	 * try to advance armies from the strongest country to another player's
	 * neighboring country with the least army for an attack. <br>
	 * if there are no neighboring countries owned by another player advance to a
	 * random neighboring country.
	 * 
	 * @return the advance order or null if cannot advance anywhere
	 * @throws Exception unexpected error
	 */
	private IOrder doAdvance() throws Exception {
		if (d_advance_sw) {
			// advance order issued already - end turn
			return null;
		}
		d_advance_sw = true;
		ICountryModel l_country_src = findStongestCountry();
		if (l_country_src.getArmies() < 1) {
			return null;
		}
		ICountryModel l_country_dest = findWeakestOpponentNeighbor(l_country_src);
		if (l_country_dest == null) {
			l_country_dest = getRandomNeighbor(l_country_src);
		}
		IOrder l_order = new OrderAdvance(d_player, l_country_src, l_country_dest, l_country_src.getArmies());
		return l_order;
	}

	/**
	 * @param p_my_country my reference country
	 * @return random neighbor of p_my_country
	 */
	private ICountryModel getRandomNeighbor(ICountryModel p_my_country) {
		ArrayList<ICountryModel> l_neighbors = p_my_country.getNeighbors();
		int l_idx = Utl.randomInt(l_neighbors.size() - 1);
		return l_neighbors.get(l_idx);
	}

	/**
	 * @return an opponent player in an adjacent country with the most armies
	 */
	private ICountryModel findStrongestOpponentAdjacentCountry() {
		ICountryModel l_country_opponent = null;

		for (ICountryModel l_country : d_player.getPlayerCountries()) {
			for (ICountryModel l_neighbor : l_country.getNeighbors()) {
				IPlayerModel l_player = l_neighbor.getOwner();
				if (l_player != null) {
					if (!l_player.getName().equals(d_player.getName())) {
						if (l_country_opponent == null) {
							l_country_opponent = l_neighbor;
						} else {
							if (l_neighbor.getArmies() > l_country_opponent.getArmies()) {
								l_country_opponent = l_neighbor;
							}
						}
					}
				}
			}
		}

		if (l_country_opponent != null) {
			if (l_country_opponent.getArmies() < 1) {
				// don't waste a bomb card on a country with no armies
				l_country_opponent = null;
			}
		}
		
		return l_country_opponent;
	}

	/**
	 * locate the opponent country with the smallest army
	 * 
	 * @param p_my_country my reference country
	 * @return the opponent country with the smallest army or null there isn't one
	 */
	private ICountryModel findWeakestOpponentNeighbor(ICountryModel p_my_country) {
		ICountryModel l_country_opponent = null;

		for (ICountryModel l_country : p_my_country.getNeighbors()) {
			IPlayerModel l_player = l_country.getOwner();
			if (l_player != null) {
				if (!l_player.getName().equals(d_player.getName())) {
					if (l_country_opponent == null) {
						l_country_opponent = l_country;
					} else {
						if (l_country.getArmies() < l_country_opponent.getArmies()) {
							l_country_opponent = l_country;
						}
					}
				}
			}
		}
		return l_country_opponent;
	}

	/**
	 * locate the country with the most armies
	 * 
	 * @return the country with the most armies; otherwise null
	 */
	private ICountryModel findStongestCountry() {
		ICountryModel l_country = d_player.getPlayerCountries().get(0);
		if (l_country == null) {
			return null;
		}
		for (ICountryModel l_country_it : d_player.getPlayerCountries()) {
			if (l_country_it.getArmies() > l_country.getArmies()) {
				l_country = l_country_it;
			}
		}
		return l_country;
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
		return "aggressive";
	}
}
