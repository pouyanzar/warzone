package soen6441.team01.warzone.view;

import java.util.Scanner;

import soen6441.team01.warzone.common.Observable;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.contracts.Observer;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.model.ModelFactory;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.entities.UserMessage;
import soen6441.team01.warzone.view.contracts.IGameStartupView;

/**
 * Warzone game startup console based view. The view interacts with the user via
 * the system console.
 */
public class GameStartupConsole implements Observer, IGameStartupView {
	private IGameStartupController d_controller = null;
	private Scanner d_keyboard = null;
	private IAppMsg d_user_message_model = null;
	private ModelFactory d_factory_model = null;

	/**
	 * Constructor.
	 * 
	 * @param p_controller    the associated controller object
	 * @param p_factory_model model factory
	 * @throws Exception unexpected error
	 */
	public GameStartupConsole(IGameStartupController p_controller, ModelFactory p_factory_model)
			throws Exception {
		d_controller = p_controller;
		d_keyboard = new Scanner(System.in);
		d_factory_model = p_factory_model;
		d_user_message_model = d_factory_model.getUserMessageModel();
	}

	/**
	 * activate the view
	 */
	public void activate() {
		if (d_user_message_model != null) {
			d_user_message_model.attach(this);
		}
	}

	/**
	 * do a clean shutdown of the view
	 */
	public void deactivate() {
		if (d_user_message_model != null) {
			d_user_message_model.detach(this);
		}
	}

	/**
	 * Displays the startup banner
	 */
	public void displayGameStartupBanner() {
		Utl.lprintln("");
		Utl.lprintln("***       Game Startup         ***");
	}

	/**
	 * Get the next command typed in on the console from the user
	 * 
	 * @return the command text typed in by the user
	 */
	public String getCommand() {
		String l_prompt = "Game startup command> ";
		System.out.println("");
		System.out.print(l_prompt);
		String l_user_command = d_keyboard.nextLine();
		Utl.logln(l_prompt + l_user_command);
		return l_user_command;
	}

	/**
	 * Displays a message to the user
	 * 
	 * @param p_msg_type the type of message to display as defined by the enum
	 * @param p_message  the message to display to the user
	 */
	public void processMessage(MsgType p_msg_type, String p_message) {
		Utl.lprintln(p_msg_type, p_message);
	}

	/**
	 * process message helper function
	 * 
	 * @param p_user_message the UserMessage to process
	 */
	public void processMessage(UserMessage p_user_message) {
		MsgType l_msgtyp = p_user_message.getMessageType();
		String l_msg = p_user_message.getMessage();
		processMessage(l_msgtyp, l_msg);
	}

	/**
	 * called by Observable whenever the system adds a new user message that needs
	 * to be communicated to the user.
	 */
	@Override
	public void update(Observable p_obserable) {
		if (p_obserable == d_user_message_model) {
			processMessage(d_user_message_model.getLastMessage());
		}

	}

}
