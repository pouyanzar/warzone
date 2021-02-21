package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IMapEditorController;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.view.SoftwareFactoryView;
import soen6441.team01.warzone.view.contracts.IMapEditorView;

/**
 * Warzone Map Editor controller. Manages the coordination and progression of
 * the Map Editor.
 */
public class MapEditorController implements IMapEditorController {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private IMapEditorView d_view;
	private IUserMessageModel d_msg_model;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_model_factory predefined SoftwareFactoryModel.
	 * @param p_view_factory  predefined SoftwareFactoryView.
	 * @throws Exception unexpected error
	 */
	public MapEditorController(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory)
			throws Exception {
		d_model_factory = p_model_factory;
		d_view_factory = p_view_factory;
		d_view = d_view_factory.getMapEditorConsoleView(this);
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * Starts executing the game dynamics
	 * 
	 * @return String = last unprocessed command. Valid commands include: exit,
	 *         loadmap
	 */
	public String startMapEditor() {
		String l_cmd = "exit";

		try {
			l_cmd = processMapEditor();
			if (l_cmd == null || Utl.IsEmpty(l_cmd)) {
				throw new Exception("Internal error 1 processing the map editor.");
			}
			String l_cmd_params[] = new String[2];
			l_cmd_params = Utl.GetFirstWord(l_cmd);
			switch (l_cmd_params[0]) {
			case "exit":
				break;
			case "loadmap":
				break;
			default:
				throw new Exception("Internal error 2 processing the map editor.");
			}
		} catch (Exception ex) {
			System.out.println("Fatal error processing Map Editor.");
			System.out.println("Exception: " + ex.getMessage());
		}

		d_msg_model.setMessage(MessageType.Informational, "exiting map editor");

		if (d_view != null) {
			d_view.shutdown();
		}

		return l_cmd;
	}

	/**
	 * Manages the map editor's interactions with the view, and processes any
	 * commands coming from the view.
	 * 
	 * @param p_view the MapEditorView to interact with
	 * @return String the last command entered
	 * @throws Exception general exception processing the map editor
	 */
	private String processMapEditor() throws Exception {
		boolean l_exit_map_editor = false;
		String l_cmd = "exit";

		d_view.displayWarzoneBanner();
		d_view.displayMapEditorBanner();

		while (!l_exit_map_editor) {
			l_cmd = d_view.getCommand();
			l_exit_map_editor = !processMapEditorCommand(l_cmd);
		}
		return l_cmd;
	}

	/**
	 * Process the map editor command:
	 * <ul>
	 * <li>editcontinent -add continentID continentvalue -remove continentID</li>
	 * <li>editcountry -add countryID continentID -remove countryID</li>
	 * <li>editneighbor -add countryID neighborcountryID -remove countryID
	 * neighborcountryID</li>
	 * <li>showmap</li>
	 * <li>savemap filename</li>
	 * <li>editmap filename</li>
	 * <li>loadmap filename</li>
	 * <li>validatemap</li>
	 * <li>exit</li>
	 * <li>help</li>
	 * </ul>
	 * 
	 * @param l_command the command to process
	 * @return true = command processed successfully, false = command to exit
	 * @throws Exception unexpected error
	 */
	public boolean processMapEditorCommand(String l_command) throws Exception {
		String l_cmd_params[] = Utl.GetFirstWord(l_command);
		switch (l_cmd_params[0]) {
		case "help":
			mapEditorHelp();
			break;
		case "exit":
			return false;
		case "editcontinent":
			processEditContinent(l_cmd_params[1]);
			break;
		case "editcountry":
			processEditCountry(l_cmd_params[1]);
			break;
		case "editneighbor":
			d_msg_model.setMessage(MessageType.None, "editneighbor coming soon...");
			break;
		case "showmap":
			d_msg_model.setMessage(MessageType.None, "showmap coming soon...");
			break;
		case "savemap":
			d_msg_model.setMessage(MessageType.None, "savemap coming soon...");
			break;
		case "editmap":
			d_msg_model.setMessage(MessageType.None, "editmap coming soon...");
			break;
		case "validatemap":
			d_msg_model.setMessage(MessageType.None, "validatemap coming soon...");
			break;
		case "loadmap":
			d_msg_model.setMessage(MessageType.None, "loadmap coming soon...");
			return false;
		default:
			d_msg_model.setMessage(MessageType.Error, "invalid command '" + l_command + "'");
			break;
		}
		return true;
	}

	/**
	 * process the editcountry command
	 * 
	 * @param l_editcountry_params the editcountry parameters (just the parameters
	 *                             without the editcountry command itself)
	 * @throws Exception unexpected error encountered
	 */
	private void processEditCountry(String l_editcountry_params) throws Exception {
		IMapModel l_map = d_model_factory.getMapModel();
		String l_countryName;
		int l_continentId;
		String l_params[] = Utl.GetFirstWord(l_editcountry_params);

		if (Utl.IsEmpty(l_params[0])) {
			d_msg_model.setMessage(MessageType.Error, "Invalid editcountry, no options specified");
			return;
		}

		while (!Utl.IsEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.GetFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.IsValidMapName(l_countryName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editcountry -add countryId '"
								+ l_countryName + "', expecting a valid map name");
						return;
					}
					l_params = Utl.GetFirstWord(l_params[1]);
					l_continentId = Utl.ConvertToInteger(l_params[0]);
					if (l_continentId > 1000) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editcountry -add continentID '"
								+ l_params[0] + "', expecting a valid integer value less than 1000");
						return;
					}
					ICountryModel l_country = l_map.addCountry(l_countryName, l_continentId);
					if (l_country == null) {
						d_msg_model.setMessage(MessageType.Error, "Error adding country");
						return;
					}
				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.GetFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.IsValidMapName(l_countryName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editcountry -remove countryId '"
								+ l_countryName + "', expecting a valid map name");
						return;
					}
					ICountryModel l_country = l_map.removeCountry(l_countryName);
					if (l_country == null) {
						d_msg_model.setMessage(MessageType.Error, "Error removing country");
						return;
					}

				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg_model.setMessage(MessageType.Error,
						"Invalid editcountry option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.GetFirstWord(l_params[1]);
			d_msg_model.setMessage(MessageType.None, "editcountry processed successfully");
		}
	}

	/**
	 * process editcontinent command
	 * 
	 * @param l_editcontinent_params the editcontinent parameters (just the
	 *                               parameters without the editcontinent command
	 *                               itself)
	 * @throws Exception unexpected error encountered
	 */
	private void processEditContinent(String l_editcontinent_params) throws Exception {
		IMapModel l_map = d_model_factory.getMapModel();
		int l_continentId;
		String l_continentvalue;
		String l_params[] = Utl.GetFirstWord(l_editcontinent_params);

		if (Utl.IsEmpty(l_params[0])) {
			d_msg_model.setMessage(MessageType.Error, "Invalid editcontinent, no options specified");
			return;
		}

		while (!Utl.IsEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.GetFirstWord(l_params[1]);
					l_continentId = Utl.ConvertToInteger(l_params[0]);
					if (l_continentId > 1000) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editcontinent -add continentID '"
								+ l_params[0] + "', expecting a valid integer value less than 1000");
						return;
					}
					l_params = Utl.GetFirstWord(l_params[1]);
					l_continentvalue = l_params[0];
					if (!Utl.IsValidMapName(l_continentvalue)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editcontinent -add continentvalue '"
								+ l_params[0] + "', expecting a valid map name");
						return;
					}
					IContinentModel l_continent = l_map.addContinent(l_continentId, l_continentvalue, 0);
					if (l_continent == null) {
						d_msg_model.setMessage(MessageType.Error, "Error adding continent");
						return;
					}
				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.GetFirstWord(l_params[1]);
					l_continentId = Utl.ConvertToInteger(l_params[0]);
					if (l_continentId > 1000) {
						d_msg_model.setMessage(MessageType.Error, "Invalid editcontinent -remove continentID '"
								+ l_params[0] + "', expecting a valid integer value less than 1000");
						return;
					}
					IContinentModel l_continent = l_map.removeContinent(l_continentId);
					if (l_continent == null) {
						d_msg_model.setMessage(MessageType.Error, "Error removing continent");
						return;
					}

				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg_model.setMessage(MessageType.Error,
						"Invalid editcontinent option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.GetFirstWord(l_params[1]);
			d_msg_model.setMessage(MessageType.None, "editcontinent processed successfully");
		}
	}

	/**
	 * Asks the view to display the list of map editor commands along with their
	 * syntax.
	 * 
	 * @param p_view the map editor view being used by the controller
	 */
	private void mapEditorHelp() {
		d_view.displayMapEditorBanner();
		d_view.processMessage(MessageType.None, "List of commands:");
		d_view.processMessage(MessageType.None, " - editcontinent -add continentID continentvalue -remove continentID");
		d_view.processMessage(MessageType.None, " - editcountry -add countryID continentID -remove countryID");
		d_view.processMessage(MessageType.None,
				" - editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID");
		d_view.processMessage(MessageType.None,
				" - showmap (show all continents and countries and their respective neighbors)");
		d_view.processMessage(MessageType.None, " - savemap filename");
		d_view.processMessage(MessageType.None, " - editmap filename");
		d_view.processMessage(MessageType.None, " - loadmap filename");
		d_view.processMessage(MessageType.None, " - validatemap");
		d_view.processMessage(MessageType.None, " - exit");
		d_view.processMessage(MessageType.None, " - help");
	}
}