package org.bpel4chor.model.topology.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.bpel4chor.utils.BPEL4ChorUtil;


/**
 * The representation of participantSet
 *
 * @created Oct 6, 2011
 * @author Daojun Cui
 */
public class ParticipantSet {

	/**
	 * Attribute name
	 */
	protected String name;
	/**
	 * Attribute type
	 */
	protected String type;
	/**
	 * Attribute scope
	 */
	protected QName scope;
	/**
	 * Attribute forEach
	 */
	protected List<QName> forEach;
	/**
	 * sub participant list
	 */
	protected List<Participant> participantList;
	/**
	 * sub participantSet list
	 */
	protected List<ParticipantSet> participantSetList;

	
	/**
	 * Constructor
	 */
	public ParticipantSet(String name, String type){
		this.name = name;
		this.type = type;
		this.participantList = new ArrayList<Participant>();
		this.participantSetList = new ArrayList<ParticipantSet>();
		this.forEach = new ArrayList<QName>();
	}

	/**
	 * Constructor
	 * @param name
	 * @param type
	 * @param scope
	 * @param forEach
	 */
	public ParticipantSet(String name, String type, QName scope, List<QName> forEach)
	{
		this.name = name;
		this.type = type;
		this.scope = scope;
		this.forEach = forEach;
		this.participantList = new ArrayList<Participant>();
		this.participantSetList = new ArrayList<ParticipantSet>();
	}
	
	/**
	 * add a participant
	 */
	public void add(Participant p){
		this.participantList.add(p);
	}
	
	/**
	 * add a participantSet
	 */
	public void add(ParticipantSet ps){
		this.participantSetList.add(ps);
	}
	
	/**
	 * @return forEach
	 */
	public List<QName> getForEach() {
		return this.forEach;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return participantList
	 */
	public List<Participant> getParticipantList() {
		return this.participantList;
	}

	/**
	 * @return participantSetList
	 */
	public List<ParticipantSet> getParticipantSetList() {
		return this.participantSetList;
	}

	/**
	 * @return scope
	 */
	public QName getScope() {
		return this.scope;
	}

	/**
	 * @return type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Set forEach
	 */
	public void setForEach(List<QName> value) {
		this.forEach = value;
	}

	/**
	 * Set name
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Set participantList
	 */
	public void setParticipantList(List<Participant> value) {
		this.participantList = value;
	}

	/**
	 * Set participantSetList
	 */
	public void setParticipantSetList(List<ParticipantSet> value) {
		this.participantSetList = value;
	}

	/**
	 * Set scope 
	 */
	public void setScope(QName value) {
		this.scope = value;
	}

	/**
	 * set type
	 */
	public void setType(String value) {
		this.type = value;
	}



	public String toString() {
		return "ParticipantSetImpl [name=" + name + ", type=" + type
				+ ", scope=" + BPEL4ChorUtil.getString(scope) + ", forEach=" + forEach
				+ ", participantList=" + participantList
				+ ", participantSetList=" + participantSetList + "]";
	}

	
	
}
