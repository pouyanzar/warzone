package soen6441.team01.warzone.view.contracts;

import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.model.entities.UserMessage;

/**
 * Defines the interface used to support the Warzone game play view
 *
 */
public interface IGamePlayView { 
	void displayGamePlayBanner();

	String getCommand();
	
	void shutdown();

	void processMessage(MessageType none, String string);
}
