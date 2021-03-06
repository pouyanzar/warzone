package soen6441.team01.warzone.view;

import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.controller.contracts.IMapEditorController;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.UserMessageModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.view.contracts.IGamePlayView;
import soen6441.team01.warzone.view.contracts.IGameStartupView;
import soen6441.team01.warzone.view.contracts.IMapEditorView;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * views.
 */
public class SoftwareFactoryView {
	private SoftwareFactoryModel d_model_factory;

	/**
	 * Constructor with views defined. Views passed as null will result in the
	 * default view being passed back.
	 * 
	 * @param p_models the software factory for all the game models
	 */
	public SoftwareFactoryView(SoftwareFactoryModel p_models) {
		d_model_factory = p_models;
	}

	/**
	 * Helper function that creates a software factory that holds the Warzone view
	 * objects for the console version of the game.
	 *
	 * @param p_model the model software foctory to use
	 * @return newly create Warzone view software factory
	 */
	public static SoftwareFactoryView CreateWarzoneBasicConsoleGameViews(SoftwareFactoryModel p_model) {
		SoftwareFactoryView l_model = new SoftwareFactoryView(p_model);
		return l_model;
	}

	/**
	 * Creates a Map Editor console view
	 * 
	 * @param p_controller the controller to pass into the view
	 * @return an IMapEditorView object
	 * @throws Exception if there is a problem creating the view
	 */
	public IMapEditorView getMapEditorConsoleView(IMapEditorController p_controller) throws Exception {
		return new MapEditorConsoleView(p_controller, d_model_factory);
	}

	/**
	 * Creates a GameStartup console view
	 * 
	 * @param p_controller the controller to pass into the view
	 * @return an IGameStartupView object
	 * @throws Exception if there is a problem creating the view
	 */
	public IGameStartupView getGameStartupConsoleView(IGameStartupController p_controller) throws Exception {
		return new GameStartupConsoleView(p_controller, d_model_factory);
	}

	/**
	 * Creates a GamePlay console view
	 * 
	 * @param p_controller the controller to pass into the view
	 * @return an IGamePlayView object
	 * @throws Exception if there is a problem creating the view
	 */
	public IGamePlayView getGamePlayConsoleView(IGamePlayController p_controller) throws Exception {
		return new GamePlayConsoleView(p_controller, d_model_factory);
	}
}
