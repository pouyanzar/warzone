package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.IContinentModelView;

/**
 * Manages the information associated with a continent
 *
 */
public class Continent implements IContinentModel, IContinentModelView {
	private int d_id;
	private String d_continent_name;
	private int d_extra_army;

	/**
	 * The constructor for the Continent class.
	 * 
	 * @param p_id             a unique continent identifier
	 * @param p_continent_name the name of the continent
	 * @param p_extra_army     the number of extra armies to assign if player has
	 *                         all countries
	 * @throws Exception when there is an exception
	 */
	public Continent(int p_id, String p_continent_name, int p_extra_army) throws Exception {
		super();
		setId(p_id);
		setName(p_continent_name);
		setExtraArmy(p_extra_army);
	}

	/**
	 * The constructor for the Continent class.
	 * 
	 * @param p_continent_id    the name of the continent
	 * @param p_continent_value the number of extra armies to assign if player has
	 *                          all countries
	 * @throws Exception when there is an exception
	 */
	public Continent(String p_continent_id, int p_continent_value) throws Exception {
		super();
		setName(p_continent_id);
		setExtraArmy(p_continent_value);
	}

	/**
	 * @return the name of the continent
	 */
	public String getName() {
		return d_continent_name;
	}

	/**
	 * Continents can NOT have spaces in their name, if u want to have a space then
	 * put a "_" and it will be displayed as a space when you play the game.
	 * 
	 * @param p_continent_name the name of the continent to set
	 */
	public void setName(String p_continent_name) throws Exception {
		if (!Utl.isValidMapName(p_continent_name)) {
			throw new Exception("Invalid continent name: " + p_continent_name);
		}
		this.d_continent_name = p_continent_name;
	}

	/**
	 * @return the continent id
	 */
	public int getId() {
		return d_id;
	}

	/**
	 * A unique identifier to assign to the continent. A value of -1 indicates that
	 * the continent is no longer valid or has been deleted
	 * 
	 * @param p_id the continent id to set
	 */
	public void setId(int p_id) throws Exception {
		if (p_id < -1) {
			throw new Exception("Invalid continent id " + p_id);
		}
		this.d_id = p_id;
	}

	/**
	 * @return the d_extra_army
	 */
	public int getExtraArmy() {
		return d_extra_army;
	}

	/**
	 * the number of extra armies to assign if player has all countries. Must be
	 * greater than or equal to 0
	 * 
	 * @param p_extra_army the d_extra_army to set
	 */
	public void setExtraArmy(int p_extra_army) throws Exception {
		if (p_extra_army < 0) {
			throw new Exception("Invalid number of extra armies: " + p_extra_army);
		}
		this.d_extra_army = p_extra_army;
	}

	/**
	 * find a given continent
	 * 
	 * @param p_continent_id the continent id to find
	 * @param p_continents   an array of continents to search from
	 * @return null if not found, otherwise return the continent with the specified
	 *         id
	 */
	public static IContinentModel findContinent(int p_continent_id, ArrayList<IContinentModel> p_continents) {
		for (IContinentModel l_xcontinent : p_continents) {
			if (l_xcontinent.getId() == p_continent_id) {
				return l_xcontinent;
			}
		}
		return null;
	}

	/**
	 * find a given continent by name
	 * 
	 * @param p_continent_id the continent name to find
	 * @param p_continents   an array of continents to search from
	 * @return null if not found, otherwise return the continent with the specified
	 *         id
	 */
	public static IContinentModel findContinent(String p_continent_id, ArrayList<IContinentModel> p_continents) {
		for (IContinentModel l_xcontinent : p_continents) {
			if (l_xcontinent.getName() == p_continent_id) {
				return l_xcontinent;
			}
		}
		return null;
	}

	/**
	 * Deactivate the continent whenever it is no longer required, e.g. when the
	 * continent is deleted/removed. this is useful if a continent is deleted and
	 * there may still be references to it from other objects
	 */
	public void deactivate() {
		d_id = -1;
	}

	/**
	 * Checks that the continent is valid, e.g. is not deleted
	 * 
	 * @return false if the continent has been deleted
	 */
	public boolean isActive() {
		if (d_id < 0) {
			return false;
		}
		return true;
	}
}
