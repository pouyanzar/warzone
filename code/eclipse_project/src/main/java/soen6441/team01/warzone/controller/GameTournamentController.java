package soen6441.team01.warzone.controller;

import java.util.ArrayList;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.IGameTournamentController;
import soen6441.team01.warzone.model.*;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.GameState;
import soen6441.team01.warzone.view.ViewFactory;
import soen6441.team01.warzone.view.contracts.IGameTournamentView;

/**
 * Warzone game tournament controller. Manages the coordination and progression
 * of a game tournament phase.
 */
public class GameTournamentController extends Phase implements IGameTournamentController {
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
	private IGameTournamentView d_view;
	private IAppMsg d_msg;
	private IGamePlayModel d_gameplay;
	private ArrayList<String> d_map_filenames;
	private ArrayList<String> d_strategies;
	private int d_number_of_games;
	private int d_max_turns;
	private boolean d_exit_tournament_sw;

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
	public GameTournamentController(ControllerFactory p_controller_factory, ArrayList<String> p_map_filenames,
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
		try {
			nextPhase(d_controller_factory.getGameEndPhase());
			d_view.activate();
			d_view.displayTournamentGameplayBanner();

			ArrayList<String> l_val_out = new ArrayList<String>();
			if (!validateTournamentParameters(l_val_out)) {
				d_view.displayTournamentErrors(l_val_out);
				nextPhase(d_controller_factory.getGameStartupPhase());
			} else {
				startTournament();
			}
		} catch (Exception ex) {
			Utl.lprintln("Fatal error during game tournament, exception: " + ex.getMessage());
		}

		if (d_view != null) {
			d_view.deactivate();
		}
	}

	/**
	 * start processing the tournament
	 * 
	 * @throws Exception unexpected error
	 */
	private void startTournament() throws Exception {

		d_view.displayTournamentParameters(d_map_filenames, d_strategies, d_number_of_games, d_max_turns);
		// new views will be created as needed - disable our view otherwise we'll see
		// messages twice
		d_view.deactivate();
		d_view = null;

		d_gameplay = d_model_factory.getNewGamePlayModel();
		d_gameplay.setMap(d_model_factory.getMapModel());

		d_exit_tournament_sw = false;

		try {
			for (String l_map_filename : d_map_filenames) {
				playGame(l_map_filename, d_strategies, d_max_turns);
			}
		} catch (Exception ex) {
			d_msg.setMessage(MsgType.Error,
					"Error during tournament play, aborting tournament, exception: " + ex.getMessage());
		}

		d_msg.setMessage(MsgType.None, "Tournament over.");

		nextPhase(d_controller_factory.getGameEndPhase());
	}

	/**
	 * 
	 * @param p_map_filename
	 * @param p_strategies2
	 * @param p_number_of_games
	 * @param p_max_turns
	 * @throws Exception
	 */
	private void playGame(String p_map_filename, ArrayList<String> p_strategies, int p_max_turns) throws Exception {
		// create new world map
		ModelFactory l_new_factory_model = new ModelFactory(d_msg);
		IMapModel l_map = Map.loadMapFromFile(p_map_filename, l_new_factory_model);
		l_new_factory_model.setMapModel(l_map);

		// create players
		IGamePlayModel l_gpm = l_new_factory_model.getNewGamePlayModel();
		int l_player_num = 1;
		for (String l_strategy_str : p_strategies) {
			IPlayerModel l_player = new Player("Player" + l_player_num++, l_new_factory_model);
			IPlayerStrategy l_strategy = getTournamentStrategy(l_strategy_str, l_player);
			l_player.setStrategy(l_strategy);
			l_gpm.addPlayer(l_player);
		}

		// assign countries
		ControllerFactory l_controller_factory = new ControllerFactory(l_new_factory_model, d_view_factory);
		l_gpm.assignCountries();
		l_gpm.setGameState(GameState.GamePlay);

		// start and execute a game starting at gameplay
		GameEngine l_game_engine = new GameEngine(l_new_factory_model, d_view_factory, l_controller_factory);
		l_new_factory_model.setGameEngine(l_game_engine);
		l_controller_factory.getGamePlayController().setMaxRounds(p_max_turns);
		// l_game_engine.setNextPhase(l_controller_factory.getReinforcementPhase());
		l_game_engine.setNextPhase(l_controller_factory.getGamePlayPhase());
		l_game_engine.startNewGame();
		// john - *ici* - need to fix end game phase in gameplaycontroller
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
}
