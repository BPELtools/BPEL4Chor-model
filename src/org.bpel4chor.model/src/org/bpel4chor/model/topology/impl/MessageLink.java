/**
 * 
 */
package org.bpel4chor.model.topology.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * The representation for Topology MessageLink
 * <p>
 * It contains the information for {@link #name}, {@link #sender},
 * {@link #sendActivity}, {@link #receiver}, and {@link #receiveActivity}.
 * 
 * @created Oct 6, 2011
 * @author Daojun Cui
 */
public class MessageLink {

	/**
	 * Attribute name
	 */
	protected String name;
	/**
	 * Attribute messageName
	 */
	protected String messageName;
	/**
	 * Attribute sender
	 */
	protected String sender;
	/**
	 * Attribute senders
	 */
	protected List<String> senders;
	/**
	 * Attribute sendActivity
	 */
	protected String sendActivity;
	/**
	 * Attribute receiver
	 */
	protected String receiver;
	/**
	 * Attribute receiveActivity
	 */
	protected String receiveActivity;
	/**
	 * Attribute bindSenderTo
	 */
	protected String bindSenderTo;
	/**
	 * Attribute participantRefs
	 */
	protected List<String> participantRefs;
	/**
	 * Attribue copyParticipantRefsTo
	 */
	protected List<String> copyParticipantRefsTo;

	/**
	 * Constructor
	 */
	public MessageLink() {
		this.senders = new ArrayList<String>();
		this.participantRefs = new ArrayList<String>();
		this.copyParticipantRefsTo = new ArrayList<String>();
	}

	/**
	 * Constructor
	 */
	public MessageLink(String name, String sender, String sendActivity,
			String receiver, String receiveActivity, String messageName) {
		this.name = name;
		this.sender = sender;
		this.sendActivity = sendActivity;
		this.receiver = receiver;
		this.receiveActivity = receiveActivity;
		this.messageName = messageName;
		this.senders = new ArrayList<String>();
		this.participantRefs = new ArrayList<String>();
		this.copyParticipantRefsTo = new ArrayList<String>();
	}

	/**
	 * @return bindSenderTo
	 */
	public String getBindSenderTo() {
		return this.bindSenderTo;
	}

	/**
	 * @return copyParticipantRefsTo
	 */
	public List<String> getCopyParticipantRefsTo() {
		return this.copyParticipantRefsTo;
	}

	/**
	 * @return messageName
	 */
	public String getMessageName() {
		return this.messageName;
	}

	/**
	 * @return name
	 */
	public String getName() {
		if (this.name != null && !this.name.isEmpty())
			return this.name;
		else
			return this.messageName;// default: messageName
	}

	/**
	 * @return participantRefs
	 */
	public List<String> getParticipantRefs() {
		return this.participantRefs;
	}

	/**
	 * @return receiveActivity
	 */
	public String getReceiveActivity() {
		return this.receiveActivity;
	}

	/**
	 * @return receiver
	 */
	public String getReceiver() {
		return this.receiver;
	}

	/**
	 * @return sendActivity
	 */
	public String getSendActivity() {
		return sendActivity;
	}

	/**
	 * @return sender
	 */
	public String getSender() {
		return this.sender;
	}

	/**
	 * @return senders
	 */
	public List<String> getSenders() {
		return this.senders;
	}

	/**
	 * Set bindSenderTo
	 */
	public void setBindSenderTo(String value) {
		this.bindSenderTo = value;
	}

	/**
	 * Set copyParticipantRefsTo
	 */
	public void setCopyParticipantRefsTo(List<String> value) {
		this.copyParticipantRefsTo = value;
	}

	/**
	 * Set messageName
	 */
	public void setMessageName(String value) {
		this.messageName = value;
	}

	/**
	 * Set name
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Set participantRefs
	 */
	public void setParticipantRefs(List<String> value) {
		this.participantRefs = value;
	}

	/**
	 * Set receiveActivity
	 */
	public void setReceiveActivity(String value) {
		this.receiveActivity = value;
	}

	/**
	 * Set receiver
	 */
	public void setReceiver(String value) {
		this.receiver = value;
	}

	/**
	 * Set sendActivity
	 */
	public void setSendActivity(String value) {
		this.sendActivity = value;
	}

	/**
	 * Set sender
	 */
	public void setSender(String value) {
		this.sender = value;
	}

	/**
	 * Set senders
	 */
	public void setSenders(List<String> value) {
		this.senders = value;
	}

}
