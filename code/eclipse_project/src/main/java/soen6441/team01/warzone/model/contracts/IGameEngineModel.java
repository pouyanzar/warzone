package soen6441.team01.warzone.model.contracts;

import soen6441.team01.warzone.model.GameEngine;
import soen6441.team01.warzone.model.Phase;

/**
 * Defines the GameEngine model interface
 *
 */
public interface IGameEngineModel {
	void setNextPhase(Phase p_next_phase);

	Phase getPhase();
	
	void saveGame(String p_filename) throws Exception;
	
	GameEngine loadGame(String p_filename) throws Exception;
}
