package soen6441.team01.warzone.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.LogEntryBuffer;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.view.ViewFactory;

/**
 * Supports all the test methods used to test class GameStartupController
 * 
 *
 */
public class GameStartupControllerTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";

	public ModelFactory d_model_factory = null;
	public GameStartupController d_startup_controller = null;
	public IGamePlayModel d_gameplay = null;
	public ViewFactory d_view_factory = null;
	public LogEntryBuffer d_msg = null;
	public ControllerFactory d_controller_factory = null;

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
		d_startup_controller = (GameStartupController) d_controller_factory.getGameStartupController();
		d_msg = (LogEntryBuffer) d_model_factory.getUserMessageModel();
	}

	/**
	 * test processGameStartupCommand invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGameStartupCommand_invalid_commands() throws Exception {
		d_startup_controller.processGameStartupCommand("", d_gameplay);
		assertTrue(d_msg.getLastMessageAndClear().d_message.contains("invalid command"));
		d_startup_controller.processGameStartupCommand("John", d_gameplay);
		assertTrue(d_msg.getLastMessageAndClear().d_message.contains("invalid command"));
	}

	/**
	 * test processGameStartupCommand_gameplayer invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGameStartupCommand_gameplayer_invalid() throws Exception {
		String l_msg;
		d_startup_controller.processGameStartupCommand("gameplayer", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));

		d_startup_controller.processGameStartupCommand("gameplayer add", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid gameplayer option"));

		d_startup_controller.processGameStartupCommand("gameplayer -add 1", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("expecting a valid player name."));

		d_startup_controller.processGameStartupCommand("gameplayer -add Player01 -add Player01", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since that name already exists"));

		d_startup_controller.processGameStartupCommand("gameplayer -remove Player02", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since that player doesn't exist"));

		d_startup_controller.processGameStartupCommand("gameplayer -remove", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid gameplayer -remove playername"));

		d_startup_controller.processGameStartupCommand("gameplayer -remove 1", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid gameplayer -remove playername"));
	}

	/**
	 * test processGameStartupCommand_editcontinent valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGameStartupCommand_editcontinent_valid() throws Exception {
		String l_msg;
		d_startup_controller.processGameStartupCommand("gameplayer -add Player01", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Player01 added to game"));

		d_startup_controller.processGameStartupCommand("gameplayer -remove Player01", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Player01 removed from game"));

		d_startup_controller.processGameStartupCommand("gameplayer -add Player01 -remove Player01", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Player01 removed from game"));

		d_startup_controller.processGameStartupCommand(
				"gameplayer -add Player01 -add Player02 -add Player03 -remove Player02 -remove Player03 -remove Player01",
				d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Player01 removed from game"));

		d_startup_controller.processGameStartupCommand(
				"gameplayer -add   Player01     -add   Player02      -add Player03       -remove     Player01   -remove Player02    -remove         Player03",
				d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Player03 removed from game"));
	}

	/**
	 * test loadmap invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGameStartupCommand_loadmap_invalid() throws Exception {
		String l_msg;
		d_startup_controller.processGameStartupCommand("loadmap", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));

		d_startup_controller.processGameStartupCommand("loadmap " + d_MAP_DIR + "canada/quebac.map", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Error loading map file"));
	}

	/**
	 * test loadmap valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGameStartupCommand_loadmap_valid() throws Exception {
		String l_msg;
		d_startup_controller.processGameStartupCommand("loadmap " + d_MAP_DIR + "canada/canada.map", d_gameplay);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("loadmap processed successfully"));
	}
	
	/**
	 * test assigncountries command
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processGameStartupCommand_assigncountries_valid() throws Exception {
		String l_msg;
		MapEditorController l_map_editor_controller = new MapEditorController(d_controller_factory);
		l_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe");
		l_map_editor_controller.processMapEditorCommand("editcountry -add Italy 1 -add France 1");
		d_startup_controller.processGameStartupCommand("gameplayer -add Player01", d_gameplay);
		Phase next_phase = d_startup_controller.processGameStartupCommand("assigncountries");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("assigncountries processed successfully"));
		assertTrue(next_phase instanceof GamePlayController);
	}
}
