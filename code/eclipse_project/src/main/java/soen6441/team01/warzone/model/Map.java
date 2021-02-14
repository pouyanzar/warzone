package soen6441.team01.warzone.model;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages Warzone Maps
 * 
 * The following link describes the format of the "domination" map file used
 * herein: http://domination.sourceforge.net/makemaps.shtml
 *
 */
public class Map implements IMapModel, IMapModelView {

	private HashMap<Country, List<Country>> neighborhood;

	public Map() {
		this.neighborhood = new HashMap<Country, List<Country>>();
	}
	
	/**
	 * Adds desired country to map
	 * @param p_countryID
	 * @return none
	 */
	public void addCountry(String p_countryID) {
		neighborhood.putIfAbsent(new Country(p_countryID), new ArrayList<Country>());
	}
	
	/**
	 * Removes selected country from map
	 * @param p_countryID
	 * @return none
	 */
	public void removeCountry(String p_countryID) {
		Country l_countryToRemove = new Country(p_countryID);
		neighborhood.values().stream().forEach(l_country -> l_country.remove(l_countryToRemove));
		neighborhood.remove(new Country(p_countryID));
	}

	/**
	 * Loads for editing an existing Warzone map from an existing "domination" map
	 * file; or, will create an empty Warzone map if there is no existing map file.
	 * 
	 * @param p_map_filename the filename of the "domination" map file to load or
	 *                       create
	 * @throws Exception
	 */
	public void editMap(String p_map_filename) throws Exception {
		File l_map_file = new File(p_map_filename);
		if (!l_map_file.exists()) {
			// create 'empty' map file
			String l_empty_map = "; map: #MAP#.map\n" + "[files]\n" + "[continents]\n" + "[countries]\n"
					+ "[borders]\n";
			PrintWriter l_pw = new PrintWriter(p_map_filename);
			l_pw.println(l_empty_map);
			l_pw.close();
		}
		loadMap(p_map_filename);
	}

	/**
	 * Loads a Warzone map from an existing “domination” map file
	 * 
	 * @param p_map_filename the filename of the "domination" map file
	 * @throws Exception
	 */
	public void loadMap(String p_map_filename) throws Exception {
		List<String> list = Files.readAllLines(new File(p_map_filename).toPath(), Charset.defaultCharset());
		// todo: parse the map file... (another method?)
	}

}
