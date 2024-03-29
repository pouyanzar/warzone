package soen6441.team01.warzone.model.contracts;

/**
 * defines the interface of a Warzone Order
 * 
 */
public interface IOrder {

	String execute() throws Exception;

	void cloneToPlayer(IPlayerModel p_player) throws Exception;
}
