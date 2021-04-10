package soen6441.team01.warzone.model;

import java.util.ArrayList;
import java.util.List;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.entities.DominationBorder;
import soen6441.team01.warzone.model.entities.DominationContinent;
import soen6441.team01.warzone.model.entities.DominationCountry;
import soen6441.team01.warzone.model.entities.DominationMap;

/**
 * Loads and saves the Warzone map to and from a domination style map file. The
 * following link describes the format of the "domination" map file :
 * http://domination.sourceforge.net/makemaps.shtml.
 *
 */
public class MapIoDomination {

	/**
	 * parses the input domination style map.
	 * 
	 * @param p_domination_map an array holding a domination style map
	 * @return the domination style data parsed from the input style map
	 * @throws Exception unexpected error
	 */
	public DominationMap parseMap(List<String> p_domination_map) throws Exception {
		int l_line_ctr = 0;
		String l_rec;
		String l_header_flag = "None";
		int l_continent_ctr = 1;
		DominationMap l_map = new DominationMap();

		try {
			for (l_line_ctr = 0; l_line_ctr < p_domination_map.size(); l_line_ctr++) {
				l_rec = p_domination_map.get(l_line_ctr);
				switch (l_rec.toLowerCase()) {
				case "[continents]":
					l_rec = "";
					l_header_flag = "continents";
					break;
				case "[countries]":
					l_rec = "";
					l_header_flag = "countries";
					break;
				case "[borders]":
					l_rec = "";
					l_header_flag = "borders";
					break;
				}

				if (!Utl.isEmpty(l_rec)) {
					switch (l_header_flag) {
					case "continents":
						parseMapFileContinent(l_continent_ctr++, l_rec, l_map.d_continents);
						break;
					case "countries":
						parseMapFileCountry(l_rec, l_map.d_countries);
						break;
					case "borders":
						parseMapFileBorders(l_rec, l_map.d_borders);
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
	 * Convert the current map into the "domination" game map format. Note that this
	 * implementation of Warzone does not process the continent color which is part
	 * of the domination map file format.
	 * 
	 * @param p_map the warzone world map to convert into domination style map file
	 * @return the current map in the domination game style map file. Each string of
	 *         the array is a line in the map file.
	 */
	public ArrayList<String> getMapAsTextArray(IMapModel p_map) {
		ArrayList<String> l_dmap = new ArrayList<String>();
		l_dmap.add("[files]");

		// process continent section
		l_dmap.add("");
		l_dmap.add("[continents]");
		for (IContinentModel l_continent : p_map.getContinents()) {
			String l_smap = l_continent.getName() + " " + l_continent.getExtraArmy() + " white";
			l_dmap.add(l_smap);
		}

		// process country section
		l_dmap.add("");
		l_dmap.add("[countries]");
		for (ICountryModel l_country : p_map.getCountries()) {
			String l_smap = l_country.getId() + " " + l_country.getName() + " " + l_country.getContinent().getId() + " "
					+ l_country.getX() + " " + l_country.getY();
			l_dmap.add(l_smap);
		}

		// process border section
		l_dmap.add("");
		l_dmap.add("[borders]");
		for (ICountryModel l_country : p_map.getCountries()) {
			String l_smap = l_country.getId() + " ";
			for (ICountryModel d_neighbor : l_country.getNeighbors()) {
				l_smap += d_neighbor.getId() + " ";
			}
			l_dmap.add(l_smap.trim());
		}

		return l_dmap;
	}

	/**
	 * Parse country borders
	 * 
	 * @param p_rec     the country record to parse
	 * @param p_borders the map borders
	 * @throws Exception error parsing the neighbor record, unexpected error
	 */
	private void parseMapFileBorders(String p_rec, ArrayList<DominationBorder> p_borders) throws Exception {
		DominationBorder l_border = new DominationBorder();
		String[] l_tokens = Utl.getFirstWord(p_rec);

		String l_country_id_str = l_tokens[0];
		int l_country_id = Utl.convertToInteger(l_country_id_str);
		if (l_country_id >= Integer.MAX_VALUE || l_country_id < 0) {
			throw new Exception("Invalid border country id value '" + l_country_id_str + "' specified");
		}
		l_border.d_country_id = l_country_id_str;

		l_tokens = Utl.getFirstWord(l_tokens[1]);
		while (!Utl.isEmpty(l_tokens[0])) {
			String l_border_id_str = l_tokens[0];
			int l_border_id = Utl.convertToInteger(l_border_id_str);
			if (l_border_id >= Integer.MAX_VALUE || l_border_id < 0) {
				throw new Exception("Invalid border country id value '" + l_border_id_str
						+ "' specified for country with id of '" + l_country_id_str + "'");
			}
			l_border.d_border_country_id.add(l_border_id_str);
			l_tokens = Utl.getFirstWord(l_tokens[1]);
		}
		p_borders.add(l_border);
	}

	/**
	 * Parse a country specification from the map file
	 * 
	 * @param p_rec       the country record to parse
	 * @param p_countries the map countries
	 * @throws Exception error parsing the country record, unexpected error
	 */
	private void parseMapFileCountry(String p_rec, ArrayList<DominationCountry> p_countries) throws Exception {
		String[] l_tokens = Utl.getFirstWord(p_rec);

		String l_country_id_str = l_tokens[0];
		int l_country_id = Utl.convertToInteger(l_country_id_str);
		if (l_country_id >= Integer.MAX_VALUE || l_country_id < 0) {
			throw new Exception("Invalid country id value '" + l_country_id_str + "' specified");
		}

		l_tokens = Utl.getFirstWord(l_tokens[1]);

		String l_country_name = l_tokens[0];
		if (!Utl.isValidMapName(l_country_name)) {
			throw new Exception("Invalid country name '" + l_country_name + "'");
		}

		l_tokens = Utl.getFirstWord(l_tokens[1]);
		String l_continent_id_str = l_tokens[0];
		int l_continent_id = Utl.convertToInteger(l_continent_id_str);
		if (l_continent_id >= Integer.MAX_VALUE || l_continent_id < 0) {
			throw new Exception("Invalid continent id value '" + l_continent_id_str + "' specified for country '"
					+ l_country_name + "'");
		}

		l_tokens = Utl.getFirstWord(l_tokens[1]);
		String l_x = l_tokens[0];
		l_tokens = Utl.getFirstWord(l_tokens[1]);
		String l_y = l_tokens[0];

		DominationCountry l_country = new DominationCountry();
		l_country.d_id = Integer.toString(l_country_id);
		l_country.d_name = l_country_name;
		l_country.d_continent_id = l_continent_id_str;
		l_country.d_x = l_x;
		l_country.d_y = l_y;
		p_countries.add(l_country);
	}

	/**
	 * Parse a continent specification from the map file
	 * 
	 * @param p_id         the continent id
	 * @param p_rec        the continent record to parse
	 * @param p_continents the map continents
	 * @throws Exception error parsing the continent record, unexpected error
	 */
	private void parseMapFileContinent(int p_id, String p_rec, ArrayList<DominationContinent> p_continents)
			throws Exception {
		String[] l_tokens = Utl.getFirstWord(p_rec);

		String l_continent_name = l_tokens[0];
		if (!Utl.isValidMapName(l_continent_name)) {
			throw new Exception("Invalid continent name '" + l_continent_name + "'");
		}

		l_tokens = Utl.getFirstWord(l_tokens[1]);

		String l_continent_xtra_army_str = l_tokens[0];
		int l_continent_xtra_army = Utl.convertToInteger(l_continent_xtra_army_str);
		if (l_continent_xtra_army >= Integer.MAX_VALUE) {
			throw new Exception("Invalid extra army value '" + l_continent_xtra_army_str + "' specified for continent '"
					+ l_continent_name + "'");
		}

		l_tokens = Utl.getFirstWord(l_tokens[1]);
		String l_color = l_tokens[0];

		DominationContinent l_continent = new DominationContinent();
		l_continent.d_id = Integer.toString(p_id);
		l_continent.d_name = l_continent_name;
		l_continent.d_extra_armies = l_continent_xtra_army;
		l_continent.d_color = l_color;
		p_continents.add(l_continent);
	}

	/**
	 * scans the supplied map and checks if the format conforms to a domination
	 * style file format.
	 * 
	 * @param p_map_records the map file to check
	 * @return true if the p_map_records contain a format that conforms to a
	 *         domination style file format.
	 */
	public static boolean isDominationFileFormat(List<String> p_map_records) {
		int l_line_ctr = 0;
		String l_rec;
		boolean l_has_continents = false;
		boolean l_has_countries = false;
		boolean l_has_borders = false;

		try {
			for (l_line_ctr = 0; l_line_ctr < p_map_records.size(); l_line_ctr++) {
				l_rec = p_map_records.get(l_line_ctr);
				switch (l_rec.toLowerCase()) {
				case "[continents]":
					l_has_continents = true;
					break;
				case "[countries]":
					l_has_countries = true;
					break;
				case "[borders]":
					l_has_borders = true;
					break;
				}
			}
		} catch (Exception ex) {
			return false;
		}

		if (l_has_continents && l_has_countries && l_has_borders) {
			return true;
		}

		return false;
	}
}
