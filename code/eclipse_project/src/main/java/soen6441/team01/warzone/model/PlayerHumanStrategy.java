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
	 * Create the order by asking the user for the next order. <br>
	 * The order requires user input, which is not possible in a model based class
	 * without breaking the MVC pattern; therefore we'll invoke the getOrder()
	 * method which has been defined as a generic interface into a data source class
	 * that will provide the order. In theory the data source can be another model
	 * based class that gets the input via a file for example; however in our case
	 * the data source is a controller class that is currently managing the view
	 * that is responsible for getting user input. In theory this doesn't not break
	 * the MVC pattern.
	 * 
	 * @return the next order
	 * 
	 */
	public IOrder createOrder() throws Exception {
		IOrder l_order = d_order_datasource.getOrder(d_player);
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
		IPlayerStrategy l_player_strategy = new PlayerHumanStrategy(p_player, d_order_datasource);
		return l_player_strategy;
	}

	/**
	 * @return the name of this strategy
	 */
	@Override
	public String toString() {
		return "human";
	}
}
