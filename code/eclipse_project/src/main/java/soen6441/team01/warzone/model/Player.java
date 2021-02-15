package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IPlayerModelView;

/**
 * The class Player manages the information associated to player
 * @author pouyan
 *
 */
public class Player implements IPlayerModel, IPlayerModelView {

	private String d_name;
	private int d_player_army;
	private ArrayList<ICountryModel> d_player_countries;
	private ArrayList<IContinentModel> d_player_continents;

	/**
	 * Constructor for class Player
	 * @param d_name the name of player
	 */
	public Player(String d_name) throws Exception{
		super();
		setName(d_name);
		this.d_player_army = 0;
		this.d_player_countries = null;
		this.d_player_continents = null;
	}

	/**
	 * getter method for player's name
	 * @return d_name the name of player
	 */
	public String getName() {
		return d_name;
	}

	/**
	 * setter for player's name
	 * @throws Exception in case of invalid name
	 */
	public void setName(String p_name) throws Exception{
		this.d_name = p_name;
	}

	/**
	 * gets the number of player's army
	 * @return d_player_army the number of player's army
	 */
	public int getPlayerArmy() {
		return d_player_army;
	}

	/**
	 * set the number of player's army
	 * @throws Exception when there is an exception
	 */
	public void setPlayerArmy(int p_number_of_army) throws Exception{
		this.d_player_army = p_number_of_army;
	}

	/**
	 * gets the list of countries the player controls
	 * @return d_player_countries the list of countries player controls
	 */
	public ArrayList<ICountryModel> getPlayerCountries() {
		return d_player_countries;
	}

	/**
	 * adds the appropriate country to the list of countries player controls
	 * @throws Exception when there is an exception
	 */
	public void addPlayerCountry(ICountryModel p_country) throws Exception{
		d_player_countries.add(p_country);
	}

	/**
	 * removes the appropriate country from the list of countries player controls
	 * @throws Exception when there is an exception
	 */
	public void removePlayerCountry(ICountryModel p_country) throws Exception {
		d_player_countries.remove(p_country);
	}

	/**
	 * gets the list of continents the player controls
	 * @return d_player_continents the list of continents player controls
	 */
	public ArrayList<IContinentModel> getPlayerContinents() {
		return d_player_continents;
	}

	/**
	 * adds the appropriate continent to the list of continent player controls
	 * @throws Exception when there is an exception
	 */
	public void addPlayerContinent(IContinentModel p_continent) throws Exception {
		d_player_continents.add(p_continent);
	}

	/**
	 * removes the appropriate continent from the list of continents player controls
	 * @throws Exception when there is an exception
	 */
	public void removePlayerContinent(IContinentModel p_continent) throws Exception {
		d_player_continents.remove(p_continent);
	}


}
