package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGameplayOrderDatasource;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;

/**
 * Supports the human player strategy. <br>
 * A human player that requires user interaction to make decisions.
 *
 */
public class PlayerRandomStrategy implements IPlayerStrategy {

	// the map is available from within the player object
	private IPlayerModel d_player;
	private IAppMsg d_msg_model;
	private int d_random_attack = 1;
	private int d_random_move = 1;

	/**
	 * constructor
	 * 
	 * @param p_player    the player requiring the human player strategy
	 * @param p_msg_model the message model used to send messages to the view
	 */
	public PlayerRandomStrategy(IPlayerModel p_player, IAppMsg p_msg_model) {
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
		String l_msg_header = "Gameplay - computer player " + d_player.getName() + " [random] issuing order> ";

		IOrder l_order = doRandomDeploy();

		if (d_random_attack > 0 && l_order == null) {
			l_order = doRandomAttack();
			d_random_attack--;
		}
		if (d_random_move > 0 && l_order == null) {
			l_order = doRandomMove();
			d_random_move--;
		}

		if (l_order != null) {
			d_msg_model.setMessage(MsgType.Informational, l_msg_header + l_order.toString());
			l_order.execute(); // see method comment above
		} else {
			d_msg_model.setMessage(MsgType.Informational, l_msg_header + "end turn");
		}

		return l_order;
	}

	/**
	 * moves armies randomly between its countries, if possible
	 * 
	 * @return the order or null if this order type is not possible
	 * @throws Exception an unexpected error
	 */
	private IOrder doRandomMove() throws Exception {
		IOrder l_order = null;
		return l_order;
	}

	/**
	 * attacks random neighboring countries, if possible
	 * 
	 * @return the order or null if this order type is not possible
	 * @throws Exception an unexpected error
	 */
	private IOrder doRandomAttack() throws Exception {
		IOrder l_order = null;
		return l_order;
	}

	/**
	 * do a Deploy to one of the player's countries, if possible<br>
	 * 
	 * @return the order or null if a deploy is not allowed
	 * @throws Exception an unexpected error
	 */
	private IOrder doRandomDeploy() throws Exception {
		IOrder l_order = null;

		if (d_player.getReinforcements() < 1) {
			return l_order;
		}

		ArrayList<ICountryModel> l_player_countries = d_player.getPlayerCountries();
		int l_country_idx = Utl.randomInt(l_player_countries.size() - 1);
		ICountryModel l_country = l_player_countries.get(l_country_idx);
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
		IPlayerStrategy l_player_strategy = new PlayerRandomStrategy(p_player,
				p_player.getPlayerModelFactory().getUserMessageModel());
		return l_player_strategy;
	}

	/**
	 * @return the name of this strategy
	 */
	@Override
	public String toString() {
		return "random";
	}
}
