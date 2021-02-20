package soen6441.team01.warzone.model.contracts;

import soen6441.team01.warzone.common.contracts.Observer;
import soen6441.team01.warzone.common.entities.MessageType;

/**
 * defines the model interface for user messages. Used mainly by the controller
 * to invoke changes to the models.
 *
 */
public interface IUserMessageModel {
	void setMessage(MessageType p_message_type, String p_message);
}
