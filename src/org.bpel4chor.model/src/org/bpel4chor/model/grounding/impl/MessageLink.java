package org.bpel4chor.model.grounding.impl;

import javax.xml.namespace.QName;

/**
 * The representation of messageLink in BPEL4Chor grounding
 *
 * @since Oct 22, 2011
 * @author Daojun.Cui
 */
public class MessageLink {
	
	/** the associated messageLink in topology */
	protected org.bpel4chor.model.topology.impl.MessageLink refMsgLink;
	
	/** port type */
	protected QName portType;
	
	/** operation */
	protected String operation;

	/**
	 * Constructor
	 * @param refMsgLink
	 * @param portTypeQName
	 * @param operationName
	 */
	public MessageLink(org.bpel4chor.model.topology.impl.MessageLink refMsgLink, 
			QName portTypeQName, String operationName)
	{
		this.refMsgLink = refMsgLink;
		this.portType = portTypeQName;
		this.operation = operationName;
	}

	public String getName() {
		return this.refMsgLink.getName();
	}

	public QName getPortType() {
		return portType;
	}

	public void setPortType(QName portType) {
		this.portType = portType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String toString() {
		return "MessageLinkImpl [name=" + this.getName() + ", portType=" + this.portType
				+ ", operation=" + this.operation + "]";
	}

	
}
