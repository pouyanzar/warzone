package soen6441.team01.warzone.model.entities;

import java.util.ArrayList;

/**
 * Holds border information from a domination style map.
 */
public class DominationBorder {
	public String d_country_id;
	public ArrayList<String> d_border_country_id;

	/**
	 * default constructor
	 */
	public DominationBorder() {
		d_border_country_id = new ArrayList<String>();
	}
}
