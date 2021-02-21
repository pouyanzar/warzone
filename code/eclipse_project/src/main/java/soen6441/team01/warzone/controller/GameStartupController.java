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
	 * @return String = last command entered. Valid commands include: exit,
	 *         assigncountries
	 */
	public String processGameStartup() {
		String l_cmd = "exit";

		try {
			d_view.displayGameStartupBanner();
			l_cmd = processUserCommands();
			if (l_cmd == null || Utl.IsEmpty(l_cmd)) {
				throw new Exception("Internal error 1 processing game startup.");
			}
			String l_cmd_params[] = new String[2];
			l_cmd_params = Utl.GetFirstWord(l_cmd);
			switch (l_cmd_params[0]) {
			case "exit":
				break;
			case "assigncountries":
				break;
			default:
				throw new Exception("Internal error 2 processing the game startup.");
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
	 * @return String the last command entered
	 * @throws Exception general exception processing the map editor
	 */
	private String processUserCommands() throws Exception {
		boolean l_exit_startup = false;
		String l_cmd = "exit";
		while (!l_exit_startup) {
			l_cmd = d_view.getCommand();
			l_exit_startup = !processGameStartupCommand(l_cmd);
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
	 * @return true = command processed successfully, false = command to exit
	 * @throws Exception unexpected error
	 */
	public boolean processGameStartupCommand(String l_command) throws Exception {
		String l_cmd_params[] = Utl.GetFirstWord(l_command);
		switch (l_cmd_params[0]) {
		case "help":
			GameStartupHelp();
			break;
		case "exit":
			return false;
		case "gameplayer":
			// processEditContinent(l_cmd_params[1]);
			d_msg_model.setMessage(MessageType.None, "gameplayer coming soon...");
			break;
		case "assigncountries":
			// processEditContinent(l_cmd_params[1]);
			d_msg_model.setMessage(MessageType.None, "assigncountries coming soon...");
			break;
		default:
			d_msg_model.setMessage(MessageType.Error, "invalid command '" + l_command + "'");
			break;
		}
		return true;
	}

	/**
	 * process the editneighbor command
	 * 
	 * @param l_editneighbor_params the editneighbor parameters (just the parameters
	 *                              without the editneighbor command itself)
	 * @throws Exception unexpected error encountered
	 */
	private void processEditNeighbor(String l_editneighbor_params) throws Exception {
		IMapModel l_map = d_model_factory.getMapModel();
		String l_countryName;
		String l_neighbor_name;
		String l_params[] = Utl.GetFirstWord(l_editneighbor_params);

		if (Utl.IsEmpty(l_params[0])) {
			d_msg_model.setMessage(MessageType.Error, "Invalid editneighbor, no options specified");
			return;
		}

		while (!Utl.IsEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.GetFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.IsValidMapName(l_countryName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editneighbor -add countryId '"
								+ l_countryName + "', expecting a valid map name");
						return;
					}
					l_params = Utl.GetFirstWord(l_params[1]);
					l_neighbor_name = l_params[0];
					if (!Utl.IsValidMapName(l_neighbor_name)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editneighbor -add neighborcountryID '"
								+ l_countryName + "', expecting a valid map name");
						return;
					}
					l_map.addNeighbor(l_countryName, l_neighbor_name);
				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.GetFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.IsValidMapName(l_countryName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editneighbor -remove countryId '"
								+ l_countryName + "', expecting a valid map name");
						return;
					}
					l_params = Utl.GetFirstWord(l_params[1]);
					l_neighbor_name = l_params[0];
					if (!Utl.IsValidMapName(l_neighbor_name)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editneighbor -remove neighborcountryID '"
								+ l_countryName + "', expecting a valid map name");
						return;
					}
					l_map.removeNeighbor(l_countryName, l_neighbor_name);
				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg_model.setMessage(MessageType.Error,
						"Invalid editneighbor option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.GetFirstWord(l_params[1]);
			d_msg_model.setMessage(MessageType.None, "editneighbor processed successfully");
		}
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
