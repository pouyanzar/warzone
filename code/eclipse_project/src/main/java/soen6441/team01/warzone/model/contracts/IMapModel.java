package soen6441.team01.warzone.model.contracts;

import soen6441.team01.warzone.model.Continent;

/**
 * Defines the Map model interface used to define the methods available to
 * manipulate the current Warzone map.
 *
 */
public interface IMapModel {
	IContinentModel addContinent(int p_continent_id, String p_continent_name, int p_extra_army) throws Exception;

	ICountryModel addCountry(int p_country_id, String p_country_name, IContinentModel p_continent, int p_x, int p_y)
			throws Exception;
}