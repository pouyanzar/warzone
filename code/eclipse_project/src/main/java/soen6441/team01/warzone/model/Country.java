package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.ICountryModelView;

/**
 * Manages the information associated with a country
 *
 */
public class Country implements ICountryModel, ICountryModelView {
	private int d_id;
	private String d_country_name;
	private IContinentModel d_continent;
	private int d_x;
	private int d_y;
	private int d_continent_id;
	private ArrayList<ICountryModel> d_neighbors = new ArrayList<ICountryModel>();

	/**
	 * The constructor for the Country class.
	 * 
	 * @param p_id           a unique country identifier
	 * @param p_country_name the name of the country
	 * @param p_continent    the continent this country belongs to countries
	 * @param p_x            x coordinate on the map
	 * @param p_y            x coordinate on the map
	 * @throws Exception when there is an exception
	 */
	public Country(int p_id, String p_country_name, IContinentModel p_continent, int p_x, int p_y) throws Exception {
		super();
		setId(p_id);
		setName(p_country_name);
		setContinent(p_continent);
		d_x = p_x;
		d_y = p_y;
	}

	/**
	 * The constructor for the Country class.
	 * 
	 * @param p_id           a unique country identifier
	 * @param p_continent_id the continent id this country belongs to countries
	 * @throws Exception when there is an exception
	 */
	public Country(int p_id, int p_continent_id) throws Exception {
		super();
		setId(p_id);
		setContinentId(p_continent_id);
	}

	/**
	 * @return the name of the country
	 */
	public String getName() {
		return d_country_name;
	}

	/**
	 * Countries can NOT have spaces in their name, if u want to have a space then
	 * put a "_" and it will be displayed as a space when you play the game.
	 * 
	 * @param p_country_name the name of the country to set
	 */
	public void setName(String p_country_name) throws Exception {
		if (!Utl.IsValidMapName(p_country_name)) {
			throw new Exception("Invalid country name: " + p_country_name);
		}
		this.d_country_name = p_country_name;
	}

	/**
	 * @return the country id
	 */
	public int getId() {
		return d_id;
	}

	/**
	 * A unique identifier to assign to the country. A value of -1 indicates that
	 * the country is no longer valid or has been deleted
	 * 
	 * @param p_id the country id to set
	 */
	public void setId(int p_id) throws Exception {
		if (p_id < -1) {
			throw new Exception("Invalid country id " + p_id);
		}
		this.d_id = p_id;
	}

	/**
	 * @return the d_continent
	 */
	public IContinentModel getContinent() {
		return d_continent;
	}

	/**
	 * the continent that this country is associated with. set to null if not
	 * associated with a continent
	 * 
	 * @param p_continent the continent associated with this country to set
	 */
	public void setContinent(IContinentModel p_continent) {
		this.d_continent = p_continent;
	}

	/**
	 * the continent that this country is associated with. set to null if not
	 * associated with a continent
	 * 
	 * @param p_continent_id the continent id associated with this country to set
	 */
	public void setContinentId(int p_continent_id) {
		this.d_continent_id = p_continent_id;
	}

	/**
	 * @return the list of neighboring countries that this country can access
	 */
	public ArrayList<ICountryModel> getNeighbors() {
		return (ArrayList<ICountryModel>) d_neighbors.clone();
	}

	/**
	 * Add a neighboring country that this country can access
	 * 
	 * @param p_country country to add as a neighboring country
	 */
	public void addNeighbor(ICountryModel p_country) throws Exception {
		if (p_country == this || p_country.getId() == d_id) {
			throw new Exception("Cannot add yourself as a neighbor");
		}
		ICountryModel l_neighbor = findCountry(p_country.getId(), d_neighbors);
		if (l_neighbor == null) {
			d_neighbors.add(p_country);
		}
	}

	/**
	 * find a given country
	 * 
	 * @param p_country_id the country id of the neighboring country to find
	 * @param p_countries  list of countries to search from
	 * @return null if not found, otherwise return the country with the specified id
	 */
	public static ICountryModel findCountry(int p_country_id, ArrayList<ICountryModel> p_countries) {
		for (ICountryModel l_xcountry : p_countries) {
			if (l_xcountry.getId() == p_country_id) {
				return l_xcountry;
			}
		}
		return null;
	}
}
