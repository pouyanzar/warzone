package soen6441.team01.warzone.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.GameEngine;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.LogEntryBuffer;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.MapIoAdaptor;
import soen6441.team01.warzone.model.MapIoConquest;
import soen6441.team01.warzone.model.MapIoDomination;
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
	public LogEntryBuffer d_msg = null;

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
		d_msg = (LogEntryBuffer) d_model_factory.getUserMessageModel();
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

		// non-existant map file
		d_map_editor_controller.processMapEditorCommand("loadmap " + d_MAP_DIR + "canada/quebac.map");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Error loading map file"));

		// invalid random text file
		d_map_editor_controller.processMapEditorCommand("loadmap " + d_MAP_DIR + "canada_invalid/canada.map");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("loadmap error - map is not a valid map."));
	}

	/**
	 * test loadmap valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processMapEditorCommand_loadmap_valid() throws Exception {
		String l_msg;
		Phase next_phase = d_map_editor_controller
				.processMapEditorCommand("loadmap " + d_MAP_DIR + "canada/canada.map");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("loadmap processed successfully"));
		assertTrue(next_phase instanceof GameStartupController);
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

	/**
	 * test tournament valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processCommand_tournament_valid_1() throws Exception {
		String l_msg;
		l_msg = "tournament -M ./src/test/resources/maps/world_small/world_small.map -P benevolent, aggressive -G 1 -D 10";
		d_map_editor_controller.processMapEditorCommand(l_msg);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("tournament command processed successfully"));

		l_msg = "tournament -M ./src/test/resources/maps/world_small/world_small.map,"
				+ " ./src/test/resources/maps/canada/canada.map ," + " ./src/test/resources/maps/usa8/usa8regions.map"
				+ " -P benevolent ,benevolent -G 1 -D 10";
		d_map_editor_controller.processMapEditorCommand(l_msg);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("tournament command processed successfully"));
	}

	/**
	 * test tournament valid commands
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processCommand_tournament_valid_2() throws Exception {
		String l_msg;
		l_msg = "tournament  -D 10 -G 1 -P benevolent, benevolent -M ./src/test/resources/maps/world_small/world_small.map";
		d_map_editor_controller.processMapEditorCommand(l_msg);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("tournament command processed successfully"));
	}

	/**
	 * test tournament invalid command: invalid
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processCommand_tournament_invalid_1() throws Exception {
		String l_msg;
		d_map_editor_controller
				.processMapEditorCommand("tournament -M ./world_small/world_small.map -P benevolent -G 1 -D 10");
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid tournament command"));
		assertTrue(Utl.logContains("invalid: Error loading map file"));

		l_msg = "tournament -M ./src/test/resources/maps/world_small/world_small.map -P bene -G 1 -D 10";
		d_map_editor_controller.processMapEditorCommand(l_msg);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid tournament command"));
		assertTrue(Utl.logContains("bene - invalid"));

		l_msg = "tournament -M ./src/test/resources/maps/world_small/world_small.map -P benevolent -G -1 -D 10";
		d_map_editor_controller.processMapEditorCommand(l_msg);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid tournament command"));
		assertTrue(Utl.logContains(": -1 error: invalid number of games should be between 1 and 5"));

		l_msg = "tournament -M ./src/test/resources/maps/world_small/world_small.map -P benevolent -G 1 -D -10";
		d_map_editor_controller.processMapEditorCommand(l_msg);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid tournament command"));
		assertTrue(Utl.logContains("invalid number of max turns per game, should be between 10 and 50"));
	}

	/**
	 * test tournament invalid command: invalid
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processCommand_tournament_invalid_2() throws Exception {
		String l_msg;
		l_msg = "tournament -M ./src/test/resources/maps/world_small/world_small.map -P benevolent";
		d_map_editor_controller.processMapEditorCommand(l_msg);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("Invalid tournament command"));
		assertTrue(Utl.logContains("0 error: invalid number of games"));
		assertTrue(Utl.logContains("error: invalid number of max turns per game"));
	}

	/**
	 * deletes a file
	 * 
	 * @param p_filename filename to delete
	 */
	private void deleteFile(String p_filename) {
		try {
			File file = new File(p_filename);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * test savemap domination to domination 
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processCommand_savemap_valid_1() throws Exception {
		String l_msg;
		String l_save_fn = "";
		d_map_editor_controller.processMapEditorCommand("editmap " + d_MAP_DIR + "canada/canada.map");

		l_save_fn = "\\tmp\\savemap_domdom_1.map";
		deleteFile(l_save_fn);
		d_map_editor_controller.processMapEditorCommand("savemap " + l_save_fn);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("map saved successfully"));
		IMapModel l_map = Map.loadMapFromFile(l_save_fn, d_model_factory);
		assertTrue(l_map.getContinents().size() == 6);
		assertTrue(l_map.getCountries().size() == 31);
		List<String> l_records = Files.readAllLines(new File(l_save_fn).toPath(), Charset.defaultCharset());
		assertTrue( MapIoDomination.isDominationFileFormat(l_records));
		
		l_save_fn = "\\tmp\\savemap_domdom_2.map";
		deleteFile(l_save_fn);
		d_map_editor_controller.processMapEditorCommand("savemap -d " + l_save_fn);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("map saved successfully"));
		l_map = Map.loadMapFromFile(l_save_fn, d_model_factory);
		assertTrue(l_map.getContinents().size() == 6);
		assertTrue(l_map.getCountries().size() == 31);
		l_records = Files.readAllLines(new File(l_save_fn).toPath(), Charset.defaultCharset());
		assertTrue( MapIoDomination.isDominationFileFormat(l_records));
	}

	/**
	 * test savemap conquest to domination 
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processCommand_savemap_valid_2() throws Exception {
		String l_msg;
		String l_save_fn = "";
		d_map_editor_controller.processMapEditorCommand("editmap " + d_MAP_DIR + "conquest_maps/Earth.map");

		l_save_fn = "\\tmp\\savemap_condom_1.map";
		deleteFile(l_save_fn);
		d_map_editor_controller.processMapEditorCommand("savemap -d " + l_save_fn);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("map saved successfully"));
		IMapModel l_map = Map.loadMapFromFile(l_save_fn, d_model_factory);
		assertTrue(l_map.getContinents().size() == 7);
		assertTrue(l_map.getCountries().size() == 42);
		List<String> l_records = Files.readAllLines(new File(l_save_fn).toPath(), Charset.defaultCharset());
		assertTrue( MapIoDomination.isDominationFileFormat(l_records));
	}

	/**
	 * test savemap conquest to conquest 
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processCommand_savemap_valid_3() throws Exception {
		String l_msg;
		String l_save_fn = "";
		d_map_editor_controller.processMapEditorCommand("editmap " + d_MAP_DIR + "conquest_maps/Earth.map");

		l_save_fn = "\\tmp\\savemap_concon_1.map";
		deleteFile(l_save_fn);
		d_map_editor_controller.processMapEditorCommand("savemap -c " + l_save_fn);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("map saved successfully"));
		IMapModel l_map = Map.loadMapFromFile(l_save_fn, d_model_factory);
		assertTrue(l_map.getContinents().size() == 7);
		assertTrue(l_map.getCountries().size() == 42);
		List<String> l_records = Files.readAllLines(new File(l_save_fn).toPath(), Charset.defaultCharset());
		assertTrue( MapIoConquest.isConquestFileFormat(l_records));
	}
	
	/**
	 * test savemap domination to conquest 
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_processCommand_savemap_valid_4() throws Exception {
		String l_msg;
		String l_save_fn = "";
		d_map_editor_controller.processMapEditorCommand("editmap " + d_MAP_DIR + "canada/canada.map");

		l_save_fn = "\\tmp\\savemap_domcon_1.map";
		deleteFile(l_save_fn);
		d_map_editor_controller.processMapEditorCommand("savemap -c " + l_save_fn);
		l_msg = d_msg.getLastMessageAndClear().d_message;
		assertTrue(l_msg.contains("map saved successfully"));
		IMapModel l_map = Map.loadMapFromFile(l_save_fn, d_model_factory);
		assertTrue(l_map.getContinents().size() == 6);
		assertTrue(l_map.getCountries().size() == 31);
		List<String> l_records = Files.readAllLines(new File(l_save_fn).toPath(), Charset.defaultCharset());
		assertTrue( MapIoConquest.isConquestFileFormat(l_records));
	}
	
}
