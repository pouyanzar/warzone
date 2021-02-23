package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.GameState;

/**
 * Manages the state of game play.
 *
 */
public class GamePlay implements IGamePlayModel {
	private GameState d_game_state = GameState.Startup;
	private IMapModel d_map = null;
	private ArrayList<IPlayerModel> d_players = new ArrayList<IPlayerModel>();

	/**
	 * Constructor
	 */
	public GamePlay() {
	}

	/**
	 * Set the state of the game. The setting affects which methods can be processed
	 * in a given state.
	 * 
	 * @param p_game_state the new game state.
	 */
	public void setGameState(GameState p_game_state) {
		d_game_state = p_game_state;
	}

	/**
	 * Set the game play map. Only available in GameState.Startup.
	 * 
	 * @param p_map the map used for the game
	 */
	public void setMap(IMapModel p_map) {
		if (d_game_state != GameState.Startup) {
			return;
		}
		d_map = p_map;
	}
	
	/**
	 * Get the current list of players in the game
	 * @return The list of players currently defined in the game 
	 */
	public ArrayList<IPlayerModel> getPlayers() {
		return (ArrayList<IPlayerModel>) d_players.clone();
	}

	/**
	 * Add a player to the game. If a player with the same name exists then the
	 * request is ignored. Only available in GameState.Startup.
	 * 
	 * @param p_player the new player to add
	 * @throws Exception unexpected error, or if the player already exists
	 */
	public void addPlayer(IPlayerModel p_player) throws Exception {
		if (d_game_state != GameState.Startup) {
			return;
		}
		String l_pname = p_player.getName();
		IPlayerModel l_player = Player.FindPlayer(l_pname, d_players);
		if (l_player != null) {
			throw new Exception("Cannot add player '" + p_player + "' to the game, since that name already exists");
		}
		d_players.add(p_player);
	}

	/**
	 * Remove a player to the game. If a player doesn't exist then the request is
	 * ignored. Only available in GameState.Startup.
	 * 
	 * @param p_name the player to remove
	 * @throws Exception unexpected error, or if the player does not exist
	 */
	public void removePlayer(String p_name) throws Exception {
		if (d_game_state != GameState.Startup) {
			return;
		}
		IPlayerModel l_player = Player.FindPlayer(p_name, d_players);
		if (l_player == null) {
			throw new Exception(
					"Cannot remove player '" + p_name + "' from the game, since that player doesn't exists");
		}
		d_players.remove(l_player);
	}

	/**
	 * Assign countries to the players. All countries are randomly assigned to
	 * players. Only available in GameState.Startup. Upon successfully assigning all
	 * countries the game state is changed to game play.
	 */
	public void assignCountries() {
		if (d_game_state != GameState.Startup) {
			return;
		}
		d_game_state = GameState.GamePlay;
	}

	/**
	 * Assign to each player the correct number of reinforcement armies according to
	 * the Warzone rules. Only available in GameState.GamePlay.
	 */
	public void assignReinforcements() {
		if (d_game_state != GameState.GamePlay) {
			return;
		}
	}

	/**
	 * Calls the next_order() method of the Player. Then the Order object’s
	 * execute() method is called, which will enact the order. The effect of a
	 * deploy order is to place num armies on the country countryID. Only available
	 * in GameState.GamePlay.
	 */
	public void executeOrders() {
		if (d_game_state != GameState.GamePlay) {
			return;
		}
	}

}
