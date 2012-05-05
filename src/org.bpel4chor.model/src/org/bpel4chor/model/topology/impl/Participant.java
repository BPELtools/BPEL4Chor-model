package org.bpel4chor.model.topology.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.bpel4chor.utils.BPEL4ChorConstants;
import org.bpel4chor.utils.BPEL4ChorUtil;
import org.bpel4chor.utils.ContainmentValue;

/**
 * The representation of participant in topology
 * <p>
 * It contains {@link #name}, {@link #type}, {@link #selects}, {@link #scope},
 * {@link #forEach}, and {@link #containment}.
 * 
 * @created Oct 6, 2011
 * @author Daojun Cui
 */
public class Participant {

	/**
	 * Participant name
	 */
	protected String name;
	/**
	 * Type of this participant
	 */
	protected String type;
	/**
	 * The participants that are invoked 
	 */
	protected List<String> selects;
	/**
	 * Attribute scope
	 */
	protected QName scope;
	/**
	 * Attribute forEach
	 */
	protected List<QName> forEach;
	/**
	 * Attribute containment
	 */
	protected String containment;

	/**
	 * Constructor
	 */
	public Participant() {
		this.forEach = new ArrayList<QName>();
		this.selects = new ArrayList<String>();
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param type
	 */
	public Participant(String name, String type) {
		this.name = name;
		this.type = type;
		this.forEach = new ArrayList<QName>();
		this.selects = new ArrayList<String>();
	}

	/**
	 * @return containment
	 * @see org.bpel4chor.model.topology.Participant#getContainment()
	 */
	public String getContainment() {
		return this.containment;
	}

	/**
	 * @return forEach
	 * @see org.bpel4chor.model.topology.Participant#getForEach()
	 */
	public List<QName> getForEach() {
		return this.forEach;
	}

	/**
	 * @return name
	 * @see org.bpel4chor.model.topology.Participant#getName()
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return scope
	 * @see org.bpel4chor.model.topology.Participant#getScope()
	 */
	public QName getScope() {
		return this.scope;
	}

	/**
	 * @return selects
	 * @see org.bpel4chor.model.topology.Participant#getSelects()
	 */
	public List<String> getSelects() {
		return this.selects;
	}

	/**
	 * @return type
	 * @see org.bpel4chor.model.topology.Participant#getType()
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Set containment
	 * 
	 * @see org.bpel4chor.model.topology.Participant#setContainment(java.lang.String)
	 */
	public void setContainment(ContainmentValue value) {
		switch (value) {
		case ADD_IF_NOT_EXISTS:
			this.containment = BPEL4ChorConstants.CONTAINMENT_ADD_IF_NOT_EXISTS;
			break;
		case MUST_ADD:
			this.containment = BPEL4ChorConstants.CONTAINMENT_MUST_ADD;
			break;
		case REQUIRED:
			this.containment = BPEL4ChorConstants.CONTAINMENT_REQUIRED;
		default:
		}
	}

	/**
	 * Set forEach
	 * 
	 * @see org.bpel4chor.model.topology.Participant#setForEach(java.lang.String)
	 */
	public void setForEach(List<QName> value) {
		this.forEach = value;
	}

	/**
	 * Set name
	 * 
	 * @see org.bpel4chor.model.topology.Participant#setName(java.lang.String)
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Set scope
	 * 
	 * @see org.bpel4chor.model.topology.Participant#setScope(java.lang.String)
	 */
	public void setScope(QName value) {
		this.scope = value;
	}

	/**
	 * Set scope with prefix:localname:namespaceUri
	 */
	public void setScope(String prefix, String localName, String namespaceUri) {
		this.scope = new QName(namespaceUri, localName, prefix);
	}

	/**
	 * Set selects
	 * 
	 * @see org.bpel4chor.model.topology.Participant#setSelects(java.lang.String)
	 */
	public void setSelects(List<String> value) {
		this.selects = value;
	}

	/**
	 * Set type
	 * 
	 * @see org.bpel4chor.model.topology.Participant#setType(java.lang.String)
	 */
	public void setType(String value) {
		this.type = value;
	}

	public String toString() {
		return "ParticipantImpl [name=" + name + ", type=" + type
				+ ", selects=" + selects + ", scope="
				+ (scope != null ? BPEL4ChorUtil.getString(scope) : "")
				+ ", forEach=" + forEach + ", containment=" + containment + "]";
	}

}
