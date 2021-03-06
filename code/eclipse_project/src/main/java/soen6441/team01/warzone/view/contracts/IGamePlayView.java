package soen6441.team01.warzone.view.contracts;

import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.entities.UserMessage;

/**
 * Defines the interface used to support the Warzone game play view
 *
 */
public interface IGamePlayView { 
	void displayGamePlayBanner();

	String getCommand();
	
	String getCommand(String p_prompt);
	
	void shutdown();

	void processMessage(MessageType none, String string);
	
	void showCountry(ICountryModel p_country);
}
