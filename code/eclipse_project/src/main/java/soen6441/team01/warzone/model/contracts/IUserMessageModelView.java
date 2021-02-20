package soen6441.team01.warzone.model.contracts;

import soen6441.team01.warzone.common.contracts.Observer;
import soen6441.team01.warzone.model.entities.UserMessage;

/**
 * Defines the view-model interface for user messages, i.e. used mainly by the
 * views to get data from the model
 *
 */
public interface IUserMessageModelView {
	void attach(Observer observer);
	void detach(Observer observer);
	UserMessage getLastMessage();
}
