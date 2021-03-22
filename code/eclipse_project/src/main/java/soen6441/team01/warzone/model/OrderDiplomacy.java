package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;


/**
 * Supports the definition and implementation of the 'diplomacy' order.
 *
 */
public class OrderDiplomacy implements IOrder {

	private String d_player_target_name;
	private int d_reinforcements;
	private IPlayerModel d_player;

	/**
	 * Constructor
	 * 
	 * @param p_player_target_name the country that will receive the reinforcements
	 * @param p_player             the player model needed to process this order
	 */
	public OrderDiplomacy(String p_player_target_name, IPlayerModel p_player) {
		d_player_target_name = p_player_target_name;
		d_player = p_player;
	}

	/**
	 * Execute the diplomacy order
	 * 
	 * @return informational message about what was done, ie if successfully
	 *         executed (ie no exceptions)
	 * @throws Exception unexpected error
	 */
	public String execute() throws Exception {
//			return d_player.diplomacy(d_player_target_name);
		return null;
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
	 * override the default toString() to describe what this order is
	 * 
	 * @return string describing what this order is
	 */
	@Override
	public String toString() {
		String l_str = "negotiating " + " to " + d_player_target_name;
		return l_str;
	}
}
