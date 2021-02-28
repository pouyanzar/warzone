package soen6441.team01.warzone.view;

import java.util.Scanner;

import soen6441.team01.warzone.common.Observable;
import soen6441.team01.warzone.common.Utl;
import soen6441.team01.warzone.common.contracts.Observer;
import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.controller.contracts.IMapEditorController;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.SoftwareFactoryModel;
import soen6441.team01.warzone.model.contracts.IContinentModel;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IMapModel;
import soen6441.team01.warzone.model.contracts.IUserMessageModel;
import soen6441.team01.warzone.model.entities.UserMessage;
import soen6441.team01.warzone.view.contracts.IMapEditorView;

/**
 * Warzone map editor console based view. The view interacts with the user via
 * the system console.
 */
public class MapEditorConsoleView implements Observer, IMapEditorView {
	private IMapEditorController d_controller = null;
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
	public MapEditorConsoleView(IMapEditorController p_controller, SoftwareFactoryModel p_factory_model)
			throws Exception {
		d_controller = p_controller;
		d_keyboard = new Scanner(System.in);
		d_factory_model = p_factory_model;
		d_user_message_model = d_factory_model.getUserMessageModel();
		d_user_message_model.attach(this);
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
	 * do a clean shutdown of the view
	 */
	public void shutdown() {
		if (d_user_message_model != null) {
			d_user_message_model.detach(this);
		}
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
	 * show all continents and countries and their respective neighbors
	 * 
	 * @param p_map the map to show
	 */
	public void showmap(IMapModel p_map) {
		for(int j=0;j<p_map.getCountries().size();j++){
			
			ICountryModel l_country_model=p_map.getCountries().get(j);				
			IContinentModel l_continent_model=l_country_model.getContinent();
			System.out.println("\n" +l_continent_model.getName());				
			System.out.println("\n\t" +l_country_model.getName()+"[\n");				
			for(int k=0;k<l_country_model.getNeighbors().size();k++){		
				ICountryModel l_neighbor=l_country_model.getNeighbors().get(k);
				System.out.println(l_neighbor.getName()+", ");
			}
			System.out.println("\n\t" +l_country_model.getName()+"]\n");
		}
	}

}
