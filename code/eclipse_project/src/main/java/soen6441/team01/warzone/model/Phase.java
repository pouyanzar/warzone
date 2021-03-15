package soen6441.team01.warzone.model;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IGameEngineModel;

/**
 * Supports the management of the different phases of the Warzone game. This
 * class defines all the commands supported by the game during different phases
 * of the game. The intent is to use this class as a superclass and for
 * subclasses to override methods supported during different phases of the game.
 * 
 * <pre>
 * The game defines the following phases:
 * 
 *		Phase
 *        Map Edit
 *        Game Startup
 *        Game Play
 *          Reinforcement
 *          Order Creation 
 *          Order Execution
 *        Help
 *        End
 * </pre>
 * 
 * each subclass must specify nextState() which the system uses to move between
 * different states. all the other methods should be overidden by each subclass
 * that needs to support the respective function; otherwise an invalid command
 * error is displayed to the user.
 */
public abstract class Phase {

	/**
	 * Contains a reference to the State of the GameEngine so that the state object
	 * can change the state of the GameEngine to transition between states.
	 */
	IGameEngineModel d_game_engine;

	public Phase(IGameEngineModel p_ge) {
		d_game_engine = p_ge;
	}

	// required to be defined by all subclasses
	abstract public void execPhase();
	
	/**
	 * set the next phase of the game
	 * @param p_next_phase the phase that the game will switch to
	 */
	public void nextPhase(Phase p_next_phase) {
		d_game_engine.setNextPhase(p_next_phase);
	}
}
