package soen6441.team01.warzone.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.OrderDeploy;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IGameplayOrderDatasource;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IOrderModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.model.entities.CountrySummary;
import soen6441.team01.warzone.model.entities.GameState;
import soen6441.team01.warzone.view.SoftwareFactoryView;
import soen6441.team01.warzone.view.contracts.IGamePlayView;

/**
 * Warzone game play controller. Manages the coordination and progression of the
 * game play phase.
 */
public class GamePlayController implements IGamePlayController, IGameplayOrderDatasource {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private IGamePlayView d_view;
	private IUserMessageModel d_msg_model;
	private IGamePlayModel d_gameplay_model;
	private boolean d_exit = false;
	private boolean d_game_over = false;

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
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * Starts executing the game startup dynamics
	 * 
	 * @return String one of: exit, game_over
	 * @throws Exception unexpected error
	 */
	public String processGamePlay() throws Exception {
		d_view = d_view_factory.getGamePlayConsoleView(this);
		d_gameplay_model = d_model_factory.getGamePlayModel();
		d_gameplay_model.setGameState(GameState.GamePlay);
		int l_round = 1;
		String l_cmd = "exit";

		try {
			d_view.displayGamePlayBanner();
			// main game play loop
			while (!d_exit) {
				
				// assigning reinforcements phase
				d_msg_model.setMessage(MessageType.None,
						"\n* round " + l_round++ + " *\n\n* assigning reinforcements:");
				d_gameplay_model.assignReinforcements();

				// issue_order phase
				if (!d_exit) {
					d_msg_model.setMessage(MessageType.None, "\n* issuing orders:");
					int l_num_orders = issueOrders();
					if (l_num_orders < 1) {
						d_msg_model.setMessage(MessageType.Warning, "no new orders issued - ending game");
						break;
					}
				}

				// execute phase
				if (!d_exit) {
					d_msg_model.setMessage(MessageType.None, "\n* executing orders:");
					d_gameplay_model.executeOrders();
				}
			}
		} catch (Exception ex) {
			System.out.println("Fatal error encountered during gameplay.");
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
	 * @return the number of orders issued - if there are none then the game ends
	 * @throws Exception general exception processing the map editor
	 */
	private int issueOrders() throws Exception {
		int l_orders_issued = 0;
		ArrayList<IPlayerModel> l_player_clones = new ArrayList<IPlayerModel>();
		ArrayList<IPlayerModel> l_players = d_gameplay_model.getPlayers();
		Queue<IPlayerModel> l_queue = new LinkedList<IPlayerModel>();

		// clone the players and their countries to simplify keeping track of
		// issuing orders before the execution phase and to isolate each players map
		// from one another
		for (IPlayerModel l_player : l_players) {
			SoftwareFactoryModel l_cloned_sf_model = new SoftwareFactoryModel(d_model_factory);
			IMapModel l_cloned_map = Map.deepCloneMap(d_model_factory.getMapModel(), l_cloned_sf_model);
			IPlayerModel l_player_clone = l_player.deepClonePlayer(l_cloned_map);
			l_player_clones.add(l_player_clone);
			l_queue.add(l_player_clone);
		}

		// get player commands/orders
		IPlayerModel l_player_clone = l_queue.peek();
		while (!d_exit && l_player_clone != null) {
			l_queue.remove();
			if (l_player_clone.getReinforcements() > 0) {
				l_player_clone.issue_order();
				l_queue.add(l_player_clone);
				l_orders_issued++;
			}
			l_player_clone = l_queue.peek();
		}

		// copy orders from cloned players to real players for execution
		for (int l_idx = 0; l_idx < l_players.size(); l_idx++) {
			l_players.get(l_idx).copyOrders(l_player_clones.get(l_idx));
		}

		return l_orders_issued;
	}

	/**
	 * This method acts as a datasource for those Player (model) classes that
	 * require the user to provide an order.
	 * 
	 * @return the next order, or null if the player is done with this round
	 * @throws Exception invalid command or unexpected error
	 */
	public IOrderModel getOrder(IPlayerModel l_player_clone) throws Exception {
		IOrderModel l_order = null;

		while (!d_exit && l_order == null) {
			String l_cmd = d_view.getCommand("Gameplay " + l_player_clone.getName() + ">");
			l_order = processGamePlayCommand(l_cmd, l_player_clone);
		}
		return l_order;
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
	 * @param p_cmd          user full command
	 * @param l_player_clone the player to get and process commands for
	 * @return the order, null = order not fulfilled - try again
	 * @throws Exception unexpected error
	 */
	public IOrderModel processGamePlayCommand(String p_cmd, IPlayerModel l_player_clone) throws Exception {
		IOrderModel l_order = null;
		String l_cmd_params[] = Utl.getFirstWord(p_cmd);

		switch (l_cmd_params[0]) {
		case "help":
			GameStartupHelp();
			break;
		case "exit":
			d_exit = true;
			break;
		case "showmap":
			showMap(l_player_clone);
			break;
		case "deploy":
			l_order = processDeployCommand(l_cmd_params[1], l_player_clone);
			break;
		default:
			d_msg_model.setMessage(MessageType.Error, "invalid command '" + p_cmd + "'");
			break;
		}
		return l_order;
	}

	/**
	 * process the deploy command.
	 * <ul>
	 * <li>deploy countryID num_reinforcements</li>
	 * </ul>
	 * 
	 * @param p_deploy_params the loadmap parameters (just the parameters without
	 *                        the loadmap command itself)
	 * @param l_player_clone  the player object who wishes to deploy
	 * @return the player's order or null if there was a problem creating the order
	 * @throws Exception unexpected error encountered
	 */
	private IOrderModel processDeployCommand(String p_deploy_params, IPlayerModel l_player_clone) throws Exception {
		IOrderModel l_order = null;
		String l_params[] = Utl.getFirstWord(p_deploy_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MessageType.Error, "Invalid deploy command, no options specified");
			return null;
		}
		try {
			// parse the countryID
			String l_country_name = l_params[0];
			if (!Utl.isValidMapName(l_country_name)) {
				d_msg_model.setMessage(MessageType.Error, "Invalid deploy county name '" + l_country_name + "'.");
				return null;
			}
			// parse the num_reinforcements
			l_params = Utl.getFirstWord(l_params[1]);
			String l_reinforcements_str = l_params[0];
			if (Utl.isEmpty(l_reinforcements_str)) {
				d_msg_model.setMessage(MessageType.Error,
						"Invalid deploy command, number of reinforcements not specified.");
				return null;
			}
			int l_reinforcements = Utl.convertToInteger(l_reinforcements_str);
			if (l_reinforcements >= Integer.MAX_VALUE) {
				d_msg_model.setMessage(MessageType.Error,
						"Invalid number of deploy reinforcements '" + l_reinforcements_str + "'.");
				return null;
			}
			l_order = new OrderDeploy(l_country_name, l_reinforcements, l_player_clone);
			// execute the order on the cloned player to 1) see if it's valid 2) set the
			// state of the cloned player for the next command
			l_order.execute();
			String l_msg = "Deploy order successful.\n" + l_player_clone.getReinforcements()
					+ " reinforcement(s) left for " + l_player_clone.getName() + " to allocate.";
			d_msg_model.setMessage(MessageType.Informational, l_msg);
		} catch (Exception ex) {
			d_msg_model.setMessage(MessageType.Error, ex.getMessage());
			return null;
		}
		return l_order;
	}

	/**
	 * Show a map of the specified player
	 * 
	 * @param p_player the play of whom wants to see a map of their owned countries
	 *                 and their neighbors.
	 */
	private void showMap(IPlayerModel p_player) {
		ArrayList<ICountryModel> l_countries = p_player.getPlayerCountries();
		for (ICountryModel l_country : l_countries) {
			d_view.showCountry(l_country);
		}
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
