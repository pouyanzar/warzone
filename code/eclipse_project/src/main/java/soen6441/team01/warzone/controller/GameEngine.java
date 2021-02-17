package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.MessageType;
import soen6441.team01.warzone.common.Utl;
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
		d_model_factory = new SoftwareFactoryModel();
		d_view_factory = new SoftwareFactoryView();
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
			d_model_factory = new SoftwareFactoryModel();
		}
		if (p_model_factory != null) {
			d_view_factory = p_view_factory;
		} else {
			d_view_factory = new SoftwareFactoryView();
		}
	}

	/**
	 * Starts executing the game dynamics
	 */
	public void startGame() {
		boolean l_end_game = false;
		IMapEditorView l_map_editor_view = d_view_factory.getMapEditorView(this);
		try {
			String[] l_cmd_params = processMapEditor(l_map_editor_view);
			if (l_cmd_params == null || Utl.isEmpty(l_cmd_params[0])) {
				throw new Exception("Something went wrong with the map editor - aborting game.");
			}
			switch (l_cmd_params[0]) {
			case "exit":
				l_map_editor_view.processMessage(MessageType.None, "Warzone exiting.");
				l_end_game = true;
				break;
			}
		} catch (Exception ex) {
			l_map_editor_view.processMessage(MessageType.Error, ex.getMessage());
			l_map_editor_view.processMessage(MessageType.None, "Warzone exiting.");
			l_end_game = true;
		}
		l_map_editor_view.shutdown();

		if (!l_end_game) {
			// process Startup phase of game
			System.out.println("startup game phase...");
			try {
			} catch (Exception ex) {
				l_end_game = true;
			}
			System.out.println("end of game.");
		}
	}

	/**
	 * Manages the map editor's interactions with the view, and processes any
	 * commands coming from the view.
	 * 
	 * @param p_view the MapEditorView to interact with
	 * @return String[0] = next phase command, String[1] = next phase command
	 *         parameters
	 */
	private String[] processMapEditor(IMapEditorView p_view) {
		boolean l_exit_map_editor = false;
		p_view.displayWarzoneBanner();
		p_view.displayMapEditorBanner();
		String l_cmd;
		String l_cmd_params[] = new String[2];

		while (!l_exit_map_editor) {
			l_cmd = p_view.getCommand();
			l_cmd_params = getCommandAndParams(l_cmd);
			switch (l_cmd_params[0]) {
			case "help":
				mapEditorHelp(p_view);
				break;
			case "exit":
				l_exit_map_editor = true;
				break;
			case "editcontinent":
				p_view.processMessage(MessageType.None, "editcontinent coming soon...");
				break;
			case "editcountry":
				p_view.processMessage(MessageType.None, "editcountry coming soon...");
				break;
			case "editneighbor":
				p_view.processMessage(MessageType.None, "editneighbor coming soon...");
				break;
			case "showmap":
				p_view.processMessage(MessageType.None, "showmap coming soon...");
				break;
			case "savemap":
				p_view.processMessage(MessageType.None, "savemap coming soon...");
				break;
			case "editmap":
				p_view.processMessage(MessageType.None, "editmap coming soon...");
				break;
			case "validatemap":
				p_view.processMessage(MessageType.None, "validatemap coming soon...");
				break;
			case "loadmap":
				p_view.processMessage(MessageType.None, "loadmap coming soon...");
				l_exit_map_editor = true;
				break;
			default:
				p_view.processMessage(MessageType.Error, "invalid command '" + l_cmd + "'");
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

	/**
	 * Parse and extract the command and the command parameter(s). The syntax is
	 * 'first-word-is-command' 'rest-of-string-as-parameters'.
	 * 
	 * @param p_command the string containing the command (as the 1st word),
	 *                  followed by the command parameters.
	 * @return String[0] = command, String[1] = command parameters
	 */
	public String[] getCommandAndParams(String p_command) {
		String[] l_reply = new String[2];
		if (p_command == null) {
			l_reply[0] = "";
			l_reply[1] = "";
			return l_reply;
		}

		p_command = p_command.trim();
		int l_idx = p_command.indexOf(' ');
		if (l_idx < 1 && p_command.length() > 1) {
			l_reply[0] = p_command;
			l_reply[1] = "";
			return l_reply;
		}

		try {
			l_reply[0] = p_command.substring(0, l_idx);
		} catch (Exception ex) {
			l_reply[0] = "";
			l_reply[1] = "";
			return l_reply;
		}

		try {
			l_idx++;
			l_reply[1] = p_command.substring(l_idx, p_command.length()).trim();
		} catch (Exception ex) {
			l_reply[1] = "";
			return l_reply;
		}
		return l_reply;
	}
}
