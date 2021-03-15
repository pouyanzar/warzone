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
public class GameEngine implements IGameEngineModel {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private SoftwareFactoryController d_controller_factory;
	private Phase d_phase = null;

	/**
	 * Constructor with no views or models defined. Use Software factory with
	 * defaults
	 * @throws Exception unexpected error
	 */
	public GameEngine() throws Exception {
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
	 * @throws Exception unexpected error
	 */
	public GameEngine(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory,
			SoftwareFactoryController p_controller_factory) throws Exception {
		if (p_model_factory != null) {
			d_model_factory = p_model_factory;
		} else {
			d_model_factory = SoftwareFactoryModel.createWarzoneBasicConsoleGameModels();
		}
		d_model_factory.setGameEngine(this);

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

		d_phase = (Phase) d_controller_factory.getMapEditorPhase();
	}

	/**
	 * Starts executing the game dynamics
	 */
	public void startNewGame() {
		try {
			while( d_phase != null ) {
				d_phase.execPhase();
			}
		} catch (Exception ex) {
			Utl.consoleMessage("Fatal error processing GameEngine.");
			Utl.consoleMessage("Exception: " + ex.getMessage());
		}

		Utl.consoleMessage("Game execution terminated.\n");
	}

	/**
	 * set the current phase of the game
	 * 
	 * @param p_next_phase the phase that the game will switch to
	 */
	public void setNextPhase(Phase p_next_phase) {
		d_phase = p_next_phase;
	}
}
