package soen6441.team01.warzone.controller.contracts;

import soen6441.team01.warzone.model.contracts.IGamePlayModel;

/**
 * Defines the interface used to support the Warzone game startup controller
 *
 */
public interface IGameStartupController {
	String processGameStartup(IGamePlayModel p_gameplay);
}
