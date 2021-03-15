package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.controller.contracts.*;
import soen6441.team01.warzone.model.*;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.view.SoftwareFactoryView;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * controllers.
 */
public class SoftwareFactoryController {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;

	private IMapEditorController d_map_editor_controller = null;
	private IGameStartupController d_startup_controller = null;
	private IGameEndController d_gameend_controller = null;
	private GamePlayController d_game_play_controller = null;

	/**
	 * Constructor with views defined. Views passed as null will result in the
	 * default view being passed back.
	 * 
	 * @param p_models       the software factory for all the game models
	 * @param p_view_factory the software factory for all the views
	 */
	public SoftwareFactoryController(SoftwareFactoryModel p_models, SoftwareFactoryView p_view_factory) {
		d_model_factory = p_models;
		d_view_factory = p_view_factory;
	}

	/**
	 * Helper function that creates a software factory that holds the Warzone
	 * controller objects for the console version of the game.
	 *
	 * @param p_model the model software factory to use
	 * @param p_view the view software factory to use
	 * @return newly create Warzone view software factory
	 */
	public static SoftwareFactoryController CreateWarzoneBasicConsoleGameControllers(SoftwareFactoryModel p_model,
			SoftwareFactoryView p_view) {
		SoftwareFactoryController l_controller = new SoftwareFactoryController(p_model, p_view);
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
	public SoftwareFactoryModel getModelFactory() {
		return d_model_factory;
	}

	/**
	 * @return the currently defined View Software Factory
	 */
	public SoftwareFactoryView getViewFactory() {
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
			d_startup_controller = new GameStartupController(d_model_factory, d_view_factory, this);
		}
		return d_startup_controller;
	}

	/**
	 * Create or return the current GamePlayController
	 * 
	 * @return game play controller
	 * @throws Exception unexpected error
	 */
	public IGamePlayController getGamePlayController() throws Exception {
		if (d_game_play_controller == null) {
			d_game_play_controller = new GamePlayController(d_model_factory, d_view_factory);
			;
		}
		return d_game_play_controller;
	}

	/**
	 * Use the current GamePlayController as the order datasource
	 * 
	 * @return game play controller
	 * @throws Exception unexpected error
	 */
	public IGameplayOrderDatasource getGamePlayOrderDatasource() throws Exception {
		if (d_game_play_controller == null) {
			d_game_play_controller = new GamePlayController(d_model_factory, d_view_factory);
			;
		}
		return d_game_play_controller;
	}
}