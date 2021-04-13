package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.controller.contracts.IGameEndController;
import soen6441.team01.warzone.model.Phase;

/**
 * Warzone game end controller. Manages the coordination and progression of the
 * game end phase.
 */
public class GameEndController extends Phase implements IGameEndController {
	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public GameEndController(ControllerFactory p_controller_factory) throws Exception {
		super(p_controller_factory.getModelFactory().getGameEngine());
	}

	/**
	 * displays end game to the user and sets the next phase to null, which should
	 * cause the game engine to terminate the game.
	 */
	public void execPhase() {
		Utl.lprintln("\n*** End Game ***\n");
		nextPhase(null);
	}
}
