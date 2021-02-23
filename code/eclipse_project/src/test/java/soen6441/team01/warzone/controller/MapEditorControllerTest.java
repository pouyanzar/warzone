package soen6441.team01.warzone.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.UserMessageModel;
import soen6441.team01.warzone.view.SoftwareFactoryView;

/**
 * Supports all the test methods used to test class MapEditorController
 * 
 *
 */
public class MapEditorControllerTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";
	
	public SoftwareFactoryModel d_model_factory = null;
	public MapEditorController d_map_editor_controller = null;
	public SoftwareFactoryView d_view_factory = null;
	public UserMessageModel d_msg = null;

	/**
	 * setup the environment for testing of MapEditorController
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupMapEditController() throws Exception {
		d_model_factory = SoftwareFactoryModel.CreateWarzoneBasicConsoleGameModels();
		d_view_factory = SoftwareFactoryView.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		d_map_editor_controller = new MapEditorController(d_model_factory, d_view_factory);
		d_msg = (UserMessageModel) d_model_factory.getUserMessageModel();
	}

	/**
	 * test processMapEditorCommand_editcontinent invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_invalid_commands() throws Exception {
		d_map_editor_controller.processMapEditorCommand("");
		assertTrue(d_msg.getLastMessageAndClear().d_message.contains("invalid command"));
		d_map_editor_controller.processMapEditorCommand("John");
		assertTrue(d_msg.getLastMessageAndClear().d_message.contains("invalid command"));
	}

	/**
	 * test processMapEditorCommand_editcontinent invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_editcontinent_invalid() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editcontinent");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));

		d_map_editor_controller.processMapEditorCommand("editcontinent add");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcontinent option"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add Europe");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcontinent -add continentID"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcontinent -add continentvalue"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe add");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcontinent option"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe -add 1 Africa");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since it already exists"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -remove 090");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since it doesn't exist"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -remove");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcontinent -remove continentID"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -remove Europe");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcontinent -remove continentID"));
	}

	/**
	 * test processMapEditorCommand_editcontinent valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_editcontinent_valid() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcontinent processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -remove 1");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcontinent processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 0001 North_America");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcontinent processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 2 Europe -remove 1");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcontinent processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 3 Europe -remove 03");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcontinent processed successfully"));

		d_map_editor_controller.processMapEditorCommand(
				"editcontinent -add 5 Europe -add 6 North_America -add 7 Africa -remove 6 -remove 7 -remove 5");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcontinent processed successfully"));

		d_map_editor_controller.processMapEditorCommand(
				" editcontinent  -add 5   Europe    -add     6 North_America     -add 7    Africa     -remove 6 -remove 00007   -remove  05");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcontinent processed successfully"));
	}

	/**
	 * test editcountry invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_editcountry_invalid() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editcountry");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));

		d_map_editor_controller.processMapEditorCommand("editcountry add");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry option"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Can!ada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry -add countryId"));
		
		d_map_editor_controller.processMapEditorCommand("editcountry -add 0001 Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry -add countryId"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry -add continentID"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada1 Europe");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry -add continentID"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 1 add");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry option"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada1 1 -add Canada1 2");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since it already exists"));

		d_map_editor_controller.processMapEditorCommand("editcountry -remove CanadaX");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since it doesn't exist"));

		d_map_editor_controller.processMapEditorCommand("editcountry -remove Cana!X");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry -remove countryId"));

		d_map_editor_controller.processMapEditorCommand("editcountry -remove");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry -remove countryId"));
	}

	/**
	 * test processMapEditorCommand_editcountry valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_editcountry_valid() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 1");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcountry processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editcountry -remove Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcountry processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 1 -add USA 1 -remove USA -remove Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcountry processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 1 -remove Canada -add Canada 1 -remove Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcountry processed successfully"));

		d_map_editor_controller.processMapEditorCommand(" editcountry   -add   Canada   01   -add   USA  0001 -remove     USA   -remove  Canada  ");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcountry processed successfully"));
	}

	/**
	 * test editneighbor invalid add commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_editneighbor_invalid_add() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editneighbor");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));

		d_map_editor_controller.processMapEditorCommand("editneighbor add");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editneighbor option"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Can!ada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editneighbor -add countryId"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editneighbor -add neighborcountryID"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada U!SA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editneighbor -add neighborcountryID"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Canada' doesn't exist."));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 1");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("USA' doesn't exist."));
	}

	/**
	 * test editneighbor invalid remove commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_editneighbor_invalid_remove() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editneighbor -remove Can!ada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editneighbor -remove countryId"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -remove Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editneighbor -remove neighborcountryID"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -remove Canada U!SA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editneighbor -remove neighborcountryID"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -remove Canada USA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Canada' doesn't exist."));
	}

	/**
	 * test editneighbor valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_editneighbor_valid() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 1");
		d_map_editor_controller.processMapEditorCommand("editcountry -add USA 1");

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -remove Canada USA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA -add USA Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -remove Canada USA -remove USA Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA -add USA Canada -remove Canada USA -remove USA Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA -remove Canada USA -add USA Canada -remove USA Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));

		d_map_editor_controller.processMapEditorCommand(" editneighbor   -add   Canada   USA   -remove     Canada      USA     -add       USA       Canada       -remove    USA   Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));
	}

	/**
	 * test loadmap invalid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_loadmap_invalid() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("loadmap");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("no options specified"));
		
		d_map_editor_controller.processMapEditorCommand("loadmap " + d_MAP_DIR + "canada/quebac.map");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Error loading map file"));
	}

	/**
	 * test loadmap valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_loadmap_valid() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("loadmap " + d_MAP_DIR + "canada/canada.map");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("loadmap processed successfully"));
	}
}
