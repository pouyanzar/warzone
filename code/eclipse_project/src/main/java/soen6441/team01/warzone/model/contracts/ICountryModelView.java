package soen6441.team01.warzone.model.contracts;

import java.util.ArrayList;

import soen6441.team01.warzone.model.Country;

/**
 * Defines the Country model interface used to define the methods available to
 * view the current country
 *
 */
public interface ICountryModelView {
	String getName();

	int getId();

	IContinentModel getContinent();

	ArrayList<ICountryModel> getNeighbors();
}
