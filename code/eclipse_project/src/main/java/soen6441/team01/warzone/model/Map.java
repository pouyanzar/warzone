package soen6441.team01.warzone.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashMap;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.IContinentModelView;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IMapModelView;

/**
 * Manages Warzone Maps. Maps are basically composed of continents, countries
 * and the links (ie neighbors) between countries.
 *
 */
public class Map implements IMapModel, IMapModelView {
	private ArrayList<IContinentModel> d_continents = new ArrayList<IContinentModel>();
	private ArrayList<ICountryModel> d_countries = new ArrayList<ICountryModel>();

	/**
	 * @return the current list of continents defined on the map
	 */
	public ArrayList<IContinentModel> getContinents() {
		return (ArrayList<IContinentModel>) d_continents.clone();
	}

	/**
	 * @return the current list of countries defined on the map
	 */
	public ArrayList<ICountryModel> getCountries() {
		return (ArrayList<ICountryModel>) d_countries.clone();
	}

	/**
	 * @return list of neighboring countries for the specified country. returns null
	 *         if specified country does not exist or if it doesn't have any
	 *         neighbors.
	 */
	public ArrayList<ICountryModel> getNeighbors(int p_country_id) {
		ICountryModel l_country = Country.findCountry(p_country_id, d_countries);
		if (l_country == null) {
			return null;
		}
		ArrayList<ICountryModel> l_neighbors = l_country.getNeighbors();
		return l_neighbors;
	}

	/**
	 * Add a continent to the current map
	 *
	 * @param p_continent_id   a unique continent identifier
	 * @param p_continent_name the name of the continent
	 * @param p_extra_army     the number of extra armies to assign if player has
	 *                         all countries
	 * @return the created continent
	 * @throws Exception when there is an exception
	 */
	public IContinentModel addContinent(int p_continent_id, String p_continent_name, int p_extra_army)
			throws Exception {
		IContinentModel l_continent = Continent.findContinent(p_continent_id, d_continents);
		if (l_continent != null) {
			throw new Exception("Cannot add continent with id " + p_continent_id + " since it already exists.");
		}
		l_continent = new Continent(p_continent_id, p_continent_name, p_extra_army);
		d_continents.add(l_continent);
		return l_continent;
	}

	/**
	 * Add a continent to the current map
	 *
	 * @param p_continent_id    the name of the continent
	 * @param p_continent_value the number of extra armies to assign if player
	 *                          controls all countries
	 * @return the created continent
	 * @throws Exception when there is an exception
	 */
	public IContinentModel addContinent(String p_continent_id, int p_continent_value) throws Exception {
		IContinentModel l_continent = Continent.findContinent(p_continent_id, d_continents);
		if (l_continent != null) {
			throw new Exception("Cannot add continent with id " + p_continent_id + " since it already exists.");
		}
		l_continent = new Continent(p_continent_id, p_continent_value);
		d_continents.add(l_continent);
		return l_continent;
	}

	/**
	 * Add a country to the current map
	 *
	 * @param p_country_id   the id of the country
	 * @param p_country_name the name of the country
	 * @param p_continent    the associated continent
	 * @param p_x            the x coordinate as defined in the map file
	 * @param p_y            the y coordinate as defined in the map file
	 * @return the created country
	 * @throws Exception when there is an exception
	 */
	public ICountryModel addCountry(int p_country_id, String p_country_name, IContinentModel p_continent, int p_x,
			int p_y) throws Exception {
		ICountryModel l_country = new Country(p_country_id, p_country_name, p_continent, p_x, p_y);
		d_countries.add(l_country);
		return l_country;
	}

	/**
	 * Add a country to the current map
	 *
	 * @param p_country_name the name of the country
	 * @param p_continent    the associated continent
	 * @param p_x            the x coordinate as defined in the map file
	 * @param p_y            the y coordinate as defined in the map file
	 * @return the created country
	 * @throws Exception when there is an exception
	 */
	public ICountryModel addCountry(String p_country_name, IContinentModel p_continent, int p_x, int p_y)
			throws Exception {
		ICountryModel l_country = Country.findCountry(p_country_name, d_countries);
		if (l_country != null) {
			throw new Exception("Cannot add country '" + p_country_name + "' since it already exists.");
		}
		int l_id = d_countries.size() + 1;
		l_country = addCountry(l_id, p_country_name, p_continent, p_x, p_y);
		return l_country;
	}

	/**
	 * Add a country to the current map
	 *
	 * @param p_country_id   the id of the country
	 * @param p_country_name a unique country identifier
	 * @param p_continent_id the associated continent
	 * @return the created country
	 * @throws Exception when there is an exception
	 */
	public ICountryModel addCountry(int p_country_id, String p_country_name, int p_continent_id) throws Exception {
		IContinentModel l_continent = Continent.findContinent(p_continent_id, d_continents);
		ICountryModel l_country = addCountry(p_country_id, p_country_name, l_continent, 0, 0);
		return l_country;
	}

	/**
	 * Add a country to the current map
	 *
	 * @param p_country_name a unique country identifier
	 * @param p_continent_id the associated continent
	 * @return the created country
	 * @throws Exception when there is an exception
	 */
	public ICountryModel addCountry(String p_country_name, int p_continent_id) throws Exception {
		IContinentModel l_continent = Continent.findContinent(p_continent_id, d_continents);
		ICountryModel l_country = addCountry(p_country_name, l_continent, 0, 0);
		return l_country;
	}

	/**
	 * remove the continent from the list of existing continents
	 *
	 * @param p_continent_id the id of the continent
	 * @return the deleted continent object
	 * @throws Exception if the continent cannot be removed
	 */
	public IContinentModel removeContinent(int p_continent_id) throws Exception {
		IContinentModel l_continent = Continent.findContinent(p_continent_id, d_continents);
		if (l_continent == null) {
			throw new Exception("Cannot remove continent with id " + p_continent_id + " since it doesn't exist.");
		}
		l_continent.deactivate();
		d_continents.remove(l_continent);
		return l_continent;
	}

	/**
	 * remove the continent from the list of existing continents
	 *
	 * @param p_continent_id the continent name
	 * @return the deleted continent object
	 * @throws Exception if the continent cannot be removed
	 */
	public IContinentModel removeContinent(String p_continent_id) throws Exception {
		IContinentModel l_continent = Continent.findContinent(p_continent_id, d_continents);
		if (l_continent == null) {
			throw new Exception("Cannot remove continent with id " + p_continent_id + " since it doesn't exist.");
		}
		l_continent.deactivate();
		d_continents.remove(l_continent);
		return l_continent;
	}

	/**
	 * remove the country from the list of existing countries
	 *
	 * @param p_country_name the country id
	 * @return the deleted country object
	 * @throws Exception if the country cannot be removed
	 */
	public ICountryModel removeCountry(String p_country_name) throws Exception {
		ICountryModel l_country = Country.findCountry(p_country_name, d_countries);
		if (l_country == null) {
			throw new Exception("Cannot remove continent with id " + p_country_name + " since it doesn't exist.");
		}
		d_countries.remove(l_country);
		return l_country;
	}

	/**
	 * Add a neighboring country to an existing country.
	 *
	 * @param p_country_name             the source country name
	 * @param p_neighboring_country_name the neighboring country name
	 * @throws Exception unexpected error or if either countries don't exist
	 */
	public void addNeighbor(String p_country_name, String p_neighboring_country_name) throws Exception {
		ICountryModel p_country = Country.findCountry(p_country_name, d_countries);
		if (p_country == null) {
			throw new Exception("Cannot add neighbor '" + p_neighboring_country_name + "' to county '" + p_country_name
					+ "' since country '" + p_country_name + "' doesn't exist.");
		}
		ICountryModel p_neighbor = Country.findCountry(p_neighboring_country_name, d_countries);
		if (p_neighbor == null) {
			throw new Exception("Cannot add neighbor '" + p_neighboring_country_name + "' to county '" + p_country_name
					+ "' since country '" + p_neighboring_country_name + "' doesn't exist.");
		}
		p_country.addNeighbor(p_neighbor);
	}

	/**
	 * Add a neighboring country to an existing country.
	 *
	 * @param p_country_id             the source country id
	 * @param p_neighboring_country_id the neighboring country id
	 * @throws Exception unexpected error or if either countries don't exist
	 */
	public void addNeighbor(int p_country_id, int p_neighboring_country_id) throws Exception {
		ICountryModel p_country = Country.findCountry(p_country_id, d_countries);
		if (p_country == null) {
			throw new Exception("Cannot add neighbor with id of '" + p_neighboring_country_id
					+ "' to county with id of '" + p_country_id + "' since country doesn't exist.");
		}
		ICountryModel p_neighbor = Country.findCountry(p_neighboring_country_id, d_countries);
		if (p_neighbor == null) {
			throw new Exception("Cannot add neighbor with id of '" + p_neighboring_country_id
					+ "' to county with if of '" + p_country_id + "' since neighboring country doesn't exist.");
		}
		p_country.addNeighbor(p_neighbor);
	}

	/**
	 * Remove a neighboring country
	 *
	 * @param p_country_name             the source country name
	 * @param p_neighboring_country_name the neighboring country name
	 * @throws Exception if either countries don't exist or if there is an
	 *                   unexpected error
	 */
	public void removeNeighbor(String p_country_name, String p_neighboring_country_name) throws Exception {
		ICountryModel p_country = Country.findCountry(p_country_name, d_countries);
		if (p_country == null) {
			throw new Exception("Cannot remove neighbor '" + p_neighboring_country_name + "' from county '"
					+ p_country_name + "' since country '" + p_country_name + "' doesn't exist.");
		}
		p_country.removeNeighbor(p_neighboring_country_name);
	}

	/**
	 * Checks if there is at least one continent, one country, and there is at least
	 * one neighbor for each country on the current map
	 * 
	 * @param p_filename the map file to validate
	 * @return l_isValid when the map is valid is true otherwise is false.
	 * @throws Exception when there is an exception
	 */
	public boolean validatemap(String p_filename) throws Exception {

		loadMapFromFile(p_filename);
		boolean l_isValid = false;
		ArrayList<Integer> l_passed_countries = new ArrayList<>();
		if (d_continents.size() < 1)
			return false;
		if (d_countries.size() < 1)
			return false;
		for (ICountryModel l_country : d_countries) {
			if (mapTraversal(l_country, l_passed_countries))
				l_isValid = true;
			else
				l_isValid = false;

		}

		return l_isValid;
	}

	/**
	 * implementation of DFS algorithm to traverse all nodes of the graph
	 * 
	 * @param p_country           the starting point for traverse
	 * @param p_visited_countries the list of countries visited through the search
	 * @return l_all_countries_visited
	 */
	public static boolean mapTraversal(ICountryModel p_country, ArrayList<Integer> p_visited_countries) {
		p_visited_countries.add(p_country.getId());
		boolean l_all_countries_visited = false;
		for (ICountryModel l_country : p_country.getNeighbors()) {
			if (!p_visited_countries.contains(l_country.getId())) {
				mapTraversal(l_country, p_visited_countries);
			} else {
				l_all_countries_visited = true;
			}
		}
		return l_all_countries_visited;
	}

	/**
	 * Loads a Warzone map from an array containing records defined by "domination"
	 * style map file. The following link describes the format of the "domination"
	 * map file : http://domination.sourceforge.net/makemaps.shtml.
	 *
	 * @param p_records a list of map records
	 * @return instance of new Map model
	 * @throws Exception error parsing the contents of the map file
	 */
	public static IMapModel loadMap(List<String> p_records) throws Exception {
		IMapModel l_map_model = new Map();
		int l_line_ctr = 0;
		String l_rec;
		String l_header_flag = "None";
		int l_continent_ctr = 1;

		try {
			for (l_line_ctr = 0; l_line_ctr < p_records.size(); l_line_ctr++) {
				l_rec = p_records.get(l_line_ctr);
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
						parseMapFileContinent(l_continent_ctr++, l_rec, l_map_model);
						break;
					case "countries":
						parseMapFileCountry(l_rec, l_map_model);
						break;
					case "borders":
						parseMapFileBorders(l_rec, l_map_model);
						break;
					}
				}
			}
		} catch (Exception ex) {
			String l_msg = "Encountered the following exception while processing line " + (l_line_ctr + 1)
					+ ", exception: " + ex.getMessage();
			throw new Exception(l_msg);
		}

		return l_map_model;
	}

	/**
	 * Loads a Warzone map from an existing "domination" style map file. The
	 * following link describes the format of the "domination" map file :
	 * http://domination.sourceforge.net/makemaps.shtml.
	 * 
	 * @param p_map_filename the filename of the map file
	 * @return instance of new Map model
	 * @throws Exception error parsing the contents of the map file
	 */
	public static IMapModel loadMapFromFile(String p_map_filename) throws Exception {
		List<String> l_records = null;
		IMapModel l_map_model = null;
		try {
			l_records = Files.readAllLines(new File(p_map_filename).toPath(), Charset.defaultCharset());
			l_map_model = loadMap(l_records);
		} catch (Exception ex) {
			String l_msg = "Error loading map file '" + p_map_filename + "'. " + ex;
			throw new Exception(l_msg);
		}
		return l_map_model;
	}

	/**
	 * Parse country borders
	 * 
	 * @param l_rec       the country record to parse
	 * @param l_map_model the map model to add the continent to
	 * @throws Exception error parsing the neighbor record, unexpected error
	 */
	private static void parseMapFileBorders(String l_rec, IMapModel l_map_model) throws Exception {
		String[] l_tokens = Utl.getFirstWord(l_rec);

		String l_country_id_str = l_tokens[0];
		int l_country_id = Utl.convertToInteger(l_country_id_str);
		if (l_country_id >= Integer.MAX_VALUE) {
			throw new Exception("Invalid border country id value '" + l_country_id_str + "' specified");
		}

		l_tokens = Utl.getFirstWord(l_tokens[1]);
		while (!Utl.isEmpty(l_tokens[0])) {
			String l_border_id_str = l_tokens[0];
			int l_border_id = Utl.convertToInteger(l_border_id_str);
			if (l_border_id >= Integer.MAX_VALUE) {
				throw new Exception("Invalid border country id value '" + l_border_id_str
						+ "' specified for country with id of '" + l_country_id_str + "'");
			}
			l_map_model.addNeighbor(l_country_id, l_border_id);
			l_tokens = Utl.getFirstWord(l_tokens[1]);
		}
	}

	/**
	 * Parse a country specification from the map file
	 * 
	 * @param l_rec       the country record to parse
	 * @param l_map_model the map model to add the continent to
	 * @throws Exception error parsing the country record, unexpected error
	 */
	private static void parseMapFileCountry(String l_rec, IMapModel l_map_model) throws Exception {
		String[] l_tokens = Utl.getFirstWord(l_rec);

		String l_country_id_str = l_tokens[0];
		int l_country_id = Utl.convertToInteger(l_country_id_str);
		if (l_country_id >= Integer.MAX_VALUE) {
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
		if (l_continent_id >= Integer.MAX_VALUE) {
			throw new Exception("Invalid continent id value '" + l_continent_id_str + "' specified for country '"
					+ l_country_name + "'");
		}

		l_map_model.addCountry(l_country_id, l_country_name, l_continent_id);
	}

	/**
	 * Parse a continent specification from the map file
	 * 
	 * @param p_id        the continent id
	 * @param l_rec       the continent record to parse
	 * @param l_map_model the map model to add the continent to
	 * @throws Exception error parsing the continent record, unexpected error
	 */
	private static void parseMapFileContinent(int p_id, String l_rec, IMapModel l_map_model) throws Exception {
		String[] l_tokens = Utl.getFirstWord(l_rec);

		String l_continent_name = l_tokens[0];
		if (!Utl.isValidMapName(l_continent_name)) {
			throw new Exception("Invalid continent  name '" + l_continent_name + "'");
		}

		l_tokens = Utl.getFirstWord(l_tokens[1]);

		String l_continent_xtra_army_str = l_tokens[0];
		int l_continent_xtra_army = Utl.convertToInteger(l_continent_xtra_army_str);
		if (l_continent_xtra_army >= Integer.MAX_VALUE) {
			throw new Exception("Invalid extra army value '" + l_continent_xtra_army_str + "' specified for continent '"
					+ l_continent_name + "'");
		}

		l_map_model.addContinent(p_id, l_continent_name, l_continent_xtra_army);
	}

	/**
	 * parse the loapmap command.
	 * <p>
	 * Syntax: loapmap filename
	 * </p>
	 *
	 * @param p_loadmap_params the loapmap parameters (just the parameters without
	 *                         the loapmap command itself)
	 * @return map model based on the supplied map file filename.
	 * @throws Exception any problem parsing or creating the new map
	 */
	public static IMapModel processLoadMapCommand(String p_loadmap_params) throws Exception {
		String l_params[] = Utl.getFirstWord(p_loadmap_params);
		String l_filename = l_params[0];
		if (Utl.isEmpty(l_filename)) {
			throw new Exception("Invalid loapmap command, no options specified");
		}
		IMapModel l_map = Map.loadMapFromFile(l_filename);
		return l_map;
	}

	/**
	 * loads an existing map file or create a new one in case file does not exist
	 * 
	 * @param p_filename map file name
	 * @return the loaded or created map model
	 * @throws NumberFormatException when it is not possible to cast string to
	 *                               integer
	 * @throws Exception             when there is an exception
	 */
	public static IMapModel editmap(String p_filename) throws Exception {
		return loadMapFromFile(p_filename);
//		File l_filename = new File(p_filename + ".map");
//		if (l_filename.exists()) {
//			loadmap(p_filename);
//		}
//
//		else {
//			try {
//				l_filename.createNewFile();
//			} catch (IOException e) {
//
//				e.printStackTrace();
//			}
//
//		}
	}
}
