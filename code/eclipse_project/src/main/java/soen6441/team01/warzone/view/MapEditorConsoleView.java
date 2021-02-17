package soen6441.team01.warzone.view;

import java.util.Scanner;

import soen6441.team01.warzone.common.MessageType;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.controller.IInteractionDrivenController;

/**
 * Warzone MVC console based view. Supports the different views required for
 * playing Warzone. The view interacts with the user via the system console.
 */
public class MapEditorConsoleView implements IMapEditorView {
	private IInteractionDrivenController d_controller;
	private Scanner d_keyboard = null;

	/**
	 * Constructor.
	 * 
	 * @param p_controller the associated controller object
	 */
	public MapEditorConsoleView(IInteractionDrivenController p_controller) {
		d_controller = p_controller; 
		d_keyboard = new Scanner(System.in);
	}

	/**
	 * Display the Warzone banner
	 */
	public void displayWarzoneBanner() {
		System.out.println("**********************************");
		System.out.println("*                                *");
		System.out.println("*           Warzone              *");
		System.out.println("*                                *");
		System.out.println("**********************************");
	}

	/**
	 * Displays the Warzone map editor banner
	 */
	public void displayMapEditorBanner() {
		System.out.println("");
		System.out.println("***        Map Editor          ***");
	}

	/**
	 * Get the next command typed in on the console from the user
	 * 
	 * @return the command text typed in by the user
	 */
	public String getCommand() {
		System.out.println("");
		System.out.print("Map editor command> ");
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
		switch (p_msg_type) {
		case None:
			System.out.println(p_message);
			break;
		case Informational:
			System.out.println("info: " + p_message);
			break;
		case Warning:
			System.out.println("warn: " + p_message);
			break;
		default:
			System.out.println("error: " + p_message);
			break;
		}
	}

	/**
	 * do a clean shutdown of the view
	 */
	public void shutdown() {
		if (d_keyboard != null) {
			d_keyboard.close();
		}
	}

}
