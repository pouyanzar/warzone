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
public class PlayerBenevolentStrategy implements IPlayerStrategy {

	// the map is available from within the player object
	IPlayerModel d_player = null;

	/**
	 * constructor
	 * 
	 * @param p_player the player requiring the human player strategy
	 */
	public PlayerBenevolentStrategy(IPlayerModel p_player) {
		d_player = p_player;
	}

	/**
	 * create the order
	 * 
	 * @return the next order
	 */
	public IOrder createOrder() throws Exception {
		IOrder l_order = null;
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
		IPlayerStrategy l_player_strategy = new PlayerBenevolentStrategy(p_player);
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
