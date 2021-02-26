package soen6441.team01.warzone.model;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.SoftwareFactoryController;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.view.*;

/**
 * Warzone MVC main controller. Manages the coordination and progression of the
 * different stages of the game. As well as supporting user actions (ie.
 * interactions, gestures).
 */
public class GameEngine {
	SoftwareFactoryModel d_model_factory;
	SoftwareFactoryView d_view_factory;
	SoftwareFactoryController d_controller_factory;

	/**
	 * Constructor with no views or models defined. Use Software factory with
	 * defaults
	 */
	public GameEngine() {
		this(null, null, null);
	}

	/**
	 * Constructor with view and models defined. Parameters passed as null will
	 * result in the default view or model being used.
	 * 
	 * @param p_model_factory      predefined SoftwareFactoryModel. Pass null to
	 *                             have the controller generate a model factory
	 *                             based on the default models.
	 * @param p_view_factory       predefined SoftwareFactoryView. Pass null to have
	 *                             the controller generate a view factory based on
	 *                             the default views (i.e. console based).
	 * @param p_controller_factory predefined SoftwareFactoryController. Pass null
	 *                             to have the controller generate a controller
	 *                             factory based on the default controllers (i.e.
	 *                             console based).
	 */
	public GameEngine(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory,
			SoftwareFactoryController p_controller_factory) {
		if (p_model_factory != null) {
			d_model_factory = p_model_factory;
		} else {
			d_model_factory = SoftwareFactoryModel.createWarzoneBasicConsoleGameModels();
		}
		if (p_model_factory != null) {
			d_view_factory = p_view_factory;
		} else {
			d_view_factory = SoftwareFactoryView.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		}
		if (p_controller_factory != null) {
			d_controller_factory = p_controller_factory;
		} else {
			d_controller_factory = new SoftwareFactoryController(d_model_factory, d_view_factory);
		}
	}

	/**
	 * Starts executing the game dynamics
	 */
	public void startNewGame() {
		try {
			boolean l_continue_game = false;
			l_continue_game = processMapEditor();
			IGamePlayModel p_gameplay = d_model_factory.getNewGamePlayModel();
			p_gameplay.setMap(d_model_factory.getMapModel());
			if (l_continue_game) {
				l_continue_game = processGameStartup(); 
			}
			if (l_continue_game) {
				l_continue_game = processGamePlay();
			}
		} catch (Exception ex) {
			System.out.println("Fatal error processing GameEngine.");
			System.out.println("Exception: " + ex.getMessage());
			System.out.println("Terminating game.");
		}

		System.out.println("End of game.");
	}

	/**
	 * Startup and process the map editor phase of the game
	 * 
	 * @return true=continue game; false=exit game
	 * @throws Exception unexpected errors
	 */
	public boolean processMapEditor() throws Exception {
		String l_cmd = d_controller_factory.getNewMapEditorController().startMapEditor();
		switch (l_cmd) {
		case "exit":
			return false;
		case "map_loaded":
			break;
		default:
			throw new Exception("Internal error processing map editor.");
		}
		return true;
	}

	/**
	 * Startup and process the game startup phase of the game
	 *
	 * @return true=continue game; false=exit game
	 * @throws Exception unexpected errors
	 */
	public boolean processGameStartup() throws Exception {
		String l_cmd = d_controller_factory.getGameStartupController().processGameStartup();
		switch (l_cmd) {
		case "exit":
			return false;
		case "startup_complete":
			break;
		default:
			throw new Exception("Internal error processing GameEngine startup.");
		}
		return true;
	}
	
	/**
	 * Startup and process the game startup phase of the game
	 * 
	 * @return true=continue game; false=exit game
	 * @throws Exception unexpected errors
	 */
	public boolean processGamePlay() throws Exception {
		String l_cmd = d_controller_factory.getGamePlayController().processGamePlay();
		switch (l_cmd) {
		case "exit":
			return false;
		case "game_over":
			break;
		default:
			throw new Exception("Internal error processing GameEngine gameplay.");
		}
		return true;
	}	
}
