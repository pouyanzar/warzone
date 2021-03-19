package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.*;
import soen6441.team01.warzone.view.ViewFactory;
import soen6441.team01.warzone.view.contracts.IGamePlayView;

/**
 * Warzone game play controller. Manages the coordination and progression of the
 * game play phase.
 */
public class GamePlayController extends Phase implements IGamePlayController{
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
	private IGamePlayView d_view;
	private IGamePlayModel d_gameplay_model;
	private IAppMsg d_msg_model;
	private int d_round = 1;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public GamePlayController(ControllerFactory p_controller_factory) throws Exception {
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
			d_msg_model.setMessage(MsgType.None, "\n-- round " + d_round++ + " --");
			Phase l_gmaeplay_start_phase = d_controller_factory.getReinforcementPhase();
			nextPhase(l_gmaeplay_start_phase);
		} catch (Exception ex) {
			d_msg_model.setMessage(MsgType.Error, "exception in GamePlayController: " + ex.getMessage());
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
			Utl.lprintln("Fatal error during game play, exception: " + ex.getMessage());
		}
		nextPhase(l_end_phase);

		if (d_view != null) {
			d_view.deactivate();
		}
	}
}
