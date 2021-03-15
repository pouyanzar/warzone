package soen6441.team01.warzone.view.contracts;

import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.entities.UserMessage;

/**
 * Defines the interface used to support the Warzone game startup view
 *
 */
public interface IGameStartupView { 
	void displayGameStartupBanner();

	String getCommand();
	
	void deactivate();

	void processMessage(MsgType none, String string);
	
	void activate();
}
