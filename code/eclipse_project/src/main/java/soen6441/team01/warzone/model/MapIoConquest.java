package soen6441.team01.warzone.model;

import java.util.ArrayList;
import java.util.List;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.entities.ConquestContinent;
import soen6441.team01.warzone.model.entities.ConquestTerritory;
import soen6441.team01.warzone.model.entities.ConquestMap;

/**
 * Loads and saves the Warzone map to and from a Conquest style map file. The
 * following link provides sample Conquest map files :
 * http://www.windowsgames.co.uk/conquest_maps.html
 *
 */
public class MapIoConquest {

	/**
	 * parses the input conquest style map.
	 * 
	 * @param p_conquest_map an array holding a conquest style map
	 * @return the conquest style data parsed from the input style map
	 * @throws Exception unexpected error
	 */
	public ConquestMap parseMap(List<String> p_conquest_map) throws Exception {
		int l_line_ctr = 0;
		String l_rec;
		String l_header_flag = "None";
		ConquestMap l_map = new ConquestMap();

		try {
			for (l_line_ctr = 0; l_line_ctr < p_conquest_map.size(); l_line_ctr++) {
				l_rec = p_conquest_map.get(l_line_ctr).trim();
				switch (l_rec.toLowerCase()) {
				case "[continents]":
					l_rec = "";
					l_header_flag = "continents";
					break;
				case "[territories]":
					l_rec = "";
					l_header_flag = "Territories";
					break;
				}

				if (!Utl.isEmpty(l_rec)) {
					switch (l_header_flag) {
					case "continents":
						parseMapContinent(l_rec, l_map.d_continents);
						break;
					case "Territories":
						parseMapTerritory(l_rec, l_map.d_territories);
						break;
					}
				}
			}
		} catch (Exception ex) {
			String l_msg = "Encountered the following exception while processing line " + (l_line_ctr + 1)
					+ ", exception: " + ex.getMessage();
			throw new Exception(l_msg);
		}

		return l_map;
	}

	/**
	 * Parse a country specification from the map file
	 * 
	 * @param p_rec       the country record to parse
	 * @param p_countries the map countries
	 * @throws Exception error parsing the country record, unexpected error
	 */
	private void parseMapTerritory(String p_rec, ArrayList<ConquestTerritory> p_countries) throws Exception {
		ConquestTerritory l_terri = new ConquestTerritory();

		String[] l_tokens = p_rec.split(",");
		int l_idx = 0;

		l_terri.d_name = l_tokens[l_idx++].trim();
		if (Utl.isEmpty(l_terri.d_name)) {
			throw new Exception("Invalid country name '" + l_terri.d_name + "'");
		}

		l_terri.d_x = l_tokens[l_idx++].trim();
		l_terri.d_y = l_tokens[l_idx++].trim();

		l_terri.d_continent_name = l_tokens[l_idx++].trim();
		if (Utl.isEmpty(l_terri.d_continent_name)) {
			throw new Exception("Invalid continent name '" + l_terri.d_continent_name + "'");
		}

		for (; l_idx < l_tokens.length; l_idx++) {
			String l_neighbor_name = l_tokens[l_idx].trim();
			if (Utl.isEmpty(l_neighbor_name)) {
				throw new Exception("Invalid neighbor country name '" + l_neighbor_name + "'");
			}
			l_terri.d_neighbors.add(l_neighbor_name);
		}

		p_countries.add(l_terri);
	}

	/**
	 * Parse a continent specification from the map file
	 * 
	 * @param p_rec        the continent record to parse
	 * @param p_continents the map continents
	 * @throws Exception error parsing the continent record, unexpected error
	 */
	private void parseMapContinent(String p_rec, ArrayList<ConquestContinent> p_continents) throws Exception {
		String[] l_tokens = p_rec.split("=");

		String l_continent_name = l_tokens[0].trim();
		if (Utl.isEmpty(l_continent_name)) {
			throw new Exception("Invalid continent name '" + l_continent_name + "'");
		}

		String l_continent_xtra_army_str = l_tokens[1].trim();
		int l_continent_xtra_army = Utl.convertToInteger(l_continent_xtra_army_str);
		if (l_continent_xtra_army >= Integer.MAX_VALUE) {
			throw new Exception("Invalid extra army value '" + l_continent_xtra_army_str + "' specified for continent '"
					+ l_continent_name + "'");
		}

		ConquestContinent l_continent = new ConquestContinent();
		l_continent.d_name = l_continent_name;
		l_continent.d_extra_armies = l_continent_xtra_army;
		p_continents.add(l_continent);
	}

	/**
	 * Convert the current map into the "conquest" game map format.
	 * 
	 * @param p_map the warzone world map to convert into conquest style map file
	 * @return the current map in the conquest game style map file. Each string of
	 *         the array is a line in the map file.
	 */
	public ArrayList<String> getMapAsConquestMapFormat(IMapModel p_map) {
		ArrayList<String> l_dmap = new ArrayList<String>();
		l_dmap.add("[map]");

		// process continent section
		l_dmap.add("");
		l_dmap.add("[continents]");
		for (IContinentModel l_continent : p_map.getContinents()) {
			String l_smap = l_continent.getName() + " " + l_continent.getExtraArmy() + " white";
			l_dmap.add(l_smap);
		}

		// process country section
		l_dmap.add("");
		l_dmap.add("[Territories]");
		for (ICountryModel l_country : p_map.getCountries()) {
			String l_smap = l_country.getId() + " " + l_country.getName() + " " + l_country.getContinent().getId() + " "
					+ l_country.getX() + " " + l_country.getY();
			l_dmap.add(l_smap);
		}

		return l_dmap;
	}

	/**
	 * scans the supplied map and checks if the format conforms to a conquest
	 * style file format.
	 * 
	 * @param p_map_records the map file to check
	 * @return true if the p_map_records contain a format that conforms to a
	 *         conquest style file format.
	 */
	public static boolean isConquestFileFormat(List<String> p_map_records) {
		int l_line_ctr = 0;
		String l_rec;
		boolean l_has_continents = false;
		boolean l_has_territories = false;
		boolean l_has_map = false;

		try {
			for (l_line_ctr = 0; l_line_ctr < p_map_records.size(); l_line_ctr++) {
				l_rec = p_map_records.get(l_line_ctr);
				switch (l_rec.toLowerCase()) {
				case "[map]":
					l_has_map = true;
					break;
				case "[continents]":
					l_has_continents = true;
					break;
				case "[territories]":
					l_has_territories = true;
					break;
				}
			}
		} catch (Exception ex) {
			return false;
		}

		if (l_has_continents && l_has_territories && l_has_map) {
			return true;
		}

		return false;
	}

}
