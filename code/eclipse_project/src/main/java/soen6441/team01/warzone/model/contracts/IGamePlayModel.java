package soen6441.team01.warzone.model.contracts;

import java.util.ArrayList;

import soen6441.team01.warzone.model.entities.GameState;

/**
 * Defines the GamePlay model interface used to define the methods available to
 * manipulate the current game play.
 *
 */
public interface IGamePlayModel {
	void setGameState(GameState p_game_state);

	void setMap(IMapModel p_map);

	ArrayList<IPlayerModel> getPlayers();

	void addPlayer(IPlayerModel p_player) throws Exception;

	void removePlayer(String p_name) throws Exception;

	void assignCountries() throws Exception;

	void assignReinforcements() throws Exception;

	void executeOrders();
}
