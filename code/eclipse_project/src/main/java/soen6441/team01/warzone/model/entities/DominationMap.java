package soen6441.team01.warzone.model.entities;

import java.util.ArrayList;

/**
 * Holds country summary information. Entity class (purpose is to hold data,
 * i.e. basic data structure).
 *
 */
public class DominationMap {
	public ArrayList<DominationContinent> d_continents;
	public ArrayList<DominationCountry> d_countries;
	public ArrayList<DominationBorder> d_borders;

	/**
	 * default constructor
	 */
	public DominationMap() {
		d_continents = new ArrayList<DominationContinent>();
		d_countries = new ArrayList<DominationCountry>();
		d_borders = new ArrayList<DominationBorder>();
	}
}
