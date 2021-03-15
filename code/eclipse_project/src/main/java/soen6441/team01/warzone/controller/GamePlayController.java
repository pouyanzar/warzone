package soen6441.team01.warzone.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.OrderDeploy;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IGameplayOrderDatasource;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IOrderModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.model.entities.CountrySummary;
import soen6441.team01.warzone.model.entities.GameState;
import soen6441.team01.warzone.view.SoftwareFactoryView;
import soen6441.team01.warzone.view.contracts.IGamePlayView;

/**
 * Warzone game play controller. Manages the coordination and progression of the
 * game play phase.
 */
public class GamePlayController extends Phase implements IGamePlayController{
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private SoftwareFactoryController d_controller_factory;
	private IGamePlayView d_view;
	private IGamePlayModel d_gameplay_model;
	private IUserMessageModel d_msg_model;
	private int d_round = 1;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public GamePlayController(SoftwareFactoryController p_controller_factory) throws Exception {
		super(p_controller_factory.getModelFactory().getGameEngine());
		d_controller_factory = p_controller_factory;
		d_model_factory = p_controller_factory.getModelFactory();
		d_view_factory = p_controller_factory.getViewFactory();
		d_view = d_view_factory.getGamePlayConsoleView(this);
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * invoked by the game engine as part of the game play phase of the game.
	 */
	@Override
	public void execPhase() {
		try {
			if( d_round == 1 ) {
				d_view.activate();
				d_view.displayGamePlayBanner();
				d_gameplay_model = d_model_factory.getGamePlayModel();
				d_gameplay_model.setGameState(GameState.GamePlay);
			}
			d_msg_model.setMessage(MessageType.None, "\n-- round " + d_round++ + " --");
			Phase l_gmaeplay_start_phase = d_controller_factory.getReinforcementPhase();
			nextPhase(l_gmaeplay_start_phase);
		} catch (Exception ex) {
			d_msg_model.setMessage(MessageType.Error, "exception in GamePlayController: " + ex.getMessage());
		}
	}
	
	/**
	 * setups the game to end the game play phase
	 */
	public void endGamePlayPhase() {
		Phase l_end_phase = null;
		try {
			l_end_phase = d_controller_factory.getGameEndPhase();
		} catch (Exception ex) {
			Utl.consoleMessage("Fatal error during game play, exception: " + ex.getMessage());
		}
		nextPhase(l_end_phase);

		if (d_view != null) {
			d_view.deactivate();
		}
	}
}
