package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;

/**
 * Tests for the Country model class
 *
 */
public class GamePlayTest {
	public GamePlay d_gameplay = null;
	public ArrayList<IPlayerModel> d_players = null;

	@Before
	public void class_instance_setup() {
		
		d_gameplay = new GamePlay(SoftwareFactoryModel.createWarzoneBasicConsoleGameModels());
	}

	/**
	 * simple test of add player to the game
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_add_player() throws Exception {
		Player l_player_1 = new Player("Player_1");
		Player l_player_2 = new Player("Player_2");
		
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
		Player l_player_1 = new Player("Player_1");
		Player l_player_2 = new Player("Player_1");
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
		Player l_player_1 = new Player("Player_1");
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
		Player l_player_1 = new Player("Player_1");
		Player l_player_2 = new Player("Player_2");
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
		Player l_player_1 = new Player("Player_1");
		d_gameplay.addPlayer(l_player_1);
		d_gameplay.removePlayer("Player_2");
	}
}
