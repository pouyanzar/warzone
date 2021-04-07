package soen6441.team01.warzone.view.contracts;

import java.util.ArrayList;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.entities.UserMessage;

/**
 * Defines the interface used to support the Warzone game startup view
 *
 */
public interface IGameTournamentView {
	void activate();

	void deactivate();

	void displayTournamentGameplayBanner();

	void processMessage(MsgType none, String string);

	void displayTournamentParameters(ArrayList<String> p_map_filenames, ArrayList<String> p_strategies,
			int p_number_of_games, int p_max_turns);
	
	void displayTournamentErrors(ArrayList<String> p_output);
}
