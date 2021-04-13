package soen6441.team01.warzone.model;

import java.io.Serializable;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;

/**
 * Supports the benevolent player strategy. <br>
 * A computer based player that does not require user interaction to make decisions.
 *
 */
public class PlayerBenevolentStrategy implements IPlayerStrategy, Serializable {
	private static final long serialVersionUID = 1L;
	// the map is available from within the player object
	private IPlayerModel d_player;
	private IAppMsg d_msg_model;

	/**
	 * constructor
	 * 
	 * @param p_player    the cloned player to which to apply this order strategy 
	 * @param p_msg_model the message model
	 */
	public PlayerBenevolentStrategy(IPlayerModel p_player, IAppMsg p_msg_model) {
		d_player = p_player;
		d_msg_model = p_msg_model;
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
		String l_msg_header = "Gameplay - computer player " + d_player.getName() + " [benevolent] issuing order> ";  

		try {
			l_order = doDeploy();
			if (l_order != null) {
				l_order.execute(); // see why - method comment above
			}
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Warning, "exception encountered while creating [benevolent] order: " + ex.getMessage());
		}

		if (l_order != null) {
			d_msg_model.setMessage(MsgType.Informational, l_msg_header + l_order.toString());
		} else {
			d_msg_model.setMessage(MsgType.Informational, l_msg_header + "end turn");
		}

		return l_order;		
	}

	/**
	 * do a Deploy if possible
	 * 
	 * @return the order or null if a deploy is not allowed
	 * @throws Exception an unexpected error
	 */
	private IOrder doDeploy() throws Exception {
		IOrder l_order = null;

		if (d_player.getReinforcements() < 1) {
			return l_order;
		}

		int l_min_army = Integer.MAX_VALUE;
		ICountryModel l_country = null;

		for (ICountryModel l_country_it : d_player.getPlayerCountries()) {
			if (l_country_it.getArmies() <= l_min_army) {
				l_min_army = l_country_it.getArmies();
				l_country = l_country_it;
			}
		}

		if (l_country != null) {
			l_order = new OrderDeploy(l_country.getName(), d_player.getReinforcements(), d_player);
		}

		return l_order;
	}

	/**
	 * create a deep clone of the current strategy<br>
	 * required because we want the player to analyze based on recent changes to the
	 * map made by recent orders
	 * 
	 * @return a new strategy object cloned from this object
	 */
	public IPlayerStrategy cloneStrategy(IPlayerModel p_player) throws Exception {
		IPlayerStrategy l_player_strategy = new PlayerBenevolentStrategy(p_player, p_player.getPlayerModelFactory().getUserMessageModel());
		return l_player_strategy;
	}

	/**
	 * @return the name of this strategy
	 */
	@Override
	public String toString() {
		return "benevolent";
	}
}
