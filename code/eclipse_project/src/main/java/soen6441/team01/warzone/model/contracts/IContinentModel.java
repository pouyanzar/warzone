package soen6441.team01.warzone.model.contracts;

import java.util.ArrayList;

/**
 * Defines the Continent model interface used to define the methods available to
 * manipulate or view the current continent
 *
 */
public interface IContinentModel {
	String getName();

	void setName(String p_continent_name) throws Exception;

	int getId();

	void setId(int p_id) throws Exception;

	int getExtraArmy();

	void setExtraArmy(int p_extra_army) throws Exception;

	void deactivate();

	boolean isActive();

	void refreshCountriesOfContinent(IMapModel p_map);

	ArrayList<ICountryModel> getCountries();

	String toDominationMapString();
}
