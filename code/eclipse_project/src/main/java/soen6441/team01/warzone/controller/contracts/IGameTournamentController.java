package soen6441.team01.warzone.controller.contracts;

import java.util.ArrayList;

import soen6441.team01.warzone.model.contracts.IGamePlayModel;

/**
 * Defines the interface used to support the Warzone game startup controller
 *
 */
public interface IGameTournamentController {
	void execGameStartup();
	
	boolean validateTournamentParameters(ArrayList<String> p_val_out);
}
