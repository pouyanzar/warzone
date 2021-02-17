package soen6441.team01.warzone.view;

import soen6441.team01.warzone.common.MessageType;

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
}
