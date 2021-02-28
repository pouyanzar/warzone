package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.controller.contracts.IMapEditorController;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.IGameplayOrderDatasource;
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
	private GamePlayController d_game_play_controller = null;

	/**
	 * Constructor with views defined. Views passed as null will result in the
	 * default view being passed back.
	 * 
	 * @param p_models the software factory for all the game models
	 * @param p_view_factory the software factory for all the views
	 */
	public SoftwareFactoryController(SoftwareFactoryModel p_models, SoftwareFactoryView p_view_factory) {
		d_model_factory = p_models;
		d_view_factory = p_view_factory;
	}

	/**
	 * Create a Map Edit controller 
	 * @return map edit controller
	 * @throws Exception unexpected error
	 */
	public IMapEditorController getNewMapEditorController() throws Exception {
		d_map_editor_controller = new MapEditorController(d_model_factory, d_view_factory); 
		return d_map_editor_controller;
	}

	/**
	 * return the existing Map Edit controller 
	 * @return map edit controller
	 * @throws Exception unexpected error
	 */
	public IMapEditorController getMapEditorController() throws Exception {
		return d_map_editor_controller;
	}
	
	/**
	 * Create or return the current GameStartupController 
	 * @return game startup controller
	 * @throws Exception unexpected error
	 */
	public IGameStartupController getGameStartupController() throws Exception {
		if( d_startup_controller == null ) {
			d_startup_controller = new GameStartupController(d_model_factory, d_view_factory, this);
		}
		return d_startup_controller;
	}

	/**
	 * Create or return the current GamePlayController 
	 * @return game play controller
	 * @throws Exception unexpected error
	 */
	public IGamePlayController getGamePlayController() throws Exception {
		if( d_game_play_controller == null ) {
			d_game_play_controller = new GamePlayController(d_model_factory, d_view_factory);;
		}
		return d_game_play_controller;
	}
	
	/**
	 * Use the current GamePlayController as the order datasource 
	 * @return game play controller
	 * @throws Exception unexpected error
	 */
	public IGameplayOrderDatasource getGamePlayOrderDatasource() throws Exception {
		if( d_game_play_controller == null ) {
			d_game_play_controller = new GamePlayController(d_model_factory, d_view_factory);;
		}
		return d_game_play_controller;
	}
}