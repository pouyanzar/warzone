package soen6441.team01.warzone.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.OrderDeploy;
import soen6441.team01.warzone.model.Phase;
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
public class IssueOrderController extends GamePlayController implements IGamePlayController, IGameplayOrderDatasource {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private SoftwareFactoryController d_controller_factory;
	private IGamePlayView d_view;
	private IUserMessageModel d_msg_model;
	private IGamePlayModel d_gameplay_model;
	private Phase d_next_phase = null;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public IssueOrderController(SoftwareFactoryController p_controller_factory) throws Exception {
		super(p_controller_factory);
		d_controller_factory = p_controller_factory;
		d_model_factory = p_controller_factory.getModelFactory();
		d_view_factory = p_controller_factory.getViewFactory();
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * invoked by the game engine as part of the Map Editor phase of the game.
	 */
	@Override
	public void execPhase() {
		try {
			processIssueOrdersGamePlay();
		} catch (Exception ex) {
			d_msg_model.setMessage(MessageType.Error, "exception in GamePlayController: " + ex.getMessage());
			endGamePlayPhase();
		}
	}

	/**
	 * Starts executing the game issue_order phase dynamics
	 * 
	 * @throws Exception unexpected error
	 */
	public void processIssueOrdersGamePlay() throws Exception {
		d_view = d_view_factory.getGamePlayConsoleView(this);
		d_gameplay_model = d_model_factory.getGamePlayModel();
		d_msg_model.setMessage(MessageType.None, "\n* issuing orders:");
		Phase l_next_phase = issueOrders();
		if (l_next_phase != null) {
			nextPhase(l_next_phase);
		}
	}

	/**
	 * Manages the map editor's interactions with the game startup view, and
	 * processes any commands coming from the view.
	 * 
	 * @return the next phase to execute
	 * @throws Exception general exception processing the map editor
	 */
	private Phase issueOrders() throws Exception {
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
		d_next_phase = null;

		while (d_next_phase == null && l_player_clone != null) {
			l_queue.remove();
			l_player_clone.issue_order();
			if (!l_player_clone.isDoneTurn()) {
				l_queue.add(l_player_clone);
			}
			l_player_clone = l_queue.peek();
		}

		// copy orders from cloned players to real players for execution
		for (int l_idx = 0; l_idx < l_players.size(); l_idx++) {
			l_players.get(l_idx).copyOrders(l_player_clones.get(l_idx));
		}
		
		if( d_next_phase == null ) {
			d_next_phase = d_controller_factory.getOrderExecPhase();
		}

		return d_next_phase;
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

		while (d_next_phase == null && l_order == null && !l_player_clone.isDoneTurn() ) {
			String l_cmd = d_view.getCommand("Gameplay " + l_player_clone.getName() + ">");
			l_order = processGamePlayCommand(l_cmd, l_player_clone);
		}
		return l_order;
	}

	/**
	 * Process the game play command:
	 * <ul>
	 * <li>deploy countryID num_reinforcements</li>
	 * <li>advance countrynamefrom countynameto numarmies</li>
	 * <li>bomb countryID (requires bomb card)</li>
	 * <li>blockade countryID (required blockade card)</li>
	 * <li>airlift sourcecountryID targetcountryID numarmies (requires the airlift
	 * card)</li>
	 * <li>negotiate playerID (requires the diplomacy card)</li>
	 * <li>showmap</li>
	 * <li>end (end turn)</li>
	 * <li>exit (exit game)</li>
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
		case "showmap":
			showMap(l_player_clone);
			break;
		case "deploy":
			l_order = processDeployCommand(l_cmd_params[1], l_player_clone);
			break;
		case "advance":
			d_msg_model.setMessage(MessageType.Warning, "command '" + p_cmd + "' coming soon.");
			break;
		case "bomb":
			d_msg_model.setMessage(MessageType.Warning, "command '" + p_cmd + "' coming soon.");
			break;
		case "blockade":
			d_msg_model.setMessage(MessageType.Warning, "command '" + p_cmd + "' coming soon.");
			break;
		case "airlift":
			d_msg_model.setMessage(MessageType.Warning, "command '" + p_cmd + "' coming soon.");
			break;
		case "negotiate":
			d_msg_model.setMessage(MessageType.Warning, "command '" + p_cmd + "' coming soon.");
			break;
		case "end":
			processEndTurn(l_cmd_params[1], l_player_clone);
			break;
		case "help":
			GameStartupHelp();
			break;
		case "exit":
			d_next_phase = d_controller_factory.getGameEndPhase();
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
	 * @param p_end_params   the end turn params - should be blank
	 * @param l_player_clone the player object who wishes to end their turn
	 */
	private void processEndTurn(String p_end_params, IPlayerModel l_player) {
		if (!Utl.isEmpty(p_end_params)) {
			d_msg_model.setMessage(MessageType.Error, "invalid parameters for end command: '" + p_end_params + "'");
			return;
		}
		if (l_player.getReinforcements() > 0) {
			d_msg_model.setMessage(MessageType.Error, "Cannot end turn as player '" + l_player.getName()
					+ "' has " + l_player.getReinforcements() + " reinforcement(s) left to deploy.");
			return;
		}
		l_player.setDoneTurn(true);
	}

	/**
	 * process the deploy command.
	 * <ul>
	 * <li>deploy countryName num_reinforcements</li>
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
				d_msg_model.setMessage(MessageType.Error, "Invalid deploy country name '" + l_country_name + "'.");
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
			if (l_reinforcements >= Integer.MAX_VALUE || l_reinforcements < 1) {
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
		d_view.processMessage(MessageType.None, " - deploy countryID num_reinforcements");
		d_view.processMessage(MessageType.None, " - advance countrynamefrom countynameto numarmies");
		d_view.processMessage(MessageType.None, " - bomb countryID (requires bomb card)");
		d_view.processMessage(MessageType.None, " - blockade countryID (required blockade card)");
		d_view.processMessage(MessageType.None,
				" - airlift sourcecountryID targetcountryID numarmies (requires the airlift card)");
		d_view.processMessage(MessageType.None, " - negotiate playerID (requires the diplomacy card)");
		d_view.processMessage(MessageType.None, " - showmap");
		d_view.processMessage(MessageType.None, " - end (end turn)");
		d_view.processMessage(MessageType.None, " - exit (exit game)");
		d_view.processMessage(MessageType.None, " - help");
	}
}
