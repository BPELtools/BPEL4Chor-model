package org.bpel4chor.model.topology.impl;

import javax.xml.namespace.QName;

import org.bpel4chor.utils.BPEL4ChorUtil;

/**
 * The representation of participantType
 * <p>
 * It contains {@link #name}, {@link #participantBehaviorDescription}, and
 * {@link #processLanguage}.
 * 
 * @created Oct 6, 2011
 * @author Daojun Cui
 */
public class ParticipantType {

	/**
	 * Attribute name
	 */
	protected String name = null;
	/**
	 * Attribute participantBehaviorDescription
	 */
	protected QName participantBehaviorDescription = null;
	/**
	 * Attribute processLanguage
	 */
	protected String processLanguage = null;

	/**
	 * Constructor
	 */
	public ParticipantType() {

	}

	/**
	 * Constructor
	 */
	public ParticipantType(String participantTypeName) {
		this.name = participantTypeName;
	}

	/**
	 * Constructor
	 */
	public ParticipantType(String participantTypeName, QName participantBehaviorDescription) {
		this.name = participantTypeName;
		this.participantBehaviorDescription = participantBehaviorDescription;
	}

	/**
	 * Constructor
	 */
	public ParticipantType(String name, QName participantBehaviorDescription,
			String processLanguage) {
		this.name = name;
		this.participantBehaviorDescription = participantBehaviorDescription;
		this.processLanguage = processLanguage;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return participantBehaviorDescription
	 */
	public QName getParticipantBehaviorDescription() {
		return this.participantBehaviorDescription;
	}

	/**
	 * @return processLanguage
	 */
	public String getProcessLanguage() {
		return this.processLanguage;
	}

	/**
	 * Set name
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Set participantBehaviorDescription
	 */
	public void setParticipantBehaviorDescription(QName value) {
		this.participantBehaviorDescription = value;
	}

	/**
	 * Set processLanguage
	 */
	public void setProcessLanguage(String value) {
		this.processLanguage = value;
	}

	public String toString() {
		return "ParticipantTypeImpl [name=" + name
				+ ", participantBehaviorDescription="
				+ BPEL4ChorUtil.getString(participantBehaviorDescription)
				+ ", processLanguage=" + processLanguage + "]";
	}

}
