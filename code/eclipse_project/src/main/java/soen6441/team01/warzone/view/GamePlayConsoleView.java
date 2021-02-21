package soen6441.team01.warzone.view;

import java.util.Scanner;

import soen6441.team01.warzone.common.Observable;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.contracts.Observer;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.model.contracts.IUserMessageModelView;
import soen6441.team01.warzone.model.entities.UserMessage;
import soen6441.team01.warzone.view.contracts.IGamePlayView;

/**
 * Warzone game play console based view. The view interacts with the user via
 * the system console.
 */
public class GamePlayConsoleView implements Observer, IGamePlayView {
	private IGamePlayController d_controller = null;
	private Scanner d_keyboard = null;
	private IUserMessageModelView d_user_message_model = null;

	/**
	 * Constructor.
	 * 
	 * @param p_controller         the associated controller object
	 * @param p_user_message_model the user message model to use. note that this
	 *                             view gets notifications from the model to display
	 *                             messages to the user.
	 */
	public GamePlayConsoleView(IGamePlayController p_controller, IUserMessageModelView p_user_message_model) {
		d_controller = p_controller;
		d_keyboard = new Scanner(System.in);
		d_user_message_model = p_user_message_model;
		p_user_message_model.attach(this);
	}

	/**
	 * do a clean shutdown of the view
	 */
	public void shutdown() {
		if (d_user_message_model != null) {
			d_user_message_model.detach(this);
		}
	}

	/**
	 * Displays the startup banner
	 */
	public void displayGamePlayBanner() {
		System.out.println("");
		System.out.println("***         Game Play          ***");
	}

	/**
	 * Get the next command typed in on the console from the user
	 * 
	 * @return the command text typed in by the user
	 */
	public String getCommand() {
		System.out.println("");
		System.out.print("Game command> ");
		String l_user_command = d_keyboard.nextLine();
		return l_user_command;
	}

	/**
	 * Displays a message to the user
	 * 
	 * @param p_msg_type the type of message to display as defined by the enum
	 * @param p_message  the message to display to the user
	 */
	public void processMessage(MessageType p_msg_type, String p_message) {
		Utl.ConsoleMessage(p_msg_type, p_message);
	}

	/**
	 * process message helper function
	 * 
	 * @param p_user_message the UserMessage to process
	 */
	public void processMessage(UserMessage p_user_message) {
		MessageType l_msgtyp = p_user_message.getMessageType();
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
