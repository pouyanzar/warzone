package soen6441.team01.warzone.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.model.*;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.CardType;
import soen6441.team01.warzone.view.ViewFactory;
import soen6441.team01.warzone.view.contracts.IGamePlayView;

/**
 * Warzone game play controller. Manages the coordination and progression of the
 * game play phase.
 */
public class IssueOrderController extends GamePlayController implements IGamePlayController, IGameplayOrderDatasource {
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
	private IGamePlayView d_view;
	private IAppMsg d_msg_model;
	private IGamePlayModel d_gameplay_model;
	private Phase d_next_phase = null;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public IssueOrderController(ControllerFactory p_controller_factory) throws Exception {
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
			d_msg_model.setMessage(MsgType.Error, "exception in GamePlayController: " + ex.getMessage());
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
		d_msg_model.setMessage(MsgType.None, "\n* issuing orders:");
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

		// initialize per round state
		for (IPlayerModel l_xplayer : l_players) {
			// l_xplayer.addCard(new Card(CardType.bomb)); // add cards for testing
			// l_xplayer.addCard(new Card(CardType.blockade)); // add cards for testing
			// l_xplayer.addCard(new Card(CardType.airlift)); // add cards for testing
			// l_xplayer.addCard(new Card(CardType.diplomacy)); // add cards for testing
			l_xplayer.clearAllDiplomacy();
		}

		// clone the map, players and their countries to simplify keeping track of
		// issuing orders before the execution phase and to isolate each players map
		// from one another.
		for (int idx1 = 0; idx1 < l_players.size(); idx1++) {
			ModelFactory l_cloned_sf_model = d_model_factory.getMapModel().deepCloneMap();
			IGamePlayModel l_gpm = l_cloned_sf_model.getNewGamePlayModel();
			for (int idx2 = 0; idx2 < l_players.size(); idx2++) {
				IPlayerModel l_player_clone = l_players.get(idx2).deepClonePlayer(l_cloned_sf_model);
				l_gpm.addPlayer(l_player_clone);
				if (idx2 == idx1) {
					l_player_clones.add(l_player_clone);
					l_queue.add(l_player_clone);
				}
			}
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

		// copy orders + necessary state information from cloned players to real players
		for (IPlayerModel l_player : l_players) {
			IPlayerModel l_pclone = Player.FindPlayer(l_player.getName(), l_player_clones);
			l_player.copyOrders(l_pclone);
		}

		if (d_next_phase == null) {
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
	public IOrder getOrder(IPlayerModel p_player_clone) throws Exception {
		IOrder l_order = null;

		while (d_next_phase == null && l_order == null && !p_player_clone.isDoneTurn()) {
			String l_cmd = d_view.getCommand("Gameplay " + p_player_clone.getName() + ">");
			l_order = processGamePlayCommand(l_cmd, p_player_clone);
		}

		return l_order;
	}

	/**
	 * Process the game play command:
	 * <ul>
	 * <li>deploy countryName num_reinforcements</li>
	 * <li>advance countrynamefrom countynameto numarmies</li>
	 * <li>bomb countryName (requires bomb card)</li>
	 * <li>blockade countryName (required blockade card)</li>
	 * <li>airlift sourcecountryName targetcountryName numarmies (requires the
	 * airlift card)</li>
	 * <li>negotiate playerName (requires the diplomacy card)</li>
	 * <li>showmap</li>
	 * <li>showcards</li>
	 * <li>end (end turn)</li>
	 * <li>exit (exit game)</li>
	 * <li>help</li>
	 * </ul>
	 * 
	 * 
	 * advance Italy France 5
	 * 
	 * 
	 * @param p_cmd          user full command
	 * @param p_player_clone the player to get and process commands for
	 * @return the order, null = order not fulfilled - try again
	 * @throws Exception unexpected error
	 */
	public IOrder processGamePlayCommand(String p_cmd, IPlayerModel p_player_clone) throws Exception {
		IOrder l_order = null;
		String l_cmd_params[] = Utl.getFirstWord(p_cmd);

		switch (l_cmd_params[0]) {
		case "showmap":
		case "sm":
			showMap(p_player_clone);
			break;
		case "deploy":
			l_order = processDeployCommand(l_cmd_params[1], p_player_clone);
			break;
		case "advance":
			l_order = processAdvanceCommand(l_cmd_params[1], p_player_clone);
			break;
		case "bomb":
			l_order = processBombCommand(l_cmd_params[1], p_player_clone);
			break;
		case "blockade":
			l_order = processBlockadeCommand(l_cmd_params[1], p_player_clone);
			break;
		case "airlift":
			l_order = processAirliftCommand(l_cmd_params[1], p_player_clone);
			break;
		case "negotiate":
			l_order = processNegotiateCommand(l_cmd_params[1], p_player_clone);
			break;
		case "showcards":
			showCards(p_player_clone);
			break;
		case "end":
			processEndTurn(l_cmd_params[1], p_player_clone);
			break;
		case "help":
			GameStartupHelp();
			break;
		case "exit":
			d_next_phase = d_controller_factory.getGameEndPhase();
			break;
		default:
			d_msg_model.setMessage(MsgType.Error, "invalid command '" + p_cmd + "'");
			break;
		}
		return l_order;
	}

	/**
	 * process the negotiate command.
	 * <ul>
	 * <li>negotiate playerName (requires the diplomacy card)</li>
	 * </ul>
	 * 
	 * @param p_negotiate_params the airlift parameters (just the parameters without
	 *                           the airlift command itself)
	 * @param p_player           the player object who wishes to advance
	 * @return the player's order or null if there was a problem creating the order
	 * @throws Exception unexpected error encountered
	 */
	private IOrder processNegotiateCommand(String p_negotiate_params, IPlayerModel p_player) throws Exception {

		IOrder l_order = null;
		String l_params[] = Utl.getFirstWord(p_negotiate_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MsgType.Error, "Invalid negotiate command, no options specified");
			return null;
		}
		try {
			// parse the target player
			String l_target_player = l_params[0];

			IGamePlayModel l_gply = p_player.getPlayerModelFactory().getGamePlayModel();
			IPlayerModel l_tplayer = Player.FindPlayer(l_target_player, l_gply.getPlayers());
			if (l_tplayer == null) {
				d_msg_model.setMessage(MsgType.Error, "Player '" + l_target_player + "' does not exist");
				return null;
			}
			if (!Utl.isEmpty(l_params[1])) {
				d_msg_model.setMessage(MsgType.Error, "Invalid negotiate option '" + l_params[1] + "'");
				return null;
			}

			// create the diplomacy order object. note that the order will throw an
			// exception
			// if it's not valid
			l_order = new OrderDiplomacy(p_player, l_tplayer);

			// execute the order on the cloned player to 1) see if it's valid 2) set the
			// state of the cloned player for the next command
			l_order.execute();

			String l_msg = "negotiate order successful";
			d_msg_model.setMessage(MsgType.Informational, l_msg);
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, ex.getMessage());
			return null;
		}
		return l_order;
	}

	/**
	 * process the Airlift command.
	 * <ul>
	 * <li>airlift sourcecountryName targetcountryName numarmies (requires the
	 * airlift card)</li>
	 * </ul>
	 * 
	 * @param p_airlift_params the airlift parameters (just the parameters without
	 *                         the airlift command itself)
	 * @param p_player         the player object who wishes to advance
	 * @return the player's order or null if there was a problem creating the order
	 * @throws Exception unexpected error encountered
	 */
	private IOrder processAirliftCommand(String p_airlift_params, IPlayerModel p_player) throws Exception {

		IOrder l_order = null;
		String l_params[] = Utl.getFirstWord(p_airlift_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MsgType.Error, "Invalid airlift command, no options specified");
			return null;
		}
		try {
			// parse the source country
			String l_source_country = l_params[0];

			// parse the target country
			l_params = Utl.getFirstWord(l_params[1]);
			String l_target_country = l_params[0];

			// parse the numarmies
			l_params = Utl.getFirstWord(l_params[1]);
			String l_num_armies_str = l_params[0];
			int l_num_armies = Utl.convertToInteger(l_num_armies_str);
			if (l_num_armies >= Integer.MAX_VALUE || l_num_armies < 1) {
				d_msg_model.setMessage(MsgType.Error, "Invalid number of armies '" + l_num_armies_str + "'.");
				return null;
			}
			if (!Utl.isEmpty(l_params[1])) {
				d_msg_model.setMessage(MsgType.Error, "Invalid airlift option '" + l_params[1] + "'");
				return null;
			}

			// create the airlift order object. note that the order will throw an exception
			// if it's not valid
			l_order = new OrderAirlift(l_source_country, l_target_country, l_num_armies, p_player);

			// execute the order on the cloned player to 1) see if it's valid 2) set the
			// state of the cloned player for the next command
			l_order.execute();

			String l_msg = "airlift order successful";
			d_msg_model.setMessage(MsgType.Informational, l_msg);
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, ex.getMessage());
			return null;
		}
		return l_order;
	}

	/**
	 * process the blockade command (requires blockade card):<br>
	 * blockade countryName
	 * 
	 * @param p_blockade_params the remaining parameters for the blockade command
	 * @param p_player          the player for which this command is for
	 */
	private IOrder processBlockadeCommand(String p_blockade_params, IPlayerModel p_player) {
		IOrder l_order = null;
		String l_params[] = Utl.getFirstWord(p_blockade_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MsgType.Error, "Invalid blockade command, no options specified");
			return null;
		}

		try {
			// parse the countryName
			String l_country_name = l_params[0];
			if (!Utl.isValidMapName(l_country_name)) {
				d_msg_model.setMessage(MsgType.Error, "Invalid country name '" + l_country_name + "'.");
				return null;
			}
			if (!Utl.isEmpty(l_params[1])) {
				d_msg_model.setMessage(MsgType.Error, "Invalid blockade option '" + l_params[1] + "'");
				return null;
			}

			ICountryModel l_country_to_blockade = Country.findCountry(l_country_name,
					p_player.getPlayerModelFactory().getMapModel().getCountries());
			if (l_country_to_blockade == null) {
				d_msg_model.setMessage(MsgType.Error,
						"Cannot blockade '" + l_country_name + "' since it is not a country");
				return null;
			}

			// create the bomb order
			l_order = new OrderBlockade(p_player, l_country_to_blockade);

			// execute the order on the cloned player to 1) see if it's valid 2) set the
			// state of the cloned player for the next command
			l_order.execute();
			String l_msg = "blockade order successful";
			d_msg_model.setMessage(MsgType.Informational, l_msg);
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, ex.getMessage());
			return null;
		}
		return l_order;
	}

	/**
	 * process the advance command.
	 * <ul>
	 * <li>advance countrynamefrom countynameto numarmies</li>
	 * </ul>
	 * 
	 * @param p_advance_params the advance parameters (just the parameters without
	 *                         the advance command itself)
	 * @param p_player         the player object who wishes to advance
	 * @return the player's order or null if there was a problem creating the order
	 * @throws Exception unexpected error encountered
	 */
	private IOrder processAdvanceCommand(String p_advance_params, IPlayerModel p_player) throws Exception {

		IOrder l_order = null;
		String l_params[] = Utl.getFirstWord(p_advance_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MsgType.Error, "Invalid advance command, no options specified");
			return null;
		}
		try {
			// parse the countrynamefrom
			String l_country_name_from = l_params[0];
			ICountryModel l_from_countries = Country.findCountry(l_country_name_from, p_player.getPlayerCountries());
			if (l_from_countries == null) {
				d_msg_model.setMessage(MsgType.Error, l_country_name_from + " is not owned by " + p_player.getName());
				return null;
			}

			// parse the countynameto
			l_params = Utl.getFirstWord(l_params[1]);
			String l_country_name_to = l_params[0];
			ICountryModel l_to_countries = Country.findCountry(l_country_name_to, l_from_countries.getNeighbors());
			if (l_to_countries == null) {
				d_msg_model.setMessage(MsgType.Error,
						l_country_name_from + " is not a neighbor of " + l_country_name_to);
				return null;
			}

			// parse the numarmies
			l_params = Utl.getFirstWord(l_params[1]);
			String l_numarmies_str = l_params[0];
			int l_numarmies = Utl.convertToInteger(l_numarmies_str);
			if (l_numarmies >= Integer.MAX_VALUE || l_numarmies < 1) {
				d_msg_model.setMessage(MsgType.Error, "Invalid number of armies '" + l_numarmies_str + "'.");
				return null;
			}
			if (!Utl.isEmpty(l_params[1])) {
				d_msg_model.setMessage(MsgType.Error, "Invalid advance option '" + l_params[1] + "'");
				return null;
			}

			// create the advance order object. note that the order will throw an exception
			// if it's not valid
			l_order = new OrderAdvance(p_player, l_from_countries, l_to_countries, l_numarmies);

			// execute the order on the cloned player to 1) see if it's valid 2) set the
			// state of the cloned player for the next command
			l_order.execute();

			String l_msg = "advance order successful";
			d_msg_model.setMessage(MsgType.Informational, l_msg);
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, ex.getMessage());
			return null;
		}
		return l_order;
	}

	/**
	 * process the bomb command (requires bomb card):<br>
	 * bomb countryName
	 * 
	 * @param p_bomb_params the remaining parameters for the bomb command
	 * @param p_player      the player for which this command is for
	 */
	private IOrder processBombCommand(String p_bomb_params, IPlayerModel p_player) {
		IOrder l_order = null;
		String l_params[] = Utl.getFirstWord(p_bomb_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MsgType.Error, "Invalid bomb command, no options specified");
			return null;
		}

		try {
			// parse the countryName
			String l_country_name = l_params[0];
			if (!Utl.isValidMapName(l_country_name)) {
				d_msg_model.setMessage(MsgType.Error, "Invalid country name '" + l_country_name + "'.");
				return null;
			}
			if (!Utl.isEmpty(l_params[1])) {
				d_msg_model.setMessage(MsgType.Error, "Invalid bomb option '" + l_params[1] + "'");
				return null;
			}

			ICountryModel l_country_to_bomb = Country.findCountry(l_country_name,
					p_player.getPlayerModelFactory().getMapModel().getCountries());
			if (l_country_to_bomb == null) {
				d_msg_model.setMessage(MsgType.Error, "Cannot bomb '" + l_country_name + "' since it is not a country");
				return null;
			}

			// create the bomb order
			l_order = new OrderBomb(p_player, l_country_to_bomb);

			// execute the order on the cloned player to 1) see if it's valid 2) set the
			// state of the cloned player for the next command
			l_order.execute();
			String l_msg = "bomb order successful";
			d_msg_model.setMessage(MsgType.Informational, l_msg);
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, ex.getMessage());
			return null;
		}
		return l_order;
	}

	/**
	 * process the end command.
	 * <ul>
	 * <li>deploy countryID num_reinforcements</li>
	 * </ul>
	 * 
	 * @param p_end_params   the end turn params - should be blank
	 * @param l_player_clone the player object who wishes to end their turn
	 */
	private void processEndTurn(String p_end_params, IPlayerModel p_player) {
		if (!Utl.isEmpty(p_end_params)) {
			d_msg_model.setMessage(MsgType.Error, "invalid parameters for end command: '" + p_end_params + "'");
			return;
		}
		if (p_player.getReinforcements() > 0) {
			d_msg_model.setMessage(MsgType.Error, "Cannot end turn as player '" + p_player.getName() + "' has "
					+ p_player.getReinforcements() + " reinforcement(s) left to deploy.");
			return;
		}
		p_player.setDoneTurn(true);
	}

	/**
	 * process the deploy command.
	 * <ul>
	 * <li>deploy countryName num_reinforcements</li>
	 * </ul>
	 * 
	 * @param p_deploy_params the loadmap parameters (just the parameters without
	 *                        the loadmap command itself)
	 * @param p_player_clone  the player object who wishes to deploy
	 * @return the player's order or null if there was a problem creating the order
	 * @throws Exception unexpected error encountered
	 */
	private IOrder processDeployCommand(String p_deploy_params, IPlayerModel p_player_clone) throws Exception {
		IOrder l_order = null;
		String l_params[] = Utl.getFirstWord(p_deploy_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MsgType.Error, "Invalid deploy command, no options specified");
			return null;
		}
		try {
			// parse the countryID
			String l_country_name = l_params[0];
			if (!Utl.isValidMapName(l_country_name)) {
				d_msg_model.setMessage(MsgType.Error, "Invalid deploy country name '" + l_country_name + "'.");
				return null;
			}

			// parse the num_reinforcements
			l_params = Utl.getFirstWord(l_params[1]);
			String l_reinforcements_str = l_params[0];
			if (Utl.isEmpty(l_reinforcements_str)) {
				d_msg_model.setMessage(MsgType.Error,
						"Invalid deploy command, number of reinforcements not specified.");
				return null;
			}

			int l_reinforcements = Utl.convertToInteger(l_reinforcements_str);
			if (l_reinforcements >= Integer.MAX_VALUE || l_reinforcements < 1) {
				d_msg_model.setMessage(MsgType.Error,
						"Invalid number of deploy reinforcements '" + l_reinforcements_str + "'.");
				return null;
			}
			l_order = new OrderDeploy(l_country_name, l_reinforcements, p_player_clone);

			// execute the order on the cloned player to 1) see if it's valid 2) set the
			// state of the cloned player for the next command
			l_order.execute();
			String l_msg = "Deploy order successful.\n" + p_player_clone.getReinforcements()
					+ " reinforcement(s) left for " + p_player_clone.getName() + " to allocate.";
			d_msg_model.setMessage(MsgType.Informational, l_msg);
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, ex.getMessage());
			return null;
		}
		return l_order;
	}

	/**
	 * Show a map of the specified player
	 * 
	 * @param p_player the player whom wants to see a map of their owned countries
	 *                 and their neighbors.
	 */
	private void showMap(IPlayerModel p_player) {
		ArrayList<ICountryModel> l_countries = p_player.getPlayerCountries();
		for (ICountryModel l_country : l_countries) {
			d_view.showCountry(l_country);
		}
	}

	/**
	 * Show the cards held by specified player
	 * 
	 * @param p_player the player whom wants to see their cards
	 */
	private void showCards(IPlayerModel p_player) {
		d_view.showCards(p_player);
	}

	/**
	 * used mainly for testing
	 * 
	 * @return the next phase that will be invoked
	 */
	public Phase getNextPhase() {
		return d_next_phase;
	}

	/**
	 * Asks the view to display the list of map editor commands along with their
	 * syntax.
	 * 
	 * @param p_view the map editor view being used by the controller
	 */
	private void GameStartupHelp() {
		d_view.displayGamePlayBanner();
		d_view.processMessage(MsgType.None, "Gameplay commands:");
		d_view.processMessage(MsgType.None, " - deploy countryName num_reinforcements");
		d_view.processMessage(MsgType.None, " - advance countrynamefrom countynameto numarmies");
		d_view.processMessage(MsgType.None, " - bomb countryName (requires bomb card)");
		d_view.processMessage(MsgType.None, " - blockade countryName (requires blockade card)");
		d_view.processMessage(MsgType.None,
				" - airlift sourcecountryID targetcountryID numarmies (requires the airlift card)");
		d_view.processMessage(MsgType.None, " - negotiate playerName (requires the diplomacy card)");
		d_view.processMessage(MsgType.None, " - showmap");
		d_view.processMessage(MsgType.None, " - showcards");
		d_view.processMessage(MsgType.None, " - end (end turn)");
		d_view.processMessage(MsgType.None, " - exit (exit game)");
		d_view.processMessage(MsgType.None, " - help");
	}
}
