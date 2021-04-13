package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IAppMsg;

/**
 * Warzone game play controller. Manages the coordination and progression of the
 * game play phase.
 */
public class ReinforcementController extends SingleGameController {
	private ModelFactory d_model_factory;
	private ControllerFactory d_controller_factory;
	private IAppMsg d_msg_model;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public ReinforcementController(ControllerFactory p_controller_factory) throws Exception {
		super(p_controller_factory);
		d_controller_factory = p_controller_factory;
		d_model_factory = p_controller_factory.getModelFactory();
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * invoked by the game engine as part of the Map Editor phase of the game.
	 */
	@Override
	public void execPhase() {
		try {
			processGamePlayReinforcements();
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, "exception in ReinforcementController: " + ex.getMessage());
			endGamePlayPhase();
		}
	}

	/**
	 * executes the assigning of reinforcements phase
	 * 
	 * @throws Exception unexpected error
	 */
	public void processGamePlayReinforcements() throws Exception {
		IGamePlayModel l_gameplay_model = d_model_factory.getGamePlayModel();
		d_msg_model.setMessage(MsgType.None, "\n* assigning reinforcements:");
		
		l_gameplay_model.assignReinforcements();

		Phase l_phase = d_controller_factory.getIssueOrderPhase();
		nextPhase(l_phase);
	}
}
