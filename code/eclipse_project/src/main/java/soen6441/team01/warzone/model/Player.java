package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IOrderModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerModelView;

/**
 * The class Player manages the information associated to player
 * 
 * @author pouyan
 *
 */
public class Player implements IPlayerModel, IPlayerModelView {

	private String d_name;
	private int d_player_army;
	private IOrderModel d_order;
	private ArrayList<ICountryModel> d_player_countries;
	private ArrayList<IContinentModel> d_player_continents;
	private ArrayList<IOrderModel> d_order_list;

	/**
	 * Constructor for class Player
	 * 
	 * @param d_name the name of player
	 * @throws Exception general exceptions
	 */
	public Player(String d_name) throws Exception {
		super();
		setName(d_name);
		this.d_player_army = 0;
		this.d_player_countries = new ArrayList<ICountryModel>();
		this.d_player_continents = new ArrayList<IContinentModel>();
		this.d_order_list = new ArrayList<IOrderModel>();
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
		this.d_name = p_name;
	}

	/**
	 * gets the number of player's army
	 * 
	 * @return d_player_army the number of player's army
	 */
	public int getPlayerArmy() {
		return d_player_army;
	}

	/**
	 * set the number of player's army
	 * 
	 * @throws Exception when there is an exception
	 */
	public void setPlayerArmy(int p_number_of_army) throws Exception {
		this.d_player_army = p_number_of_army;
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
	}

	/**
	 * removes the appropriate country from the list of countries player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	public void removePlayerCountry(ICountryModel p_country) throws Exception {
		d_player_countries.remove(p_country);
	}

	/**
	 * gets the list of continents the player controls
	 * 
	 * @return d_player_continents the list of continents player controls
	 */
	public ArrayList<IContinentModel> getPlayerContinents() {
		return d_player_continents;
	}

	/**
	 * adds the appropriate continent to the list of continent player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	public void addPlayerContinent(IContinentModel p_continent) throws Exception {
		d_player_continents.add(p_continent);
	}

	/**
	 * removes the appropriate continent from the list of continents player controls
	 * 
	 * @throws Exception when there is an exception
	 */
	public void removePlayerContinent(IContinentModel p_continent) throws Exception {
		d_player_continents.remove(p_continent);
	}

	/**
	 * gets the list of orders player issues
	 * 
	 * @return d_order_list the list of orders player issues
	 */
	public ArrayList<IOrderModel> getOrders() {
		return d_order_list;
	}

	/**
	 * adds the order issued by player to the player's order list
	 */
	public void issue_order() {
		d_order_list.add(d_order);
	}

	/**
	 * finds the first order in order list returns it and removes it from the order
	 * list
	 * 
	 * @return l_first_order the first order in order list
	 */
	public IOrderModel next_order() {
		IOrderModel l_first_order = d_order_list.get(0);
		d_order_list.remove(d_order_list.get(0));
		return l_first_order;
	}

	/**
	 * find a player given a name
	 * 
	 * @param p_name    the country id of the neighboring country to find
	 * @param p_players list of countries to search from
	 * @return null if not found, otherwise return the first player with the specified name
	 */
	public static IPlayerModel FindPlayer(String p_name, ArrayList<IPlayerModel> p_players) {
		for (IPlayerModel l_xplayer : p_players) {
			if (l_xplayer.getName().equals(p_name)) {
				return l_xplayer;
			}
		}
		return null;
	}

}
