package soen6441.team01.warzone.model.contracts;

import java.util.ArrayList;

import soen6441.team01.warzone.model.entities.CountrySummary;

/**
 * Defines the Country model interface used to define the methods available to
 * manipulate or view the current country
 *
 */
public interface ICountryModel {
	String getName();

	void setName(String p_country_name) throws Exception;

	int getId();

	void setId(int p_id) throws Exception;

	IContinentModel getContinent();

	void setContinent(IContinentModel p_continent);

	int getX();

	void setX(int l_x);

	int getY();

	void setY(int l_y);

	ArrayList<ICountryModel> getNeighbors();

	void addNeighbor(ICountryModel p_country) throws Exception;

	void removeNeighbor(String p_neighbor_name);

	void setArmies(int p_num_armies);

	void removeArmies(int p_num_armies);

	int getArmies();

	void setOwner(IPlayerModel p_player);

	public IPlayerModel getOwner();

	CountrySummary getSummary();

	void addArmies(int p_num_armies);
}
