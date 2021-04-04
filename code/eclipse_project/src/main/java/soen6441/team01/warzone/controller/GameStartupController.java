package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.model.*;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.view.ViewFactory;
import soen6441.team01.warzone.view.contracts.IGameStartupView;

/**
 * Warzone game startup controller. Manages the coordination and progression of
 * the game startup phase.
 */
public class GameStartupController extends Phase implements IGameStartupController {
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
	private IGameStartupView d_view;
	private IAppMsg d_msg_model;
	private IGamePlayModel d_gameplay;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public GameStartupController(ControllerFactory p_controller_factory) throws Exception {
		super(p_controller_factory.getModelFactory().getGameEngine());
		d_controller_factory = p_controller_factory;
		d_model_factory = p_controller_factory.getModelFactory();
		d_view_factory = p_controller_factory.getViewFactory();
		d_view = d_view_factory.getGameStartupConsoleView(this);
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * invoked by the game engine as part of the game startup phase of the game.
	 */
	@Override
	public void execPhase() {
		execGameStartup();
	}

	/**
	 * Starts executing the game startup dynamics
	 */
	public void execGameStartup() {
		Phase l_next_phase = null;
		Phase l_end_phase = null;

		try {
			d_view.activate();
			l_end_phase = d_controller_factory.getGameEndPhase();
			d_view.displayGameStartupBanner();

			d_gameplay = d_model_factory.getNewGamePlayModel();
			d_gameplay.setMap(d_model_factory.getMapModel());

			String l_cmd;
			while (l_next_phase == null) {
				l_cmd = d_view.getCommand();
				l_next_phase = processGameStartupCommand(l_cmd);
			}
			nextPhase(l_next_phase);
		} catch (Exception ex) {
			Utl.lprintln("Fatal error during game startup, exception: " + ex.getMessage());
			nextPhase(l_end_phase);
		}

		if (d_view != null) {
			d_view.deactivate();
		}
	}

	/**
	 * Process the game startup command:
	 * <ul>
	 * <li>gameplayer -add playername [human|aggr|bene|rand|cheat] -remove
	 * playername</li>
	 * <li>assigncountries</li>
	 * <li>loadmap filename</li>
	 * <li>exit</li>
	 * <li>help</li>
	 * </ul>
	 * 
	 * @param p_command the command to process
	 * @return the next phase of the game. null = stay in current phase.
	 * @throws Exception unexpected error
	 */
	public Phase processGameStartupCommand(String p_command) throws Exception {
		Phase l_next_phase = null;

		String l_cmd_params[] = Utl.getFirstWord(p_command);
		switch (l_cmd_params[0]) {
		case "help":
			GameStartupHelp();
			break;
		case "exit":
			l_next_phase = d_controller_factory.getGameEndPhase();
			break;
		case "loadmap":
			if (processLoadMap(l_cmd_params[1])) {
				// will execute this startup phase with a new instance of this class.
				// this is desired as this is essentially the start of a new game.
				d_controller_factory.getNewGameStartupController();
				l_next_phase = d_controller_factory.getGameStartupPhase();
			}
			break;
		case "gameplayer":
			processGameplayer(l_cmd_params[1]);
			break;
		case "assigncountries":
			if (processAssignCountries(d_gameplay)) {
				// move on to gameplay
				l_next_phase = d_controller_factory.getGamePlayPhase();
			}
			break;
		default:
			d_msg_model.setMessage(MsgType.Error, "invalid command '" + p_command + "'");
			break;
		}
		return l_next_phase;
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
		Phase l_phase = processGameStartupCommand(p_command);
		if (l_phase instanceof GameEndController) {
			return "exit";
		}
		if (l_phase instanceof GamePlayController) {
			return "exit";
		}

		return "";
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
			d_msg_model.setMessage(MsgType.None, "assigncountries processed successfully");
			return true;
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, "assigncountries exception: " + ex.getMessage());
			return false;
		}
	}

	/**
	 * process the gameplayer command
	 * <p>
	 * syntax: gameplayer -add playername [human|aggr|bene|rand|cheat] -remove
	 * playername <br>
	 * [] = optional items - if not specified then human is the default
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
			d_msg_model.setMessage(MsgType.Error, "Invalid gameplayer, no options specified");
			return;
		}

		while (!Utl.isEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					// parse player name
					l_params = Utl.getFirstWord(l_params[1]);
					l_playerName = l_params[0];
					if (!Utl.isValidMapName(l_playerName)) {
						d_msg_model.setMessage(MsgType.Error, "Invalid gameplayer -add playername '" + l_playerName
								+ "', expecting a valid player name.");
						return;
					}

					// parse for the optional player strategy and return the proper player object
					IPlayerModel l_player = getPlayerWithStrategy(l_playerName, l_params);
					d_gameplay.addPlayer(l_player);

					d_msg_model.setMessage(MsgType.None,
							"player " + l_player.getName() + " added to game [" + l_player.getStrategy() + "]");
				} catch (Exception ex) {
					d_msg_model.setMessage(MsgType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_playerName = l_params[0];
					if (!Utl.isValidMapName(l_playerName)) {
						d_msg_model.setMessage(MsgType.Error, "Invalid gameplayer -remove playername '" + l_playerName
								+ "', expecting a valid player name.");
						return;
					}
					d_gameplay.removePlayer(l_playerName);
					d_msg_model.setMessage(MsgType.None, "player " + l_playerName + " removed from game.");
				} catch (Exception ex) {
					d_msg_model.setMessage(MsgType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg_model.setMessage(MsgType.Error,
						"Invalid gameplayer option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.getFirstWord(l_params[1]);
		}
	}

	/**
	 * parse for player strategy this is optional - if not provided then default is
	 * human.<br>
	 * note: that the tokens in l_param will be returned as follows: l_param[0] =
	 * last word parsed (ie either the player strategy, or the playername,
	 * l_param[1] the remaining part of the command-line, i.e. the next option
	 * (-add..., -remove... or blank)
	 * 
	 * @param p_playerName the new player's name
	 * @param p_params     l_param[0] the player strategy
	 *                     [human|aggr|bene|rand|cheat], or the next option
	 *                     (-add..., -remove... or blank)
	 * @return the newly created player with the appropriate strategy
	 * @throws Exception unexpected errir
	 */
	private IPlayerModel getPlayerWithStrategy(String p_player_name, String[] p_params) throws Exception {
		String l_tmp_params[] = Utl.getFirstWord(p_params[1]);
		boolean l_next_word = false;

		IPlayerModel l_player = new Player(p_player_name, d_model_factory);
		IPlayerStrategy l_strategy = null;

		switch (l_tmp_params[0]) {
		case "aggr":
			l_strategy = new PlayerAggressiveStrategy(l_player);
			l_next_word = true;
			break;
		case "bene":
			l_strategy = new PlayerBenevolentStrategy(l_player);
			l_next_word = true;
			break;
		case "rand":
			l_strategy = null;
			l_next_word = true;
			break;
		case "cheat":
			l_strategy = null;
			l_next_word = true;
			break;
		case "human":
			l_next_word = true;
		default:
			// for a human player the player model requires user input during issue_order();
			// therefore setup the player datasource (which the Player model will use to
			// retrieve user input) as a method in the GamePlayController which will request
			// the command from the view and pass it back to the Player model. This sort of
			// breaks the MVC symmetry but is required in this case in order to keep
			// issue_order() as generic as possible (e.g. computer player).
			IGameplayOrderDatasource l_order_datasource = d_controller_factory.getGamePlayOrderDatasource();
			l_strategy = new PlayerHumanStrategy(l_player, l_order_datasource);
		}
		l_player.setStrategy(l_strategy);

		// fixup the returning command-line params
		if (l_next_word) {
			p_params[0] = l_tmp_params[0];
			p_params[1] = l_tmp_params[1];
		}

		return l_player;
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
				d_msg_model.setMessage(MsgType.None, "loadmap processed successfully");
				d_model_factory.setMapModel(l_map_model);
			} else {
				d_msg_model.setMessage(MsgType.Error, "loadmap error - map is not a valid map.");
				return false;
			}
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, ex.getMessage());
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
		d_view.processMessage(MsgType.None, "Game startup commands:");
		d_view.processMessage(MsgType.None,
				" - gameplayer -add playername [human*|aggr|bene|rand|cheat] -remove playername");
		d_view.processMessage(MsgType.None,
				"              [] = optional items - if not specified then human is the default");
		d_view.processMessage(MsgType.None, " - assigncountries");
		d_view.processMessage(MsgType.None, " - loadmap filename");
		d_view.processMessage(MsgType.None, " - exit");
		d_view.processMessage(MsgType.None, " - help");
	}
}
