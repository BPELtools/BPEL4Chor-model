package org.bpel4chor.utils;

import java.util.List;

import javax.xml.namespace.QName;

import org.bpel4chor.model.grounding.impl.Grounding;
import org.bpel4chor.model.grounding.impl.ParticipantRef;
import org.bpel4chor.model.topology.impl.MessageLink;
import org.bpel4chor.model.topology.impl.Participant;
import org.bpel4chor.model.topology.impl.ParticipantSet;
import org.bpel4chor.model.topology.impl.ParticipantType;
import org.bpel4chor.model.topology.impl.Topology;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Process;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;

/**
 * Factory for Creating BPEL4Chor Artifacts, such as Topology, Process Behavior
 * Description, and Grounding
 * 
 * @created Oct 6, 2011
 * @author Daojun Cui
 */
public class BPEL4ChorFactory {

	// **************************************************
	// Topology
	// **************************************************

	/**
	 * Create an empty topology
	 * 
	 * @return
	 */
	public static Topology createTopology() {
		return new Topology();
	}

	/**
	 * Create a topology with the name given
	 * 
	 * @param name
	 *            topology name
	 * @return
	 */
	public static Topology createTopology(String name) {

		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("argument is null or empty");

		return new Topology(name);
	}

	/**
	 * Create a topology with name and targetNamespace
	 * 
	 * @param name
	 *            topology name
	 * @param targetNS
	 *            targetNamespace
	 * @return
	 */
	public static Topology createTopology(String name, String targetNS) {

		if (name == null || targetNS == null)
			throw new NullPointerException();

		if (name.isEmpty() || targetNS.isEmpty())
			throw new IllegalArgumentException();

		return new Topology(name, targetNS);
	}

	/**
	 * Create a new ParticipantType
	 * 
	 * @return new participantType
	 */
	public static ParticipantType createParticipantType() {
		return new ParticipantType();
	}

	/**
	 * Create a participantType
	 * 
	 * @param participantTypeName
	 *            ParticipantType Name
	 * @param pbdQName
	 *            participant behavior description
	 * @param processLanguage
	 *            process language
	 * @return new ParticipantType
	 */
	public static ParticipantType createParticipantType(String participantTypeName, QName pbdQName,
			String processLanguage) {

		if (participantTypeName == null || pbdQName == null)
			throw new NullPointerException();

		return new ParticipantType(participantTypeName, pbdQName, processLanguage);

	}

	/**
	 * Create a participantType based on the process given
	 * 
	 * @param process
	 * @return
	 */
	public static ParticipantType createParticipantType(Process process) {
		String participantTypeName = process.getName() + "Type";
		QName pbdQName = new QName(process.getTargetNamespace(), process.getName());
		return new ParticipantType(participantTypeName, pbdQName);
	}

	/**
	 * Create an empty participant
	 * 
	 * @return new Participant
	 */
	public static Participant createParticipant() {
		return new Participant();
	}

	/**
	 * Create a new Participant
	 * 
	 * @param participantName
	 *            participant name
	 * @param participantTypeName
	 *            participant type
	 * @return new participant
	 */
	public static Participant createParticipant(String participantName, String participantTypeName) {

		if (participantName == null || participantTypeName == null)
			throw new NullPointerException();

		return new Participant(participantName, participantTypeName);
	}

	/**
	 * Create a new ParticipantSet
	 * 
	 * @param participantSetName
	 *            participantSet name
	 * @param participantTypeName
	 *            participant type
	 * 
	 * @return new ParticipantSet
	 */
	public static ParticipantSet createParticipantSet(String participantSetName,
			String participantTypeName) {

		if (participantSetName == null || participantTypeName == null)
			throw new NullPointerException();

		return new ParticipantSet(participantSetName, participantTypeName);
	}

	/**
	 * Create a new participantSet
	 * 
	 * @param name
	 *            participantSet name
	 * @param type
	 *            participant type
	 * @param scope
	 *            scope of participant
	 * @param forEach
	 *            for each element
	 * @return
	 */
	public static ParticipantSet createTopologyParticipantSet(String name, String type,
			QName scope, List<QName> forEach) {

		if (name == null || type == null)
			throw new NullPointerException();

		return new ParticipantSet(name, type, scope, forEach);
	}

	/**
	 * Create a new Topology MessageLink
	 * 
	 * @return new MessageLink
	 */
	public static MessageLink createTopologyMessageLink() {
		return new MessageLink();
	}

	/**
	 * Create a new Topology MessageLink with the given parameters
	 * 
	 * @param msgLinkName
	 *            message link name
	 * @param sender
	 *            invoking participant name
	 * @param sendActivity
	 *            invoke activity name
	 * @param receiver
	 *            receiving participant name
	 * @param receiveActivity
	 *            receiving activity name
	 * @param messageName
	 *            message name
	 * @return new MessageLink
	 */
	public static org.bpel4chor.model.topology.impl.MessageLink createTopologyMessageLink(
			String msgLinkName, String sender, String sendActivity, String receiver,
			String receiveActivity, String messageName) {

		if (msgLinkName == null || sender == null || sendActivity == null || receiver == null
				|| receiveActivity == null || messageName == null)
			throw new NullPointerException();

		return new org.bpel4chor.model.topology.impl.MessageLink(msgLinkName, sender, sendActivity,
				receiver, receiveActivity, messageName);

	}

	/**
	 * Create a topology message link that will be given the 'name', 'sender',
	 * 'sendActivity', and 'messageName'.
	 * <p>
	 * Note that the 'receiver' and 'receiveActivity' is still empty.
	 * 
	 * @param message
	 * @param topology
	 * @param sourceProcess
	 * @param sendAct
	 * @return
	 */
	public static org.bpel4chor.model.topology.impl.MessageLink createTopologyMessageLink(
			Message message, Topology topology, Process sourceProcess, Activity sendAct) {

		String msgName = message.getQName().getLocalPart();
		BPEL4ChorNameGenerator nameGen = new BPEL4ChorNameGenerator(topology);

		org.bpel4chor.model.topology.impl.MessageLink topologyMsgLink = createTopologyMessageLink();
		String topoMsgLinkName = nameGen.getUniqueTopoMsgLinkName(msgName + "Link");
		String senderName = sourceProcess.getName();
		String sendActName = sendAct.getName();

		topologyMsgLink.setName(topoMsgLinkName);
		topologyMsgLink.setSender(senderName);
		topologyMsgLink.setSendActivity(sendActName);
		topologyMsgLink.setMessageName(msgName);

		return topologyMsgLink;
	}

	// **************************************************
	// Grounding
	// **************************************************

	/**
	 * Create a new grounding associated to a given topology
	 * 
	 * @return new grounding
	 */
	public static Grounding createGrounding(Topology topology) {
		Grounding grounding = new Grounding(topology);
		return grounding;
	}

	/**
	 * Create a new grounding messageLink
	 * 
	 * @param topoMsgLink
	 *            topology message link
	 * @param portTypeQName
	 *            portType Qname
	 * @param operationName
	 *            operation name
	 * @return
	 */
	public static org.bpel4chor.model.grounding.impl.MessageLink createGroundingMessageLink(
			org.bpel4chor.model.topology.impl.MessageLink topoMsgLink, QName portTypeQName,
			String operationName) {

		if (topoMsgLink == null || portTypeQName == null || operationName == null)
			throw new NullPointerException();

		return new org.bpel4chor.model.grounding.impl.MessageLink(topoMsgLink, portTypeQName,
				operationName);
	}

	/**
	 * Create a new grounding messageLink
	 * 
	 * @param topoMsgLink
	 * @param portType
	 * @param operation
	 * @return
	 */
	public static org.bpel4chor.model.grounding.impl.MessageLink createGroundingMessageLink(
			org.bpel4chor.model.topology.impl.MessageLink topoMsgLink, PortType portType,
			Operation operation) {

		if (topoMsgLink == null || portType == null || operation == null)
			throw new NullPointerException();
		return new org.bpel4chor.model.grounding.impl.MessageLink(topoMsgLink, portType.getQName(),
				operation.getName());
	}

	/**
	 * Create a new grounding participantRef
	 * 
	 * @param name
	 *            participant name
	 * @param wSDLProperty
	 *            wsdl property QName
	 * @return
	 */
	public static ParticipantRef createGroundingParticipantRef(String name, QName wSDLProperty) {

		if (name == null || wSDLProperty == null)
			throw new NullPointerException();
		return new ParticipantRef(name, wSDLProperty);
	}

	/**
	 * Create a new grounding WSDLproperty
	 * 
	 * @param name
	 * @param wSDLProperty
	 * @return
	 */
	public static org.bpel4chor.model.grounding.impl.Property createGroundingProperty(String name,
			QName wSDLProperty) {
		if (name == null || wSDLProperty == null)
			throw new NullPointerException();
		return new org.bpel4chor.model.grounding.impl.Property(name, wSDLProperty);
	}
}
