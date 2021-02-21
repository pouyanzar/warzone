package soen6441.team01.warzone.model;

import soen6441.team01.warzone.model.contracts.*;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * models.
 */
public class SoftwareFactoryModel {
	private IMapModel d_map_model = null;
	private IUserMessageModel d_user_message_model = null;

	/**
	 * Constructor with models defined. Models passed as null will result in the
	 * default model being passed back.
	 * 
	 * @param p_map_model          the map model to use
	 * @param p_user_message_model the user message model to use
	 */
	public SoftwareFactoryModel(IMapModel p_map_model, IUserMessageModel p_user_message_model) {
		d_map_model = p_map_model;
		d_user_message_model = p_user_message_model;
	}

	/**
	 * Helper function that creates a software factory that holds the Warzone model
	 * objects.
	 * 
	 * @return newly create Warzone model software factory
	 */
	public static SoftwareFactoryModel CreateWarzoneBasicConsoleGameModels() {
		IMapModel l_map = new Map();
		IUserMessageModel l_usermsg = new UserMessageModel();
		SoftwareFactoryModel l_model = new SoftwareFactoryModel(l_map, l_usermsg);
		return l_model;
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
	 * Create a new player model based on the specified name
	 * 
	 * @param p_player_name the name of the player
	 * @return an instance of the IPlayerModel
	 * @throws Exception as defined by class Player
	 */
	public IPlayerModel getPlayerModel(String p_player_name) throws Exception {
		IPlayerModel l_player = new Player(p_player_name);
		return l_player;

	}

	/**
	 * Get the current user message model, or create one if it hasn't already been
	 * defined.
	 * 
	 * @return the user message model created or previously defined
	 * @throws Exception if there was a problem creating a new user message object
	 */
	public IUserMessageModel getUserMessageModel() throws Exception {
		if (d_user_message_model == null)
			d_user_message_model = new UserMessageModel();
		return d_user_message_model;
	}
}
