package soen6441.team01.warzone.controller;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IMapEditorController;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.Phase;
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
public class MapEditorController extends Phase implements IMapEditorController {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private SoftwareFactoryController d_controller_factory;
	private IMapEditorView d_view;
	private IUserMessageModel d_msg_model;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public MapEditorController(SoftwareFactoryController p_controller_factory) throws Exception {
		super(p_controller_factory.getModelFactory().getGameEngine());
		d_controller_factory = p_controller_factory;
		d_model_factory = p_controller_factory.getModelFactory();
		d_view_factory = p_controller_factory.getViewFactory();
		d_view = d_view_factory.getMapEditorConsoleView(this);
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * invoked by the game engine as part of the Map Editor phase of the game.
	 */
	@Override
	public void execPhase() {
		execMapEditor();
	}

	/**
	 * Start executing the map editor dynamics
	 */
	public void execMapEditor() {
		Phase l_next_phase = null;

		try {
			d_view.activate();
			d_view.displayWarzoneBanner();
			d_view.displayMapEditorBanner();

			String l_cmd;
			while (l_next_phase == null) {
				l_cmd = d_view.getCommand();
				l_next_phase = processMapEditorCommand(l_cmd);
			}
			nextPhase(l_next_phase);
		} catch (Exception ex) {
			Utl.consoleMessage("Fatal error processing Map Editor, exception: " + ex.getMessage());
		}

		if (d_view != null) {
			d_view.deactivate();
		}
	}

	/**
	 * Process the map editor command:
	 * <ul>
	 * <li>editcontinent -add continentID continentvalue -remove continentID</li>
	 * <li>editcountry -add countryID continentID -remove countryID (countryID =
	 * country name)</li>
	 * <li>editneighbor -add countryID neighborcountryID -remove countryID
	 * neighborcountryID (countryID = country name, neighborcountryID = country
	 * name)</li>
	 * <li>showmap</li>
	 * <li>savemap filename</li>
	 * <li>editmap filename</li>
	 * <li>loadmap filename (this command initiates game startup)</li>
	 * <li>validatemap</li>
	 * <li>exit</li>
	 * <li>help</li>
	 * </ul>
	 * 
	 * @param p_command the command to process
	 * @return the next phase in the game, null if this phase is not done yet
	 * @throws Exception unexpected error
	 */
	public Phase processMapEditorCommand(String p_command) throws Exception {
		Phase l_next_phase = null;

		String l_cmd_params[] = Utl.getFirstWord(p_command);
		switch (l_cmd_params[0]) {
		case "help":
			mapEditorHelp();
			break;
		case "exit":
			l_next_phase = d_controller_factory.getGameEndPhase();
			break;
		case "editcontinent":
			processEditContinent(l_cmd_params[1]);
			break;
		case "editcountry":
			processEditCountry(l_cmd_params[1]);
			break;
		case "editneighbor":
			processEditNeighbor(l_cmd_params[1]);
			break;
		case "showmap":
			d_view.showmap(d_model_factory.getMapModel());
			break;
		case "savemap":
			processSaveMap(l_cmd_params[1]);
			break;
		case "editmap":
			processEditMap(l_cmd_params[1]);
			break;
		case "validatemap":
			if (d_model_factory.getMapModel().validatemap()) {
				d_msg_model.setMessage(MessageType.None, "The current map is valid.");
			} else {
				d_msg_model.setMessage(MessageType.None, "The current map is invalid.");
			}
			break;
		case "loadmap":
			// if the map is not a valid map then stay in the editor, otherwise move on to
			// the game startup phase.
			if (processLoadMap(l_cmd_params[1])) {
				l_next_phase = d_controller_factory.getGameStartupPhase();
			}
			break;
		default:
			d_msg_model.setMessage(MessageType.Error, "invalid command '" + p_command + "'");
			break;
		}
		return l_next_phase;
	}

	/**
	 * process the editmap command
	 * <p>
	 * editmap filename
	 * </p>
	 * 
	 * @param p_editmap_params the editmap parameters (just the parameters without
	 *                         the editmap command itself)
	 * @return true if successful
	 * @throws Exception unexpected error encountered
	 */
	public boolean processEditMap(String p_editmap_params) throws Exception {
		try {
			String l_params[] = Utl.getFirstWord(p_editmap_params);
			String l_filename = l_params[0];
			if (Utl.isEmpty(l_filename)) {
				throw new Exception("Invalid editmap command, filename not specified");
			}
			IMapModel l_map_model = Map.editmap(l_filename, d_model_factory);
			if (l_map_model != null) {
				d_model_factory.setMapModel(l_map_model);
			}
		} catch (Exception ex) {
			d_msg_model.setMessage(MessageType.Error, ex.getMessage());
			return false;
		}
		d_msg_model.setMessage(MessageType.None, "editmap processed successfully");
		return true;
	}

	/**
	 * process the loadmap command. if the map is not a valid map then stay in the
	 * editor, otherwise move on to the game startup phase.
	 * 
	 * @param p_loadmap_params the loadmap parameters (just the parameters without
	 *                         the loadmap command itself)
	 * @return true if successful
	 * @throws Exception unexpected error encountered
	 */
	public boolean processLoadMap(String p_loadmap_params) throws Exception {
		try {
			String l_params[] = Utl.getFirstWord(p_loadmap_params);
			IMapModel l_map_model = Map.processLoadMapCommand(l_params[0], d_model_factory);
			d_model_factory.setMapModel(l_map_model);
		} catch (Exception ex) {
			d_msg_model.setMessage(MessageType.Error, ex.getMessage());
			return false;
		}
		if (d_model_factory.getMapModel().validatemap()) {
			d_msg_model.setMessage(MessageType.None, "loadmap processed successfully");
		} else {
			d_msg_model.setMessage(MessageType.Error, "loadmap error - map is not a valid map.");
			return false;
		}

		return true;
	}

	/**
	 * process the savemap command.
	 * <p>
	 * syntax: savemap filename
	 * </p>
	 * 
	 * @param p_editmap_params the savemap parameters (just the parameters without
	 *                         the savemap command itself)
	 * @return true if successful
	 * @throws Exception unexpected error encountered
	 */
	public boolean processSaveMap(String p_editmap_params) throws Exception {
		String l_filename = "";
		try {
			String l_params[] = Utl.getFirstWord(p_editmap_params);
			l_filename = l_params[0];
			d_model_factory.getMapModel().saveMap(l_filename);
		} catch (Exception ex) {
			d_msg_model.setMessage(MessageType.Error, ex.getMessage());
			return false;
		}
		d_msg_model.setMessage(MessageType.None, "map saved successfully to file '" + l_filename + "'");
		return true;
	}

	/**
	 * process the editneighbor command:<br>
	 * 
	 * <pre>
	 * editneighbor -add countryName neighborcountryName -remove countryName neighborcountryName
	 * </pre>
	 * 
	 * @param p_editneighbor_params the editneighbor parameters (just the parameters
	 *                              without the editneighbor command itself)
	 * @throws Exception unexpected error encountered
	 */
	private void processEditNeighbor(String p_editneighbor_params) throws Exception {
		IMapModel l_map = d_model_factory.getMapModel();
		String l_countryName;
		String l_neighbor_name;
		String l_params[] = Utl.getFirstWord(p_editneighbor_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MessageType.Error, "Invalid editneighbor, no options specified");
			return;
		}

		while (!Utl.isEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.isValidMapName(l_countryName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid country name '" + l_countryName + "'");
						return;
					}
					l_params = Utl.getFirstWord(l_params[1]);
					l_neighbor_name = l_params[0];
					if (!Utl.isValidMapName(l_neighbor_name)) {
						d_msg_model.setMessage(MessageType.Error,
								"Invalid country neighbor name '" + l_countryName + "'");
						return;
					}
					l_map.addNeighbor(l_countryName, l_neighbor_name);
				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.isValidMapName(l_countryName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid country name '" + l_countryName + "'");
						return;
					}
					l_params = Utl.getFirstWord(l_params[1]);
					l_neighbor_name = l_params[0];
					if (!Utl.isValidMapName(l_neighbor_name)) {
						d_msg_model.setMessage(MessageType.Error,
								"Invalid country neighbor name '" + l_countryName + "'");
						return;
					}
					l_map.removeNeighbor(l_countryName, l_neighbor_name);
				} catch (Exception ex) {
					d_msg_model.setMessage(MessageType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg_model.setMessage(MessageType.Error,
						"Invalid editneighbor option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.getFirstWord(l_params[1]);
			Map.refreshCountriesOfAllContinents(l_map);
			d_msg_model.setMessage(MessageType.None, "editneighbor processed successfully");
		}
	}

	/**
	 * process the editcountry command: <br>
	 * editcountry -add countryName continentID -remove countryName
	 * 
	 * @param p_editcountry_params the editcountry parameters (just the parameters
	 *                             without the editcountry command itself)
	 * @throws Exception unexpected error encountered
	 */
	private void processEditCountry(String p_editcountry_params) throws Exception {
		IMapModel l_map = d_model_factory.getMapModel();
		String l_countryName;
		int l_continentId;
		String l_params[] = Utl.getFirstWord(p_editcountry_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MessageType.Error, "Invalid editcountry, no options specified");
			return;
		}

		while (!Utl.isEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.isValidMapName(l_countryName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid country name '" + l_countryName + "'");
						return;
					}
					l_params = Utl.getFirstWord(l_params[1]);
					l_continentId = Utl.convertToInteger(l_params[0]);
					if (l_continentId > 1000 || l_continentId < 0) {
						d_msg_model.setMessage(MessageType.Error, "Invalid continent id: '" + l_params[0]
								+ "', expecting a positive integer less than 1000");
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
					l_params = Utl.getFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.isValidMapName(l_countryName)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid country name '" + l_countryName + "'");
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
			l_params = Utl.getFirstWord(l_params[1]);
			Map.refreshCountriesOfAllContinents(l_map);
			d_msg_model.setMessage(MessageType.None, "editcountry processed successfully");
		}
	}

	/**
	 * process editcontinent command: <br>
	 * editcontinent -add continentID continentName -remove continentID
	 * 
	 * @param p_editcontinent_params the editcontinent parameters (just the
	 *                               parameters without the editcontinent command
	 *                               itself)
	 * @throws Exception unexpected error encountered
	 */
	private void processEditContinent(String p_editcontinent_params) throws Exception {
		IMapModel l_map = d_model_factory.getMapModel();
		int l_continentId;
		String l_continentvalue;
		String l_params[] = Utl.getFirstWord(p_editcontinent_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg_model.setMessage(MessageType.Error, "Invalid editcontinent, no options specified");
			return;
		}

		while (!Utl.isEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_continentId = Utl.convertToInteger(l_params[0]);
					if (l_continentId > 1000 || l_continentId < 0) {
						d_msg_model.setMessage(MessageType.Error, "Invalid continent id '" + l_params[0]
								+ "', expecting a positive integer less than 1000");
						return;
					}
					l_params = Utl.getFirstWord(l_params[1]);
					l_continentvalue = l_params[0];
					if (!Utl.isValidMapName(l_continentvalue)) {
						d_msg_model.setMessage(MessageType.Error, "Invalid continent name: '" + l_params[0] + "'");
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
					l_params = Utl.getFirstWord(l_params[1]);
					l_continentId = Utl.convertToInteger(l_params[0]);
					if (l_continentId > 1000) {
						d_msg_model.setMessage(MessageType.Error, "Invalid continent id: '" + l_params[0]
								+ "', expecting a positive integer less than 1000");
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
			l_params = Utl.getFirstWord(l_params[1]);
			d_msg_model.setMessage(MessageType.None, "editcontinent processed successfully");
		}
	}

	/**
	 * 
	 * @return the current map editor view
	 */
	public IMapEditorView getMapEditorView() {
		return d_view;
	}

	/**
	 * Asks the view to display the list of map editor commands along with their
	 * syntax.
	 * 
	 * @param p_view the map editor view being used by the controller
	 */
	private void mapEditorHelp() {
		d_view.displayMapEditorBanner();
		d_view.processMessage(MessageType.None, "Map editor commands:");
		d_view.processMessage(MessageType.None, " - editcontinent -add continentID continentName -remove continentID");
		d_view.processMessage(MessageType.None, " - editcountry -add countryName continentID -remove countryName");
		d_view.processMessage(MessageType.None,
				" - editneighbor -add countryName neighborcountryName -remove countryName neighborcountryName");
		d_view.processMessage(MessageType.None, " - showmap");
		d_view.processMessage(MessageType.None, " - savemap filename");
		d_view.processMessage(MessageType.None, " - editmap filename");
		d_view.processMessage(MessageType.None, " - loadmap filename (initiates game startup)");
		d_view.processMessage(MessageType.None, " - validatemap");
		d_view.processMessage(MessageType.None, " - exit");
		d_view.processMessage(MessageType.None, " - help");
	}
}
