package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.model.GamePlay;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
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
	private SoftwareFactoryController d_controller_factory;
	private IGameStartupView d_view;
	private IUserMessageModel d_msg_model;
	private IGamePlayModel d_gameplay;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_model_factory      predefined SoftwareFactoryModel.
	 * @param p_view_factory       predefined SoftwareFactoryView.
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public GameStartupController(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory,
			SoftwareFactoryController p_controller_factory) throws Exception {
		d_model_factory = p_model_factory;
		d_view_factory = p_view_factory;
		d_view = d_view_factory.getGameStartupConsoleView(this); 
		d_controller_factory = p_controller_factory;
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * Starts executing the game startup dynamics
	 * 
	 * @return String one of: exit, startup_complete
	 * @throws Exception unexpected error
	 */
	public String processGameStartup() throws Exception {
		String l_cmd = "exit";
		d_gameplay = d_model_factory.getGamePlayModel();

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
	 * mirror of method processGameStartupCommand(String p_command). this method is
	 * used mainly to test processGameStartupCommand.
	 * 
	 * @param p_command  the command to process
	 * @param p_gameplay the gameplay model to process
	 * @return String one of; exit, assigncountries
	 * @throws Exception unexpected error
	 */
	public String processGameStartupCommand(String p_command, IGamePlayModel p_gameplay) throws Exception {
		d_gameplay = p_gameplay;
		return processGameStartupCommand(p_command);
	}

	/**
	 * Process the game startup command:
	 * <ul>
	 * <li>gameplayer -add playername -remove playername</li>
	 * <li>assigncountries</li>
	 * <li>loadmap filename</li>
	 * <li>exit</li>
	 * <li>help</li>
	 * </ul>
	 * 
	 * @param p_command the command to process
	 * @return String one of; exit, assigncountries
	 * @throws Exception unexpected error
	 */
	public String processGameStartupCommand(String p_command) throws Exception {
		String l_return_command = "";
		String l_cmd_params[] = Utl.getFirstWord(p_command);
		switch (l_cmd_params[0]) {
		case "help":
			GameStartupHelp();
			break;
		case "exit":
			l_return_command = "exit";
			break;
		case "loadmap":
			processLoadMap(l_cmd_params[1]);
			break;
		case "gameplayer":
			processGameplayer(l_cmd_params[1]);
			break;
		case "assigncountries":
			l_return_command = "";
			if( processAssignCountries(d_gameplay) ) {
				// move on to gameplay
				l_return_command = "assignedcountries";				
			}
			break;
		default:
			d_msg_model.setMessage(MessageType.Error, "invalid command '" + p_command + "'");
			break;
		}
		return l_return_command;
	}

	/**
	 * process the assigncountries command
	 * <p>
	 * syntax: assigncountries
	 * </p>
	 * 
	 * @param p_gameplay the gameplay model containing the countries and players to
	 *                   use for the assignment.
	 * @return true if successful otherwise false                  
	 */
	private boolean processAssignCountries(IGamePlayModel p_gameplay) {
		try {
			p_gameplay.assignCountries();
			d_msg_model.setMessage(MessageType.None, "assigncountries processed successfully");
			return true;
		} catch (Exception ex) {
			d_msg_model.setMessage(MessageType.Error, "assigncountries exception: " + ex.getMessage());
			return false;
		}
	}

	/**
	 * process the gameplayer command
	 * <p>
	 * syntax: gameplayer -add playername -remove playername
	 * </p>
	 * 
	 * @param p_cmd_params the gameplayer parameters (just the parameters without
	 *                     the gameplayer command itself)
	 * @throws Exception unexpected error encountered
	 */
	private void processGameplayer(String p_cmd_params) throws Exception {
		String l_playerName;
		String l_params[] = Utl.getFirstWord(p_cmd_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MessageType.Error, "Invalid gameplayer, no options specified");
			return;
		}

		while (!Utl.isEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_playerName = l_params[0];
					if (!Utl.isValidMapName(l_playerName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid gameplayer -add playername '" + l_playerName
								+ "', expecting a valid player name.");
						return;
					}
					// for a human player the player model requires user input during issue_order();
					// therefore setup the player datasource (which the Player model will use to
					// retrieve user input) as a method in the GamePlayController which will request
					// the command from the view and pass it back to the Player model. This sort of
					// breaks the MVC symmetry but is required in this case in order to keep
					// issue_order() as generic as possible (e.g. computer player).
					IPlayerModel l_player = d_model_factory.newHumanPlayerModel(l_playerName,
							d_controller_factory.getGamePlayOrderDatasource());

					d_gameplay.addPlayer(l_player);
					d_msg_model.setMessage(MessageType.None, "player " + l_player.getName() + " added to game.");
				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_playerName = l_params[0];
					if (!Utl.isValidMapName(l_playerName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid gameplayer -remove playername '"
								+ l_playerName + "', expecting a valid player name.");
						return;
					}
					d_gameplay.removePlayer(l_playerName);
					d_msg_model.setMessage(MessageType.None, "player " + l_playerName + " removed from game.");
				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg_model.setMessage(MessageType.Error,
						"Invalid gameplayer option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.getFirstWord(l_params[1]);
		}
	}

	/**
	 * process the loadmap command
	 * 
	 * @param p_loadmap_params the loadmap parameters (just the parameters without
	 *                         the loadmap command itself)
	 * @return true if successful
	 * @throws Exception unexpected error encountered
	 */
	public boolean processLoadMap(String p_loadmap_params) throws Exception {
		try {
			String l_params[] = Utl.getFirstWord(p_loadmap_params);
			IMapModel l_map_model = Map.processLoadMapCommand(l_params[0], d_model_factory);
			if (l_map_model.validatemap()) {
				d_msg_model.setMessage(MessageType.None, "loadmap processed successfully");
				d_model_factory.setMapModel(l_map_model);
			} else {
				d_msg_model.setMessage(MessageType.Error, "loadmap error - map is not a valid map.");
				return false;
			}
		} catch (Exception ex) {
			d_msg_model.setMessage(MessageType.Error, ex.getMessage());
			return false;
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
		d_view.displayGameStartupBanner();
		d_view.processMessage(MessageType.None, "Game startup commands:");
		d_view.processMessage(MessageType.None, " - gameplayer -add playername -remove playername");
		d_view.processMessage(MessageType.None, " - assigncountries");
		d_view.processMessage(MessageType.None, " - loapmap filename");
		d_view.processMessage(MessageType.None, " - exit");
		d_view.processMessage(MessageType.None, " - help");
	}
}
