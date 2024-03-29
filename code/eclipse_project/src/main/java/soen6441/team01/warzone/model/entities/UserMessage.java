package soen6441.team01.warzone.model.entities;

import java.io.Serializable;

import soen6441.team01.warzone.common.entities.MsgType;

/**
 * Entity class (purpose is to hold data, i.e. basic data structure) in support
 * of user messages
 *
 */
public class UserMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	public MsgType d_message_type = MsgType.None;
	public String d_message = "";

	/**
	 * basic constructor. message type and message will need to be provided later.
	 * 
	 */
	public UserMessage() {
	}

	/**
	 * constructor that initializes the primary attributes
	 * 
	 * @param p_message_type the type (severity) of message
	 * @param p_message      the message to present to the user
	 */
	public UserMessage(MsgType p_message_type, String p_message) {
		setMessageType(p_message_type);
		setMessage(p_message);
	}

	/**
	 * sets the message type (ie severity)
	 * 
	 * @param p_message_type the message severity to set to
	 */
	public void setMessageType(MsgType p_message_type) {
		d_message_type = p_message_type;
	}

	/**
	 * gets the message type or severity of the user message
	 * @return the message type or severity of the user message
	 */
	public MsgType getMessageType() {
		return d_message_type;
	}
	
	/**
	 * Sets the user message text
	 * 
	 * @param p_message the user message text to set the message to
	 */
	public void setMessage(String p_message) {
		d_message = p_message;
	}
	
	/**
	 * retrieves the user text message
	 * @return the user text message
	 */
	public String getMessage() {
		return d_message;
	}
}
