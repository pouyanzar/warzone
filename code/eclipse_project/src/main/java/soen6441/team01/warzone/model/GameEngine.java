package soen6441.team01.warzone.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.controller.ControllerFactory;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.view.*;

/**
 * Warzone MVC main controller. Manages the coordination and progression of the
 * different stages of the game. As well as supporting user actions (ie.
 * interactions, gestures).
 */
public class GameEngine implements IGameEngineModel, Serializable {
	private static final long serialVersionUID = 1L;
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
	private Phase d_phase = null;
	private GameEngine d_switch_to_loaded_engine = null;

	/**
	 * Constructor with no views or models defined. Use Software factory with
	 * defaults
	 * 
	 * @throws Exception unexpected error
	 */
	public GameEngine() throws Exception {
		this(null, null, null);
	}

	/**
	 * Constructor with view and models defined. Parameters passed as null will
	 * result in the default view or model being used.
	 * 
	 * @param p_model_factory      predefined SoftwareFactoryModel. Pass null to
	 *                             have the controller generate a model factory
	 *                             based on the default models.
	 * @param p_view_factory       predefined SoftwareFactoryView. Pass null to have
	 *                             the controller generate a view factory based on
	 *                             the default views (i.e. console based).
	 * @param p_controller_factory predefined SoftwareFactoryController. Pass null
	 *                             to have the controller generate a controller
	 *                             factory based on the default controllers (i.e.
	 *                             console based).
	 * @throws Exception unexpected error
	 */
	public GameEngine(ModelFactory p_model_factory, ViewFactory p_view_factory, ControllerFactory p_controller_factory)
			throws Exception {
		if (p_model_factory != null) {
			d_model_factory = p_model_factory;
		} else {
			d_model_factory = ModelFactory.createWarzoneBasicConsoleGameModels();
		}
		d_model_factory.setGameEngine(this);

		if (p_model_factory != null) {
			d_view_factory = p_view_factory;
		} else {
			d_view_factory = ViewFactory.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		}

		if (p_controller_factory != null) {
			d_controller_factory = p_controller_factory;
		} else {
			d_controller_factory = new ControllerFactory(d_model_factory, d_view_factory);
		}

		d_phase = (Phase) d_controller_factory.getMapEditorPhase();
	}

	/**
	 * Starts executing the game dynamics
	 * 
	 * @return null = terminate game; otherwise a loaded game to be played next
	 */
	public GameEngine startGame() {
		try {
			while (d_phase != null) {
				if (d_switch_to_loaded_engine != null) {
					return d_switch_to_loaded_engine;
				}
				d_phase.execPhase();
			}
		} catch (Exception ex) {
			Utl.lprintln("Fatal error processing GameEngine.");
			Utl.lprintln("Exception: " + ex.getMessage());
		}

		Utl.lprintln("Game execution terminated.\n");
		Utl.closeLog();

		return null;
	}

	/**
	 * set the current phase of the game
	 * 
	 * @param p_next_phase the phase that the game will switch to
	 */
	public void setNextPhase(Phase p_next_phase) {
		d_phase = p_next_phase;
	}

	/**
	 * used mainly for unit testing
	 * 
	 * @return the current / next phase to be invoked
	 */
	public Phase getPhase() {
		return d_phase;
	}

	/**
	 * 
	 * @return the current model factory
	 */
	public ModelFactory getModelFactory() {
		return d_model_factory;
	}

	/**
	 * @return the current view factory
	 */
	public ViewFactory getViewFactory() {
		return d_view_factory;
	}

	/**
	 * 
	 * @return the current controller factory
	 */
	public ControllerFactory getControllerFactory() {
		return d_controller_factory;
	}

	/**
	 * set the game to run the new game engine
	 * 
	 * @param p_game_engine the game engine to switch to
	 */

	public void setNewGameEngineToRun(GameEngine p_game_engine) {
		d_switch_to_loaded_engine = p_game_engine;
	}

	/**
	 * saves the current game to a file
	 * 
	 * @param p_filename the filename of the saved game file
	 * @throws Exception unexpected error
	 */
	public void saveGame(String p_filename) throws Exception {
		FileOutputStream l_file_output_stream = new FileOutputStream(p_filename);
		ObjectOutputStream l_object_output_stream = new ObjectOutputStream(l_file_output_stream);
		l_object_output_stream.writeObject(this);
		l_object_output_stream.flush();
		l_object_output_stream.close();
	}

	/**
	 * loads the game from a file.<br>
	 * 
	 * @param p_filename the filename of the saved game file to load
	 * @return the saved game engine
	 * @throws Exception unexpected error
	 */
	public GameEngine loadGame(String p_filename) throws Exception {
		FileInputStream l_file_input_stream = new FileInputStream(p_filename);
		ObjectInputStream l_object_input_stream = new ObjectInputStream(l_file_input_stream);
		GameEngine l_game_engine = (GameEngine) l_object_input_stream.readObject();
		l_object_input_stream.close();
		return l_game_engine;
	}

}
