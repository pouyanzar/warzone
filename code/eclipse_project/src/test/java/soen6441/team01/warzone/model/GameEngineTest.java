package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.controller.ControllerFactory;
import soen6441.team01.warzone.controller.GameStartupController;
import soen6441.team01.warzone.controller.IssueOrderController;
import soen6441.team01.warzone.controller.SingleGameController;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.entities.GameState;

/**
 * Tests for the Country model class
 *
 */
public class GameEngineTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";
	private String d_SAVED_GAMES_DIR = "./src/test/resources/saved_games/";

	/**
	 * test savegame and loadgame 
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_saveloadgame_1() throws Exception {
		GameEngine l_engine = new GameEngine();
		ControllerFactory l_controller_factory = l_engine.getControllerFactory();
		ModelFactory l_model_factory = l_engine.getModelFactory();
		l_model_factory.getNewGamePlayModel();

		GameStartupController l_startup_controller = (GameStartupController)l_controller_factory.getGameStartupController();
		l_startup_controller.processGameStartupCommand("loadmap " + d_MAP_DIR + "world_small/world_small.map");
		l_startup_controller.processGameStartupCommand("gameplayer -add H1 -add B1 bene");
		l_startup_controller.processGameStartupCommand("assigncountries");
		l_controller_factory.getSingleGameController();
		l_engine.saveGame("d:\\tmp\\savegame_1.wz");

		GameEngine l_engine2 = l_engine.loadGame("d:\\tmp\\savegame_1.wz");
		assertTrue(l_engine2.getModelFactory().getMapModel().getContinents().size() == 4);
		assertTrue(l_engine2.getModelFactory().getMapModel().getCountries().size() == 11);
	}

	/**
	 * test assignReinforcements. build 1 requirement: Unit testing framework. (3)
	 * calculation of number of reinforcement armies;
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_loadgame_1() throws Exception {
		GameEngine l_engine = new GameEngine();
		GameEngine l_engine2 = l_engine.loadGame(d_SAVED_GAMES_DIR + "sg_jj.wz");
		assertTrue(l_engine2.getModelFactory().getMapModel().getContinents().size() == 4);
		assertTrue(l_engine2.getModelFactory().getMapModel().getCountries().size() == 11);
		ArrayList<IPlayerModel> l_pl1 = l_engine2.getModelFactory().getGamePlayModel().getPlayers();
		assertTrue(l_pl1.size() == 2);
	}
}
