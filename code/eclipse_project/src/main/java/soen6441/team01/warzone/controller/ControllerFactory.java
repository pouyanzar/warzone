package soen6441.team01.warzone.controller;

import java.io.Serializable;
import java.util.ArrayList;

import soen6441.team01.warzone.controller.contracts.*;
import soen6441.team01.warzone.model.*;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.view.ViewFactory;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * controllers.
 */
public class ControllerFactory implements Serializable{
	private static final long serialVersionUID = 1L;
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;

	private IMapEditorController d_map_editor_controller = null;
	private IGameStartupController d_startup_controller = null;
	private ITournamentController d_tournament_controller = null;
	private IGameEndController d_gameend_controller = null;
	private SingleGameController d_game_play_controller = null;
	private ReinforcementController d_reinforcement_controller = null;
	private IssueOrderController d_issue_order_controller = null;
	private OrderExecController d_order_exec_controller = null;
	private Phase d_gameplay_phase = null;

	/**
	 * Constructor with views defined. Views passed as null will result in the
	 * default view being passed back.
	 * 
	 * @param p_models       the software factory for all the game models
	 * @param p_view_factory the software factory for all the views
	 */
	public ControllerFactory(ModelFactory p_models, ViewFactory p_view_factory) {
		d_model_factory = p_models;
		d_view_factory = p_view_factory;
	}

	/**
	 * Helper function that creates a software factory that holds the Warzone
	 * controller objects for the console version of the game.
	 *
	 * @param p_model the model software factory to use
	 * @param p_view  the view software factory to use
	 * @return newly create Warzone view software factory
	 */
	public static ControllerFactory CreateWarzoneBasicConsoleGameControllers(ModelFactory p_model, ViewFactory p_view) {
		ControllerFactory l_controller = new ControllerFactory(p_model, p_view);
		return l_controller;
	}

	/**
	 * Create a Map Edit controller
	 * 
	 * @return map edit controller
	 * @throws Exception unexpected error
	 */
	public IMapEditorController getNewMapEditorController() throws Exception {
		d_map_editor_controller = new MapEditorController(this);
		return d_map_editor_controller;
	}

	/**
	 * @return the currently defined Model Software Factory
	 */
	public ModelFactory getModelFactory() {
		return d_model_factory;
	}

	/**
	 * @return the currently defined View Software Factory
	 */
	public ViewFactory getViewFactory() {
		return d_view_factory;
	}

	/**
	 * return the existing Map Edit controller
	 * 
	 * @return map edit controller
	 * @throws Exception unexpected error
	 */
	public IMapEditorController getMapEditorController() throws Exception {
		if (d_map_editor_controller == null) {
			getNewMapEditorController();
		}
		return d_map_editor_controller;
	}

	/**
	 * return the existing Map Edit controller
	 * 
	 * @return map edit controller
	 * @throws Exception unexpected error
	 */
	public Phase getMapEditorPhase() throws Exception {
		return (Phase) getMapEditorController();
	}

	/**
	 * return the Game End phase controller
	 * 
	 * @return map edit controller
	 * @throws Exception unexpected error
	 */
	public Phase getGameEndPhase() throws Exception {
		return (Phase) getGameEndController();
	}

	/**
	 * return the Game Startup phase controller - single game mode
	 * 
	 * @return map edit controller
	 * @throws Exception unexpected error
	 */
	public Phase getGameStartupPhase() throws Exception {
		return (Phase) getGameStartupController();
	}

	/**
	 * Create or return the current GameEndController
	 * 
	 * @return game end controller
	 * @throws Exception unexpected error
	 */
	public IGameEndController getGameEndController() throws Exception {
		if (d_gameend_controller == null) {
			d_gameend_controller = new GameEndController(this);
		}
		return d_gameend_controller;
	}

	/**
	 * Create or return the current GameStartupController
	 * 
	 * @return game startup controller
	 * @throws Exception unexpected error
	 */
	public IGameStartupController getGameStartupController() throws Exception {
		if (d_startup_controller == null) {
			d_startup_controller = new GameStartupController(this);
		}
		return d_startup_controller;
	}

	/**
	 * Create new GameStartupController
	 * 
	 * @return game end controller
	 * @throws Exception unexpected error
	 */
	public IGameStartupController getNewGameStartupController() throws Exception {
		d_startup_controller = null;
		return getGameStartupController();
	}

	/**
	 * Create or return the current GamePlayController
	 * 
	 * @return game play controller
	 * @throws Exception unexpected error
	 */
	public ISingleGameController getSingleGameController() throws Exception {
		if (d_game_play_controller == null) {
			d_game_play_controller = new SingleGameController(this);
		}
		return d_game_play_controller;
	}

	/**
	 * return the current GameTournamentController
	 * 
	 * @return game tournament controller
	 * @throws Exception unexpected error
	 */
	public ITournamentController getGameTournamentController() throws Exception {
		return d_tournament_controller;
	}

	/**
	 * Create the current GameTournamentController
	 * 
	 * @param p_map_filenames   the list of map filenames
	 * @param p_strategies      the list of player strategies
	 * @param p_number_of_games the number of games to play for each map
	 * @param p_max_turns       the maximum nuber of turns to play before game is
	 *                          stopped
	 * @return the newly created controller
	 * @throws Exception unexpected error
	 */
	public ITournamentController createGameTournamentController(ArrayList<String> p_map_filenames,
			ArrayList<String> p_strategies, int p_number_of_games, int p_max_turns) throws Exception {
		d_tournament_controller = new TournamentController(this, p_map_filenames, p_strategies, p_number_of_games,
				p_max_turns);
		return d_tournament_controller;
	}

	/**
	 * return the current GamePlayController
	 * 
	 * @return game play controller
	 * @throws Exception unexpected error
	 */
	public Phase getGamePlayPhase() throws Exception {
		return d_gameplay_phase;
	}

	/**
	 * set the current GamePlayController phase (either Single or Tournament game)
	 * 
	 * @param p_gameplay_phase the phase to set as the gameplay phase
	 * @throws Exception unexpected error
	 */
	public void setGamePlayPhase(Phase p_gameplay_phase) throws Exception {
		d_gameplay_phase = p_gameplay_phase;
	}

	/**
	 * return the current ReinforcementController
	 * 
	 * @return reinforcement controller as a phase object
	 * @throws Exception unexpected error
	 */
	public Phase getReinforcementPhase() throws Exception {
		if (d_reinforcement_controller == null) {
			d_reinforcement_controller = new ReinforcementController(this);
		}
		return (Phase) d_reinforcement_controller;
	}

	/**
	 * return the current ReinforcementController
	 * 
	 * @return reinforcement controller as a phase object
	 * @throws Exception unexpected error
	 */
	public Phase getIssueOrderPhase() throws Exception {
		if (d_issue_order_controller == null) {
			d_issue_order_controller = new IssueOrderController(this);
		}
		return (Phase) d_issue_order_controller;
	}

	/**
	 * Create or return the current GamePlayController
	 * 
	 * @return instance of IssueOrderController
	 * @throws Exception unexpected error
	 */
	public IssueOrderController getIssueOrderController() throws Exception {
		if (d_issue_order_controller == null) {
			d_issue_order_controller = new IssueOrderController(this);
		}
		return d_issue_order_controller;
	}

	/**
	 * return the current ReinforcementController
	 * 
	 * @return reinforcement controller as a phase object
	 * @throws Exception unexpected error
	 */
	public Phase getOrderExecPhase() throws Exception {
		if (d_order_exec_controller == null) {
			d_order_exec_controller = new OrderExecController(this);
		}
		return (Phase) d_order_exec_controller;
	}

	/**
	 * Use the current GamePlayController as the order datasource
	 * 
	 * @return game play controller
	 * @throws Exception unexpected error
	 */
	public IGameplayOrderDatasource getGamePlayOrderDatasource() throws Exception {
		if (d_issue_order_controller == null) {
			d_issue_order_controller = new IssueOrderController(this);
		}
		return d_issue_order_controller;
	}
}