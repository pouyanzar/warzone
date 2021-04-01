package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.IGameplayOrderDatasource;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;

/**
 * Supports the human player strategy. <br>
 * A human player that requires user interaction to make decisions.
 *
 */
public class PlayerHumanStrategy implements IPlayerStrategy {

	IPlayerModel d_player = null;
	IGameplayOrderDatasource d_order_datasource = null;

	/**
	 * constructor
	 * 
	 * @param p_player           the player requiring the human player strategy
	 * @param p_order_datasource used to get the player commands during
	 *                           issue_order()
	 */
	public PlayerHumanStrategy(IPlayerModel p_player, IGameplayOrderDatasource p_order_datasource) {
		d_player = p_player;
		d_order_datasource = p_order_datasource;
	}

	/**
	 * create the order by asking the user for the next order
	 */
	public IOrder createOrder() throws Exception {
		IOrder l_order = d_order_datasource.getOrder(d_player);
		return l_order;
	}

	/**
	 * create a deep clone of the current strategy<br>
	 * required because we want the player to analyze based on recent changes to the
	 * map made by recent orders
	 */
	public IPlayerStrategy cloneStrategy(IPlayerModel p_player) throws Exception {
		IPlayerStrategy l_player_strategy = new PlayerHumanStrategy(p_player, d_order_datasource);
		return l_player_strategy;
	}
}
