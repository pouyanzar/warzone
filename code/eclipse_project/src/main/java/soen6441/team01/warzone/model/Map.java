package soen6441.team01.warzone.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.activation.ActivationGroup_Stub;
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
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;

/**
 * Manages Warzone Maps. Maps are basically composed of continents, countries
 * and the links (ie neighbors) between countries.
 *
 */
public class Map implements IMapModel {

	private SoftwareFactoryModel d_factory_model = null;
	private ArrayList<IContinentModel> d_continents = new ArrayList<IContinentModel>();
	private ArrayList<ICountryModel> d_countries = new ArrayList<ICountryModel>();

	/**
	 * Constructor
	 * 
	 * @param p_factory_model the model software factory
	 */
	public Map(SoftwareFactoryModel p_factory_model) {
		d_factory_model = p_factory_model;
	}

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
	 * @param l_continent continent to add
	 * @throws Exception unexpected error
	 */
	public void addContinent(IContinentModel l_continent) throws Exception {
		d_continents.add(l_continent);
	}

	/**
	 * Add a country to the current map
	 *
	 * @param p_country the country object to add
	 * @throws Exception when there is an exception
	 */
	public void addCountry(ICountryModel p_country) throws Exception {
		d_countries.add(p_country);
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
		ICountryModel l_country = new Country(p_country_id, p_country_name, p_continent, p_x, p_y, d_factory_model);
		addCountry(l_country);
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
	 * @return l_isValid when the map is valid is true otherwise is false.
	 * @throws Exception when there is an exception
	 */
	public boolean validatemap() throws Exception {

		ArrayList<ICountryModel> l_passed_countries = new ArrayList<>(); // list of visited countries from start point
																			// country
		ArrayList<ICountryModel> l_continent_countries = new ArrayList<>(); // list of continent's countries
		IUserMessageModel l_msg = d_factory_model.getUserMessageModel();

		// Checks if there is at least one continent on the map
		if (d_continents.size() < 1) {
			l_msg.setMessage(MessageType.Error, "Map does not have at least 1 continent.");
			return false;
		}

		// Checks if there is at least one country on the map
		if (d_countries.size() < 1) {
			l_msg.setMessage(MessageType.Error, "Map does not have at least 1 country.");
			return false;
		}

		// Checks if it is possible to reach to all countries on the map from any
		// country
		for (ICountryModel l_country : d_countries) {
			l_passed_countries.removeAll(l_passed_countries);
			if (l_country.getNeighbors().size() < 1) {
				// Checks if the country have at least one neighbor
				l_msg.setMessage(MessageType.Error,
						"Country '" + l_country.getName() + "' should have at least 1 neighbor");
				return false;
			}
			mapTraversal(l_country, l_passed_countries);
			for (ICountryModel l_country1 : d_countries) {
				if (!l_passed_countries.contains(l_country1)) {
					l_msg.setMessage(MessageType.Error,
							"Country '" + l_country.getName() + "' is not fully connected to all other countries");
					return false;
				}
			}
		}

		// Checks if it is possible to reach to all countries of a continent from any
		// country of that continent without exiting the continent
		for (IContinentModel l_continent : d_continents) {
			l_continent_countries = getContinentCountries(l_continent);
			for (ICountryModel l_country : l_continent_countries) {
				l_passed_countries.removeAll(l_passed_countries);
				if (l_country.getNeighbors().size() < 1) {
					// Checks if the country have at least one neighbor
					l_msg.setMessage(MessageType.Error,
							"Country '" + l_country.getName() + "' should have at least 1 neighbor");
					return false;
				}
				mapContinentTraversal(l_continent, l_country, l_passed_countries);
				for (ICountryModel l_country1 : l_continent_countries) {
					if (!l_passed_countries.contains(l_country1)) {
						l_msg.setMessage(MessageType.Error, "Country '" + l_country.getName() + "' in continent '"
								+ l_continent.getName() + " is not fully connected to all other countries");
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Takes a continent and returns the list of its countries
	 * 
	 * @param p_continent an instance of Continent class
	 * @return l_continent_countries an ArrayList of the continent's countries
	 */
	public ArrayList<ICountryModel> getContinentCountries(IContinentModel p_continent) {
		ArrayList<ICountryModel> l_continent_countries = new ArrayList<>();
		for (ICountryModel l_country : d_countries) {
			if (l_country.getContinent().getId() == p_continent.getId()) {
				l_continent_countries.add(l_country);
			}
		}
		return l_continent_countries;
	}

	/**
	 * implementation of DFS algorithm to traverse all nodes of the graph
	 * 
	 * @param p_country           the starting point for traverse
	 * @param p_visited_countries the list of countries visited through the search
	 * 
	 */
	public static void mapTraversal(ICountryModel p_country, ArrayList<ICountryModel> p_visited_countries) {
		p_visited_countries.add(p_country);
		for (ICountryModel l_country : p_country.getNeighbors()) {
			if (!p_visited_countries.contains(l_country)) {
				mapTraversal(l_country, p_visited_countries);
			}
		}
	}

	/**
	 * implements a DFS algorithm to traverse all the countries of a continent
	 * 
	 * @param p_continent         the desired continent
	 * @param p_country           the start point country
	 * @param p_visited_countries list of all countries visited
	 */
	public static void mapContinentTraversal(IContinentModel p_continent, ICountryModel p_country,
			ArrayList<ICountryModel> p_visited_countries) {
		p_visited_countries.add(p_country);
		for (ICountryModel l_country : p_country.getNeighbors()) {
			if (!p_visited_countries.contains(l_country)) {
				if (l_country.getContinent().getId() == p_continent.getId())
					mapContinentTraversal(p_continent, l_country, p_visited_countries);
			}
		}
	}

	/**
	 * Loads a Warzone map from an array containing records defined by "domination"
	 * style map file. The following link describes the format of the "domination"
	 * map file : http://domination.sourceforge.net/makemaps.shtml.
	 *
	 * @param p_records       a list of map records
	 * @param p_factory_model the required factory model
	 * @return instance of new Map model
	 * @throws Exception error parsing the contents of the map file
	 */
	public static IMapModel loadMap(List<String> p_records, SoftwareFactoryModel p_factory_model) throws Exception {
		IMapModel l_map_model = new Map(p_factory_model);
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

		// ask the continent objects to build their list of countries
		refreshCountriesOfAllContinents(l_map_model);

		return l_map_model;
	}

	/**
	 * ask the continent objects to build their list of countries
	 * 
	 * @param p_map_model the map model containing the continents to refresh
	 */
	public static void refreshCountriesOfAllContinents(IMapModel p_map_model) {
		ArrayList<IContinentModel> l_continents = p_map_model.getContinents();
		for (IContinentModel l_continent : l_continents) {
			l_continent.refreshCountriesOfContinent(p_map_model);
		}
	}

	/**
	 * Loads a Warzone map from an existing "domination" style map file. The
	 * following link describes the format of the "domination" map file :
	 * http://domination.sourceforge.net/makemaps.shtml.
	 * 
	 * @param p_map_filename  the filename of the map file
	 * @param p_factory_model the model factory to use when needed
	 * @return instance of new Map model
	 * @throws Exception error parsing the contents of the map file
	 */
	public static IMapModel loadMapFromFile(String p_map_filename, SoftwareFactoryModel p_factory_model)
			throws Exception {
		List<String> l_records = null;
		IMapModel l_map_model = null;
		try {
			l_records = Files.readAllLines(new File(p_map_filename).toPath(), Charset.defaultCharset());
			l_map_model = loadMap(l_records, p_factory_model);
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
		if (l_country_id >= Integer.MAX_VALUE || l_country_id < 0) {
			throw new Exception("Invalid border country id value '" + l_country_id_str + "' specified");
		}

		l_tokens = Utl.getFirstWord(l_tokens[1]);
		while (!Utl.isEmpty(l_tokens[0])) {
			String l_border_id_str = l_tokens[0];
			int l_border_id = Utl.convertToInteger(l_border_id_str);
			if (l_border_id >= Integer.MAX_VALUE || l_border_id < 0) {
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
	 * @param p_factory_model  the model factory to use when needed
	 * @return map model based on the supplied map file filename.
	 * @throws Exception any problem parsing or creating the new map
	 */
	public static IMapModel processLoadMapCommand(String p_loadmap_params, SoftwareFactoryModel p_factory_model)
			throws Exception {
		String l_params[] = Utl.getFirstWord(p_loadmap_params);
		String l_filename = l_params[0];
		if (Utl.isEmpty(l_filename)) {
			throw new Exception("Invalid loapmap command, no options specified");
		}
		IMapModel l_map = Map.loadMapFromFile(l_filename, p_factory_model);
		return l_map;
	}

	/**
	 * Load a map from an existing "domination" map file, or create a new map from
	 * scratch if the file does not exist.
	 * 
	 * @param p_filename      map file name
	 * @param p_factory_model the model factory to use when needed
	 * @return the loaded or created map model
	 * @throws NumberFormatException when it is not possible to cast string to
	 *                               integer
	 * @throws Exception             when there is an exception
	 */
	public static IMapModel editmap(String p_filename, SoftwareFactoryModel p_factory_model) throws Exception {
		IMapModel l_map;

		// if the specified filename exists then load the existing map from the file
		File l_filename = new File(p_filename);
		if (l_filename.exists()) {
			l_map = Map.loadMapFromFile(p_filename, p_factory_model);
			return l_map;
		}

		// the specified filename does not exist, therefore create a new map
		p_factory_model.getUserMessageModel().setMessage(MessageType.Warning,
				"Specified filename '" + p_filename + "' does not exist. Creating new (empty) map.");
		l_map = new Map(p_factory_model);
		p_factory_model.setMapModel(l_map);

		return l_map;
	}

	/**
	 * Create a new isolated map that contains a copy of all counties and
	 * continents.
	 * 
	 * @param p_src_map              the source map to copy from
	 * @param p_cloned_factory_model the factory model to base the copy on (modifies
	 *                               respective references)
	 * @return newly created isolated copy of the map
	 * @throws Exception unexpected error
	 */
	public static IMapModel deepCloneMap(IMapModel p_src_map, SoftwareFactoryModel p_cloned_factory_model)
			throws Exception {
		Map l_map = new Map(p_cloned_factory_model);
		p_cloned_factory_model.setMapModel(l_map);
		// clone continents
		for (IContinentModel l_continent : p_src_map.getContinents()) {
			l_map.addContinent(new Continent(l_continent));
		}
		// clone countries (note: neighbors not set yet)
		for (ICountryModel l_country : p_src_map.getCountries()) {
			ICountryModel l_country_clone = new Country(l_country.getId(), l_country.getName(),
					l_country.getContinent(), 0, 0, p_cloned_factory_model);
			l_country_clone.setArmies(l_country.getArmies());
			l_country_clone.setOwner(l_country.getOwner());
			l_map.addCountry(l_country_clone);
		}
		// set countries neighbors
		for (ICountryModel l_src_country : p_src_map.getCountries()) {
			ICountryModel l_country = Country.findCountry(l_src_country.getId(), l_map.getCountries());
			if (l_country == null) {
				throw new Exception("Internal error cloning map countries");
			}
			for (ICountryModel l_src_neighbors : l_src_country.getNeighbors()) {
				ICountryModel l_neighbor = Country.findCountry(l_src_neighbors.getId(), l_map.getCountries());
				if (l_neighbor == null) {
					throw new Exception("Internal error cloning country neighbors");
				}
				l_country.addNeighbor(l_neighbor);
			}
		}

		return l_map;
	}

	/**
	 * Convert the current map into the "domination" game map format. Note that this
	 * implementation of Warzone does not process the continent color which is part
	 * of the domination map file format.
	 * 
	 * @return the current map in the domination game style map file. Each string of
	 *         the array is a line in the map file.
	 */
	public ArrayList<String> getMapAsDominationMapFormat() {
		ArrayList<String> l_dmap = new ArrayList<String>();
		l_dmap.add("[files]");

		// process continent section
		l_dmap.add("");
		l_dmap.add("[continents]");
		for (IContinentModel l_continent : d_continents) {
			l_dmap.add(l_continent.toDominationMapString());
		}

		// process country section
		l_dmap.add("");
		l_dmap.add("[countries]");
		for (ICountryModel l_country : d_countries) {
			l_dmap.add(l_country.toDominationMapString());
		}

		// process border section
		l_dmap.add("");
		l_dmap.add("[borders]");
		for (ICountryModel l_country : d_countries) {
			l_dmap.add(l_country.toDominationMapBorderString().trim());
		}

		return l_dmap;
	}

	/**
	 * Save a map to a text file exactly as edited (using the "domination" game map
	 * format). Note that the map is validated before it is saved, and a warning is
	 * issued to the user if it's invalid; however the map is still saved if it's
	 * invalid to give the user a chance to reload it and finish editing it at a
	 * later time.
	 * 
	 * @param p_filename the filaname of the map file
	 * @throws Exception unexpected error
	 */
	public void saveMap(String p_filename) throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(p_filename));
		ArrayList<String> l_map_data = getMapAsDominationMapFormat();
		for (String l_map_rec : l_map_data)
			pw.println(l_map_rec);
		pw.close();
		if (!validatemap()) {
			d_factory_model.getUserMessageModel().setMessage(MessageType.Warning,
					"The saved map is not a fully connected valid map. Please use the map editor to fix the issue before playing a game.");
		}
	}

}
