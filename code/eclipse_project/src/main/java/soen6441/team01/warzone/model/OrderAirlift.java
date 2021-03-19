package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.entities.CardType;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IOrder;

/**
 * Supports the definition and implementation of the 'airlift' order.
 *
 */
public class OrderAirlift implements IOrder {

	private String d_country_source;
	private String d_country_target;
	private int d_reinforcements;
	private IPlayerModel d_player;

	/**
	 * Constructor
	 * 	 
	 * @param p_country_source the country to move the reinforcements from
	 * @param p_country_target the country that will receive the reinforcements
	 * @param p_armies       the number of reinforcement armies to add to the
	 *                       specified country
	 * @param p_player       the player model needed to process this order
	 */
	public OrderAirlift(String p_country_source, String p_country_target, int p_armies, IPlayerModel p_player) {
		d_country_source = p_country_source;
		d_country_target = p_country_target;
		d_reinforcements = p_armies;
		d_player = p_player;
	}

	/**
	 * Execute the airlift order
	 * 
	 * @return informational message about what was done, ie if successfully
	 *         executed (ie no exceptions)
	 * @throws Exception unexpected error
	 */
	public String execute() throws Exception {
		return null;
//		return d_player.airlift(d_country_source, d_country_target, d_reinforcements);
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
		String l_str = "advance " + d_reinforcements + " from " + d_country_source + " to " + d_country_target;
		return l_str;
	}
}
