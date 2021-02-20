package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.model.*;
import soen6441.team01.warzone.model.contracts.*;
import soen6441.team01.warzone.view.*;

/**
 * Warzone MVC main controller. Manages the coordination and progression of the
 * different stages of the game. As well as supporting user actions (ie.
 * interactions, gestures).
 */
public class GameEngine implements IInteractionDrivenController {
	SoftwareFactoryModel d_model_factory;
	SoftwareFactoryView d_view_factory;

	/**
	 * Constructor with no views or models defined. Use Software factory with
	 * defaults
	 */
	public GameEngine() {
		this(null, null);
	}

	/**
	 * Constructor with view and models defined. Parameters passed as null will
	 * result in the default view or model being used.
	 * 
	 * @param p_model_factory predefined SoftwareFactoryModel. Pass null to have the
	 *                        controller generate a model factory based on the
	 *                        default models.
	 * @param p_view_factory  predefined SoftwareFactoryView. Pass null to have the
	 *                        controller generate a view factory based on the
	 *                        default views (i.e. console based).
	 */
	public GameEngine(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory) {
		if (p_model_factory != null) {
			d_model_factory = p_model_factory;
		} else {
			d_model_factory = SoftwareFactoryModel.CreateWarzoneBasicConsoleGameModels();
		}
		if (p_model_factory != null) {
			d_view_factory = p_view_factory;
		} else {
			d_view_factory = SoftwareFactoryView.CreateWarzoneBasicConsoleGameViews(d_model_factory);
		}
	}

	/**
	 * Starts executing the game dynamics
	 */
	public void startNewGame() {
		boolean l_end_game = false;
		IMapEditorView l_map_editor_view = null;
		try {
			l_map_editor_view = d_view_factory.getMapEditorConsoleView(this);
			String[] l_cmd_params = processMapEditor(l_map_editor_view);
			if (l_cmd_params == null || Utl.IsEmpty(l_cmd_params[0])) {
				throw new Exception("Something went wrong with the map editor - aborting game.");
			}
			switch (l_cmd_params[0]) {
			case "exit":
				l_map_editor_view.processMessage(MessageType.None, "Warzone exiting.");
				l_end_game = true;
				break;
			}
		} catch (Exception ex) {
			System.out.println("Fatal error processing Map Editor.");
			System.out.println("Exception: " + ex.getMessage());
			System.out.println("Terminating game.");
			l_end_game = true;
		}

		if (l_map_editor_view != null) {
			l_map_editor_view.shutdown();
		}

		if (!l_end_game) {
			// process Startup phase of game
			System.out.println("startup game phase...");
			try {
			} catch (Exception ex) {
				l_end_game = true;
			}
		}

		System.out.println("end of game.");
	}

	/**
	 * Manages the map editor's interactions with the view, and processes any
	 * commands coming from the view.
	 * 
	 * @param p_view the MapEditorView to interact with
	 * @return String[0] = next phase command, String[1] = next phase command
	 *         parameters
	 * @throws Exception general exception processing the map editor
	 */
	private String[] processMapEditor(IMapEditorView p_view) throws Exception {
		IUserMessageModel msg_model = d_model_factory.getUserMessageModel();
		boolean l_exit_map_editor = false;
		p_view.displayWarzoneBanner();
		p_view.displayMapEditorBanner();
		String l_cmd;
		String l_cmd_params[] = new String[2];

		while (!l_exit_map_editor) {
			l_cmd = p_view.getCommand();
			l_cmd_params = Utl.GetFirstWord(l_cmd);
			switch (l_cmd_params[0]) {
			case "help":
				mapEditorHelp(p_view);
				break;
			case "exit":
				l_exit_map_editor = true;
				break;
			case "editcontinent":
				msg_model.setMessage(MessageType.None, "editcontinent coming soon...");
				break;
			case "editcountry":
				msg_model.setMessage(MessageType.None, "editcountry coming soon...");
				break;
			case "editneighbor":
				msg_model.setMessage(MessageType.None, "editneighbor coming soon...");
				break;
			case "showmap":
				msg_model.setMessage(MessageType.None, "showmap coming soon...");
				break;
			case "savemap":
				msg_model.setMessage(MessageType.None, "savemap coming soon...");
				break;
			case "editmap":
				msg_model.setMessage(MessageType.None, "editmap coming soon...");
				break;
			case "validatemap":
				msg_model.setMessage(MessageType.None, "validatemap coming soon...");
				break;
			case "loadmap":
				msg_model.setMessage(MessageType.None, "loadmap coming soon...");
				l_exit_map_editor = true;
				break;
			default:
				//p_view.processMessage(MessageType.Error, "invalid command '" + l_cmd + "'");
				msg_model.setMessage(MessageType.Error, "invalid command '" + l_cmd + "'");
				break;
			}
		}
		p_view.processMessage(MessageType.Informational, "exiting map editor");
		return l_cmd_params;
	}

	/**
	 * Asks the view to display the list of map editor commands along with their
	 * syntax.
	 * 
	 * @param p_view the map editor view being used by the controller
	 */
	private void mapEditorHelp(IMapEditorView p_view) {
		p_view.displayMapEditorBanner();
		p_view.processMessage(MessageType.None, "List of commands:");
		p_view.processMessage(MessageType.None, " - editcontinent -add continentID continentvalue -remove continentID");
		p_view.processMessage(MessageType.None, " - editcountry -add countryID continentID -remove countryID");
		p_view.processMessage(MessageType.None,
				" - editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID");
		p_view.processMessage(MessageType.None,
				" - showmap (show all continents and countries and their respective neighbors)");
		p_view.processMessage(MessageType.None, " - savemap filename");
		p_view.processMessage(MessageType.None, " - editmap filename");
		p_view.processMessage(MessageType.None, " - loadmap filename");
		p_view.processMessage(MessageType.None, " - validatemap");
		p_view.processMessage(MessageType.None, " - exit");
		p_view.processMessage(MessageType.None, " - help");
	}
}
