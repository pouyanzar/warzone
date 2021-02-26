package soen6441.team01.warzone.model;

import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.model.contracts.*;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * models.
 */
public class SoftwareFactoryModel {
	private IMapModel d_map_model = null;
	private IUserMessageModel d_user_message_model = null;
	private IGamePlayModel d_gameplay = null;

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
	public static SoftwareFactoryModel createWarzoneBasicConsoleGameModels() {
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
	 * set the current map model to the supplied map
	 * 
	 * @param p_map the new map model to use
	 */
	public void setMapModel(IMapModel p_map) {
		d_map_model = p_map;
	}

	/**
	 * Create a new player model based on the specified name
	 * 
	 * @param p_player_name the name of the player
	 * @param p_order_datasource used to get the player commands during
	 *                           issue_order()
	 * @return an instance of the IPlayerModel
	 * @throws Exception as defined by class Player
	 */
	public IPlayerModel newHumanPlayerModel(String p_player_name, IGameplayOrderDatasource p_order_datasource) throws Exception {
		IPlayerModel l_player = new Player(p_player_name, p_order_datasource);
		return l_player;

	}

	/**
	 * Get the existing game play model
	 * 
	 * @return an instance of the IGamePlayModel
	 * @throws Exception unexpected error
	 */
	public IGamePlayModel getGamePlayModel() throws Exception {
		if (d_gameplay == null) {
			throw new Exception("Internal error, GamePlay model was not previously created.");
		}
		return d_gameplay;
	}

	/**
	 * Create a new game play model
	 * 
	 * @return an instance of the IGamePlayModel
	 * @throws Exception unexpected error
	 */
	public IGamePlayModel getNewGamePlayModel() throws Exception {
		d_gameplay = new GamePlay(this);
		return d_gameplay;
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
