package soen6441.team01.warzone.common.contracts;

import soen6441.team01.warzone.common.Observable;

/**
 * Defines the observer methods used to support the Observer design pattern.
 * 
 *
 */
public interface Observer {
	void update(Observable p_obserable);
}
