package soen6441.team01.warzone.view;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.controller.MapEditorController;
import soen6441.team01.warzone.controller.ControllerFactory;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.LogEntryBuffer;

/**
 * Supports all the test methods used to test class MapEditorConsoleView
 * 
 *
 */
public class MapEditorConsoleTest {

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
	 * test showmap command
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_showmap_command_1() throws Exception {
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 1 Europe");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Italy 1 -add France 1");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Italy France");
		d_map_editor_controller.processMapEditorCommand("editcontinent -add 2 North_America");
		d_map_editor_controller.processMapEditorCommand("editcountry -add Canada 2 -add USA 2");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Canada USA -add USA Canada");
		d_map_editor_controller.processMapEditorCommand("editneighbor -add Italy Canada -add France USA");

		MapEditorConsole l_view = (MapEditorConsole)d_map_editor_controller.getMapEditorView();
		l_view.showmap(d_model_factory.getMapModel());
		ArrayList<String> l_showmap = l_view.d_last_showmap;

		assertTrue(l_showmap.get(0).contains("Europe"));
		assertTrue(l_showmap.get(1).contains("Italy"));
		assertTrue(l_showmap.get(2).contains("France, Canada"));
		assertTrue(l_showmap.get(3).contains("France"));
		assertTrue(l_showmap.get(4).contains("USA"));
		assertTrue(l_showmap.get(5).contains("North_America"));
		assertTrue(l_showmap.get(6).contains("Canada"));
		assertTrue(l_showmap.get(7).contains("[USA]"));
		assertTrue(l_showmap.get(8).contains("USA"));
		assertTrue(l_showmap.get(9).contains("[Canada]"));
	}
}
