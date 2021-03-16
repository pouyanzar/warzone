package soen6441.team01.warzone.view.contracts;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.ICountryModel;
import soen6441.team01.warzone.model.contracts.IPlayerModel;
import soen6441.team01.warzone.model.entities.UserMessage;

/**
 * Defines the interface used to support the Warzone game play view
 *
 */
public interface IGamePlayView { 
	void displayGamePlayBanner();

	String getCommand();
	
	String getCommand(String p_prompt);
	
	void deactivate();

	void processMessage(MsgType none, String string);
	
	void showCountry(ICountryModel p_country);
	
	void activate();
	
	void showCards(IPlayerModel p_player);
}
