package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.controller.contracts.IMapEditorController;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.view.SoftwareFactoryView;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * controllers.
 */
public class SoftwareFactoryController {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	

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
	public IMapEditorController getMapEditorController() throws Exception {
		return new MapEditorController(d_model_factory, d_view_factory);
	}

	/**
	 * Create a GameStartupController 
	 * @return game startup controller
	 * @throws Exception unexpected error
	 */
	public IGameStartupController getGameStartupController() throws Exception {
		return new GameStartupController(d_model_factory, d_view_factory);
	}

	/**
	 * Create a GamePlayController 
	 * @return game play controller
	 * @throws Exception unexpected error
	 */
	public IGamePlayController getGamePlayController() throws Exception {
		return new GamePlayController(d_model_factory, d_view_factory);
	}
}