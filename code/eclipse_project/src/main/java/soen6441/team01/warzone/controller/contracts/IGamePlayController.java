package soen6441.team01.warzone.controller.contracts;

import soen6441.team01.warzone.model.contracts.IGamePlayModel;

/**
 * Defines the interface used to support the Warzone game play controller
 *
 */
public interface IGamePlayController {
	String processGamePlay(IGamePlayModel p_gameplay_model);
}
