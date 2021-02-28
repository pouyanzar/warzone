package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.entities.CountrySummary;

/**
 * Manages the information associated with a country
 *
 */
public class Country implements ICountryModel {
	private int d_country_id;
	private String d_country_name;
	private IContinentModel d_continent;
	private int d_x;
	private int d_y;
	private ArrayList<ICountryModel> d_neighbors = new ArrayList<ICountryModel>();
	private int d_armies = 0;
	private IPlayerModel d_owner = null;
	private SoftwareFactoryModel d_factory_model = null;

	/**
	 * The constructor for the Country class.
	 * 
	 * @param p_country_id    a unique country identifier
	 * @param p_country_name  the name of the country
	 * @param p_continent     the continent this country belongs to countries
	 * @param p_x             x coordinate on the map
	 * @param p_y             x coordinate on the map
	 * @param p_factory_model the model factory to use when needed
	 * @throws Exception when there is an exception
	 */
	public Country(int p_country_id, String p_country_name, IContinentModel p_continent, int p_x, int p_y,
			SoftwareFactoryModel p_factory_model) throws Exception {
		super();
		setId(p_country_id);
		setName(p_country_name);
		setContinent(p_continent);
		d_x = p_x;
		d_y = p_y;
		d_factory_model = p_factory_model;
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
		if (!Utl.isValidMapName(p_country_name)) {
			throw new Exception("Invalid country name: " + p_country_name);
		}
		this.d_country_name = p_country_name;
	}

	/**
	 * @return the country id
	 */
	public int getId() {
		return d_country_id;
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
		this.d_country_id = p_id;
	}

	/**
	 * set the number of armies on this country.
	 * 
	 * @param p_num_armies the number of armies stationed at country
	 */
	public void setArmies(int p_num_armies) {
		d_armies = p_num_armies;
	}

	/**
	 * 
	 * @return the number of armies currently situated in this country
	 */
	public int getArmies() {
		return d_armies;
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
	 * set the player owns owns this country
	 * 
	 * @param p_player player to make the owner of this country
	 */
	public void setOwner(IPlayerModel p_player) {
		d_owner = p_player;
	}

	/**
	 * 
	 * @return The player who owns this country. Null if there is no owner.
	 */
	public IPlayerModel getOwner() {
		return d_owner;
	}

	/**
	 * @return the list of neighboring countries that this country can access
	 */
	public ArrayList<ICountryModel> getNeighbors() {
		return (ArrayList<ICountryModel>) d_neighbors.clone();
	}

	/**
	 * Add a neighboring country that this country can access. If the specified
	 * neighboring country is already a neighbor simply ignore the request.
	 * 
	 * @param p_neighbor country to add as a neighboring country
	 */
	public void addNeighbor(ICountryModel p_neighbor) throws Exception {
		if (p_neighbor == this || p_neighbor.getId() == d_country_id) {
			throw new Exception("Cannot add yourself as a neighbor");
		}
		ICountryModel l_neighbor = findCountry(p_neighbor.getId(), d_neighbors);
		if (l_neighbor == null) {
			d_neighbors.add(p_neighbor);
		}
	}

	/**
	 * Remove a neighboring country
	 * 
	 * @param p_neighbor_name the neighboring country's name
	 */
	public void removeNeighbor(String p_neighbor_name) {
		ICountryModel l_neighbor = findCountry(p_neighbor_name, d_neighbors);
		if (l_neighbor != null) {
			d_neighbors.remove(l_neighbor);
		}
	}

	/**
	 * find a given country given it's ID
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

	/**
	 * find a given country given it's name
	 * 
	 * @param p_country_name the country id of the neighboring country to find
	 * @param p_countries    list of countries to search from
	 * @return null if not found, otherwise return the country with the specified id
	 */
	public static ICountryModel findCountry(String p_country_name, ArrayList<ICountryModel> p_countries) {
		for (ICountryModel l_xcountry : p_countries) {
			if (l_xcountry.getName().equals(p_country_name)) {
				return l_xcountry;
			}
		}
		return null;
	}

	/**
	 * Gathers summary information about this country
	 * 
	 * @return summary information about the current country
	 */
	public CountrySummary getSummary() {
		CountrySummary l_sum = new CountrySummary();
		l_sum.d_country_name = getName();
		if (d_owner != null) {
			l_sum.d_country_owner_name = d_owner.getName();
		}
		l_sum.d_armies = getArmies();
		l_sum.d_continent_name = getContinent().getName();
		l_sum.d_continent_bonus = "+" + getContinent().getExtraArmy();
//		ArrayList<ICountryModel> l_continent_countries = d_factory_model.getMapModel()
//				.getCountriesOfContinent(getContinent());
		ArrayList<ICountryModel> l_continent_countries = getContinent().getCountries();
		int l_owns_ctr = 0;
		for (ICountryModel l_xcountry : l_continent_countries) {
			if (l_xcountry.getOwner() != null && l_xcountry.getOwner().getName().equals(getOwner().getName())) {
				l_owns_ctr++;
			}
		}
		l_sum.d_player_owns_countries_of_continent = l_owns_ctr + "/" + l_continent_countries.size();
		return l_sum;
	}
}
