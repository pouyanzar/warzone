package soen6441.team01.warzone.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.entities.DominationBorder;
import soen6441.team01.warzone.model.entities.DominationContinent;
import soen6441.team01.warzone.model.entities.DominationCountry;
import soen6441.team01.warzone.model.entities.DominationMap;
import soen6441.team01.warzone.model.contracts.IAppMsg;

/**
 * Manages Warzone Maps. Maps are basically composed of continents, countries
 * and the links (ie neighbors) between countries.
 *
 */
public class Map implements IMapModel {

	private ModelFactory d_factory_model = null;
	private ArrayList<IContinentModel> d_continents = new ArrayList<IContinentModel>();
	private ArrayList<ICountryModel> d_countries = new ArrayList<ICountryModel>();

	/**
	 * Constructor
	 * 
	 * @param p_factory_model the model software factory
	 */
	public Map(ModelFactory p_factory_model) {
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
	 * @param p_color          the color of the continent
	 * @return the created continent
	 * @throws Exception when there is an exception
	 */
	public IContinentModel addContinent(int p_continent_id, String p_continent_name, int p_extra_army, String p_color)
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
		IAppMsg l_msg = d_factory_model.getUserMessageModel();

		// Checks if there is at least one continent on the map
		if (d_continents.size() < 1) {
			l_msg.setMessage(MsgType.Error, "Map does not have at least 1 continent.");
			return false;
		}

		// Checks if there is at least one country on the map
		if (d_countries.size() < 1) {
			l_msg.setMessage(MsgType.Error, "Map does not have at least 1 country.");
			return false;
		}

		// Checks if it is possible to reach to all countries on the map from any
		// country
		for (ICountryModel l_country : d_countries) {
			l_passed_countries.removeAll(l_passed_countries);
			if (l_country.getNeighbors().size() < 1) {
				// Checks if the country have at least one neighbor
				l_msg.setMessage(MsgType.Error,
						"Country '" + l_country.getName() + "' should have at least 1 neighbor");
				return false;
			}
			mapTraversal(l_country, l_passed_countries);
			for (ICountryModel l_country1 : d_countries) {
				if (!l_passed_countries.contains(l_country1)) {
					l_msg.setMessage(MsgType.Error,
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
					l_msg.setMessage(MsgType.Error,
							"Country '" + l_country.getName() + "' should have at least 1 neighbor");
					return false;
				}
				mapContinentTraversal(l_continent, l_country, l_passed_countries);
				for (ICountryModel l_country1 : l_continent_countries) {
					if (!l_passed_countries.contains(l_country1)) {
						l_msg.setMessage(MsgType.Error, "Country '" + l_country.getName() + "' in continent '"
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
	public static IMapModel loadMap(List<String> p_records, ModelFactory p_factory_model) throws Exception {
		IMapModel l_map = new Map(p_factory_model);

		MapIoDomination l_mapio = new MapIoDomination();
		DominationMap l_xmap = l_mapio.parseMap(p_records);

		// create continents
		for (DominationContinent d_continent : l_xmap.d_continents) {
			int l_id = Utl.convertToInteger(d_continent.d_id);
            if (l_id >= Integer.MAX_VALUE || l_id < 0) {
                throw new Exception("Invalid continent id value '" + d_continent.d_id + "' specified for country '");
            }
            if (!Utl.isValidMapName(d_continent.d_name)) {
                throw new Exception("Invalid continent  name '" + d_continent.d_name + "'");
            }
			l_map.addContinent(l_id, d_continent.d_name, d_continent.d_extra_armies, d_continent.d_color);
		}

		// create countries
		for (DominationCountry d_country : l_xmap.d_countries) {
			int l_id = Utl.convertToInteger(d_country.d_id);
            if (l_id >= Integer.MAX_VALUE || l_id < 0) {
                throw new Exception("Invalid country id value '" + d_country.d_id + "' specified");
            }
            if (!Utl.isValidMapName(d_country.d_name)) {
                throw new Exception("Invalid country name '" + d_country.d_name + "'");
            }
			int l_contid = Utl.convertToInteger(d_country.d_continent_id);
            if (l_contid >= Integer.MAX_VALUE || l_contid < 0) {
                throw new Exception("Invalid continent id value '" + d_country.d_continent_id + "' specified for country '"
                                + d_country.d_name + "'");
            }
			ICountryModel l_tmp_country = l_map.addCountry(l_id, d_country.d_name, l_contid);
			int l_xy = Utl.convertToInteger(d_country.d_x);
			l_tmp_country.setX(l_xy);
			l_xy = Utl.convertToInteger(d_country.d_y);
			l_tmp_country.setY(l_xy);
		}

		// create neighbors
		for (DominationBorder l_border : l_xmap.d_borders) {
			int l_country_id = Utl.convertToInteger(l_border.d_country_id);
            if (l_country_id >= Integer.MAX_VALUE || l_country_id < 0) {
                throw new Exception("Invalid border country id value '" + l_border.d_country_id + "' specified");
            }
			for (String l_border_id_str : l_border.d_border_country_id) {
				int l_border_id = Utl.convertToInteger(l_border_id_str);
				if (l_border_id >= Integer.MAX_VALUE || l_border_id < 0) {
					throw new Exception("Invalid border country id value '" + l_border_id_str
							+ "' specified for country with id of '" + l_border.d_country_id + "'");
				}
				l_map.addNeighbor(l_country_id, l_border_id);
			}
		}

		// ask the continent objects to build their list of countries
		refreshCountriesOfAllContinents(l_map);

		return l_map;
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
	public static IMapModel loadMapFromFile(String p_map_filename, ModelFactory p_factory_model) throws Exception {
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
	 * parse the loadmap command.
	 * <p>
	 * Syntax: loadmap filename
	 * </p>
	 *
	 * @param p_loadmap_params the loadmap parameters (just the parameters without
	 *                         the loadmap command itself)
	 * @param p_factory_model  the model factory to use when needed
	 * @return map model based on the supplied map file filename.
	 * @throws Exception any problem parsing or creating the new map
	 */
	public static IMapModel processLoadMapCommand(String p_loadmap_params, ModelFactory p_factory_model)
			throws Exception {
		String l_params[] = Utl.getFirstWord(p_loadmap_params);
		String l_filename = l_params[0];
		if (Utl.isEmpty(l_filename)) {
			throw new Exception("Invalid loadmap command, no options specified");
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
	public static IMapModel editmap(String p_filename, ModelFactory p_factory_model) throws Exception {
		IMapModel l_map;

		// if the specified filename exists then load the existing map from the file
		File l_filename = new File(p_filename);
		if (l_filename.exists()) {
			l_map = Map.loadMapFromFile(p_filename, p_factory_model);
			return l_map;
		}

		// the specified filename does not exist, therefore create a new map
		p_factory_model.getUserMessageModel().setMessage(MsgType.Warning,
				"Specified filename '" + p_filename + "' does not exist. Creating new (empty) map.");
		l_map = new Map(p_factory_model);
		p_factory_model.setMapModel(l_map);

		return l_map;
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
		ArrayList<String> l_map_data = new MapIoDomination().getMapAsDominationMapFormat(this);
		for (String l_map_rec : l_map_data) {
			pw.println(l_map_rec);
		}
		pw.close();
		if (!validatemap()) {
			d_factory_model.getUserMessageModel().setMessage(MsgType.Warning,
					"The saved map is not a fully connected valid map. Please use the map editor to fix the issue before playing a game.");
		}
	}

	/**
	 * Create a new isolated map that contains a copy of all counties and
	 * continents.
	 * 
	 * @return newly created isolated copy of the map
	 * @throws Exception unexpected error
	 */
	public ModelFactory deepCloneMap() throws Exception {
		ModelFactory l_new_factory_model = new ModelFactory(d_factory_model);
		ArrayList<String> l_map_data = new MapIoDomination().getMapAsDominationMapFormat(this);
		IMapModel l_map_model = loadMap(l_map_data, l_new_factory_model);
		l_new_factory_model.setMapModel(l_map_model);
		for (ICountryModel l_country_src : d_countries) {
			ICountryModel l_country_dest = Country.findCountry(l_country_src.getName(), l_map_model.getCountries());
			l_country_dest.setArmies(l_country_src.getArmies());
		}
		return l_new_factory_model;
	}
}
