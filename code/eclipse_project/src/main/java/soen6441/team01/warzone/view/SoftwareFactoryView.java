package soen6441.team01.warzone.view;

import soen6441.team01.warzone.controller.IInteractionDrivenController;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.UserMessageModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModelView;

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
	public IMapEditorView getMapEditorConsoleView(IInteractionDrivenController p_controller) throws Exception {
		IUserMessageModelView l_usermsg = (IUserMessageModelView) d_model_factory.getUserMessageModel();
		return new MapEditorConsoleView(p_controller, l_usermsg);
	}

}
