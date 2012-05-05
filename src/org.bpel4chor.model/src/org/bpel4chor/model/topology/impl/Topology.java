/**
 * 
 */
package org.bpel4chor.model.topology.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.bpel4chor.interfaces.INamespaceMap;
import org.bpel4chor.utils.BPEL4ChorConstants;
import org.bpel4chor.utils.BPEL4ChorNamespaceMap;

/**
 * The representation of BPEL4Chor Topology
 * 
 * <p>
 * If any unknown namespaceUri of a QName appears in the newly added element, it
 * will be propagated to the namespaceMap of topology.
 * 
 * <p>
 * As default the prefix blank ("") will be assigned to
 * "http://docs.oasis-open.org/wsbpel/2.0/process/abstract"
 * 
 * @created Oct 5, 2011
 * @author Daojun Cui
 */
public class Topology {

	/**
	 * Attribute name
	 */
	protected String name;

	/**
	 * targetNamespace of topology, default value is
	 * {@link BPEL4ChorConstants#TOPOLOGY_TARGET_NAMESPACE}
	 */
	protected String targetNamespace;

	/**
	 * Namespace Map of (Prefix, Namespace) and also holds the reverse map of
	 * (Namespace, Prefix)
	 */
	protected INamespaceMap<String, String> namespaceMap;

	/**
	 * Extra Attribute Map of (K, V) and the reverse map of (v, K), In case of
	 * any attribute that is not namespaceUri should be added to topology, it
	 * can be inserted here.
	 */
	protected INamespaceMap<String, String> extAttributes;

	/**
	 * The topology element 'participantTypes'
	 */
	protected List<ParticipantType> participantTypes;

	/**
	 * The topology element 'participants' (&lt; participants / &gt;)
	 */
	protected List<Participant> participants = null;

	/**
	 * The topology element 'participantSet' (&lt; participantSets / &gt;)
	 */
	protected List<ParticipantSet> participantSets = null;

	/**
	 * The topology element 'messageLinks'
	 */
	protected List<MessageLink> messageLinks = null;

	private Logger logger = Logger.getLogger(Topology.class);

	/**
	 * Constructor
	 */
	public Topology() {
		this.participantTypes = new ArrayList<ParticipantType>();
		this.participants = new ArrayList<Participant>();
		this.participantSets = new ArrayList<ParticipantSet>();
		this.messageLinks = new ArrayList<MessageLink>();
		this.namespaceMap = new BPEL4ChorNamespaceMap();
		this.extAttributes = new BPEL4ChorNamespaceMap();
		// this.namespaceMap.put("", BPEL4ChorConstants.TOPOLOGY_XMLNS);//
		// assign "" as prefix to default namespace
		this.targetNamespace = BPEL4ChorConstants.TOPOLOGY_TARGET_NAMESPACE;
	}

	/**
	 * Constructor with name and targetNamespace
	 * 
	 * @param name
	 * @param targetNamespace
	 */
	public Topology(String name) {
		this.name = name;
		this.targetNamespace = BPEL4ChorConstants.TOPOLOGY_TARGET_NAMESPACE;
		this.participantTypes = new ArrayList<ParticipantType>();
		this.participants = new ArrayList<Participant>();
		this.participantSets = new ArrayList<ParticipantSet>();
		this.messageLinks = new ArrayList<MessageLink>();
		this.namespaceMap = new BPEL4ChorNamespaceMap();
		this.extAttributes = new BPEL4ChorNamespaceMap();
	}

	/**
	 * Constructor with name and targetNamespace
	 * 
	 * @param name
	 * @param targetNamespace
	 */
	public Topology(String name, String targetNamespace) {
		this.name = name;
		this.targetNamespace = targetNamespace;
		this.participantTypes = new ArrayList<ParticipantType>();
		this.participants = new ArrayList<Participant>();
		this.participantSets = new ArrayList<ParticipantSet>();
		this.messageLinks = new ArrayList<MessageLink>();
		this.namespaceMap = new BPEL4ChorNamespaceMap();
		this.extAttributes = new BPEL4ChorNamespaceMap();
	}

	/**
	 * @return messageLinks
	 */
	public List<MessageLink> getMessageLinks() {
		return this.messageLinks;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return participantTypes
	 */
	public List<ParticipantType> getParticipantTypes() {
		return this.participantTypes;
	}

	/**
	 * @return participants
	 * 
	 */
	public List<Participant> getParticipants() {
		return this.participants;
	}

	/**
	 * @return targetNamespace
	 */
	public String getTargetNamespace() {
		return this.targetNamespace;
	}

	/**
	 * Set messageLinks
	 */
	public void setMessageLinks(List<MessageLink> msgLinks) {
		this.messageLinks = msgLinks;
	}

	/**
	 * Set name
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Set participantTypes
	 */
	public void setParticipantTypes(List<ParticipantType> participantTypes) {
		this.participantTypes = participantTypes;
	}

	/**
	 * set participants
	 */
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public List<ParticipantSet> getParticipantSets() {
		return this.participantSets;
	}

	public void setParticipantSets(List<ParticipantSet> participantSets) {
		this.participantSets = participantSets;
	}

	/**
	 * Set targetNamespace
	 */
	public void setTargetNamespace(String targetNS) {
		this.targetNamespace = targetNS;
	}

	public void add(MessageLink ml) {
		this.messageLinks.add(ml);
	}

	public void add(ParticipantType pt) {

		if (this.contains(pt)) {
			logger.warn("Unable to add ParticpiantType<" + pt.getName()
					+ ">. Duplicate participantType:" + pt.toString());
			return;
		}

		// 1. collect the namespaceUri and compare with the namespaceUri in
		// topology root,
		// if it is unknown namespaceUri, fill it in the namespaceMap of the
		// topology.
		this.collectNamespace(pt);

		// 2. add to the list
		this.participantTypes.add(pt);
	}

	public void add(Participant p) {

		if (this.contains(p)) {
			logger.error("Unable to add Participant <" + p.getName()
					+ ">. Duplicate participant:" + p.toString());
			return;
		}

		// 1. collect the namespaceUri and compare with the namespaceUri in
		// topology root,
		// if it does not exists, fill it in the namespaceMap of the topology.
		this.collectNamespace(p);

		// 2. add to the list
		this.participants.add(p);

	}

	public void add(ParticipantSet ps) throws Exception {

		if (this.contains(ps)) {
			logger.error("Unable to add participantSet<" + ps.getName()
					+ ">. Duplicate ParticipantSet:" + ps.toString());
			return;
		}

		// 1. collect the namespaceUri and compare with the namespaceUri in
		// topology root,
		// if it does not exists, fill it in the moreAttribute of the
		// topology.
		this.collectNamespace(ps);

		// 2. add to the list
		this.participantSets.add(ps);

	}

	public INamespaceMap<String, String> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(INamespaceMap<String, String> map) {
		this.namespaceMap = map;
	}

	public INamespaceMap<String, String> getExtAttributes() {
		return this.extAttributes;
	}

	public void setExtAttributes(INamespaceMap<String, String> map) {
		this.extAttributes = map;
	}

	/**
	 * Collect the unknown Namespace in the QName of the participantType.
	 * 
	 * @param pt
	 *            participantType
	 */
	protected void collectNamespace(ParticipantType pt) {
		if (pt == null)
			throw new NullPointerException("argument is null.");

		if (pt.getParticipantBehaviorDescription() != null) {
			QName qName = this.namespaceMap.collectNamespace(pt
					.getParticipantBehaviorDescription());
			pt.setParticipantBehaviorDescription(qName);
		}

	}

	/**
	 * Collect the unknown Namespace in the QName of the participant.
	 * 
	 * @param p
	 *            participant
	 */
	protected void collectNamespace(Participant p)  {
		if (p == null)
			throw new NullPointerException("argument is null.");

		QName qName;
		if (p.getScope() != null) {
			qName = this.namespaceMap.collectNamespace(p.getScope());
			p.setScope(qName);
		}
		if (!p.getForEach().isEmpty()) {
			List<QName> currentQNames = p.getForEach();
			List<QName> resQNames = new ArrayList<QName>();
			for (int i = 0; i < currentQNames.size(); i++) {
				qName = this.namespaceMap
						.collectNamespace(currentQNames.get(i));
				resQNames.add(qName);
			}
			p.setForEach(resQNames);
		}
	}

	/**
	 * Collect the unknown Namespace in the QName of the participantSet.
	 * 
	 * @param pt
	 *            participantSet
	 */
	protected void collectNamespace(ParticipantSet ps)  {
		if (ps == null)
			throw new NullPointerException("argument is null");

		QName qName;

		// scope
		if (ps.getScope() != null) {
			qName = this.namespaceMap.collectNamespace(ps.getScope());
			ps.setScope(qName);
		}
		// forEach
		if (ps.getForEach() != null && !ps.getForEach().isEmpty()) {
			List<QName> currentQNames = ps.getForEach();
			List<QName> resQNames = new ArrayList<QName>();
			for (int i = 0; i < currentQNames.size(); i++) {
				qName = this.namespaceMap
						.collectNamespace(currentQNames.get(i));
				resQNames.add(qName);
			}
			ps.setForEach(resQNames);
		}
		// participant list
		if (!ps.getParticipantList().isEmpty()) {
			for (Participant p : ps.getParticipantList()) {
				this.collectNamespace(p);
			}
		}
		// participantSet list
		if (!ps.getParticipantSetList().isEmpty()) {
			for (ParticipantSet subPS : ps.getParticipantSetList()) {
				this.collectNamespace(subPS);
			}
		}

	}

	/**
	 * If the participantType.name is the same, it is considered to have been
	 * existed.
	 */
	protected boolean contains(ParticipantType pt) {
		if (pt == null)
			throw new NullPointerException("argument is null.");

		for (ParticipantType partType : this.participantTypes) {
			if (partType.getName().equals(pt.getName())) {
				return true;
			}
			if (partType.getParticipantBehaviorDescription().equals(
					pt.getParticipantBehaviorDescription())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * If the participant.name is the same, it is considered to have been
	 * existed.
	 * 
	 * @param p
	 * @return
	 */
	protected boolean contains(Participant p) {
		if (p == null)
			throw new NullPointerException("argument is null.");

		for (Participant child : this.participants) {
			if (child.getName().equals(p.getName()))
				return true;
		}
		return false;
	}

	/**
	 * If the participantSet.name is the same, it is considered to have been
	 * existed.
	 * 
	 * @param ps
	 * @return
	 */
	protected boolean contains(ParticipantSet ps) {
		if (ps == null)
			throw new NullPointerException("argument is null.");

		for (ParticipantSet childPS : this.participantSets) {
			if (childPS.getName().equals(ps.getName()))
				return true;
		}
		return false;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Topology [");
		sb.append(name);
		sb.append(" targetNamespace=");
		sb.append(targetNamespace);
		sb.append(", participantTypes=");
		sb.append(participantTypes);
		sb.append(", participants=");
		sb.append(participants);
		sb.append(", messageLinks=");
		sb.append(messageLinks);
		sb.append("]");
		return sb.toString();
	}
}
