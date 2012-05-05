package org.bpel4chor.model.grounding.impl;

import javax.xml.namespace.QName;

/**
 * The representation of participantRef in the BPEL4Chor grounding
 * 
 *
 * @since Oct 22, 2011
 * @author Daojun.Cui
 */
public class ParticipantRef {

	/** participantRef Name */
	protected String name;
	
	/** WSDL Property */
	protected QName WSDLproperty;

	/**
	 * Constructor
	 * @param name
	 * @param wSDLProperty
	 */
	public ParticipantRef(String name, QName wSDLProperty)
	{
		this.name = name;
		this.WSDLproperty = wSDLProperty;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QName getWSDLproperty() {
		return WSDLproperty;
	}

	public void setWSDLproperty(QName wSDLproperty) {
		WSDLproperty = wSDLproperty;
	}


	public String toString() {
		return "ParticipantRefImpl [name=" + name + ", WSDLproperty="
				+ WSDLproperty + "]";
	}

	
}
