package soen6441.team01.warzone.model;

import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.model.contracts.IOrderModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;

/**
 * Supports the definition and implementation of the 'deploy' order.
 *
 */
public class OrderDeploy implements IOrderModel {

	private String d_country_name;
	private int d_reinforcements;
	private IPlayerModel d_player;

	/**
	 * Constructor
	 * 
	 * @param p_country_name the country that will receive the reinforcements
	 * @param p_armies       the number of reinforcement armies to add to the
	 *                       specified country
	 * @param p_player       the player model needed to process this order
	 */
	public OrderDeploy(String p_country_name, int p_armies, IPlayerModel p_player) {
		d_country_name = p_country_name;
		d_reinforcements = p_armies;
		d_player = p_player;
	}

	/**
	 * Execute the deploy order
	 * 
	 * @return informational message about what was done, ie if successfully
	 *         executed (ie no exceptions)
	 * @throws Exception unexpected error
	 */
	public String execute() throws Exception {
		return d_player.deploy(d_country_name, d_reinforcements);
	}

	/**
	 * Change the current player to the specified player
	 * 
	 * @param p_player the new player to assign this order to
	 */
	public void setPlayer(IPlayerModel p_player) {
		d_player = p_player;
	}

	/**
	 * override the default toString() to describe what this order is
	 * 
	 * @return string describing what this order is
	 */
	@Override
	public String toString() {
		String l_str = "deploy " + d_reinforcements + " to country " + d_country_name;
		return l_str;
	}
}
