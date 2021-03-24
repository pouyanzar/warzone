package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.GameState;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Manages the state of game play.
 *
 */
public class GamePlay implements IGamePlayModel {
	private GameState d_game_state = GameState.Startup;
	private IMapModel d_map = null;
	private ArrayList<IPlayerModel> d_players = new ArrayList<IPlayerModel>();
	private ModelFactory d_model_factory = null;

	/**
	 * Constructor
	 * 
	 * @param p_model_factory the model software factory
	 */
	public GamePlay(ModelFactory p_model_factory) {
		d_model_factory = p_model_factory;
		d_map = d_model_factory.getMapModel();
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
	 * Helper function that returns the model that manages system messages
	 * 
	 * @return user message model
	 * @throws Exception unexpected errors
	 */
	private IAppMsg getMsg() throws Exception {
		return d_model_factory.getUserMessageModel();
	}

	/**
	 * Get the current list of players in the game
	 * 
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
			throw new Exception(
					"Cannot add player '" + p_player.getName() + "' to the game, since that name already exists");
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
			throw new Exception("Cannot remove player '" + p_name + "' from the game, since that player doesn't exist");
		}
		d_players.remove(l_player);
	}

	/**
	 * Assign countries to the players. All countries are randomly assigned to
	 * players. Only available in GameState.Startup. Upon successfully assigning all
	 * countries the game state is changed to game play.
	 * 
	 * @throws Exception unexpected error
	 */
	public void assignCountries() throws Exception {
		if (d_game_state != GameState.Startup) {
			return;
		}
		// setup and do some high level validations
		ArrayList<ICountryModel> l_countries = d_map.getCountries();
		if (l_countries.size() < 1) {
			throw new Exception("No countries defined to assigncountries to");
		}
		if (d_players.size() < 1) {
			throw new Exception("No players defined to assigncountries to");
		}
		// assign countries to players
		while (l_countries.size() > 0) {
			for (IPlayerModel l_player : d_players) {
				int randomIdx = ThreadLocalRandom.current().nextInt(0, l_countries.size());
				ICountryModel l_country = l_countries.get(randomIdx);
				l_player.addPlayerCountry(l_country);
				l_countries.remove(randomIdx);
				getMsg().setMessage(MsgType.Informational, l_player.getName() + " owns " + l_country.getName());
				if (l_countries.size() < 1) {
					break;
				}
			}
		}
		d_game_state = GameState.GamePlay;
	}

	/**
	 * Assign to each player the correct number of reinforcement armies according to
	 * the Warzone rules. Only available in GameState.GamePlay.
	 * 
	 * <pre>
	 * formula:
	 * (max(3, # of countries the player own/3)+(continent value of all continents controlled by the player))
	 * </pre>
	 * 
	 * @throws Exception unexpected error
	 */
	public void assignReinforcements() throws Exception {
		if (d_game_state != GameState.GamePlay) {
			return;
		}

		// calc the number of starting reinforcement armies for each player
		for (IPlayerModel l_player : d_players) {
			int l_num_countries = l_player.getPlayerCountries().size();
			l_num_countries = l_num_countries / 3;
			if (l_num_countries < 3) {
				l_num_countries = 3;
			}
			l_player.setReinforcements(l_num_countries);
		}

		// scan all the countries for every continent, and if all the countries owner
		// are the same player then add the continent extra army to the players
		// reinforcements.
		ArrayList<IContinentModel> l_continents = d_map.getContinents();
		ArrayList<ICountryModel> l_continent_countries;
		IPlayerModel l_xplayer1;
		IPlayerModel l_xplayer2;
		for (IContinentModel l_continent : l_continents) {
			l_continent_countries = l_continent.getCountries();
			l_xplayer1 = null;
			boolean l_owns_all = false;
			for (ICountryModel l_country : l_continent_countries) {
				l_xplayer2 = l_country.getOwner();
				if (l_xplayer2 == null) {
					l_owns_all = false;
					break;
				}
				if (l_xplayer1 == null) {
					l_xplayer1 = l_xplayer2;
					l_owns_all = true;
				}
				if (l_xplayer1 != l_xplayer2) {
					l_owns_all = false;
					break;
				}
			}
			if (l_owns_all) {
				// all countries in continent are owned by player - add extra reinforcements.
				int l_rein = l_xplayer1.getReinforcements();
				l_rein += l_continent.getExtraArmy();
				l_xplayer1.setReinforcements(l_rein);
			}
		}

		// status update notification
		for (IPlayerModel l_player : d_players) {
			String l_msg = l_player.getName() + " received " + l_player.getReinforcements() + " reinforcements.";
			getMsg().setMessage(MsgType.Informational, l_msg);
		}
	}

	/**
	 * Calls the next_order() method of the Player. Then the Order object’s
	 * execute() method is called, which will enact the order. The effect of a
	 * deploy order is to place num armies on the country countryID. Only available
	 * in GameState.GamePlay.
	 * 
	 * @throws Exception unexpected error
	 */
	public void executeOrders() throws Exception {
		if (d_game_state != GameState.GamePlay) {
			return;
		}
		int l_orders_executed = 1;
		while (l_orders_executed > 0) {
			l_orders_executed = 0;
			for (IPlayerModel l_player : d_players) {
				IOrder l_order = l_player.next_order();
				if (l_order != null) {
					String l_msg = "";
					try {
						l_msg = l_order.execute();
					} catch (Exception ex) {
						l_msg = ex.getMessage();
					}
					getMsg().setMessage(MsgType.None, l_msg);
					l_orders_executed++;
				}
			}
		}
	}
 
	public void assignCard() {
		for (IPlayerModel l_player : d_players) {
//			if(l_player.conquer()) {
			if (true) {
				// Card l_card = new Card();
				// l_player.addCard(l_card);
			}
		}
	}
}
