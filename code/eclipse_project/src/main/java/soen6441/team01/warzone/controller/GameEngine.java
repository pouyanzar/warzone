package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.model.*;
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

	/**
	 * Constructor with no views or models defined. Use Software factory with
	 * defaults
	 */
	public GameEngine() {
		d_model_factory = new SoftwareFactoryModel();
		d_view_factory = new SoftwareFactoryView();
	}

	/**
	 * Constructor with view and models defined. Parameters passed as null will
	 * result in the default view or model being used.
	 * 
	 */
	public GameEngine(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory) {
		if (p_model_factory != null) {
			d_model_factory = p_model_factory;
		} else {
			d_model_factory = new SoftwareFactoryModel();
		}
		if (p_model_factory != null) {
			d_view_factory = p_view_factory;
		} else {
			d_view_factory = new SoftwareFactoryView();
		}
	}

	/**
	 * Starts executing the game dynamics
	 */
	public void startGame() {
		processMapEditor();
	}

	/**
	 * Manages the interactions between the controller (this class) and maps
	 * gestures from the view to the model.
	 */
	private void processMapEditor() {

	}
}
