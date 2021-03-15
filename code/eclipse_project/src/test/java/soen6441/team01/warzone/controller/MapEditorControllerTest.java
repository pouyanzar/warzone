package soen6441.team01.warzone.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.AppMsg;
import soen6441.team01.warzone.view.ViewFactory;

/**
 * Supports all the test methods used to test class MapEditorController
 * 
 *
 */
public class MapEditorControllerTest {
	private String d_MAP_DIR = "./src/test/resources/maps/";

	public ModelFactory d_model_factory = null;
	public MapEditorController d_map_editor_controller = null;
	public ViewFactory d_view_factory = null;
	public ControllerFactory d_controller_factory = null;
	public AppMsg d_msg = null;

	/**
	 * setup the environment for testing of MapEditorController
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupMapEditController() throws Exception {
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		d_view_factory = ViewFactory.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		d_controller_factory = ControllerFactory.CreateWarzoneBasicConsoleGameControllers(d_model_factory,
				d_view_factory);
		d_map_editor_controller = new MapEditorController(d_controller_factory);
		d_msg = (AppMsg) d_model_factory.getUserMessageModel();
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
		assertTrue(l_msg.contains("Invalid continent id 'Europe', expecting a positive integer less than 1000"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add -1 Europe");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid continent id '-1',"));
		
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid continent name: ''"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe add");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcontinent option 'add', expecting: -add, -remove"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe -add 1 Africa");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since it already exists"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -remove 090");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since it doesn't exist"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -remove");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid continent id: '',"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -remove Europe");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid continent id: 'Europe',"));
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
		assertTrue(l_msg.contains("Invalid country name 'Can!ada'"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add 0001 Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid country name '0001'"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid continent id: '', expecting a positive integer less than 1000"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada1 Europe");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid continent id: 'Europe',"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 1 add");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid editcountry option"));
		
		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada -1");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid continent id: '-1',"));

		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada1 1 -add Canada1 2");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since it already exists"));

		d_map_editor_controller.processMapEditorCommand("editcountry -remove CanadaX");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("since it doesn't exist"));

		d_map_editor_controller.processMapEditorCommand("editcountry -remove Cana!X");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid country name 'Cana!X'"));

		d_map_editor_controller.processMapEditorCommand("editcountry -remove");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid country name ''"));
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

		d_map_editor_controller
				.processMapEditorCommand("editcountry -add Canada 1 -add USA 1 -remove USA -remove Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcountry processed successfully"));

		d_map_editor_controller
				.processMapEditorCommand("editcountry -add Canada 1 -remove Canada -add Canada 1 -remove Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editcountry processed successfully"));

		d_map_editor_controller.processMapEditorCommand(
				" editcountry   -add   Canada   01   -add   USA  0001 -remove     USA   -remove  Canada  ");
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
		assertTrue(l_msg.contains("Invalid country name 'Can!ada'"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid country neighbor name 'Canada'"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada U!SA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid country neighbor name 'Canada'"));

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
		assertTrue(l_msg.contains("Invalid country name 'Can!ada'"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -remove Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid country neighbor name 'Canada'"));

		d_map_editor_controller.processMapEditorCommand("editneighbor -remove Canada U!SA");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid country neighbor name 'Canada'"));

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

		d_map_editor_controller.processMapEditorCommand(
				"editneighbor -add Canada USA -add USA Canada -remove Canada USA -remove USA Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));

		d_map_editor_controller.processMapEditorCommand(
				"editneighbor -add Canada USA -remove Canada USA -add USA Canada -remove USA Canada");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("editneighbor processed successfully"));

		d_map_editor_controller.processMapEditorCommand(
				" editneighbor   -add   Canada   USA   -remove     Canada      USA     -add       USA       Canada       -remove    USA   Canada");
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

	/**
	 * test validatemap command fully connected. // (1) map validation – map is a
	 * connected graph; subgraph;
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_validatemap_fully_connected() throws Exception {
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Italy 1 -add France 1");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Italy France");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add France Italy");
		assertTrue(d_model_factory.getMapModel().validatemap());
	}

	/**
	 * test validatemap command - countries not connected. // (1) map validation –
	 * map is a connected graph;
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_validatemap_countries_not_connected_1() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Italy 1 -add France 1");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Italy France");
		assertFalse(d_model_factory.getMapModel().validatemap());
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.equals("Country 'France' should have at least 1 neighbor"));

		d_map_editor_controller.processMapEditorCommand("editcontinent -add 2 Asia");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Germany 2");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add France Germany");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Germany France");
		assertFalse(d_model_factory.getMapModel().validatemap());
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.equals("Country 'France' is not fully connected to all other countries"));
	}

	/**
	 * test validatemap command - countries not connected. // (1) map validation –
	 * map is a connected graph;
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_validatemap_countries_not_connected_2() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Italy 1 -add France 1");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Italy France -add France Italy");
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 2 North_America");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 2 -add USA 2");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA -add USA Canada");
		assertFalse(d_model_factory.getMapModel().validatemap());
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.equals("Country 'Italy' is not fully connected to all other countries"));
	}

	/**
	 * test validatemap command - countries not connected. // (2) continent
	 * validation – continent is a connected subgraph;
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_validatemap_continent_subgraph_not_connected() throws Exception {
		String l_msg;
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Italy 1 -add France 1");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Italy France");
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 2 North_America");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 2 -add USA 2");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA -add USA Canada");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Italy Canada -add France USA");

		assertFalse(d_model_factory.getMapModel().validatemap());
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.equals("Country 'France' is not fully connected to all other countries"));
	}
}
