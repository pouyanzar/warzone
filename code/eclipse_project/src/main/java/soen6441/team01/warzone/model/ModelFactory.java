package soen6441.team01.warzone.model;

import java.util.List;

import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.model.entities.SaveMapFormat;

/**
 * This class implements the Software Factory design pattern to manage the set
 * of objects that define the respective classes for the different Warzone
 * models.
 */
public class ModelFactory {
	private IMapModel d_map_model = null;
	private IAppMsg d_user_message_model = null;
	private IGamePlayModel d_gameplay = null;
	private GameEngine d_game_engine = null;

	/**
	 * Constructor with models defined. Models passed as null will result in the
	 * default model being passed back.
	 * 
	 * @param p_user_message_model the user message model to use
	 */
	public ModelFactory(IAppMsg p_user_message_model) {
		d_map_model = new Map(this);
		d_user_message_model = p_user_message_model;
	}

	/**
	 * Constructor (copy)
	 * 
	 * @param p_map_model the map model to use
	 * @throws Exception unexpected error
	 */
	public ModelFactory(ModelFactory p_map_model) throws Exception {
		d_map_model = p_map_model.getMapModel();
		d_user_message_model = p_map_model.getUserMessageModel();
		d_gameplay = p_map_model.getGamePlayModel();
	}

	/**
	 * Helper function that creates a software factory that holds the Warzone model
	 * objects.
	 * 
	 * @return newly create Warzone model software factory
	 */
	public static ModelFactory createWarzoneBasicConsoleGameModels() {
		IAppMsg l_usermsg = new LogEntryBuffer();
		ModelFactory l_model = new ModelFactory(l_usermsg);
		return l_model;
	}

	/**
	 * 
	 * @return an IMapModel object
	 */
	public IGameEngineModel getGameEngine() {
		return d_game_engine;
	}

	/**
	 * set the current map model to the supplied map
	 * 
	 * @param p_game_engine the game engine object
	 */
	public void setGameEngine(GameEngine p_game_engine) {
		d_game_engine = p_game_engine;
	}

	/**
	 * 
	 * @return an IMapModel object
	 */
	public IMapModel getMapModel() {
		if (d_map_model == null)
			d_map_model = new Map(this);
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
	public IAppMsg getUserMessageModel() throws Exception {
		if (d_user_message_model == null)
			d_user_message_model = new LogEntryBuffer();
		return d_user_message_model;
	}

	/**
	 * Detect from p_records what type of map definition is being specified and
	 * create the appropriate map io object.
	 * 
	 * @param p_records the records of either a domination or conquest type map
	 * @return an instance of the MapIoDomination
	 * @throws Exception unexpected error
	 */
	public MapIoDomination getMapIo(List<String> p_records) throws Exception {
		if (MapIoDomination.isDominationFileFormat(p_records)) {
			return new MapIoDomination();
		}
		if (MapIoConquest.isConquestFileFormat(p_records)) {
			return new MapIoAdaptor(new MapIoConquest());
		}
		return new MapIoDomination(); // assume as default
	}

	/**
	 * create an appropriate map io object.
	 * 
	 * @param p_format the type of map format required
	 * @return an instance of the MapIoDomination
	 * @throws Exception unexpected error
	 */
	public MapIoDomination getMapIo(SaveMapFormat p_format) throws Exception {
		switch (p_format) {
		case Domination:
			return new MapIoDomination();
		case Conquest:
			return new MapIoAdaptor(new MapIoConquest());
		}
		return new MapIoDomination();
	}
}
