package soen6441.team01.warzone.model.entities;

import java.util.ArrayList;

/**
 * Holds country summary information. Entity class (purpose is to hold data,
 * i.e. basic data structure).
 *
 */
public class ConquestMap {
	public ArrayList<ConquestContinent> d_continents;
	public ArrayList<ConquestTerritory> d_territories;

	/**
	 * default constructor
	 */
	public ConquestMap() {
		d_continents = new ArrayList<ConquestContinent>();
		d_territories = new ArrayList<ConquestTerritory>();
	}
}
