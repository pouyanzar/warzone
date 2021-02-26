package soen6441.team01.warzone.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.Country;
import soen6441.team01.warzone.model.Player;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.UserMessageModel;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.view.SoftwareFactoryView;

/**
 * Supports all the test methods used to test class GameStartupController
 * 
 *
 */
public class GamePlayControllerTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";

	public SoftwareFactoryModel d_model_factory = null;
	public GamePlayController d_gameplay_controller = null;
	public IGamePlayModel d_gameplay = null;
	public SoftwareFactoryView d_view_factory = null;
	public UserMessageModel d_msg = null;
	public SoftwareFactoryController d_controller_factory = null;
	public IContinentModel d_continent = null;
	public ICountryModel d_country = null;
	public Player d_player = null;

	/**
	 * setup the environment for testing of GameStartupController
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupGameStartupController() throws Exception {
		d_model_factory = SoftwareFactoryModel.createWarzoneBasicConsoleGameModels();
		d_gameplay = d_model_factory.getNewGamePlayModel();
		d_view_factory = SoftwareFactoryView.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		d_controller_factory = new SoftwareFactoryController(d_model_factory, d_view_factory);
		d_gameplay_controller = (GamePlayController) d_controller_factory.getGamePlayController();
		d_msg = (UserMessageModel) d_model_factory.getUserMessageModel();
		d_continent = new Continent(1, "North_America", 3);
		d_country = new Country(1, "Canada", d_continent, 0, 0);
		d_player = new Player("John");
	}

	/**
	 * test processGamePlayCommand invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_invalid_commands() throws Exception {
		d_gameplay_controller.processGamePlayCommand("", d_player);
		assertTrue(d_msg.getLastMessageAndClear().d_message.contains("invalid command"));
		d_gameplay_controller.processGamePlayCommand("John", d_player);
		assertTrue(d_msg.getLastMessageAndClear().d_message.contains("invalid command"));
	}

	/**
	 * test processGamePlayCommand_gameplayer invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_deploy_invalid() throws Exception {
		String l_msg;
		d_gameplay_controller.processGamePlayCommand("deploy", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));

		d_gameplay_controller.processGamePlayCommand("deploy Canada", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("number of reinforcements not specified."));

		d_gameplay_controller.processGamePlayCommand("deploy 5 Canada", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid deploy county name"));

		d_gameplay_controller.processGamePlayCommand("deploy Canada 5x", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid number of deploy reinforcements"));

		d_gameplay_controller.processGamePlayCommand("deploy Canada 5", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Country Canada is not owned by player"));

		// build 1 requirement: Unit testing framework
		// (4) player cannot deploy more armies that there is in their reinforcement
		// pool.
		d_player.addPlayerCountry(d_country);
		d_player.setReinforcements(3);
		d_gameplay_controller.processGamePlayCommand("deploy Canada 5", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("does not have enough reinforcements (3) to deploy 5 armies"));
	}

	/**
	 * test loadmap valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_loadmap_valid() throws Exception {
		String l_msg;
		d_gameplay_controller.processGamePlayCommand("exit", d_player);
		assertTrue(d_msg.getLastMessageAndClear() == null);

		d_player.addPlayerCountry(d_country);
		d_player.setReinforcements(5);
		d_gameplay_controller.processGamePlayCommand("deploy Canada 3", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Deploy order successful"));
	}
}
