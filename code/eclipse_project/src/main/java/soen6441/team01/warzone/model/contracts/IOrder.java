package soen6441.team01.warzone.model.contracts;

import soen6441.team01.warzone.model.ModelFactory;

/**
 * defines the interface of a Warzone Order
 * 
 * @author John
 */
public interface IOrder {

	String execute() throws Exception;

	void cloneToPlayer(IPlayerModel p_player) throws Exception;
}
