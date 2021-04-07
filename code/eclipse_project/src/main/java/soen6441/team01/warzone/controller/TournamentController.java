package soen6441.team01.warzone.controller;

import java.util.ArrayList;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.ITournamentController;
import soen6441.team01.warzone.model.*;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.GameState;
import soen6441.team01.warzone.view.ViewFactory;
import soen6441.team01.warzone.view.contracts.IGameTournamentView;

/**
 * Warzone game tournament controller. Manages the coordination and progression
 * of a game tournament phase.
 */
public class TournamentController extends Phase implements ITournamentController {
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
	private IGameTournamentView d_view;
	private IAppMsg d_msg;
	private ArrayList<String> d_map_filenames;
	private ArrayList<String> d_strategies;
	private int d_number_of_games;
	private int d_max_turns;
	private boolean d_start_tournament_sw = true;
	private int d_current_map_idx;
	private int d_current_game_idx;
	private int d_current_round;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @param p_map_filenames      the list of map filenames
	 * @param p_strategies         the list of player strategies
	 * @param p_number_of_games    the number of games to play for each map
	 * @param p_max_turns          the maximum nuber of turns to play before game is
	 *                             stopped
	 * @throws Exception unexpected error
	 */
	public TournamentController(ControllerFactory p_controller_factory, ArrayList<String> p_map_filenames,
			ArrayList<String> p_strategies, int p_number_of_games, int p_max_turns) throws Exception {
		super(p_controller_factory.getModelFactory().getGameEngine());
		d_controller_factory = p_controller_factory;
		d_model_factory = p_controller_factory.getModelFactory();
		d_view_factory = p_controller_factory.getViewFactory();
		d_view = d_view_factory.getGameTournamentConsoleView(this);
		d_msg = d_model_factory.getUserMessageModel();
		d_map_filenames = p_map_filenames;
		d_strategies = p_strategies;
		d_number_of_games = p_number_of_games;
		d_max_turns = p_max_turns;
	}

	/**
	 * invoked by the game engine as the main part of the tournament phase
	 * 
	 * @throws Exception unexpected error
	 */
	@Override
	public void execPhase() throws Exception {
		try {
			if (d_start_tournament_sw) {
				startTournament();
				d_start_tournament_sw = false;
			}
			if (isEndOfCurrentGame()) {
				if (isEndOfTournament()) {
					processEndOfTournament();
				} else {
					startNextGame();
					nextPhase(this);
				}
			} else {
				d_msg.setMessage(MsgType.None,
						"\n-- game " + (d_current_game_idx + 1) + ", round " + (++d_current_round) + " --");
				nextPhase(d_controller_factory.getReinforcementPhase());
			}
		} catch (Exception ex) {
			endTournament(MsgType.Error, "exception encountered during game tournament: " + ex.getMessage(), null);
		}
	}

	/**
	 * Process the end of the tournament
	 * 
	 * @throws Exception unexpected error
	 */
	private void processEndOfTournament() throws Exception {
		endTournament(MsgType.None, "\n*** End of Tournament ***", d_controller_factory.getGameEndPhase());
	}

	/**
	 * checks if the it's the end of the tournament
	 * 
	 * @return true if the tournament has ended; otherwise false
	 */
	private boolean isEndOfTournament() {
		if (d_current_map_idx >= d_map_filenames.size()) {
			return true;
		}
		return false;
	}

	/**
	 * checks if the it's the end of the current game
	 * 
	 * @return true if the current game has ended; otherwise false
	 */
	private boolean isEndOfCurrentGame() {
		if (d_current_round >= d_max_turns) {
			return true;
		}
		return false;
	}

	/**
	 * start processing the tournament
	 * 
	 * @throws Exception unexpected error
	 */
	private void startTournament() throws Exception {
		nextPhase(d_controller_factory.getGameEndPhase());
		d_view.activate();
		d_view.displayTournamentGameplayBanner();

		ArrayList<String> l_val_out = new ArrayList<String>();
		if (!validateTournamentParameters(l_val_out)) {
			d_view.displayTournamentErrors(l_val_out);
			endTournament(MsgType.Error, "tournament aborted", d_controller_factory.getMapEditorPhase());
			return;
		} else {
			d_view.displayTournamentParameters(d_map_filenames, d_strategies, d_number_of_games, d_max_turns);
		}

		d_current_map_idx = -1;
		d_current_game_idx = d_number_of_games;
		d_current_round = d_max_turns + 1; // forces an end of current game
	}

	/**
	 * Start a new tournament game
	 * 
	 * @throws Exception unexpected error
	 */
	private void startNextGame() throws Exception {
		d_current_game_idx++;
		if (d_current_game_idx >= d_number_of_games) {
			// completed all the games for the current map. start on the next map
			d_current_map_idx++;
			if (d_current_map_idx >= d_map_filenames.size()) {
				// we're done with the tournament
				return;
			}
			d_current_game_idx = 0;
		}
		d_current_round = 0;

		String l_map_filename = d_map_filenames.get(d_current_map_idx);
		d_msg.setMessage(MsgType.None,
				"\n== starting game " + (d_current_game_idx + 1) + " on map " + l_map_filename + " ==\n");

		// create new map
		IMapModel l_map = Map.loadMapFromFile(l_map_filename, d_model_factory);
		d_model_factory.setMapModel(l_map);

		// create players
		IGamePlayModel l_gpm = d_model_factory.getNewGamePlayModel();
		int l_player_num = 1;
		for (String l_strategy_str : d_strategies) {
			IPlayerModel l_player = new Player("Player" + l_player_num++, d_model_factory);
			IPlayerStrategy l_strategy = getTournamentStrategy(l_strategy_str, l_player);
			l_player.setStrategy(l_strategy);
			l_gpm.addPlayer(l_player);
		}

		// assign countries to players
		l_gpm.assignCountries();
		l_gpm.setGameState(GameState.GamePlay);

		return;
	}

	/**
	 * Validate that the tournament parameters are valid. <br>
	 * 
	 * @param p_val_out validation output status information suitable for output to
	 *                  the user
	 * @return true if successful otherwise false
	 */
	public boolean validateTournamentParameters(ArrayList<String> p_val_out) {
		boolean l_reply = true;

		p_val_out.add(Utl.plural(d_map_filenames.size(), "Map:", "Maps:"));
		for (String l_map : d_map_filenames) {
			try {
				validateTournamentMapFile(l_map);
				p_val_out.add("   " + l_map);

			} catch (Exception ex) {
				p_val_out.add("   " + l_map + " - invalid: " + ex.getMessage());
				l_reply = false;
			}
		}

		p_val_out.add(Utl.plural(d_strategies.size(), "Strategy:", "Strategies:"));
		for (String l_str : d_strategies) {
			try {
				if (getTournamentStrategy(l_str, null) != null) {
					p_val_out.add("   " + l_str);
				} else {
					p_val_out.add("   " + l_str + " - invalid");
					l_reply = false;
				}
			} catch (Exception ex) {
				p_val_out.add("   " + l_str + " - error: " + ex.getMessage());
				l_reply = false;
			}
		}

		try {
			validateNumberOfGames(d_number_of_games);
			p_val_out.add("Number of games for each map        : " + d_number_of_games);
		} catch (Exception ex) {
			p_val_out.add("Number of games for each map        : " + d_number_of_games + " error: " + ex.getMessage());
			l_reply = false;
		}

		try {
			validateMaxTurnsPerGame(d_max_turns);
			p_val_out.add("Maximum number of turns for each map: " + d_max_turns);
		} catch (Exception ex) {
			p_val_out.add("Maximum number of turns for each map: " + d_max_turns + " error: " + ex.getMessage());
			l_reply = false;
		}

		return l_reply;
	}

	/**
	 * validate that a given map file can be loaded. throws an exception if it's
	 * invalid otherwise returns successfully.
	 * 
	 * @param p_map_filename filename of a file containing a valid map
	 * @throws Exception if the map is invalid
	 */
	public static void validateTournamentMapFile(String p_map_filename) throws Exception {
		Map.loadMapFromFile(p_map_filename, ModelFactory.createWarzoneBasicConsoleGameModels());
	}

	/**
	 * create a tournament player strategy based on the supplied string. note that
	 * human players are not valid since tournaments do not allow user interaction
	 * once started.
	 * 
	 * @param p_strategy the required player strategy. one of: aggressive,
	 *                   benevolent, random, cheater
	 * @param p_player   the player to set the strategy to
	 * @return the newly created strategy
	 * @throws Exception unexpected error
	 */
	public static IPlayerStrategy getTournamentStrategy(String p_strategy, IPlayerModel p_player) throws Exception {
		String l_strategy = p_strategy.trim().toLowerCase();
		IAppMsg l_msg_model = null;

		if (p_player != null) {
			l_msg_model = p_player.getPlayerModelFactory().getUserMessageModel();
		}

		switch (l_strategy) {
		case "aggressive":
			return new PlayerAggressiveStrategy(p_player, l_msg_model);
		case "benevolent":
			return new PlayerBenevolentStrategy(p_player, l_msg_model);
		case "random":
			return new PlayerRandomStrategy(p_player, l_msg_model);
		case "cheater":
			return new PlayerCheaterStrategy(p_player, l_msg_model);
		}

		return null;
	}

	/**
	 * validate that the number of games is between 1 and 99.
	 * 
	 * @param p_num_games the number of games to check
	 * @throws Exception if the number of games is invalid
	 */
	public static void validateNumberOfGames(int p_num_games) throws Exception {
		if (p_num_games < 1 || p_num_games > 99) {
			throw new Exception("invalid number of games should be between 1 and 99");
		}
	}

	/**
	 * validate that the maximum number of turns per game is between 1 and 999.
	 * 
	 * @param p_max_turns the number of games to check
	 * @throws Exception if the max number of turns per game is invalid
	 */
	public static void validateMaxTurnsPerGame(int p_max_turns) throws Exception {
		if (p_max_turns < 1 || p_max_turns > 999) {
			throw new Exception("invalid number of max turns per game, should be between 1 and 999");
		}
	}

	/**
	 * call this method to end the tournament. it sets up the closing message and
	 * well as the next phase and does some necessary cleanup
	 * 
	 * @param p_msg_type   the end message type
	 * @param p_msg        the end message to display
	 * @param p_next_phase the next phase to invoke
	 * @throws Exception unexpected error
	 */
	private void endTournament(MsgType p_msg_type, String p_msg, Phase p_next_phase) throws Exception {
		d_view.processMessage(p_msg_type, p_msg);
		if (p_next_phase == null) {
			p_next_phase = d_controller_factory.getGameEndPhase();
		}
		nextPhase(p_next_phase);
		if (d_view != null) {
			d_view.deactivate();
		}
	}
}
