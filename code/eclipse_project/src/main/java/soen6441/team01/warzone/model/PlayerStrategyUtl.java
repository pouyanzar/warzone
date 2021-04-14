package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;

/**
 * Utility class containing some common functions useful across player
 * strategies
 *
 */
public class PlayerStrategyUtl {

	/**
	 * Filters out either the countries that have more or less than the specified
	 * number of armies on them
	 * 
	 * @param p_counties a list of countries to scan for armies greater p_min_armies
	 * @param p_armies   the number of armies to check
	 * @param p_more_sw  return countries with more than p_armies, otherwise less
	 *                   than p_armies
	 * @return the list of countries that have desired armies
	 */
	public static ArrayList<ICountryModel> countriesWithArmies(ArrayList<ICountryModel> p_counties, int p_armies,
			boolean p_more_sw) {
		ArrayList<ICountryModel> l_countries_with_armies = new ArrayList<ICountryModel>();
		if (p_more_sw) {
			for (ICountryModel l_c : p_counties) {
				if (l_c.getArmies() > p_armies) {
					l_countries_with_armies.add(l_c);
				}
			}
		} else {
			for (ICountryModel l_c : p_counties) {
				if (l_c.getArmies() < p_armies) {
					l_countries_with_armies.add(l_c);
				}
			}
		}
		return l_countries_with_armies;
	}

	/**
	 * scans p_counties for countries that are either the same or not the same as
	 * p_country
	 * 
	 * @param p_same_sw  true = return same as countries, otherwise all countries
	 *                   not the same as p_country
	 * @param p_county   the reference country
	 * @param p_counties list of counties to scan
	 * @return list of countries that either are the same as p_country or not the
	 *         same as specified by p_same_sw
	 */
	public static ArrayList<ICountryModel> countryOwnerLike(boolean p_same_sw, ICountryModel p_county,
			ArrayList<ICountryModel> p_counties) {
		ArrayList<ICountryModel> l_countries = new ArrayList<ICountryModel>();

		IPlayerModel l_owner = p_county.getOwner();
		if (l_owner == null) {
			return l_countries;
		}

		for (ICountryModel l_c : p_counties) {
			IPlayerModel l_xowner = l_c.getOwner();
			if (l_xowner != null) {
				if (p_same_sw) {
					if (l_owner.getName().equals(l_xowner.getName())) {
						l_countries.add(l_c);
					}
				} else {
					if (!l_owner.getName().equals(l_xowner.getName())) {
						l_countries.add(l_c);
					}

				}
			}
		}

		return l_countries;
	}

	/**
	 * Find countries that can be bombed
	 * 
	 * @param p_counties the list of countries (usually a players) to find valid
	 *                   neighbors that can be bombed.
	 * @return list of countries that can be bombed
	 */
	public static ArrayList<ICountryModel> findBombTargets(ArrayList<ICountryModel> p_counties) {
		ArrayList<ICountryModel> l_countries = new ArrayList<ICountryModel>();
		ArrayList<ICountryModel> l_neighbors;
		IPlayerModel l_xowner;

		for (ICountryModel l_c : p_counties) {
			l_xowner = l_c.getOwner();
			l_neighbors = l_c.getNeighbors();
			if (l_xowner != null && l_neighbors != null) {
				for( ICountryModel l_neighbor : l_neighbors ) {
					if( l_neighbor.getOwner() == null ) {
						if( l_neighbor.getArmies() > 0 ) {
							// blockaded country
							l_countries.add(l_neighbor);
						}
					} else {
						if( !l_neighbor.getOwner().getName().equals(l_xowner.getName())) {
							if( l_neighbor.getArmies() > 0 ) {
								l_countries.add(l_neighbor);
							}
						}
					}
				}
			}
		}

		return l_countries;
	}

}
