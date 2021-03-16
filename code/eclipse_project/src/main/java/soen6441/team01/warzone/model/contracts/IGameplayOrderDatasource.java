package soen6441.team01.warzone.model.contracts;

/**
 * Defines the methods used during gameplay to get a Warzone command.
 *
 */
public interface IGameplayOrderDatasource {
	IOrder getOrder(IPlayerModel p_player) throws Exception;
}
