package soen6441.team01.warzone.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.Country;
import soen6441.team01.warzone.model.Player;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.LogEntryBuffer;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.view.ViewFactory;

/**
 * Supports all the test methods used to test class GameStartupController
 * 
 *
 */
public class IssueOrderControllerTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";

	public ModelFactory d_model_factory = null;
	public IssueOrderController d_gameplay_controller = null;
	public IGamePlayModel d_gameplay = null;
	public ViewFactory d_view_factory = null;
	public LogEntryBuffer d_msg = null;
	public ControllerFactory d_controller_factory = null;
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
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		d_gameplay = d_model_factory.getNewGamePlayModel();
		d_view_factory = ViewFactory.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		d_controller_factory = new ControllerFactory(d_model_factory, d_view_factory);
		d_gameplay_controller = (IssueOrderController) d_controller_factory.getIssueOrderController();
		d_msg = (LogEntryBuffer) d_model_factory.getUserMessageModel();
		d_continent = new Continent(1, "North_America", 3);
		d_country = new Country(1, "USA", d_continent, 0, 0, d_model_factory);
		d_country = new Country(1, "Canada", d_continent, 0, 0, d_model_factory);
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
		
		
		d_gameplay_controller.processGamePlayCommand("advance", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));
//////
		/*
		d_gameplay_controller.processGamePlayCommand("advance Italy France -1", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid number of armies"));
		*/
		
		//d_gameplay_controller.processGamePlayCommand("advance Canada USA 5", d_player);
		//l_msg = d_msg.getLastMessageAndClear().d_message;
		//assertTrue(l_msg.contains("Advance order execute method not yet implemented"));
		//assertTrue(l_msg.contains("Advance order successfull"));
		
		

		
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
		d_player.addPlayerCountry(d_country);
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
		d_player.addPlayerCountry(d_country);
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
		Phase next_phase = ((IssueOrderController)d_gameplay_controller).getNextPhase(); 
		assertTrue(next_phase instanceof GameEndController);
		
		d_player.addPlayerCountry(d_country);
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
		Phase next_phase = ((IssueOrderController)d_gameplay_controller).getNextPhase(); 

		d_gameplay_controller.processGamePlayCommand("end turn", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("invalid parameters for end command: 'turn'"));

		d_player.addPlayerCountry(d_country);
		d_player.setReinforcements(5);
		d_gameplay_controller.processGamePlayCommand("end", d_player);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Cannot end turn as player 'John' has 5 reinforcement(s) left to deploy."));
	}
	
}
