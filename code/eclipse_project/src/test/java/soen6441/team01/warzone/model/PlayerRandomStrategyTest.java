package soen6441.team01.warzone.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.entities.CardType;

/**
 * Tests for Player model class
 * 
 */
public class PlayerRandomStrategyTest {

	public ModelFactory d_model_factory = null;
	ICountryModel d_uk = null;
	ICountryModel d_france = null;
	ICountryModel d_spain = null;
	ICountryModel d_germany = null;
	ICountryModel d_italy = null;
	Player d_player1 = null;
	Player d_player2 = null;
	PlayerRandomStrategy d_strategy1 = null;

	/**
	 * setup the environment for testing
	 * 
	 * @throws Exception unexpected error
	 */
	@Before
	public void setupGameStartupController() throws Exception {
		d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		IMapModel l_map = d_model_factory.getMapModel();

		Continent l_europe = new Continent(1, "Europe", 10);
		l_map.addContinent(l_europe);
		d_uk = l_map.addCountry("UK", 1);
		d_france = l_map.addCountry("France", 1);
		d_spain = l_map.addCountry("Spain", 1);
		d_germany = l_map.addCountry("Germany", 1);
		d_italy = l_map.addCountry("Italy", 1);

		l_map.addNeighbor("UK", "France");
		l_map.addNeighbor("France", "UK");
		l_map.addNeighbor("France", "Spain");
		l_map.addNeighbor("France", "Germany");
		l_map.addNeighbor("Spain", "France");
		l_map.addNeighbor("Germany", "France");
		l_map.addNeighbor("Germany", "Italy");
		l_map.addNeighbor("Italy", "Germany");

		Map.refreshCountriesOfAllContinents(l_map);

		d_model_factory.getNewGamePlayModel();
		d_player1 = new Player("player1", d_model_factory);
		d_strategy1 = new PlayerRandomStrategy(d_player1, d_model_factory.getUserMessageModel());
		d_player2 = new Player("player2", d_model_factory);
	}

	/**
	 * Test deploy
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_deploy_noreinforcements_player() throws Exception {
		d_player1.setReinforcements(0);
		d_player1.addPlayerCountry(d_uk);
		IOrder l_order = d_strategy1.doRandomDeploy();
		assertTrue(l_order == null);
	}

	/**
	 * Test deploy
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_deploy_nocountries_player() throws Exception {
		d_player1.setReinforcements(7);
		IOrder l_order = d_strategy1.doRandomDeploy();
		assertTrue(l_order == null);
	}

	/**
	 * Test deploy 1 country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_deploy_1countries_player() throws Exception {
		d_player1.setReinforcements(7);
		d_player1.addPlayerCountry(d_uk);
		IOrder l_order = d_strategy1.doRandomDeploy();
		l_order.execute();
		ArrayList<ICountryModel> l_countries = PlayerStrategyUtl
				.countriesWithArmies(d_model_factory.getMapModel().getCountries(), 0, true);
		assertTrue(l_countries.size() == 1);
		assertTrue(l_countries.get(0) == d_uk);
		assertTrue(d_uk.getArmies() == 7);
		l_order = d_strategy1.doRandomDeploy();
		assertTrue(l_order == null);
	}

	/**
	 * Test deploy 2 countries
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_deploy_2countries_player() throws Exception {
		d_player1.setReinforcements(3);
		d_player1.addPlayerCountry(d_france);
		d_player1.addPlayerCountry(d_italy);
		IOrder l_order = d_strategy1.doRandomDeploy();
		l_order.execute();
		ArrayList<ICountryModel> l_countries = PlayerStrategyUtl
				.countriesWithArmies(d_model_factory.getMapModel().getCountries(), 0, true);
		assertTrue(l_countries.size() == 1);
		assertTrue(l_countries.get(0) == d_france || l_countries.get(0) == d_italy);
	}

	/**
	 * Test advance - attack
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_attack_noneighbors() throws Exception {
		d_player1.addPlayerCountry(d_italy);
		d_italy.setArmies(3);
		d_player1.setReinforcements(3);
		IOrder l_order = d_strategy1.doRandomAttack();
		assertTrue(l_order == null);
	}

	/**
	 * Test advance - attack
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_attack_nocountries() throws Exception {
		d_player2.addPlayerCountry(d_germany);
		d_italy.setArmies(0);
		d_player1.setReinforcements(3);
		IOrder l_order = d_strategy1.doRandomAttack();
		assertTrue(l_order == null);
	}

	/**
	 * Test advance - attack
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_attack_nocountrieswitharmies() throws Exception {
		d_player1.addPlayerCountry(d_italy);
		d_player2.addPlayerCountry(d_germany);
		d_italy.setArmies(0);
		d_player1.setReinforcements(3);
		IOrder l_order = d_strategy1.doRandomAttack();
		assertTrue(l_order == null);
	}

	/**
	 * Test advance - attack
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_attack_noneighborswitharmies() throws Exception {
		d_player1.addPlayerCountry(d_italy);
		d_player2.addPlayerCountry(d_germany);
		d_italy.setArmies(3);
		d_germany.setArmies(0);
		d_player1.setReinforcements(3);
		IOrder l_order = d_strategy1.doRandomAttack();
		assertTrue(l_order == null);
	}

	/**
	 * Test advance - attack
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_attack_1() throws Exception {
		d_player1.addPlayerCountry(d_italy);
		d_player2.addPlayerCountry(d_germany);
		d_italy.setArmies(10);
		d_germany.setArmies(3);
		IOrder l_order = d_strategy1.doRandomAttack();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_italy.getArmies() == 0);
		assertTrue(d_germany.getArmies() > 0);
		assertTrue(d_italy.getOwner() == d_player1);
		assertTrue(d_germany.getOwner() == d_player1);
	}

	/**
	 * Test advance - attack
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_attack_2() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addPlayerCountry(d_italy);
		d_player2.addPlayerCountry(d_germany);
		d_player2.addPlayerCountry(d_france);
		d_uk.setArmies(4);
		d_italy.setArmies(7);
		d_germany.setArmies(3);
		d_france.setArmies(0);
		IOrder l_order = d_strategy1.doRandomAttack();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_italy.getArmies() == 0);
		assertTrue(d_germany.getArmies() > 0);
		assertTrue(d_uk.getArmies() == 4);
		assertTrue(d_france.getArmies() == 0);
		assertTrue(d_uk.getOwner() == d_player1);
		assertTrue(d_italy.getOwner() == d_player1);
		assertTrue(d_germany.getOwner() == d_player1);
		assertTrue(d_france.getOwner() == d_player2);
	}

	/**
	 * Test advance - attack
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_attack_3() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addPlayerCountry(d_italy);
		d_player2.addPlayerCountry(d_germany);
		d_player2.addPlayerCountry(d_france);
		d_uk.setArmies(18);
		d_italy.setArmies(7);
		d_germany.setArmies(0);
		d_france.setArmies(3);
		IOrder l_order = d_strategy1.doRandomAttack();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_italy.getArmies() == 7);
		assertTrue(d_germany.getArmies() == 0);
		assertTrue(d_uk.getArmies() == 0);
		assertTrue(d_france.getArmies() > 0);

		assertTrue(d_uk.getOwner() == d_player1);
		assertTrue(d_italy.getOwner() == d_player1);
		assertTrue(d_germany.getOwner() == d_player2);
		assertTrue(d_france.getOwner() == d_player1);
	}

	/**
	 * Test advance - move
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_move_nocountries() throws Exception {
		IOrder l_order = d_strategy1.doRandomMove();
		assertTrue(l_order == null);
	}

	/**
	 * Test advance - move
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_move_noarmies() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_uk.setArmies(0);
		IOrder l_order = d_strategy1.doRandomMove();
		assertTrue(l_order == null);
	}

	/**
	 * Test advance - move
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_move_noneigbbors() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_uk.setArmies(8);
		IOrder l_order = d_strategy1.doRandomMove();
		assertTrue(l_order == null);
	}

	/**
	 * Test advance - move
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_move_me_neigbbors_1() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addPlayerCountry(d_france);
		d_uk.setArmies(8);
		d_france.setArmies(0);
		IOrder l_order = d_strategy1.doRandomMove();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_uk.getArmies() == 0);
		assertTrue(d_france.getArmies() == 8);
		assertTrue(d_uk.getOwner() == d_player1);
		assertTrue(d_france.getOwner() == d_player1);
	}
	
	/**
	 * Test advance - move
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_move_me_neigbbors_2() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addPlayerCountry(d_france);
		d_uk.setArmies(8);
		d_france.setArmies(4);
		IOrder l_order = d_strategy1.doRandomMove();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_uk.getArmies() == 0 || d_france.getArmies() == 0);
		assertTrue(d_uk.getArmies() == 12 || d_france.getArmies() == 12);
		assertTrue(d_uk.getOwner() == d_player1);
		assertTrue(d_france.getOwner() == d_player1);
	}
	
	/**
	 * Test advance - move
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_move_opp_neigbbors() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player2.addPlayerCountry(d_france);
		d_uk.setArmies(8);
		d_france.setArmies(0);
		IOrder l_order = d_strategy1.doRandomMove();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_uk.getArmies() == 0);
		assertTrue(d_france.getArmies() == 8);
		assertTrue(d_uk.getOwner() == d_player1);
		assertTrue(d_france.getOwner() == d_player1);
	}
	
	/**
	 * Test advance - move
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_move_opp_neigbbors_w_army() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player2.addPlayerCountry(d_france);
		d_uk.setArmies(8);
		d_france.setArmies(1);
		IOrder l_order = d_strategy1.doRandomMove();
		assertTrue(l_order == null);
	}
	
	/**
	 * Test card bomb 
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_card_bomb_1() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addCard(new Card(CardType.bomb));
		d_player2.addPlayerCountry(d_france);
		d_strategy1 = new PlayerRandomStrategy(d_player1, d_model_factory.getUserMessageModel());
		d_uk.setArmies(8);
		d_france.setArmies(10);
		IOrder l_order = d_strategy1.tryBomb();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_uk.getArmies() == 8);
		assertTrue(d_france.getArmies() == 5);
		assertTrue(d_uk.getOwner() == d_player1);
		assertTrue(d_france.getOwner() == d_player2);
	}

	/**
	 * Test card bomb - no armies on opponent country
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_card_bomb_2() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addCard(new Card(CardType.bomb));
		d_player2.addPlayerCountry(d_france);
		d_strategy1 = new PlayerRandomStrategy(d_player1, d_model_factory.getUserMessageModel());
		d_uk.setArmies(8);
		d_france.setArmies(0);
		IOrder l_order = d_strategy1.tryBomb();
		assertTrue(l_order == null);
	}


	/**
	 * Test card bomb - no other players
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_card_bomb_3() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addCard(new Card(CardType.bomb));
		d_uk.setArmies(8);
		IOrder l_order = d_strategy1.tryBomb();
		assertTrue(l_order == null);
	}

	/**
	 * Test card bomb - armies on country with no owner (e.g. blockade)
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_card_bomb_4() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addCard(new Card(CardType.bomb));
		d_strategy1 = new PlayerRandomStrategy(d_player1, d_model_factory.getUserMessageModel());
		d_uk.setArmies(8);
		d_france.setArmies(1);
		IOrder l_order = d_strategy1.tryBomb();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_uk.getArmies() == 8);
		assertTrue(d_france.getArmies() == 0);
		assertTrue(d_uk.getOwner() == d_player1);
		assertTrue(d_france.getOwner() == null);
	}

	/**
	 * Test card blockade 
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_card_blockade_1() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addCard(new Card(CardType.blockade));
		d_strategy1 = new PlayerRandomStrategy(d_player1, d_model_factory.getUserMessageModel());
		d_uk.setArmies(4);
		IOrder l_order = d_strategy1.tryBlockade();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_uk.getArmies() == 12);
		assertTrue(d_uk.getOwner() == null);
	}

	/**
	 * Test card airlift 
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_card_airlift_1() throws Exception {
		d_player1.addPlayerCountry(d_uk);
		d_player1.addPlayerCountry(d_italy);
		d_player1.addCard(new Card(CardType.airlift));
		d_strategy1 = new PlayerRandomStrategy(d_player1, d_model_factory.getUserMessageModel());

		d_uk.setArmies(4);
		d_italy.setArmies(0);
		IOrder l_order = d_strategy1.tryAirlift();
		assertTrue(l_order != null);
		l_order.execute();
		assertTrue(d_uk.getArmies() == 0);
		assertTrue(d_italy.getArmies() == 4);
	}

	/**
	 * Test card negotiate 
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_card_negotiate_1() throws Exception {
		d_model_factory.getGamePlayModel().addPlayer(d_player1);
		d_model_factory.getGamePlayModel().addPlayer(d_player2);
		d_player1.addPlayerCountry(d_uk);
		d_player2.addPlayerCountry(d_italy);
		d_player1.addCard(new Card(CardType.diplomacy));
		d_strategy1 = new PlayerRandomStrategy(d_player1, d_model_factory.getUserMessageModel());
		IOrder l_order = d_strategy1.tryNegotiate();
		assertTrue(l_order != null);
	}

	/**
	 * Test card negotiate 
	 * 
	 * @throws Exception when there is an exception
	 */
	@Test
	public void test_card_negotiate_2() throws Exception {
		d_model_factory.getGamePlayModel().addPlayer(d_player1);
		d_player1.addPlayerCountry(d_uk);
		d_player1.addCard(new Card(CardType.diplomacy));
		d_strategy1 = new PlayerRandomStrategy(d_player1, d_model_factory.getUserMessageModel());
		IOrder l_order = d_strategy1.tryNegotiate();
		assertTrue(l_order == null);
	}
}
