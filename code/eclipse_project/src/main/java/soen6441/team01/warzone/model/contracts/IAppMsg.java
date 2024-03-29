package soen6441.team01.warzone.model.contracts;

import soen6441.team01.warzone.common.contracts.Observer;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.entities.UserMessage;

/**
 * defines the model interface for user messages. Used mainly by the controller
 * to invoke changes to the models.
 *
 */
public interface IAppMsg {
	void setMessage(MsgType p_message_type, String p_message);

	void attach(Observer observer);

	void detach(Observer observer);

	UserMessage getLastMessage();
}
