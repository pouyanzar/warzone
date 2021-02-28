package soen6441.team01.warzone.view.contracts;

import soen6441.team01.warzone.common.entities.MessageType;
import soen6441.team01.warzone.model.Map;
import soen6441.team01.warzone.model.contracts.IMapModel;

/**
 * Defines the interface used to support the Warzone map editor view
 *
 */
public interface IMapEditorView {
	void displayWarzoneBanner();

	void displayMapEditorBanner();

	String getCommand();

	void processMessage(MessageType p_msg_type, String p_message);

	void shutdown();
	
	void showmap(IMapModel p_map);
}
