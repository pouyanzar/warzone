package soen6441.team01.warzone.controller.contracts;

/**
 * Defines the interface used to support the Warzone game play controller
 *
 */
public interface IGamePlayController {
	void endGamePlayPhase();

	void setMaxRounds(int p_max_rounds);
}
