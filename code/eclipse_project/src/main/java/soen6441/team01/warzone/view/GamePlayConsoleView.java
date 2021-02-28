package soen6441.team01.warzone.view;

import java.util.ArrayList;
import java.util.Scanner;

import soen6441.team01.warzone.common.Observable;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.contracts.Observer;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IGamePlayController;
import soen6441.team01.warzone.controller.contracts.IGameStartupController;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.model.entities.CountrySummary;
import soen6441.team01.warzone.model.entities.UserMessage;
import soen6441.team01.warzone.view.contracts.IGamePlayView;

/**
 * Warzone game play console based view. The view interacts with the user via
 * the system console.
 */
public class GamePlayConsoleView implements Observer, IGamePlayView {
	private IGamePlayController d_controller = null;
	private Scanner d_keyboard = null;
	private IUserMessageModel d_user_message_model = null;
	private SoftwareFactoryModel d_factory_model = null;

	/**
	 * Constructor.
	 * 
	 * @param p_controller    the associated controller object
	 * @param p_factory_model model factory
	 * @throws Exception unexpected error
	 */
	public GamePlayConsoleView(IGamePlayController p_controller, SoftwareFactoryModel p_factory_model)
			throws Exception {
		d_controller = p_controller;
		d_keyboard = new Scanner(System.in);
		d_factory_model = p_factory_model;
		d_user_message_model = d_factory_model.getUserMessageModel();
		d_user_message_model.attach(this);
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
		System.out.print("Gameplay> ");
		String l_user_command = d_keyboard.nextLine();
		return l_user_command;
	}

	/**
	 * Get the next command typed in on the console from the user
	 * 
	 * @param p_prompt the specified prompt
	 * @return the command text typed in by the user
	 */
	public String getCommand(String p_prompt) {
		System.out.println("");
		System.out.print(p_prompt + " ");
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
		Utl.consoleMessage(p_msg_type, p_message);
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

	/**
	 * Display the game play map for specified player
	 * 
	 * @param p_country country to show
	 */
	public void showCountry(ICountryModel p_country) {
		ArrayList<String> l_report = new ArrayList<String>();

		// generate basic report
		l_report.add("* " + printCountrySummary(p_country.getSummary()));
		for (ICountryModel l_neighbor : p_country.getNeighbors()) {
			l_report.add("     " + printCountrySummary(l_neighbor.getSummary()));
		}

		// format columns
		int l_col = Utl.farthestReportField(l_report, "[", 1) + 2;
		l_report = Utl.justifyReportyField(l_report, "[", 1, l_col, '-');
		l_col = Utl.farthestReportField(l_report, "[", 2) + 1;
		l_report = Utl.justifyReportyField(l_report, "[", 2, l_col, ' ');
		l_col = Utl.farthestReportField(l_report, "]", 2);
		l_report = Utl.justifyReportyField(l_report, "]", 2, l_col, ' ');

		// printout report
		System.out.println("");
		for (String l_line : l_report) {
			System.out.println(l_line);
		}
	}

	/**
	 * Formats the country information into a string
	 * 
	 * @param p_summary Country summary information
	 * @return
	 */
	private String printCountrySummary(CountrySummary p_summary) {
		String l_sum = p_summary.d_country_name + "[" + setNull(p_summary.d_country_owner_name, "!1") + "] ["
				+ p_summary.d_armies + "] [" + p_summary.d_continent_name + " " + p_summary.d_continent_bonus + " "
				+ p_summary.d_player_owns_countries_of_continent + "]";
		return l_sum;
	}

	/**
	 * If p_string == null then returns specified string
	 * 
	 * @param p_string the string to check for null
	 * @param p_with   string to replace null values with
	 * @return the fixed string
	 */
	private String setNull(String p_string, String p_with) {
		if (p_string == null) {
			return p_with;
		}
		return p_string;
	}

}
