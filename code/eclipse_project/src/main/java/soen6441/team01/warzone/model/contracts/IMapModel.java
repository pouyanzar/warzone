package soen6441.team01.warzone.model.contracts;

import java.util.ArrayList;

import soen6441.team01.warzone.model.Continent;
import soen6441.team01.warzone.model.ModelFactory;

/**
 * Defines the Map model interface used to define the methods available to
 * manipulate the current Warzone map.
 *
 */
public interface IMapModel {
	IContinentModel addContinent(int p_continent_id, String p_continent_name, int p_extra_army, String p_color)
			throws Exception;

	void addContinent(IContinentModel l_continent) throws Exception;

	ICountryModel addCountry(String p_country_name, int p_continent_id) throws Exception;

	ICountryModel addCountry(String p_country_name, IContinentModel p_continent, int p_x, int p_y) throws Exception;

	ICountryModel addCountry(int p_country_id, String p_country_name, int p_continent_id) throws Exception;

	void addCountry(ICountryModel p_country) throws Exception;

	IContinentModel removeContinent(String p_continent_id) throws Exception;

	IContinentModel removeContinent(int p_continent_id) throws Exception;

	ICountryModel removeCountry(String p_country_name) throws Exception;

	void addNeighbor(String p_country_name, String p_neighboring_country_name) throws Exception;

	void addNeighbor(int p_country_id, int p_neighboring_country_id) throws Exception;

	void removeNeighbor(String p_country_name, String p_neighboring_country_name) throws Exception;

	ArrayList<IContinentModel> getContinents();

	ArrayList<ICountryModel> getCountries();

	ArrayList<ICountryModel> getNeighbors(int p_country_id);

	boolean validatemap() throws Exception;

	void saveMap(String p_filename) throws Exception;

	ModelFactory deepCloneMap() throws Exception;
}
