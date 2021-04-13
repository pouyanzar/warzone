package soen6441.team01.warzone.model;

import java.util.ArrayList;
import java.util.List;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.entities.ConquestContinent;
import soen6441.team01.warzone.model.entities.ConquestMap;
import soen6441.team01.warzone.model.entities.ConquestTerritory;
import soen6441.team01.warzone.model.entities.DominationBorder;
import soen6441.team01.warzone.model.entities.DominationContinent;
import soen6441.team01.warzone.model.entities.DominationCountry;
import soen6441.team01.warzone.model.entities.DominationMap;

/**
 * Uses an adaptor pattern to allow conquest map files to be processed as
 * domination style map files.
 *
 */
public class MapIoAdaptor extends MapIoDomination {
	private MapIoConquest d_map_conquest;

	/**
	 * constructor
	 * 
	 * @param p_map_conquest the conquest map object
	 */
	public MapIoAdaptor(MapIoConquest p_map_conquest) {
		d_map_conquest = p_map_conquest;
	}

	/**
	 * parses the input conquest style map and translates it into a domination map.
	 * 
	 * @param p_conquest_map an array holding a conquest style map
	 * @return the domination style data parsed from the input style map
	 * @throws Exception unexpected error
	 */
	public DominationMap parseMap(List<String> p_conquest_map) throws Exception {
		ConquestMap l_cmap = d_map_conquest.parseMap(p_conquest_map);
		DominationMap l_dmap = translateToDomination(l_cmap);
		return l_dmap;
	}

	/**
	 * adapter method into DominationMap
	 * 
	 * @param p_map the warzome map
	 */
	public ArrayList<String> getMapAsTextArray(IMapModel p_map) {
		return d_map_conquest.getMapAsConquestMapFormat(p_map);
	}

	/**
	 * translate the conquest map into a domination map
	 * 
	 * @param p_cmap the conquest map to translate
	 * @return the translated domination map
	 * @throws Exception unexpected error
	 */
	private DominationMap translateToDomination(ConquestMap p_cmap) throws Exception {
		DominationMap l_dmap = new DominationMap();

		// build the domination continents
		int l_ctr = 1;
		DominationContinent l_dcont;
		for (ConquestContinent l_ccont : p_cmap.d_continents) {
			l_dcont = new DominationContinent();
			l_dcont.d_id = Integer.toString(l_ctr++);
			l_dcont.d_name = fixDominationName(l_ccont.d_name);
			l_dcont.d_extra_armies = l_ccont.d_extra_armies;
			l_dcont.d_color = "";
			l_dmap.d_continents.add(l_dcont);
		}

		// build the domination countries
		l_ctr = 1;
		DominationCountry l_dcountry;
		for (ConquestTerritory l_cterri : p_cmap.d_territories) {
			l_dcountry = new DominationCountry();
			l_dcountry.d_id = Integer.toString(l_ctr++);
			l_dcountry.d_name = fixDominationName(l_cterri.d_name);
			l_dcountry.d_x = l_cterri.d_x;
			l_dcountry.d_y = l_cterri.d_y;
			l_dmap.d_countries.add(l_dcountry);
			// process the continent id from the continent name
			l_dcont = findContinent(fixDominationName(l_cterri.d_continent_name), l_dmap.d_continents);
			if (l_dcont == null) {
				throw new Exception("Continent name '" + l_cterri.d_continent_name + "' not defined.");
			}
			l_dcountry.d_continent_id = l_dcont.d_id;
		}

		// build the domination borders
		DominationBorder l_dborder;
		for (ConquestTerritory l_cterri : p_cmap.d_territories) {
			l_dborder = new DominationBorder();
			l_dcountry = findCountry(fixDominationName(l_cterri.d_name), l_dmap.d_countries);
			if (l_dcountry == null) {
				throw new Exception("Country name '" + l_cterri.d_name + "' not defined.");
			}
			l_dborder.d_country_id = l_dcountry.d_id;

			for (String l_neighbor : l_cterri.d_neighbors) {
				l_dcountry = findCountry(fixDominationName(l_neighbor), l_dmap.d_countries);
				if (l_dcountry == null) {
					throw new Exception("Neighbor name '" + l_neighbor + "' not defined.");
				}
				l_dborder.d_border_country_id.add(l_dcountry.d_id);
			}
			l_dmap.d_borders.add(l_dborder);
		}

		return l_dmap;
	}

	/**
	 * domination map names have different rules than conquest map names (e.g. no
	 * spaces). alter conquest map names to valid domination names.
	 * 
	 * @param d_name the conquest name to fix
	 * @return the fixed name
	 */
	private String fixDominationName(String d_name) {
		String l_result = d_name.replace(' ', '_');
		return l_result;
	}

	/**
	 * find the specified continent from a list of domination continents
	 * 
	 * @param p_continent_name the continent name to locate
	 * @param p_continents     the list of domination continents to search
	 * @return the desired continent, or null if not found
	 */
	private DominationContinent findContinent(String p_continent_name, ArrayList<DominationContinent> p_continents) {
		for (DominationContinent l_continent : p_continents) {
			if (l_continent.d_name.equals(p_continent_name)) {
				return l_continent;
			}
		}
		return null;
	}

	/**
	 * find the specified country from a domination map
	 * 
	 * @param p_name the country name to locate
	 * @param p_cmap the list of domination countries to search
	 * @return the desired country, or null if not found
	 */
	private DominationCountry findCountry(String p_name, ArrayList<DominationCountry> p_countries) {
		for (DominationCountry l_country : p_countries) {
			if (l_country.d_name.equals(p_name)) {
				return l_country;
			}
		}
		return null;
	}

}
