package soen6441.team01.warzone.view;

import java.util.ArrayList;
import java.util.Scanner;

import soen6441.team01.warzone.common.Observable;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.contracts.Observer;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.controller.contracts.ITournamentController;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.entities.UserMessage;
import soen6441.team01.warzone.view.contracts.IGameStartupView;
import soen6441.team01.warzone.view.contracts.IGameTournamentView;

/**
 * Warzone game startup console based view. The view interacts with the user via
 * the system console.
 */
public class GameTournamentConsole implements Observer, IGameTournamentView {
	private ITournamentController d_controller = null;
	private IAppMsg d_message = null;
	private ModelFactory d_factory_model = null;

	/**
	 * Constructor.
	 * 
	 * @param p_controller    the associated controller object
	 * @param p_factory_model model factory
	 * @throws Exception unexpected error
	 */
	public GameTournamentConsole(ITournamentController p_controller, ModelFactory p_factory_model)
			throws Exception {
		d_controller = p_controller;
		d_factory_model = p_factory_model;
		d_message = d_factory_model.getUserMessageModel();
	}

	/**
	 * activate the view
	 */
	public void activate() {
		if (d_message != null) {
			d_message.attach(this);
		}
	}

	/**
	 * do a clean shutdown of the view
	 */
	public void deactivate() {
		if (d_message != null) {
			d_message.detach(this);
		}
	}

	/**
	 * Displays the startup banner
	 */
	public void displayTournamentGameplayBanner() {
		Utl.lprintln("");
		Utl.lprintln("***         Game Play          ***");
		Utl.lprintln("***      Tournament Mode       ***");
	}

	/**
	 * Displays the tournament parameters
	 */
	public void displayTournamentParameters(ArrayList<String> p_map_filenames, ArrayList<String> p_strategies,
			int p_number_of_games, int p_max_turns) {
		Utl.lprintln("");
		Utl.lprintln(Utl.plural(p_map_filenames.size(), "Map: ", "Maps: "));
		for (String l_str : p_map_filenames) {
			Utl.lprintln("   " + l_str);
		}
		Utl.lprintln(Utl.plural(p_strategies.size(), "Strategy: ", "Strategies: "));
		for (String l_str : p_strategies) {
			Utl.lprintln("   " + l_str);
		}
		Utl.lprintln("Number of games for each map        : " + p_number_of_games);
		Utl.lprintln("Maximum number of turns for each map: " + p_max_turns);
	}

	/**
	 * Displays the tournament parameters
	 */
	public void displayTournamentErrors(ArrayList<String> p_output) {
		Utl.lprintln("");
		Utl.lprintln("! Encountered errors in the tournament parameters: ");
		for (String l_str : p_output) {
			Utl.lprintln("   " + l_str);
		}
	}

	/**
	 * Displays a message to the user
	 * 
	 * @param p_msg_type the type of message to display as defined by the enum
	 * @param p_message  the message to display to the user
	 */
	public void processMessage(MsgType p_msg_type, String p_message) {
		Utl.lprintln(p_msg_type, p_message);
	}

	/**
	 * process message helper function
	 * 
	 * @param p_user_message the UserMessage to process
	 */
	public void processMessage(UserMessage p_user_message) {
		MsgType l_msgtyp = p_user_message.getMessageType();
		String l_msg = p_user_message.getMessage();
		processMessage(l_msgtyp, l_msg);
	}

	/**
	 * called by Observable whenever the system adds a new user message that needs
	 * to be communicated to the user.
	 */
	@Override
	public void update(Observable p_obserable) {
		if (p_obserable == d_message) {
			processMessage(d_message.getLastMessage());
		}
	}
}
