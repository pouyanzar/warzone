package soen6441.team01.warzone.controller;

import java.io.Serializable;
import java.util.ArrayList;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.ITournamentController;
import soen6441.team01.warzone.controller.contracts.IMapEditorController;
import soen6441.team01.warzone.model.GameEngine;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.Phase;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.entities.SaveMapFormat;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.view.ViewFactory;
import soen6441.team01.warzone.view.contracts.IMapEditorView;

/**
 * Warzone Map Editor controller. Manages the coordination and progression of
 * the Map Editor.
 */
public class MapEditorController extends Phase implements IMapEditorController, Serializable {
	private static final long serialVersionUID = 1L;
	private ModelFactory d_model_factory;
	private ViewFactory d_view_factory;
	private ControllerFactory d_controller_factory;
	private IMapEditorView d_view;
	private IAppMsg d_msg;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_controller_factory predefined SoftwareFactoryController.
	 * @throws Exception unexpected error
	 */
	public MapEditorController(ControllerFactory p_controller_factory) throws Exception {
		super(p_controller_factory.getModelFactory().getGameEngine());
		d_controller_factory = p_controller_factory;
		d_model_factory = p_controller_factory.getModelFactory();
		d_view_factory = p_controller_factory.getViewFactory();
		d_view = d_view_factory.getMapEditorConsoleView(this);
		d_msg = d_model_factory.getUserMessageModel();
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
			Utl.lprintln("Fatal error processing Map Editor, exception: " + ex.getMessage());
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
	 * <li>savemap [-d|-c] filename</li>
	 * <li>editmap filename</li>
	 * <li>loadmap filename (this command initiates game startup)</li>
	 * <li>loadgame filename</li>
	 * <li>validatemap</li>
	 * <li>tournament -M listofmapfiles(1-5) -P listofplayerstrategies (2-4) -G
	 * numberofgames (1-5) -D maxnumberofturns (10-50)</li>
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
				d_msg.setMessage(MsgType.None, "The current map is valid.");
			} else {
				d_msg.setMessage(MsgType.None, "The current map is invalid.");
			}
			break;
		case "loadmap":
			// if the map is not a valid map then stay in the editor, otherwise move on to
			// the game startup phase (single game mode).
			if (processLoadMap(l_cmd_params[1])) {
				l_next_phase = d_controller_factory.getGameStartupPhase();
			}
			break;
		case "loadgame":
			l_next_phase = processLoadGame(l_cmd_params[1]);
			break;
		case "tournament":
			if (processTournament(l_cmd_params[1])) {
				l_next_phase = (Phase) d_controller_factory.getGameTournamentController();
				d_controller_factory.setGamePlayPhase(l_next_phase);
			}
			break;
		default:
			d_msg.setMessage(MsgType.Error, "invalid command '" + p_command + "'");
			break;
		}
		return l_next_phase;
	}

	/**
	 * process the loadgame command. if the map is not a valid map then stay in the
	 * editor, otherwise move on to the phase saved in the game.<br>
	 * Syntax:<br>
	 * loadgame filename
	 * 
	 * @param p_loadgame_params the loadgame parameters (just the parameters without
	 *                          the loadgame command itself)
	 * @return true if successful
	 * @throws Exception unexpected error encountered
	 */
	public Phase processLoadGame(String p_loadgame_params) throws Exception {
		try {
			String l_params[] = Utl.getFirstWord(p_loadgame_params);
			String l_filename = l_params[0];
			GameEngine l_new_engine = d_model_factory.getGameEngine().loadGame(l_filename);
			d_msg.setMessage(MsgType.None, "loadgame processed successfully");
			// the gamed loaded successfully, set the current game engine to exit and let
			// the main() method resume execution on the newly loaded game engine.
			((GameEngine) d_model_factory.getGameEngine()).setNewGameEngineToRun(l_new_engine);
			// set the next phase to any phase. which phase doesn't matter since the game
			// engine has been set to exit to the main() function and resume execution on
			// the newly loaded game engine.
			return d_controller_factory.getGameEndPhase();
		} catch (Exception ex) {
			d_msg.setMessage(MsgType.Error, ex.getMessage());
		}

		return null;
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
			d_msg.setMessage(MsgType.Error, ex.getMessage());
			return false;
		}
		if (d_model_factory.getMapModel().validatemap()) {
			d_msg.setMessage(MsgType.None, "loadmap processed successfully");
		} else {
			d_msg.setMessage(MsgType.Error, "loadmap error - map is not a valid map.");
			return false;
		}

		return true;
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
			d_msg.setMessage(MsgType.Error, ex.getMessage());
			return false;
		}
		d_msg.setMessage(MsgType.None, "editmap processed successfully");
		return true;
	}

	/**
	 * process the savemap command.
	 * <p>
	 * syntax: savemap [-d|-c] filename<br>
	 * -d = domination map format, -c = conquest map format
	 * </p>
	 * 
	 * @param p_savemap_params the savemap parameters
	 * @return true if successful
	 * @throws Exception unexpected error encountered
	 */
	public boolean processSaveMap(String p_savemap_params) throws Exception {
		SaveMapFormat l_map_format = SaveMapFormat.Domination;
		String l_filename = "";

		try {
			String l_params[] = Utl.getFirstWord(p_savemap_params);
			String l_token = l_params[0];
			l_params = Utl.getFirstWord(l_params[1]);
			l_filename = l_params[0];

			switch (l_token.toLowerCase()) {
			case "-d":
				l_map_format = SaveMapFormat.Domination;
				break;
			case "-c":
				l_map_format = SaveMapFormat.Conquest;
				break;
			default:
				l_map_format = SaveMapFormat.Domination;
				l_filename = l_token;
				break;
			}

			d_model_factory.getMapModel().saveMap(l_filename, l_map_format);
		} catch (Exception ex) {
			d_msg.setMessage(MsgType.Error, ex.getMessage());
			return false;
		}

		d_msg.setMessage(MsgType.None, "map saved successfully to file '" + l_filename + "'");
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
			d_msg.setMessage(MsgType.Error, "Invalid editneighbor, no options specified");
			return;
		}

		while (!Utl.isEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.isValidMapName(l_countryName)) {
						d_msg.setMessage(MsgType.Error, "Invalid country name '" + l_countryName + "'");
						return;
					}
					l_params = Utl.getFirstWord(l_params[1]);
					l_neighbor_name = l_params[0];
					if (!Utl.isValidMapName(l_neighbor_name)) {
						d_msg.setMessage(MsgType.Error, "Invalid country neighbor name '" + l_countryName + "'");
						return;
					}
					l_map.addNeighbor(l_countryName, l_neighbor_name);
				} catch (Exception ex) {
					d_msg.setMessage(MsgType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.isValidMapName(l_countryName)) {
						d_msg.setMessage(MsgType.Error, "Invalid country name '" + l_countryName + "'");
						return;
					}
					l_params = Utl.getFirstWord(l_params[1]);
					l_neighbor_name = l_params[0];
					if (!Utl.isValidMapName(l_neighbor_name)) {
						d_msg.setMessage(MsgType.Error, "Invalid country neighbor name '" + l_countryName + "'");
						return;
					}
					l_map.removeNeighbor(l_countryName, l_neighbor_name);
				} catch (Exception ex) {
					d_msg.setMessage(MsgType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg.setMessage(MsgType.Error,
						"Invalid editneighbor option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.getFirstWord(l_params[1]);
			Map.refreshCountriesOfAllContinents(l_map);
			d_msg.setMessage(MsgType.None, "editneighbor processed successfully");
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
			d_msg.setMessage(MsgType.Error, "Invalid editcountry, no options specified");
			return;
		}

		while (!Utl.isEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.isValidMapName(l_countryName)) {
						d_msg.setMessage(MsgType.Error, "Invalid country name '" + l_countryName + "'");
						return;
					}
					l_params = Utl.getFirstWord(l_params[1]);
					l_continentId = Utl.convertToInteger(l_params[0]);
					if (l_continentId > 1000 || l_continentId < 0) {
						d_msg.setMessage(MsgType.Error, "Invalid continent id: '" + l_params[0]
								+ "', expecting a positive integer less than 1000");
						return;
					}
					ICountryModel l_country = l_map.addCountry(l_countryName, l_continentId);
					if (l_country == null) {
						d_msg.setMessage(MsgType.Error, "Error adding country");
						return;
					}
				} catch (Exception ex) {
					d_msg.setMessage(MsgType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_countryName = l_params[0];
					if (!Utl.isValidMapName(l_countryName)) {
						d_msg.setMessage(MsgType.Error, "Invalid country name '" + l_countryName + "'");
						return;
					}
					ICountryModel l_country = l_map.removeCountry(l_countryName);
					if (l_country == null) {
						d_msg.setMessage(MsgType.Error, "Error removing country");
						return;
					}

				} catch (Exception ex) {
					d_msg.setMessage(MsgType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg.setMessage(MsgType.Error,
						"Invalid editcountry option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.getFirstWord(l_params[1]);
			Map.refreshCountriesOfAllContinents(l_map);
			d_msg.setMessage(MsgType.None, "editcountry processed successfully");
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
			d_msg.setMessage(MsgType.Error, "Invalid editcontinent, no options specified");
			return;
		}

		while (!Utl.isEmpty(l_params[0])) {
			switch (l_params[0]) {
			case "-add":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_continentId = Utl.convertToInteger(l_params[0]);
					if (l_continentId > 1000 || l_continentId < 0) {
						d_msg.setMessage(MsgType.Error, "Invalid continent id '" + l_params[0]
								+ "', expecting a positive integer less than 1000");
						return;
					}
					l_params = Utl.getFirstWord(l_params[1]);
					l_continentvalue = l_params[0];
					if (!Utl.isValidMapName(l_continentvalue)) {
						d_msg.setMessage(MsgType.Error, "Invalid continent name: '" + l_params[0] + "'");
						return;
					}
					IContinentModel l_continent = l_map.addContinent(l_continentId, l_continentvalue, 0, null);
					if (l_continent == null) {
						d_msg.setMessage(MsgType.Error, "Error adding continent");
						return;
					}
				} catch (Exception ex) {
					d_msg.setMessage(MsgType.Error, ex.getMessage());
					return;
				}
				break;
			case "-remove":
				try {
					l_params = Utl.getFirstWord(l_params[1]);
					l_continentId = Utl.convertToInteger(l_params[0]);
					if (l_continentId > 1000) {
						d_msg.setMessage(MsgType.Error, "Invalid continent id: '" + l_params[0]
								+ "', expecting a positive integer less than 1000");
						return;
					}
					IContinentModel l_continent = l_map.removeContinent(l_continentId);
					if (l_continent == null) {
						d_msg.setMessage(MsgType.Error, "Error removing continent");
						return;
					}

				} catch (Exception ex) {
					d_msg.setMessage(MsgType.Error, ex.getMessage());
					return;
				}
				break;
			default:
				d_msg.setMessage(MsgType.Error,
						"Invalid editcontinent option '" + l_params[0] + "', expecting: -add, -remove");
				return;
			}
			l_params = Utl.getFirstWord(l_params[1]);
			d_msg.setMessage(MsgType.None, "editcontinent processed successfully");
		}
	}

	/**
	 * process the tournament command
	 * <p>
	 * syntax: <br>
	 * tournament -M listofmapfiles (1-5) -P listofplayerstrategies (2-4) -G
	 * numberofgames (1-5) -D maxnumberofturns (10-50)<br>
	 * </p>
	 * 
	 * @param p_cmd_params the tournament parameters
	 * @return return true if successful
	 * @throws Exception unexpected error encountered
	 */
	private boolean processTournament(String p_cmd_params) throws Exception {
		ArrayList<String> l_map_filenames = new ArrayList<String>();
		ArrayList<String> l_strategies = new ArrayList<String>();
		int l_number_of_games = 0;
		int l_max_turns = Integer.MAX_VALUE;

		// 1) parse tournament command

		String l_tmp_param;
		String l_params[] = Utl.getFirstWord(p_cmd_params);

		if (Utl.isEmpty(l_params[0])) {
			d_msg.setMessage(MsgType.Error, "Invalid tournament, no options specified");
			return false;
		}

		try {
			while (!Utl.isEmpty(l_params[0])) {
				switch (l_params[0]) {
				case "-M":
					l_tmp_param = parseTournamentList(l_params[1], l_map_filenames);
					l_params = Utl.getFirstWord(l_tmp_param);
					break;
				case "-P":
					l_tmp_param = parseTournamentList(l_params[1], l_strategies);
					l_params = Utl.getFirstWord(l_tmp_param);
					break;
				case "-G":
					l_params = Utl.getFirstWord(l_params[1]);
					l_number_of_games = Utl.convertToInteger(l_params[0]);
					l_params = Utl.getFirstWord(l_params[1]);
					break;
				case "-D":
					l_params = Utl.getFirstWord(l_params[1]);
					l_max_turns = Utl.convertToInteger(l_params[0]);
					l_params = Utl.getFirstWord(l_params[1]);
					break;
				default:
					d_msg.setMessage(MsgType.Error,
							"Invalid tournament option '" + l_params[0] + "', expecting: -M, -P, -G and -D");
					return false;
				}
			}

			// 2) create the tournament controller that will process the tournament
			ITournamentController l_tour_ctrl = d_controller_factory.createGameTournamentController(l_map_filenames,
					l_strategies, l_number_of_games, l_max_turns);

			// 3) validate the parameters
			ArrayList<String> l_val_out = new ArrayList<String>();
			if (!l_tour_ctrl.validateTournamentParameters(l_val_out)) {
				d_view.processMessages(l_val_out);
				throw new Exception("Invalid tournament command");
			}

			d_msg.setMessage(MsgType.Informational, "tournament command processed successfully");
		} catch (Exception ex) {
			d_msg.setMessage(MsgType.Error, ex.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Parses out all the tokens associated with tournament commands that allow
	 * lists of items i.e. -M, -P options.<br>
	 * Each item must be separated by a comma.<br>
	 * empty items are ignored (e.g. -M A,,B, -P...) <br>
	 * On entry l_params should have -option in l_param[0]<br>
	 * On exit l_param[0] should either be blank or contain the next tournament
	 * -option.
	 * 
	 * @param l_params the tournament parameters to parse. must start with the first
	 *                 token after the -option
	 * @param p_list   the list to add the items to
	 * @return the part of the parameters not yet parsed
	 */
	private String parseTournamentList(String p_params, ArrayList<String> p_list) {
		String[] l_params = Utl.getFirstWord(p_params);
		String l_reply = l_params[1];

		// parse out all the items
		String l_m_params = "";
		while (!Utl.isEmpty(l_params[0]) && !isTournamentOption(l_params[0])) {
			l_m_params += l_params[0] + " ";
			l_reply = l_params[1];
			l_params = Utl.getFirstWord(l_params[1]);
		}

		// each filename must be comma separated
		String[] l_parts = l_m_params.split(",");
		for (String l_part : l_parts) {
			l_part = l_part.replace(',', ' ');
			l_part = l_part.trim();
			if (!Utl.isEmpty(l_part)) {
				p_list.add(l_part);
			}
		}

		return l_reply;
	}

	/**
	 * 
	 * @param l_param the tournament command option to analyze
	 * @return true if l_param is one of the tournament options, ie: -M, -P, -G, -D
	 */
	private boolean isTournamentOption(String l_param) {
		switch (l_param) {
		case "-M":
		case "-P":
		case "-G":
		case "-D":
			return true;
		}
		return false;
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
		d_view.processMessage(MsgType.None, "Map editor commands:");
		d_view.processMessage(MsgType.None, " - editcontinent -add continentID continentName -remove continentID");
		d_view.processMessage(MsgType.None, " - editcountry -add countryName continentID -remove countryName");
		d_view.processMessage(MsgType.None,
				" - editneighbor -add countryName neighborcountryName -remove countryName neighborcountryName");
		d_view.processMessage(MsgType.None, " - showmap");
		d_view.processMessage(MsgType.None, " - savemap [-d|-c] filename");
		d_view.processMessage(MsgType.None, " -         where -d = domination map format, -c = conquest map format");
		d_view.processMessage(MsgType.None, " - editmap filename");
		d_view.processMessage(MsgType.None, " - loadmap filename (initiates game startup in single game mode)");
		d_view.processMessage(MsgType.None, " - loadgame filename");
		d_view.processMessage(MsgType.None, " - validatemap");
		d_view.processMessage(MsgType.None,
				" - tournament -M listofmapfiles(1-5) -P listofplayerstrategies(2-4) -G numberofgames(1-5) -D maxnumberofturns(10-50)");
		d_view.processMessage(MsgType.None, "              (starts tournament game mode)");
		d_view.processMessage(MsgType.None, " - exit");
		d_view.processMessage(MsgType.None, " - help");
	}
}
