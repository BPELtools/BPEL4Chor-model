package org.bpel4chor.model.grounding.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.bpel4chor.interfaces.INamespaceMap;
import org.bpel4chor.model.topology.impl.Topology;
import org.bpel4chor.utils.BPEL4ChorConstants;
import org.bpel4chor.utils.BPEL4ChorNamespaceMap;

/**
 * The representation of BPEL4Chor Grounding
 * 
 * <p>
 * The topology attribute, which is a QName, is from the referenced topology.
 * and like Topology, The grounding namespaceMap can also collect the namespaces
 * from the new QName element.
 * 
 * @since Oct 22, 2011
 * @author Daojun Cui
 */
public class Grounding {

	/** associated topology */
	protected Topology topology;

	/** qualified name of the topology */
	protected QName topologyQName;

	/** the targetNamespace of grounding */
	protected String targetNamespace;

	/** messageLinks */
	protected List<MessageLink> messageLinks;

	/** participantRefs */
	protected List<ParticipantRef> participantRefs;

	/** properties */
	protected List<Property> properties;

	/**
	 * Namespace Map of (Prefix, NamespaceURI) and also holds the reverse map of
	 * (Namespace, Prefix)
	 */
	protected INamespaceMap<String, String> namespaceMap;

	/**
	 * Extra Attribute Map of (K, V) and the reverse map of (v, K), In case of
	 * any attribute that is not namespaceUri should be added to grounding, it
	 * can be inserted here.
	 */
	protected INamespaceMap<String, String> extAttributes;

	private Logger logger = Logger.getLogger(Grounding.class);

	/**
	 * Constructor
	 * 
	 * @param topology
	 */
	public Grounding(Topology topology) {
		this.topology = topology;
		this.messageLinks = new ArrayList<MessageLink>();
		this.participantRefs = new ArrayList<ParticipantRef>();
		this.properties = new ArrayList<Property>();
		this.namespaceMap = new BPEL4ChorNamespaceMap();
		this.extAttributes = new BPEL4ChorNamespaceMap();
		// this.namespaceMap.put("", BPEL4ChorConstants.GROUNDING_XMLNS);
		this.targetNamespace = BPEL4ChorConstants.GOURNDING_TARGET_NAMESPACE;
		this.topologyQName = new QName(topology.getTargetNamespace(), // topology
																		// target
																		// namespaceURI
				topology.getName(), // local part
				BPEL4ChorConstants.TOPOLOGY_PREFIX_BASE);// prefix

		// collect name space

		QName qName = this.namespaceMap.collectNamespace(this.topologyQName);
		this.topologyQName = qName;

	}

	public QName getTopology() {
		return this.topologyQName;
	}

	public void setTopology(QName tQName) {
		this.topologyQName = tQName;
	}

	public List<MessageLink> getMessageLinks() {
		return messageLinks;
	}

	public void setMessageLinks(List<MessageLink> messageLinks) {
		this.messageLinks = messageLinks;
	}

	public List<ParticipantRef> getParticipantRefs() {
		return participantRefs;
	}

	public void setParticipantRefs(List<ParticipantRef> participantRefs) {
		this.participantRefs = participantRefs;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("GroundingImpl [topology=");
		sb.append(topology.getName());
		sb.append(", messageLinks=");
		sb.append(messageLinks);
		sb.append(", participantRefs=");
		sb.append(participantRefs);
		sb.append(", properties=");
		sb.append(properties);
		sb.append("]");
		return sb.toString();
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public INamespaceMap<String, String> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(INamespaceMap<String, String> namespaceMap) {
		this.namespaceMap = namespaceMap;
	}

	public INamespaceMap<String, String> getExtAttributes() {
		return extAttributes;
	}

	public void setExtAttributes(INamespaceMap<String, String> extAttributes) {
		this.extAttributes = extAttributes;
	}

	public void add(MessageLink msgLink) {

		if (contains(msgLink)) {
			logger.warn("MessageName existed, cannot add messagelink:" + msgLink);
			return;
		}

		if (msgLink.getPortType() != null) {
			QName resQName = this.namespaceMap.collectNamespace(msgLink.getPortType());
			msgLink.setPortType(resQName);
		}

		this.messageLinks.add(msgLink);

	}

	public void add(ParticipantRef partRef) {
		if (this.contains(partRef)) {
			logger.warn("ParticipantRef Name existed, cannot add ParticipantRef:" + partRef);
			return;
		}

		if (partRef.getWSDLproperty() != null) {
			QName resQName = this.namespaceMap.collectNamespace(partRef.getWSDLproperty());
			partRef.setWSDLproperty(resQName);
		}

		this.participantRefs.add(partRef);

	}

	public void add(Property property) {
		if (this.contains(property)) {
			logger.warn("Property existed, cannot add Property:" + property);
			return;
		}
		if (property.getWSDLproperty() != null) {
			QName resQName = this.namespaceMap.collectNamespace(property.getWSDLproperty());
			property.setWSDLproperty(resQName);
		}

		this.properties.add(property);
	}

	/**
	 * If the message name is same, it will be considered that it have existed.
	 * 
	 * @param msgL
	 * @return true if the message names equals
	 */
	public boolean contains(MessageLink msgL) {
		if (msgL == null)
			return false;

		for (MessageLink ml : this.messageLinks) {
			if (ml.getName().equals(msgL.getName()))
				return true;
		}
		return false;
	}

	/**
	 * Whether the grounding has contained the participantRef.
	 * 
	 * <p>
	 * The same name will be considered to be such.
	 * 
	 * @param pRef
	 * @return
	 */
	public boolean contains(ParticipantRef pRef) {
		if (pRef == null)
			return false;

		for (ParticipantRef pr : this.participantRefs) {
			if (pr.getName().equals(pRef.getName()))
				return true;
		}

		return false;
	}

	/**
	 * Whether the grounding has contained the property.
	 * 
	 * <p>
	 * The same name will be considered to be such.
	 * 
	 * @param prop
	 * @return if the WSDL property qname is equal then return 'true', otherwise
	 *         'false'.
	 */
	public boolean contains(Property prop) {
		if (prop == null)
			return false;

		for (Property pr : this.properties) {
			if (pr.getWSDLproperty().equals(prop.getWSDLproperty()))
				return true;
		}

		return false;
	}

}
