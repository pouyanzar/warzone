package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.entities.GameState;

/**
 * Tests for the Country model class
 *
 */
public class GamePlayTest {
	public GamePlay d_gameplay = null;
	public Map d_map = null;
	public ArrayList<IPlayerModel> d_players = null;
	public SoftwareFactoryModel d_factory_model = null;

	/**
	 * setup test classes before each test is executed
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void class_instance_setup() throws Exception {
		d_factory_model = SoftwareFactoryModel.createWarzoneBasicConsoleGameModels();
		d_map = (Map) d_factory_model.getMapModel();
		d_map.addContinent(1, "North_America", 3);
		d_map.addCountry(1, "Canada", 1);
		d_map.addCountry(2, "USA", 1);
		d_gameplay = new GamePlay(d_factory_model);
	}

	/**
	 * test assignReinforcements. build 1 requirement: Unit testing framework. (3)
	 * calculation of number of reinforcement armies;
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_assignReinforcements_1() throws Exception {
		IPlayerModel l_p1 = new Player("Player_1", d_factory_model);
		IPlayerModel l_p2 = new Player("Player_2", d_factory_model);
		d_gameplay.addPlayer(l_p1);
		d_gameplay.addPlayer(l_p2);
		d_gameplay.setGameState(GameState.GamePlay);
		Map.refreshCountriesOfAllContinents(d_map);
		d_gameplay.assignReinforcements();
		assertTrue(l_p1.getReinforcements() == 5);
		assertTrue(l_p2.getReinforcements() == 5);
		
		ICountryModel l_country = Country.findCountry("Canada", d_map.getCountries());
		l_country.setOwner(l_p1);
		d_gameplay.assignReinforcements();
		assertTrue(l_p1.getReinforcements() == 5);
		assertTrue(l_p2.getReinforcements() == 5);

		l_country = Country.findCountry("USA", d_map.getCountries());
		l_country.setOwner(l_p2);
		d_gameplay.assignReinforcements();
		assertTrue(l_p1.getReinforcements() == 5);
		assertTrue(l_p2.getReinforcements() == 5);
		
		l_country = Country.findCountry("USA", d_map.getCountries());
		l_country.setOwner(l_p1);
		d_gameplay.assignReinforcements();
		assertTrue(l_p1.getReinforcements() == 8);
		assertTrue(l_p2.getReinforcements() == 5);
		
		l_country = Country.findCountry("Canada", d_map.getCountries());
		l_country.setOwner(l_p2);
		l_country = Country.findCountry("USA", d_map.getCountries());
		l_country.setOwner(l_p2);
		d_gameplay.assignReinforcements();
		assertTrue(l_p1.getReinforcements() == 5);
		assertTrue(l_p2.getReinforcements() == 8);
	}

	/**
	 * simple test of add player to the game
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_player() throws Exception {
		Player l_player_1 = new Player("Player_1", d_factory_model);
		Player l_player_2 = new Player("Player_2", d_factory_model);

		d_gameplay.addPlayer(l_player_1);
		assertTrue(d_gameplay.getPlayers().size() == 1);
		d_gameplay.addPlayer(l_player_2);
		assertTrue(d_gameplay.getPlayers().size() == 2);
	}

	/**
	 * test add duplicate player to the game
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_add_dup_player_1() throws Exception {
		Player l_player_1 = new Player("Player_1", d_factory_model);
		Player l_player_2 = new Player("Player_1", d_factory_model);
		;
		d_gameplay.addPlayer(l_player_1);
		d_gameplay.addPlayer(l_player_2); // exception
	}

	/**
	 * test add duplicate player to the game
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_add_dup_player_2() throws Exception {
		Player l_player_1 = new Player("Player_1", d_factory_model);
		d_gameplay.addPlayer(l_player_1);
		d_gameplay.addPlayer(l_player_1); // exception
	}

	/**
	 * simple test of remove player to the game
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_remove_player() throws Exception {
		Player l_player_1 = new Player("Player_1", d_factory_model);
		Player l_player_2 = new Player("Player_2", d_factory_model);
		;
		d_gameplay.addPlayer(l_player_1);
		assertTrue(d_gameplay.getPlayers().size() == 1);
		d_gameplay.addPlayer(l_player_2);
		assertTrue(d_gameplay.getPlayers().size() == 2);

		d_gameplay.removePlayer("Player_1");
		assertTrue(d_gameplay.getPlayers().size() == 1);
		d_gameplay.removePlayer("Player_2");
		assertTrue(d_gameplay.getPlayers().size() == 0);
	}

	/**
	 * test remove non-existent player
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_remove_player_1() throws Exception {
		d_gameplay.removePlayer("playerX");
	}

	/**
	 * test remove non-existent player
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_remove_player_2() throws Exception {
		Player l_player_1 = new Player("Player_1", d_factory_model);
		d_gameplay.addPlayer(l_player_1);
		d_gameplay.removePlayer("Player_2");
	}

	/**
	 * test assigncountries
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_assigncountries_1() throws Exception {
		d_gameplay.setMap(d_map);
		Player l_player_1 = new Player("Player_1", d_factory_model);
		d_gameplay.addPlayer(l_player_1);
		d_gameplay.assignCountries();
		ArrayList<IPlayerModel> l_players = d_gameplay.getPlayers();
		assertTrue(l_players.get(0).getPlayerCountries().size() == 2);
		String l_c1 = l_players.get(0).getPlayerCountries().get(0).getName();
		String l_c2 = l_players.get(0).getPlayerCountries().get(1).getName();
		assertTrue((l_c1.equals("Canada") && l_c2.equals("USA")) || (l_c1.equals("USA") && l_c2.equals("Canada")));
		assertTrue(d_map.getCountries().size() == 2);
	}

	/**
	 * test assigncountries
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_assigncountries_2() throws Exception {
		d_map.addCountry("Mexico", 1);
		d_gameplay.setMap(d_map);
		d_gameplay.addPlayer(new Player("Player_1", d_factory_model));
		d_gameplay.addPlayer(new Player("Player_2", d_factory_model));
		d_gameplay.assignCountries();
		ArrayList<IPlayerModel> l_players = d_gameplay.getPlayers();
		assertTrue(l_players.get(0).getPlayerCountries().size() == 2);
		String l_c1 = l_players.get(0).getPlayerCountries().get(0).getName();
		String l_c2 = l_players.get(0).getPlayerCountries().get(1).getName();
		assertTrue(l_players.get(0).getPlayerCountries().size() > 0);
		assertTrue(l_players.get(1).getPlayerCountries().size() > 0);
	}

	/**
	 * test assigncountries no players
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test(expected = Exception.class)
	public void test_assigncountries_3() throws Exception {
		d_gameplay.setMap(d_map);
		d_gameplay.assignCountries();
	}
}
