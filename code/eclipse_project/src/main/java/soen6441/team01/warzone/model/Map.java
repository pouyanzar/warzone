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
		l_country = new Country(l_id, p_country_name, p_continent, p_x, p_y);
		d_countries.add(l_country);
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
	 * @return true when the map is valid; false if the map is not valid
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
	 * load a map from map file and provides it as a connected directed graph
	 * 
	 * @param p_map_name file map name
	 * @throws NumberFormatException when it is not possible to cast string to
	 *                               integer
	 * @throws Exception             when there is an exception
	 */
	public void loadmap(String p_map_name) throws NumberFormatException, Exception {

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

		// loop to fill the neighborhoods ArrayList
		for (int i = l_borders_index + 1; i < l_pattern.size(); i++) {
			d_neighborhoods.add(new ArrayList<>());
		}
		for (int i = l_borders_index + 1; i < l_pattern.size(); i++) {
			String[] l_neighbors = l_pattern.get(i).split(" ");
			for (int j = 1; j < l_neighbors.length; j++) {
				d_neighborhoods.get(Integer.parseInt(l_neighbors[0]) - 1).add(Integer.parseInt(l_neighbors[j]));
				System.out.println(d_neighborhoods.get(i - l_borders_index - 1));

			}
		}

		// create ArrayLists to store related continents and countries and neighbors
		ArrayList<ArrayList<ICountryModel>> l_continent_graph = new ArrayList<ArrayList<ICountryModel>>();
		;
		ArrayList<ArrayList<ArrayList<Integer>>> l_country_graph = new ArrayList<ArrayList<ArrayList<Integer>>>();

		// allocate memory to ArrayLists
		for (int i = 0; i < l_continent_graph.size(); i++) {
			l_continent_graph.add(new ArrayList<ICountryModel>());
		}
		for (int i = 0; i < l_country_graph.size(); i++) {
			l_country_graph.add(new ArrayList<ArrayList<Integer>>());
		}

		// add countries to corresponding continent
		for (int i = 0; i < d_continents.size(); i++) {
			for (int j = 0; j < d_countries.size(); j++) {
				if (d_countries.get(j).getContinentId() == (d_continents.get(i).getId())) {
					l_continent_graph.get(d_continents.get(i).getId()).add(d_countries.get(j));
				}
			}
		}

		// add neighbors to corresponding countries
		for (int i = 0; i < d_countries.size(); i++) {
			l_country_graph.get(d_countries.get(i).getId()).add(d_neighborhoods.get(i));

		}
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
