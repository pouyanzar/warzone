package soen6441.team01.warzone.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds country information from a domination style map.
 */
public class ConquestTerritory {
	public String d_name;
	public String d_x;
	public String d_y;
	public String d_continent_name;
	public List<String> d_neighbors;

	/**
	 * default constructor
	 */
	public ConquestTerritory() {
		d_neighbors = new ArrayList<String>();
	}
}
