package soen6441.team01.warzone.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.Card;
import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.Country;
import soen6441.team01.warzone.model.Player;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.LogEntryBuffer;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.entities.CardType;
import soen6441.team01.warzone.view.ViewFactory;

/**
 * Supports all the test methods used to test class GameStartupController
 * 
 *
 */
public class IssueOrderControllerTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";
	public MapEditorController d_map_editor_controller = null;
	
	public ModelFactory d_model_factory = null;
	public IssueOrderController d_gameplay_controller = null;
	public IGamePlayModel d_gameplay = null;
	public ViewFactory d_view_factory = null;
	public LogEntryBuffer d_msg = null;
	public ControllerFactory d_controller_factory = null;
	public IContinentModel d_continent = null;
	public ICountryModel d_country = null;
	public ICountryModel d_us = null;
	public ICountryModel d_canada = null;
	public Player d_player = null;
	public IMapModel d_map = null;

	/**
	 * setup the environment for testing of GameStartupController
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupGameStartupController() throws Exception {
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		d_map = d_model_factory.getMapModel();
		d_gameplay = d_model_factory.getNewGamePlayModel();
		d_view_factory = ViewFactory.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		d_controller_factory = new ControllerFactory(d_model_factory, d_view_factory);
		d_gameplay_controller = (IssueOrderController) d_controller_factory.getIssueOrderController();
		d_msg = (LogEntryBuffer) d_model_factory.getUserMessageModel();
		d_continent = new Continent(1, "North_America", 3);
		d_country = new Country(1, "Canada", d_continent, 0, 0, d_model_factory);
		d_us = new Country(1, "USA", d_continent, 0, 0, d_model_factory);
		d_canada = new Country(2, "Canada", d_continent, 0, 0, d_model_factory);
		d_map.addContinent(d_continent);
		d_map.addCountry(d_us);
		d_map.addCountry(d_canada);
		d_player = new Player("John", d_model_factory);
		
	}

	/**
	 * test processAdvanceCommand_gameplayer valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processAdvanceCommand_advance_valid() throws Exception {
		String l_msg;
		Country l_country_1 = new Country(1, "Canada", null, 0, 0, d_model_factory);
		Country l_country_2 = new Country(2, "United_States", null, 0, 0, d_model_factory);
		l_country_1.addNeighbor(l_country_2);
		l_country_2.addNeighbor(l_country_1);

		
		l_country_1.setArmies(5);
		l_country_2.setArmies(3);
		l_country_1.addNeighbor(l_country_2);		
		d_player.addPlayerCountry(l_country_1);	
		
		d_player.setReinforcements(5);
		d_gameplay_controller.processGamePlayCommand("deploy Canada 5", d_player);		
		d_gameplay_controller.processGamePlayCommand("advance Canada United_States 5  ", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		System.out.println(l_msg);
		assertTrue(l_msg.contains("advance order successful"));
		d_gameplay_controller.processGamePlayCommand("advance", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		System.out.println(l_msg);		
		assertTrue(l_msg.contains("Invalid advance command, no options specified"));
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
		assertTrue(l_msg.contains("Invalid deploy country name '5'"));

		d_gameplay_controller.processGamePlayCommand("deploy Canada 5x", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid number of deploy reinforcements"));

		d_gameplay_controller.processGamePlayCommand("deploy Canada 5", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Country Canada is not owned by player"));

		// build 1 & 2 requirement: Unit testing framework
		// (4) player cannot deploy more armies that there is in their reinforcement
		// pool.
		d_player.addPlayerCountry(d_canada);
		d_player.setReinforcements(3);
		d_gameplay_controller.processGamePlayCommand("deploy Canada 5", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("does not have enough reinforcements (3) to deploy 5 armies"));

		d_gameplay_controller.processGamePlayCommand("deploy Canada -1", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid number of deploy reinforcements '-1'"));
	}

	/**
	 * test processGamePlayCommand_gameplayer valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_deploy_valid() throws Exception {
		String l_msg;
		d_player.addPlayerCountry(d_canada);
		d_player.setReinforcements(3);
		d_gameplay_controller.processGamePlayCommand("deploy Canada 3", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Deploy order successful"));

		d_player.setReinforcements(3);
		d_gameplay_controller.processGamePlayCommand("deploy Canada 2", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Deploy order successful"));

		d_gameplay_controller.processGamePlayCommand("deploy Canada 1", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Deploy order successful"));
	}

	/**
	 * test exit and deploy valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_exit_deploy_valid() throws Exception {
		String l_msg;
		d_gameplay_controller.processGamePlayCommand("exit", d_player);
		assertTrue(d_msg.getLastMessageAndClear() == null);
		Phase next_phase = ((IssueOrderController) d_gameplay_controller).getNextPhase();
		assertTrue(next_phase instanceof GameEndController);

		d_player.addPlayerCountry(d_canada);
		d_player.setReinforcements(5);
		d_gameplay_controller.processGamePlayCommand("deploy Canada 3", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		
		assertTrue(l_msg.contains("Deploy order successful"));
	}

	/**
	 * test end turn command
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_end() throws Exception {
		String l_msg;
		d_gameplay_controller.processGamePlayCommand("end", d_player);
		assertTrue(d_msg.getLastMessageAndClear() == null);
		Phase next_phase = ((IssueOrderController) d_gameplay_controller).getNextPhase();

		d_gameplay_controller.processGamePlayCommand("end turn", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("invalid parameters for end command: 'turn'"));

		d_player.addPlayerCountry(d_canada);
		d_player.setReinforcements(5);
		d_gameplay_controller.processGamePlayCommand("end", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Cannot end turn as player 'John' has 5 reinforcement(s) left to deploy."));
	}

	/**
	 * test processGamePlayCommand_gameplayer valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_bomb_valid() throws Exception {
		String l_msg;
		d_player.addPlayerCountry(d_canada);
		d_canada.addNeighbor(d_us);
		d_player.addCard(new Card(CardType.bomb));
		d_gameplay_controller.processGamePlayCommand("bomb USA", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("bomb order successful"));
	}

	/**
	 * test processGamePlayCommand_gameplayer valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_bomb_invalid() throws Exception {
		String l_msg;
		d_gameplay_controller.processGamePlayCommand("bomb", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));

		d_gameplay_controller.processGamePlayCommand("bomb USA Canada", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid bomb option '"));
	}

	/**
	 * test processGamePlayCommand_gameplayer valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_advance_valid() throws Exception {
		String l_msg;
		d_player.addPlayerCountry(d_canada);
		d_canada.setArmies(2);
		d_canada.addNeighbor(d_us);
		d_gameplay_controller.processGamePlayCommand("advance Canada USA 1", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("advance order successful"));
	}

	/**
	 * test processGamePlayCommand_gameplayer valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGamePlayCommand_advance_invalid() throws Exception {
		String l_msg;
		d_player.addPlayerCountry(d_canada);
		
		d_gameplay_controller.processGamePlayCommand("advance", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));

		d_gameplay_controller.processGamePlayCommand("advance USA Canada 5", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("not owned by"));

		d_player.addPlayerCountry(d_canada);
		d_gameplay_controller.processGamePlayCommand("advance Canada USA 5", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("is not a neighbor of"));

		d_canada.setArmies(2);
		d_canada.addNeighbor(d_us);
		d_player.addPlayerCountry(d_canada);
		d_gameplay_controller.processGamePlayCommand("advance Canada USA -1", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid number of armies"));

		d_gameplay_controller.processGamePlayCommand("advance Canada USA 1 go", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid advance option"));
	}
}
