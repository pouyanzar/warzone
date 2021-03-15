package soen6441.team01.warzone.model;

import java.util.ArrayList;

import soen6441.team01.warzone.common.Observable;
import soen6441.team01.warzone.common.entities.MsgType;
import soen6441.team01.warzone.model.contracts.IAppMsg;
import soen6441.team01.warzone.model.entities.UserMessage;

/**
 * Manages messages destined to be communicated to the user somehow (i.e. by the
 * appropriate view)(e.g. command completion messages)
 * 
 */
public class AppMsg extends Observable implements IAppMsg {
	private ArrayList<UserMessage> d_user_messages = new ArrayList<UserMessage>();

	/**
	 * Constructor
	 */
	public AppMsg() {
	}

	/**
	 * Put a new user message on the stack. Maximum of 10 messages are kept on the
	 * stack. Notifies all observers of the new user message.
	 * 
	 * @param p_message_type the message severity
	 * @param p_message      the message text
	 */
	public void setMessage(MsgType p_message_type, String p_message) {
		d_user_messages.add(new UserMessage(p_message_type, p_message));
		while (d_user_messages.size() > 10) {
			d_user_messages.remove(0);
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * @return the last user message
	 */
	public UserMessage getLastMessage() {
		int l_idx = d_user_messages.size() - 1;
		if (l_idx < 0) {
			return null;
		}
		return d_user_messages.get(l_idx);
	}

	/**
	 * @return the last user message and clear the message stack. Useful for
	 *         unit testing.
	 */
	public UserMessage getLastMessageAndClear() {
		UserMessage l_um = getLastMessage();
		d_user_messages = new ArrayList<UserMessage>();
		return l_um;
	}
}
