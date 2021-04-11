package soen6441.team01.warzone.model;

import java.io.Serializable;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Supports the definition and implementation of the 'negotiate' command /
 * diplomacy order.
 *
 */
public class OrderDiplomacy implements IOrder, Serializable {
	private static final long serialVersionUID = 1L;
	private IPlayerModel d_source_player;
	private IPlayerModel d_target_player;

	/**
	 * Constructor
	 * 
	 * @param p_source_player the player issuing the diplomacy
	 * @param p_target_player the player receiving the diplomacy
	 * @throws Exception fails validation
	 */
	public OrderDiplomacy(IPlayerModel p_source_player, IPlayerModel p_target_player) throws Exception {
		d_source_player = p_source_player;
		d_target_player = p_target_player;
		isValid();
	}

	/**
	 * Execute the diplomacy order
	 * 
	 * @return informational message about what was done, ie if successfully
	 *         executed (ie no exceptions)
	 * @throws Exception fails validation
	 */
	public String execute() throws Exception {
		isValid();
		String l_result = doDiplomacy();
		d_source_player.removeCard(CardType.diplomacy);
		return l_result;
	}

	/**
	 * until the end of the turn, you and the target player cannot attack each
	 * other.
	 * 
	 * @return a message to show half of the armies destroyed
	 */
	public String doDiplomacy() {
		d_source_player.addDiplomacy(d_target_player);
		d_target_player.addDiplomacy(d_source_player);
		String l_msg = "diplomacy between " + d_source_player.getName() + " and " + d_target_player.getName()
				+ " for this turn.";
		return l_msg;
	}

	/**
	 * Checks if the condition for using diplomacy card is valid
	 * 
	 * @throws Exception invalid diplomacy
	 */
	private void isValid() throws Exception {
		// check that the players were specified
		if (d_source_player == null) {
			throw new Exception("source player cannot be null in diplomacy order");
		}
		if (d_target_player == null) {
			throw new Exception("target player cannot be null in diplomacy order");
		}

		// validate that the player has the diplomacy card
		if (!d_source_player.hasCard(CardType.diplomacy)) {
			throw new Exception(d_source_player.getName() + " does not have an diplomacy card!");
		}
	}

	/**
	 * Change the current player to the specified player
	 * 
	 * @param p_source_player the new player to assign this order to
	 * @throws Exception unexpected error
	 */
	public void cloneToPlayer(IPlayerModel p_source_player) throws Exception {
		d_source_player = p_source_player;
		ModelFactory l_model_factory = p_source_player.getPlayerModelFactory();
		IPlayerModel l_tplayer = Player.FindPlayer(d_target_player.getName(),
				l_model_factory.getGamePlayModel().getPlayers());
		d_target_player = l_tplayer;
	}

	/**
	 * override the default toString() to describe what this order is
	 * 
	 * @return string describing what this order is
	 */
	@Override
	public String toString() {
		String l_str = "diplomacy between " + d_source_player.getName() + " and " + d_target_player.getName()
				+ " for this turn";
		return l_str;
	}
}
