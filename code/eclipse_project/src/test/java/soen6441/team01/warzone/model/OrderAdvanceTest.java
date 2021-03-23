package soen6441.team01.warzone.model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.controller.ControllerFactory;
import soen6441.team01.warzone.controller.IssueOrderController;
import soen6441.team01.warzone.controller.MapEditorController;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.view.ViewFactory;

/**
 * Tests for Player model class
 * 
 * @author Nazanin
 *
 */

public class OrderAdvanceTest {
	public ModelFactory d_model_factory = null;
	public LogEntryBuffer d_msg = null;
	
	
	private String d_MAP_DIR = "./src/test/resources/maps/";
	public MapEditorController d_map_editor_controller = null;
	
	public IssueOrderController d_gameplay_controller = null;
	public IGamePlayModel d_gameplay = null;
	public ViewFactory d_view_factory = null;
	public ControllerFactory d_controller_factory = null;
	public IContinentModel d_continent = null;
	public ICountryModel d_country = null;
	
	public Player d_player = null;

	
	/**
	 * setup the environment for testing
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupGameStartupController() throws Exception {
//		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
//		d_msg = (LogEntryBuffer) d_model_factory.getUserMessageModel();
		
		
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		d_gameplay = d_model_factory.getNewGamePlayModel();
		d_view_factory = ViewFactory.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		d_controller_factory = new ControllerFactory(d_model_factory, d_view_factory);
		d_gameplay_controller = (IssueOrderController) d_controller_factory.getIssueOrderController();
		d_msg = (LogEntryBuffer) d_model_factory.getUserMessageModel();
		d_continent = new Continent(1, "North_America", 3);
		d_country = new Country(1, "Canada", d_continent, 0, 0, d_model_factory);
		d_player = new Player("John", d_model_factory);
		
	}

	/**
	 * Test if advanced countries added to the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_advance_1() throws Exception {
		String l_msg;
		Player l_player = new Player("Nazanin", d_model_factory);
		l_player.setReinforcements(7);
		Continent l_america = new Continent(1, "America", 10);
		Country l_canada = new Country(1, "Canada", l_america, 250, 250, d_model_factory);
		Country l_usa = new Country(2, "USA", l_america, 250, 250, d_model_factory);
		l_canada.addNeighbor(l_usa);
		l_usa.addNeighbor(l_canada);
		l_canada.setArmies(5);
		l_usa.setArmies(2);

		l_player.addPlayerCountry(l_canada);
		OrderAdvance l_advance = new OrderAdvance(l_player,l_canada,l_usa,5);
		l_msg = l_advance.execute();
		//l_msg = d_msg.getLastMessageAndClear().d_message;
		System.out.println(l_msg);		 
		//assertTrue(l_msg.contains("Number of armies are greater than source country's armies"));
		for(ICountryModel a:l_player.getPlayerCountries())
			 System.out.println(a.getName());
		//assertTrue( l_player.getPlayerCountries().contains(l_usa));
	}
}


