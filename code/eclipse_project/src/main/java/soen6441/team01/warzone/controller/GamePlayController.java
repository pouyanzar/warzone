package soen6441.team01.warzone.controller;

import java.util.LinkedList;
import java.util.Queue;

import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IGamePlayModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.view.SoftwareFactoryView;
import soen6441.team01.warzone.view.contracts.IGamePlayView;
import soen6441.team01.warzone.view.contracts.IGameStartupView;

/**
 * Warzone game play controller. Manages the coordination and progression of the
 * game play phase.
 */
public class GamePlayController implements IGamePlayController {
	private SoftwareFactoryModel d_model_factory;
	private SoftwareFactoryView d_view_factory;
	private IGamePlayView d_view;
	private IUserMessageModel d_msg_model;

	/**
	 * Constructor with view and models defined.
	 * 
	 * @param p_model_factory predefined SoftwareFactoryModel.
	 * @param p_view_factory  predefined SoftwareFactoryView.
	 * @throws Exception unexpected error
	 */
	public GamePlayController(SoftwareFactoryModel p_model_factory, SoftwareFactoryView p_view_factory)
			throws Exception {
		d_model_factory = p_model_factory;
		d_view_factory = p_view_factory;
		d_view = d_view_factory.getGamePlayConsoleView(this);
		d_msg_model = d_model_factory.getUserMessageModel();
	}

	/**
	 * Starts executing the game startup dynamics
	 * 
	 * @return String one of: exit, game_over
	 */
	public String processGamePlay(IGamePlayModel p_gameplay_model) {
		String l_cmd = "exit";
		boolean l_exit_gameplay = false;

		try {
			d_view.displayGamePlayBanner();

			while (!l_exit_gameplay) {
				// assign reinforcements phase
				p_gameplay_model.assignReinforcements();

				// issue orders phase
				l_cmd = issueOrders(p_gameplay_model);
				if (l_cmd.equals("exit") || l_cmd.equals("game_over")) {
					l_exit_gameplay = true;
				}

				// execute orders phase
				if (!l_exit_gameplay) {
					p_gameplay_model.executeOrders();
				}
			}
		} catch (Exception ex) {
			System.out.println("Fatal error encountered during gameplay.");
			System.out.println("Exception: " + ex.getMessage());
		}

		if (d_view != null) {
			d_view.shutdown();
		}

		return l_cmd;
	}

	/**
	 * Manages the map editor's interactions with the game startup view, and
	 * processes any commands coming from the view.
	 * 
	 * @param p_view the GameStartupView to interact with
	 * @return String the last command entered
	 * @throws Exception general exception processing the map editor
	 */
	private String issueOrders(IGamePlayModel p_gameplay_model) throws Exception {
		boolean l_exit = false;
		String l_cmd = "exit";

		Queue<IPlayerModel> l_queue = new LinkedList<IPlayerModel>();
		for (IPlayerModel l_player : p_gameplay_model.getPlayers()) {
			l_queue.add(l_player);
		}

		while (!l_exit) {
			IPlayerModel l_player = l_queue.peek();
			l_cmd = processGamePlayCommand(l_player);
			switch (l_cmd) {
			case "player_again": // player still has work to do
				l_queue.remove();
				l_queue.add(l_player);
				break;
			case "player_done": // player has no more work to do
				l_queue.remove();
				break;
			case "player_redo": // same player to redo another command
				break;
			case "orders_done": // move on to next phase
				l_exit = true;
				break;
			case "exit": // exit game
				l_exit = true;
				break;
			default:
				throw new Exception("Internal error issuing orders");
			}
		}
		return l_cmd;
	}

	/**
	 * Process the game play command:
	 * <ul>
	 * <li>showmap</li>
	 * <li>deploy countryID num_reinforcements</li>
	 * <li>exit</li>
	 * <li>help</li>
	 * </ul>
	 * 
	 * @param p_player the player to get and process commands for
	 * @return "exit" = exit game play, "player_redo" same player to redo another
	 *         command, "player_again" = player still has work to do, "player_done"
	 *         = player has no more work to do, "orders_done" = move on to next
	 *         phase.
	 * @throws Exception unexpected error
	 */
	public String processGamePlayCommand(IPlayerModel p_player) throws Exception {
		if (p_player == null) {
			return "orders_done";
		}

		String l_return = "exit";
		String l_cmd = d_view.getCommand("Gameplay " + p_player.getName() + ">");
		String l_cmd_params[] = Utl.getFirstWord(l_cmd);

		switch (l_cmd_params[0]) {
		case "help":
			GameStartupHelp();
			l_return = "player_redo";
			break;
		case "exit":
			l_return = "exit";
			break;
		case "showmap":
			d_msg_model.setMessage(MessageType.None, "showmap coming soon...");
			l_return = "player_redo";
			break;
		case "deploy":
			d_msg_model.setMessage(MessageType.None, "deploy coming soon...");
			l_return = "player_again";
			break;
		default:
			d_msg_model.setMessage(MessageType.Error, "invalid command '" + l_cmd + "'");
			l_return = "player_redo";
			break;
		}
		return l_return;
	}

	/**
	 * Asks the view to display the list of map editor commands along with their
	 * syntax.
	 * 
	 * @param p_view the map editor view being used by the controller
	 */
	private void GameStartupHelp() {
		d_view.displayGamePlayBanner();
		d_view.processMessage(MessageType.None, "Gameplay commands:");
		d_view.processMessage(MessageType.None, " - showmap");
		d_view.processMessage(MessageType.None, " - deploy countryID num_reinforcements");
		d_view.processMessage(MessageType.None, " - exit");
		d_view.processMessage(MessageType.None, " - help");
	}
}
