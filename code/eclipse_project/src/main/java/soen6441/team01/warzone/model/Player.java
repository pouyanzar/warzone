package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGameplayOrderDatasource;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.entities.*;

/**
 * The class Player provides the implementation of a Warzone human player
 * 
 */
public class Player implements IPlayerModel {

	private String d_name;
	private int d_reinforcements = 0;
	private IGameplayOrderDatasource d_order_datasource;
	private ArrayList<ICountryModel> d_player_countries;
	private ArrayList<IOrder> d_order_list;
	private ArrayList<Card> d_cards;
	private ModelFactory d_factory_model = null;
	private boolean d_done_turn = false;

	/**
	 * Constructor for class Player. Don't use issue_order if using this constructor
	 * as there is no way to get an order without specifying
	 * IGameplayOrderDatasource.
	 * 
	 * @param p_name          the name of player
	 * @param p_factory_model the model factory to use when needed
	 * @throws Exception general exceptions
	 */
	public Player(String p_name, ModelFactory p_factory_model) throws Exception {
		this(p_name, null, p_factory_model);
	}

	/**
	 * Constructor for class Player
	 * 
	 * @param p_name             the name of player
	 * @param p_order_datasource used to get the player commands during
	 *                           issue_order()
	 * @param p_factory_model    the model software factory
	 * @throws Exception general exceptions
	 */
	public Player(String p_name, IGameplayOrderDatasource p_order_datasource, ModelFactory p_factory_model)
			throws Exception {
		super();
		setName(p_name);
		d_reinforcements = 0;
		d_order_datasource = p_order_datasource;
		d_player_countries = new ArrayList<ICountryModel>();
		d_order_list = new ArrayList<IOrder>();
		d_cards = new ArrayList<Card>();
		d_factory_model = p_factory_model;
	}

	/**
	 * getter method for player's name
	 * 
	 * @return d_name the name of player
	 */
	public String getName() {
		return d_name;
	}

	/**
	 * setter for player's name
	 * 
	 * @throws Exception in case of invalid name
	 */
	public void setName(String p_name) throws Exception {
		d_name = p_name;
	}

	/**
	 * gets the number of player's army
	 * 
	 * @return d_player_army the number of player's army
	 */
	public int getReinforcements() {
		return d_reinforcements;
	}

	/**
	 * set the number of player's army
	 * 
	 * @throws Exception when there is an exception
	 */
	public void setReinforcements(int p_number_of_army) throws Exception {
		d_reinforcements = p_number_of_army;
	}

	/**
	 * copies the orders from the specified player. used mainly when copying the
	 * orders from cloned players used during the issue_orders() phase. Note: the
	 * specified player must have the same name as this player.
	 * 
	 * @param p_cloned_player the player to get the orders to copy from.
	 * @throws Exception unexpected error
	 */
	public void copyOrders(IPlayerModel p_cloned_player) throws Exception {
		ArrayList<IOrder> l_clone_orders = p_cloned_player.getOrders();
		for (IOrder l_clone_order : l_clone_orders) {
			l_clone_order.setPlayer(this);
			d_order_list.add(l_clone_order);
		}
	}

	/**
	 * add the specified armies (reinforcements) to the specified owned country.
	 * 
	 * @param p_country_name     the name of the country to deploy to
	 * @param p_number_of_armies the number of reinforcement armies to move to the
	 *                           specified country
	 * @return a message that describes the deployment done
	 * @throws Exception if country is not owned, or not enough reinforcements, or
	 *                   unexpected error
	 */
	public String deploy(String p_country_name, int p_number_of_armies) throws Exception {
		ICountryModel l_country = Country.findCountry(p_country_name, d_player_countries);
		if (l_country == null) {
			throw new Exception("Country " + p_country_name + " is not owned by player " + d_name);
		}
		if (p_number_of_armies > d_reinforcements) {
			throw new Exception(d_name + " does not have enough reinforcements (" + d_reinforcements + ") to deploy "
					+ p_number_of_armies + " armies to " + p_country_name);
		}

		// execute the deployment
		int l_armies = l_country.getArmies();
		l_armies += p_number_of_armies;
		l_country.setArmies(l_armies);
		d_reinforcements -= p_number_of_armies;

		// prepare a returning message
		String l_xarmy = "army has";
		if (p_number_of_armies > 1) {
			l_xarmy = "armies have";
		}
		String l_msg = p_number_of_armies + " reinforcement " + l_xarmy + " been deployed to " + p_country_name;
		return l_msg;
	}

	/**
	 * destroys half of the armies on the opponent
	 * 
	 * @param p_country_name the target country
	 * @return a message to show half of the armies destroyed
	 */
	public String bomb(String p_country_name)  {
		ICountryModel l_country = Country.findCountry(p_country_name, d_player_countries);
		l_country.setArmies(l_country.getArmies() / 2);
		String l_msg = "Half number of armies destroyed by bomb";
		return l_msg;
	}

	/**
	 * gets the list of countries the player controls
	 * 
	 * @return d_player_countries the list of countries player controls
	 */
	public ArrayList<ICountryModel> getPlayerCountries() {
		return d_player_countries;
	}

	/**
	 * adds the appropriate country to the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	public void addPlayerCountry(ICountryModel p_country) throws Exception {
		d_player_countries.add(p_country);
		p_country.setOwner(this);
	}

	/**
	 * removes the appropriate country from the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	public void removePlayerCountry(ICountryModel p_country) throws Exception {
		d_player_countries.remove(p_country);
		p_country.setOwner(null);
	}

	/**
	 * gets the list of orders player issues
	 * 
	 * @return d_order_list the list of orders player issues
	 */
	public ArrayList<IOrder> getOrders() {
		return d_order_list;
	}

	/**
	 * adds a new order to player's order list
	 * 
	 * @throws Exception unexpected error
	 */
	public void issue_order() throws Exception {
		IOrder l_order = d_order_datasource.getOrder(this);
		if (l_order != null) {
			d_order_list.add(l_order);
			d_done_turn = false;
		} else {
			d_done_turn = true;
		}
	}

	/**
	 * checks if the player is done with their turn
	 * 
	 * @return true if the player is done with their turn, false otherwise
	 */
	public boolean isDoneTurn() {
		return d_done_turn;
	}

	/**
	 * sets if the player is done with their turn or not
	 * 
	 * @param p_done_turn the value to set the done turn to
	 */
	public void setDoneTurn(boolean p_done_turn) {
		d_done_turn = p_done_turn;
	}

	/**
	 * finds the first order in order list returns it and removes it from the order
	 * list
	 * 
	 * @return l_first_order the first order in order list, null if there are no
	 *         more orders
	 */
	public IOrder next_order() {
		if (d_order_list.size() < 1) {
			return null;
		}
		IOrder l_next_order = d_order_list.get(0);
		d_order_list.remove(0);
		return l_next_order;
	}

	/**
	 * find a player given a name
	 * 
	 * @param p_name    the country id of the neighboring country to find
	 * @param p_players list of countries to search from
	 * @return null if not found, otherwise return the first player with the
	 *         specified name
	 */
	public static IPlayerModel FindPlayer(String p_name, ArrayList<IPlayerModel> p_players) {
		for (IPlayerModel l_xplayer : p_players) {
			if (l_xplayer.getName().equals(p_name)) {
				return l_xplayer;
			}
		}
		return null;
	}

	/**
	 * Create a new player cloned from the existing player.
	 * 
	 * @param l_map the map to relate owned countries to
	 * @return a deep copy of the current player.
	 * @throws Exception unexpected errors
	 */
	public IPlayerModel deepClonePlayer(IMapModel l_map) throws Exception {
		Player l_player = new Player(d_name, d_order_datasource, d_factory_model);
		l_player.setReinforcements(d_reinforcements);
		for (ICountryModel l_src_country : d_player_countries) {
			ICountryModel l_country = Country.findCountry(l_src_country.getId(), l_map.getCountries());
			if (l_country == null) {
				throw new Exception("Internal error cloning player");
			}
			l_player.addPlayerCountry(l_country);
		}
		l_player.setCards(d_cards);
		return l_player;
	}

	/**
	 * Getter method to return current player's cards
	 * 
	 * @return d_cards the list of current player's card
	 */
	public ArrayList<Card> getCards() {
		return d_cards;
	}

	/**
	 * Setter method to set current player's cards
	 * 
	 * @param p_cards cards to set
	 */
	public void setCards(ArrayList<Card> p_cards) {
		d_cards = p_cards;
	}

	/**
	 * Method to add new card into the list of cards
	 * 
	 * @param p_card the new card
	 */
	public void addCard(Card p_card) {
		d_cards.add(p_card);
	}

	/**
	 * Checks if the card exist inside the current player's card list
	 * 
	 * @param p_card desired The card to be checked if it exists in player's card
	 *               list
	 * @return true if the card exist and false otherwise
	 */
	public boolean hasCard(Card p_card) {
		if (d_cards.contains(p_card)) {
			return true;
		} else {
			return false;
		}
	}
}
