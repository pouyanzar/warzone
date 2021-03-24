package soen6441.team01.warzone.view;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.controller.MapEditorController;
import soen6441.team01.warzone.controller.ControllerFactory;
import soen6441.team01.warzone.controller.IssueOrderController;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.Player;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.Country;
import soen6441.team01.warzone.model.LogEntryBuffer;
import soen6441.team01.warzone.view.contracts.IGamePlayView;
import soen6441.team01.warzone.view.contracts.IMapEditorView;

/**
 * Supports all the test methods used to test class MapEditorConsoleView
 * 
 *
 */
public class GamePlayConsoleTest {

	public ModelFactory d_model_factory = null;
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
	 * setup the environment for testing of MapEditorController
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupMapEditController() throws Exception {
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		d_map = d_model_factory.getMapModel();
		d_gameplay = d_model_factory.getNewGamePlayModel();
		d_view_factory = ViewFactory.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		d_controller_factory = new ControllerFactory(d_model_factory, d_view_factory);
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
	 * test showmap command - neighbor with null owner - tests that is doesn't give
	 * an exception
	 * 
	 * @throws Exception unexpected error
	 */
	@Test
	public void test_showmap_command_1() throws Exception {
		String l_msg;
		d_player.addPlayerCountry(d_canada);
		d_canada.addNeighbor(d_us);

		IssueOrderController d_gameplay_controller = (IssueOrderController) d_controller_factory
				.getIssueOrderController();
		IGamePlayView l_view = (IGamePlayView) d_view_factory.getGamePlayConsoleView(d_gameplay_controller);
		ArrayList<ICountryModel> l_countries = d_player.getPlayerCountries();
		for (ICountryModel l_country : l_countries) {
			l_view.showCountry(l_country);
		}
	}
}
