package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.view.SoftwareFactoryView;
import soen6441.team01.warzone.view.contracts.IGamePlayView;
import soen6441.team01.warzone.view.contracts.IGameStartupView;

/**
 * Warzone game play controller. Manages the coordination and progression of the
 * game play phase.
 */
public class GamePlayController implements IGamePlayController {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private IGamePlayView d_view;
	private IUserMessageModel d_msg_model;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_model_factory predefined SoftwareFactoryModel.
	 * @param p_view_factory  predefined SoftwareFactoryView.
	 * @throws Exception unexpected error
	 */
	public GamePlayController(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory)
			throws Exception {
		d_model_factory = p_model_factory;
		d_view_factory = p_view_factory;
		d_view = d_view_factory.getGamePlayConsoleView(this);
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * Starts executing the game startup dynamics
	 * 
	 * @return String one of: exit, game_over 
	 */
	public String processGamePlay(IGamePlayModel p_gameplay_model) {
		String l_cmd = "exit";

		try {
			d_view.displayGamePlayBanner();
			l_cmd = processUserCommands();
			if (l_cmd == null || Utl.isEmpty(l_cmd)) {
				throw new Exception("Internal error 1 processing gameplay.");
			}
			String l_cmd_params[] = new String[2];
			l_cmd_params = Utl.getFirstWord(l_cmd);
			switch (l_cmd_params[0]) {
			case "exit":
				break;
			case "assigncountries":
				break;
			default:
				throw new Exception("Internal error 2 processing the gameplay.");
			}
		} catch (Exception ex) {
			System.out.println("Fatal error encountered during gameplay.");
			System.out.println("Exception: " + ex.getMessage());
		}

		d_msg_model.setMessage(MessageType.Informational, "exiting gameplay");

		if (d_view != null) {
			d_view.shutdown();
		}

		return l_cmd;
	}

	/**
	 * Manages the map editor's interactions with the game startup view, and
	 * processes any commands coming from the view.
	 * 
	 * @param p_view the GameStartupView to interact with
	 * @return String the last command entered
	 * @throws Exception general exception processing the map editor
	 */
	private String processUserCommands() throws Exception {
		boolean l_exit_gameplay = false;
		String l_cmd = "exit";
		while (!l_exit_gameplay) {
			l_cmd = d_view.getCommand();
			l_exit_gameplay = !processGamePlayCommand(l_cmd);
		}
		return l_cmd;
	}

	/**
	 * Process the game play command:
	 * <ul>
	 * <li>showmap</li>
	 * <li>deploy countryID num_reinforcements</li>
	 * <li>exit</li>
	 * <li>help</li>
	 * </ul>
	 * 
	 * @param p_command the command to process
	 * @return true = command processed successfully, false = command to exit
	 * @throws Exception unexpected error
	 */
	public boolean processGamePlayCommand(String p_command) throws Exception {
		String l_cmd_params[] = Utl.getFirstWord(p_command);
		switch (l_cmd_params[0]) {
		case "help":
			GameStartupHelp();
			break;
		case "exit":
			return false;
		case "showmap":
			// processEditContinent(l_cmd_params[1]);
			d_msg_model.setMessage(MessageType.None, "showmap coming soon...");
			break;
		case "deploy":
			// processEditContinent(l_cmd_params[1]);
			d_msg_model.setMessage(MessageType.None, "deploy coming soon...");
			break;
		default:
			d_msg_model.setMessage(MessageType.Error, "invalid command '" + p_command + "'");
			break;
		}
		return true;
	}

	/**
	 * Asks the view to display the list of map editor commands along with their
	 * syntax.
	 * 
	 * @param p_view the map editor view being used by the controller
	 */
	private void GameStartupHelp() {
		d_view.displayGamePlayBanner();
		d_view.processMessage(MessageType.None, "Gameplay commands:");
		d_view.processMessage(MessageType.None, " - showmap");
		d_view.processMessage(MessageType.None, " - deploy countryID num_reinforcements");
		d_view.processMessage(MessageType.None, " - exit");
		d_view.processMessage(MessageType.None, " - help");
	}
}
