package soen6441.team01.warzone.controller.contracts;

import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IOrderModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;

/**
 * Defines the interface used to support the Warzone game play controller
 *
 */
public interface IGamePlayController {
	String processGamePlay() throws Exception;
}
