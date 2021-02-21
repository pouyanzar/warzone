package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.view.SoftwareFactoryView;
import soen6441.team01.warzone.view.contracts.IGameStartupView;

/**
 * Warzone game startup controller. Manages the coordination and progression of
 * the game startup phase.
 */
public class GameStartupController implements IGameStartupController {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private IGameStartupView d_view;
	private IUserMessageModel d_msg_model;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_model_factory predefined SoftwareFactoryModel.
	 * @param p_view_factory  predefined SoftwareFactoryView.
	 * @throws Exception unexpected error
	 */
	public GameStartupController(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory)
			throws Exception {
		d_model_factory = p_model_factory;
		d_view_factory = p_view_factory;
		d_view = d_view_factory.getGameStartupConsoleView(this);
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * Starts executing the game startup dynamics
	 * 
	 * @return String one of: exit, startup_complete
	 */
	public String processGameStartup() {
		String l_cmd = "exit";

		try {
			d_view.displayGameStartupBanner();
			l_cmd = processUserCommands();
			switch (l_cmd) {
			case "exit":
				break;
			case "startup_complete":
				break;
			default:
				throw new Exception("Internal error processing the game startup.");
			}
		} catch (Exception ex) {
			System.out.println("Fatal error encountered during game startup.");
			System.out.println("Exception: " + ex.getMessage());
		}

		d_msg_model.setMessage(MessageType.Informational, "exiting game startup");

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
	 * @return String one of; exit, startup_complete
	 * @throws Exception general exception processing the map editor
	 */
	private String processUserCommands() throws Exception {
		boolean l_exit_startup = false;
		String l_cmd = "exit";
		while (!l_exit_startup) {
			l_cmd = d_view.getCommand();
			l_cmd = processGameStartupCommand(l_cmd);
			switch (l_cmd) {
			case "exit":
				l_exit_startup = true;
				break;
			case "assignedcountries":
				l_cmd = "startup_complete";
				l_exit_startup = true;
				break;
			}
		}
		return l_cmd;
	}

	/**
	 * Process the game startup command:
	 * <ul>
	 * <li>gameplayer -add playername -remove playername</li>
	 * <li>assigncountries</li>
	 * <li>exit</li>
	 * <li>help</li>
	 * </ul>
	 * 
	 * @param l_command the command to process
	 * @return String one of; exit, assigncountries
	 * @throws Exception unexpected error
	 */
	public String processGameStartupCommand(String l_command) throws Exception {
		String l_return_command = "";
		String l_cmd_params[] = Utl.GetFirstWord(l_command);
		switch (l_cmd_params[0]) {
		case "help":
			GameStartupHelp();
			break;
		case "exit":
			l_return_command = "exit";
			break;
		case "gameplayer":
			// processEditContinent(l_cmd_params[1]);
			d_msg_model.setMessage(MessageType.None, "gameplayer coming soon...");
			break;
		case "assigncountries":
			// processEditContinent(l_cmd_params[1]);
			d_msg_model.setMessage(MessageType.None, "assigncountries coming soon...");
			l_return_command = "assignedcountries";
			break;
		default:
			d_msg_model.setMessage(MessageType.Error, "invalid command '" + l_command + "'");
			break;
		}
		return l_return_command;
	}

	/**
	 * Asks the view to display the list of map editor commands along with their
	 * syntax.
	 * 
	 * @param p_view the map editor view being used by the controller
	 */
	private void GameStartupHelp() {
		d_view.displayGameStartupBanner();
		d_view.processMessage(MessageType.None, "Game startup commands:");
		d_view.processMessage(MessageType.None, " - gameplayer -add playername -remove playername");
		d_view.processMessage(MessageType.None, " - assigncountries");
		d_view.processMessage(MessageType.None, " - exit");
		d_view.processMessage(MessageType.None, " - help");
	}
}
