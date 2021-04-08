package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGameplayOrderDatasource;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IOrder;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerStrategy;
import soen6441.team01.warzone.model.entities.*;

/**
 * The class Player provides the implementation of a Warzone human player
 * 
 */
public class Player implements IPlayerModel {

	private String d_name;
	private int d_reinforcements = 0;
	private ArrayList<ICountryModel> d_countries;
	private ArrayList<IOrder> d_order_list;
	private ArrayList<Card> d_cards;
	private ModelFactory d_factory_model = null;
	private boolean d_done_turn = false;
	private ArrayList<IPlayerModel> d_diplomacy;
	private IPlayerStrategy d_strategy;

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
		super();
		setName(p_name);
		d_reinforcements = 0;
		d_countries = new ArrayList<ICountryModel>();
		d_order_list = new ArrayList<IOrder>();
		d_cards = new ArrayList<Card>();
		d_factory_model = p_factory_model;
		d_diplomacy = new ArrayList<IPlayerModel>();
		d_strategy = null; // must be set before player can be used
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
	 * Used mainly when cloning or working with cloned players
	 * 
	 * @return the player's models which basically contain all the player map
	 *         information
	 */
	public ModelFactory getPlayerModelFactory() {
		return d_factory_model;
	}

	/**
	 * gets the list of countries the player controls
	 * 
	 * @return d_player_countries the list of countries player controls
	 */
	public ArrayList<ICountryModel> getPlayerCountries() {
		return d_countries;
	}

	/**
	 * set the players' gameplay strategy
	 * 
	 * @param p_player_strategy the player's strategy
	 */
	public void setStrategy(IPlayerStrategy p_player_strategy) {
		d_strategy = p_player_strategy;
	}

	/**
	 * @return the current players strategy
	 */
	public IPlayerStrategy getStrategy() {
		return d_strategy;
	}

	/**
	 * adds the appropriate country to the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	public void addPlayerCountry(ICountryModel p_country) throws Exception {
		d_countries.add(p_country);
		p_country.setOwner(this);
	}

	/**
	 * removes the appropriate country from the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	public void removePlayerCountry(ICountryModel p_country) throws Exception {
		ICountryModel l_country = Country.findCountry(p_country.getName(), d_countries);
		d_countries.remove(l_country);
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
	 * adds player to the list of diplomatic friends (can't attack each other for 1
	 * turn).
	 * 
	 * @param p_friendly_player the player to have diplomatic relations with
	 */
	public void addDiplomacy(IPlayerModel p_friendly_player) {
		d_diplomacy.add(p_friendly_player);
	}

	/**
	 * clear all diplomacies
	 */
	public void clearAllDiplomacy() {
		d_diplomacy.clear();
	}

	/**
	 * checks if another play is diplomatic with us
	 * 
	 * @param p_other_player the other player to check
	 * @return true if diplomatic
	 */
	public boolean isDiplomatic(IPlayerModel p_other_player) {
		for (IPlayerModel l_player : d_diplomacy) {
			if (l_player.getName().equals(p_other_player.getName())) {
				return true;
			}
		}
		return false;
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
	 * Method to delete/remove a card from the player
	 * 
	 * @param p_card the card type to remove
	 */
	public void removeCard(CardType p_card) {
		for (int idx = 0; idx < d_cards.size(); idx++) {
			if (d_cards.get(idx).getCardType() == p_card) {
				d_cards.remove(idx);
			}
		}
	}

	/**
	 * Checks if the card exist inside the current player's card list
	 * 
	 * @param p_card desired The card to be checked if it exists in player's card
	 *               list
	 * @return true if the card exist and false otherwise
	 */
	public boolean hasCard(CardType p_card) {
		for (Card l_card : d_cards) {
			if (l_card.getCardType() == p_card) {
				return true;
			}
		}
		return false;
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
			l_clone_order.cloneToPlayer(this);
			d_order_list.add(l_clone_order);
		}
		if (p_cloned_player.isDiplomatic(this)) {
			IPlayerModel l_player = Player.FindPlayer(p_cloned_player.getName(),
					d_factory_model.getGamePlayModel().getPlayers());
			if (l_player != null) {
				l_player.addDiplomacy(this);
			}
		}
	}

	/**
	 * Create a new player cloned from the existing player.
	 * 
	 * @param p_factory_model the new models to use
	 * @return a deep copy of the current player.
	 * @throws Exception unexpected errors
	 */
	public IPlayerModel deepClonePlayer(ModelFactory p_factory_model) throws Exception {
		IMapModel l_map = p_factory_model.getMapModel();
		Player l_player = new Player(d_name, p_factory_model);
		IPlayerStrategy l_strategy = d_strategy.cloneStrategy(l_player);
		l_player.setStrategy(l_strategy);

		// map out the countries
		for (ICountryModel l_src_country : d_countries) {
			ICountryModel l_country = Country.findCountry(l_src_country.getId(), l_map.getCountries());
			if (l_country == null) {
				throw new Exception("Internal error cloning player");
			}
			l_player.addPlayerCountry(l_country);
		}

		l_player.setReinforcements(d_reinforcements);
		l_player.setCards((ArrayList<Card>) d_cards.clone());

		return l_player;
	}

	/**
	 * checks if the current player has won a game by owning all the countries on
	 * the map.
	 * 
	 * @return true = is a winner
	 */
	public boolean isWinner() {
		if (getPlayerCountries().size() == d_factory_model.getMapModel().getCountries().size()) {
			return true;
		}
		return false;
	}

	/**
	 * checks if the current player has lost a game by not owning any countries.
	 * 
	 * @return true = is a winner
	 */
	public boolean isLoser() {
		if (getPlayerCountries().size() < 1) {
			return true;
		}
		return false;
	}

	/**
	 * adds a new order to player's order list
	 * 
	 * @throws Exception unexpected error
	 */
	public void issue_order() throws Exception {
		if (d_strategy == null) {
			throw new Exception("Internal error, player " + getName() + " strategy not specified");
		}
		if (isLoser()) {
			d_done_turn = true;
		} else {
			IOrder l_order = d_strategy.createOrder();
			if (l_order != null) {
				d_order_list.add(l_order);
				d_done_turn = false;
			} else {
				d_done_turn = true;
			}
		}
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

}