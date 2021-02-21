package soen6441.team01.warzone.model.contracts;

import soen6441.team01.warzone.model.Continent;

/**
 * Defines the Map model interface used to define the methods available to
 * manipulate the current Warzone map.
 *
 */
public interface IMapModel {
	IContinentModel addContinent(String p_continent_id, int p_continent_value) throws Exception;

	IContinentModel addContinent(int p_continent_id, String p_continent_name, int p_extra_army) throws Exception;

	ICountryModel addCountry(String p_country_name, int p_continent_id) throws Exception;

	ICountryModel addCountry(String p_country_name, IContinentModel p_continent, int p_x, int p_y) throws Exception;

	IContinentModel removeContinent(String p_continent_id) throws Exception;

	IContinentModel removeContinent(int p_continent_id) throws Exception;

	ICountryModel removeCountry(String p_country_name) throws Exception;

	void addNeighbor(String p_country_name, String p_neighboring_country_name) throws Exception;

	void removeNeighbor(String p_country_name, String p_neighboring_country_name) throws Exception;
}
