package soen6441.team01.warzone.model;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.ControllerFactory;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.view.*;

/**
 * Warzone MVC main controller. Manages the coordination and progression of the
 * different stages of the game. As well as supporting user actions (ie.
 * interactions, gestures).
 */
public class GameEngine implements IGameEngineModel {
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
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
	public GameEngine(ModelFactory p_model_factory, ViewFactory p_view_factory,
			ControllerFactory p_controller_factory) throws Exception {
		if (p_model_factory != null) {
			d_model_factory = p_model_factory;
		} else {
			d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		}
		d_model_factory.setGameEngine(this);

		if (p_model_factory != null) {
			d_view_factory = p_view_factory;
		} else {
			d_view_factory = ViewFactory.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		}

		if (p_controller_factory != null) {
			d_controller_factory = p_controller_factory;
		} else {
			d_controller_factory = new ControllerFactory(d_model_factory, d_view_factory);
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
