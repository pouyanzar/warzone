package soen6441.team01.warzone.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.ISingleGameController;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.OrderDeploy;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IGameplayOrderDatasource;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.entities.CountrySummary;
import soen6441.team01.warzone.model.entities.GameState;
import soen6441.team01.warzone.view.ViewFactory;
import soen6441.team01.warzone.view.contracts.IGamePlayView;

/**
 * Warzone game play controller. Manages the coordination and progression of the
 * game play phase.
 */
public class ReinforcementController extends SingleGameController {
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
	private IGamePlayView d_view;
	private IAppMsg d_msg_model;
	private boolean d_exit = false;
	private boolean d_game_over = false;

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
		d_view_factory = p_controller_factory.getViewFactory();
		d_view = d_view_factory.getGamePlayConsoleView(this);
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
