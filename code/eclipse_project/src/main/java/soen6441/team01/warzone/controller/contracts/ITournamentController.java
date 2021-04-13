package soen6441.team01.warzone.controller.contracts;

import java.util.ArrayList;

/**
 * Defines the interface used to support the Warzone game startup controller
 *
 */
public interface ITournamentController {
	boolean validateTournamentParameters(ArrayList<String> p_val_out);
}
