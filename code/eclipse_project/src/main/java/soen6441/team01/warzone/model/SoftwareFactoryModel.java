package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * models.
 */
public class SoftwareFactoryModel {
	private IMapModel d_map_model = null;

	/**
	 * Constructor with no models defined. Software factory will return default
	 * models
	 */
	public SoftwareFactoryModel() {
	}

	/**
	 * Constructor with models defined. Models passed as null will result in the
	 * default model being passed back.
	 * 
	 * @param p_map_model the map model to use
	 */
	public SoftwareFactoryModel(IMapModel p_map_model) {
		d_map_model = p_map_model;
	}

	/**
	 * 
	 * @return an IMapModel object
	 */
	public IMapModel getMapModel() {
		if (d_map_model == null)
			d_map_model = new Map();
		return d_map_model;
	}

	/**
	 * 
	 * @param p_player_name the name of the player
	 * @return an instance of the IPlayerModel
	 * @throws Exception as defined by class Player
	 */
	public IPlayerModel getPlayerModel(String p_player_name) throws Exception {
		IPlayerModel l_player = new Player(p_player_name);
		return l_player;

	}
}
