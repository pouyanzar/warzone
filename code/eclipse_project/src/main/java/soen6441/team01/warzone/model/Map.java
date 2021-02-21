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
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashMap;

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
	private ArrayList<ArrayList<Integer>> d_neighborhoods = new ArrayList<ArrayList<Integer>>();

	public Map(ArrayList<IContinentModel> d_continents, ArrayList<ICountryModel> d_countries,
			ArrayList<ArrayList<Integer>> d_neighborhoods) {
		this.d_continents = (ArrayList<IContinentModel>) d_continents.clone();
		this.d_countries = (ArrayList<ICountryModel>) d_countries.clone();
		this.d_neighborhoods = (ArrayList<ArrayList<Integer>>) d_neighborhoods.clone();

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
	 * @param p_country_id   a unique country identifier
	 * @param p_country_name the name of the country
	 * @param p_continent    the associated continent
	 * @param p_x            the x coordinate as defined in the map file
	 * @param p_y            the y coordinate as defined in the map file
	 * @return the created country
	 * @throws Exception when there is an exception
	 */
	public ICountryModel addCountry(int p_country_id, String p_country_name, IContinentModel p_continent, int p_x,
			int p_y) throws Exception {
		ICountryModel l_country = Country.findCountry(p_country_id, d_countries);
		if (l_country != null) {
			throw new Exception("Cannot add country with id " + p_country_id + " since it already exists.");
		}
		l_country = new Country(p_country_id, p_country_name, p_continent, p_x, p_y);
		d_countries.add(l_country);
		return l_country;
	}

	/**
	 * Add a country to the current map
	 * 
	 * @param p_country_id   a unique country identifier
	 * @param p_continent_id the associated continent
	 * @return the created country
	 * @throws Exception when there is an exception
	 */
	public ICountryModel addCountry(int p_country_id, int p_continent_id) throws Exception {
		ICountryModel l_country = Country.findCountry(p_country_id, d_countries);
		if (l_country != null) {
			throw new Exception("Cannot add country with id " + p_country_id + " since it already exists.");
		}
		l_country = new Country(p_country_id, p_continent_id);
		d_countries.add(l_country);
		return l_country;
	}

	/**
	 * Loads a Warzone map from an existing "domination" style map file. The
	 * following link describes the format of the "domination" map file :
	 * http://domination.sourceforge.net/makemaps.shtml
	 * 
	 * @param p_map_filename the filename of the "domination" map file
	 * @throws IOException when there is a problem processing the map file
	 */
	public void loadMap(String p_map_filename) throws IOException {
		List<String> list = Files.readAllLines(new File(p_map_filename).toPath(), Charset.defaultCharset());
		// todo: parse the map file...
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
	 * @param p_country_id the country id
	 * @return the deleted country object
	 * @throws Exception if the country cannot be removed
	 */
	public ICountryModel removeCountry(int p_country_id) throws Exception {
		ICountryModel l_country = Country.findCountry(p_country_id, d_countries);
		if (l_country == null) {
			throw new Exception("Cannot remove continent with id " + p_country_id + " since it doesn't exist.");
		}
		d_continents.remove(l_country);
		return l_country;
	}

	/**
	 * creates the adjacency list of countries
	 * 
	 * @param p_neighborhoods       the current neighborhood
	 * @param p_country_id          the current country id
	 * @param p_neighbor_country_id the id of the country associated to the current
	 *                              country
	 */
	public void addNeighborhood(ArrayList<ArrayList<Integer>> p_neighborhoods, int p_country_id,
			int p_neighbor_country_id) {

		p_neighborhoods.get(p_country_id).add(p_neighbor_country_id);
	}

	public void removeNeighborhood(ArrayList<ArrayList<Integer>> p_neighborhoods, int p_country_id,
			int p_neighbor_country_id) {

		if (!p_neighborhoods.isEmpty()) {
			for (int i = 0; i < p_neighborhoods.size(); i++) {

				if (p_neighborhoods.get(p_country_id).get(i) == p_neighbor_country_id) {
					p_neighborhoods.get(p_country_id).remove(i);
				}
			}
		}

	}

	/**
	 * based on string commands adds or removes continents.
	 * 
	 * @param p_commands the string commands
	 * @throws NumberFormatException when there is no appropriate format in string
	 *                               to convert into integer
	 * @throws Exception             when there is an exception
	 */
	public void editcontinent(String p_commands) throws NumberFormatException, Exception {

		String[] l_commands = p_commands.split("-");

		for (String l_command : l_commands) {

			if (l_command.contains("add")) {
				String[] l_command_parameter = l_command.split(" ");
				addContinent(l_command_parameter[1], Integer.parseInt(l_command_parameter[2]));

			}
		}
	}

	/**
	 * based on string commands adds or removes countries.
	 * 
	 * @param p_commands the string commands
	 * @throws NumberFormatException when there is no appropriate format in string
	 *                               to convert into integer
	 * @throws Exception             when there is an exception
	 */
	public void editcountry(String p_commands) throws NumberFormatException, Exception {
		String[] l_commands = p_commands.split("-");
		for (String l_command : l_commands) {
			if (l_command.contains("add")) {
				String[] l_command_parameter = l_command.split(" ");
				addCountry(Integer.parseInt(l_command_parameter[1]), Integer.parseInt(l_command_parameter[2]));
			} else if (l_command.contains("remove")) {
				String[] l_command_parameter = l_command.split(" ");
				removeCountry(Integer.parseInt(l_command_parameter[1]));
			}

		}
	}

	/**
	 * edit adjacency of two countries based on command
	 * 
	 * @param p_commands the string command to edit adjacency of two countries
	 * @throws NumberFormatException when there is no appropriate format in string
	 *                               to convert into integer
	 * @throws Exception             when there is an exception
	 */
	public void editneighbor(String p_commands) throws NumberFormatException, Exception {
		String[] l_commands = p_commands.split("-");
		for (String l_command : l_commands) {
			if (l_command.contains("add")) {
				String[] l_command_parameter = l_command.split(" ");
				addNeighborhood(d_neighborhoods, Integer.parseInt(l_command_parameter[1]),
						Integer.parseInt(l_command_parameter[2]));
			} else if (l_command.contains("remove")) {
				String[] l_command_parameter = l_command.split(" ");
				removeNeighborhood(d_neighborhoods, Integer.parseInt(l_command_parameter[1]),
						Integer.parseInt(l_command_parameter[2]));
			}

		}
	}

	/**
	 * Checks if there is at least one continent, one country, and there is at least
	 * one neighbor for each country on the current map
	 * 
	 * @return true when the map is valid
	 * @return false if the map is not valid
	 */
	public boolean validatemap() {

		if (d_continents.size() < 1)
			return false;
		if (d_countries.size() < 1)
			return false;
		for (ArrayList<Integer> l_neighbor : d_neighborhoods) {
			if (l_neighbor.size() < 1) {
				return false;
			} else
				continue;
		}
		return true;
	}

	/**
	 * loads the desired map from the map file
	 * 
	 * @param p_map_name the name of the map file
	 * @return map the instance of map class
	 * @throws NumberFormatException when there is no appropriate format in string
	 *                               to convert into integer
	 * @throws Exception             when there is an exception
	 */
	public Map loadmap(String p_map_name) throws NumberFormatException, Exception {

		String l_filename = "src/main/resources" + p_map_name + ".map";
		Path l_path = Paths.get(l_filename);
		Stream<String> l_lines = Files.lines(l_path);
		ArrayList<String> l_pattern = new ArrayList<>(); // an ArrayList to store the map file
		int l_continent_index = 0; // index line in continents start
		int l_country_index = 0; // index line in countries start
		int l_borders_index = 0; // index line in borders start
		l_lines.forEach(s -> l_pattern.add(s)); // loop over the map file to add all lines into the ArrayList
		l_lines.close();

		// loop over ArrayList to find the line indexes for each section of the map file
		for (int i = 0; i < l_pattern.size(); i++) {
			switch (l_pattern.get(i)) {
			case "[continents]":
				l_continent_index = i;
				break;
			case "[countries]":
				l_country_index = i;
				break;
			case "[borders]":
				l_borders_index = i;
				break;
			}
		}

		// loop to fill the continents on the map inside continents ArrayList
		for (int i = l_continent_index + 1; i < l_country_index - 1; i++) {
			String[] l_continents = l_pattern.get(i).split(" ");
			IContinentModel l_continent = new Continent(i, l_continents[0], Integer.parseInt(l_continents[1]));
			d_continents.add(l_continent);
		}

		// loop to fill the countries on the map inside countries ArrayList
		for (int i = l_country_index + 1; i < l_borders_index - 1; i++) {
			String[] l_countries = l_pattern.get(i).split(" ");
			ICountryModel l_country = new Country(Integer.parseInt(l_countries[0]), l_countries[1],
					Integer.parseInt(l_countries[2]));
			d_countries.add(l_country);
		}
        
		//loop to fill the neighborhoods ArrayList
		for (int i = l_borders_index + 1; i < l_pattern.size(); i++) {
			d_neighborhoods.add(new ArrayList<>());
		}
		for (int i = l_borders_index + 1; i < l_pattern.size(); i++) {
			String[] l_neighbors = l_pattern.get(i).split(" ");
			for (int j = 0; j<l_neighbors.length ; j++) {
				d_neighborhoods.get(Integer.parseInt(l_neighbors[0])-1).add(Integer.parseInt(l_neighbors[j]));
			}
		}

		Map map = new Map(d_continents, d_countries, d_neighborhoods);

		return map;
	}
}

	

///**
//* Loads for editing an existing Warzone map from an existing "domination" style
//* map file; or, will create an empty Warzone map if there is no existing map
//* file.
//* 
//* @param p_map_filename the filename of the "domination" map file to load or
//*                       create
//* @throws Exception
//*/
//public void editMap(String p_map_filename) throws Exception {
//	File l_map_file = new File(p_map_filename);
//	if (!l_map_file.exists()) {
//		// create 'empty' map file
//		String l_empty_map = "; map: #MAP#.map\n" + "[files]\n" + "[continents]\n" + "[countries]\n"
//				+ "[borders]\n";
//		PrintWriter l_pw = new PrintWriter(p_map_filename);
//		l_pw.println(l_empty_map);
//		l_pw.close();
//	}
//	loadMap(p_map_filename);
//}
