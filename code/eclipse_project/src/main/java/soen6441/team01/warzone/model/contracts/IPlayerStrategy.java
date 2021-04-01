package soen6441.team01.warzone.model.contracts;

/**
 * defines the interface of a Warzone player strategy
 * 
 */
public interface IPlayerStrategy {
	IOrder createOrder() throws Exception;

	IPlayerStrategy cloneStrategy(IPlayerModel p_player) throws Exception;
}
