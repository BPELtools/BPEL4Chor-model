package org.bpel4chor.utils;

import java.util.List;

import javax.xml.namespace.QName;

import org.bpel4chor.model.grounding.impl.Grounding;
import org.bpel4chor.model.topology.impl.Topology;

/**
 * Utility Class
 * 
 * <p>
 * <b>changeLog date user remark</b> <br>
 * 
 * @001 2011-10-27 DC initial version
 * 
 * @since Oct 27, 2011
 * @author Daojun Cui
 */
public class BPEL4ChorUtil {
	/**
	 * QName prefix==null namespace==null
	 */
	public final static int QNAME_PREFIX_NAMESPACE_00 = 0;
	/**
	 * QName prefix==null namespace!=null
	 */
	public final static int QNAME_PREFIX_NAMESPACE_01 = 1;
	/**
	 * QName prefix!=null namespace==null
	 */
	public final static int QNAME_PREFIX_NAMESPACE_10 = 2;
	/**
	 * QName prefix!=null namespace!=null
	 */
	public final static int QNAME_PREFIX_NAMESPACE_11 = 3;

	/**
	 * Analyse which part in qName is null and which not null.
	 * 
	 * @return <li>0 if prefix == null, namespace == null <li>1 if prefix ==
	 *         null, namespace != null <li>2 if prefix != null, namespace ==
	 *         null <li>3 if prefix != null, namespace != null <li>-1 otherwise
	 */
	public static int analyseQName(QName qName) {
		if (qName.getLocalPart() == null || qName.getLocalPart().isEmpty())
			throw new IllegalArgumentException("illegal argument, qName.getLocalPart()==null:"
					+ (qName.getLocalPart() == null) + " or empty");

		boolean prefixIsNull = (qName.getPrefix() == null || qName.getPrefix().isEmpty());
		boolean nsIsNull = (qName.getNamespaceURI() == null || qName.getNamespaceURI().isEmpty());

		// 1. prefix == null, namespace == null
		if (prefixIsNull && nsIsNull)
			return QNAME_PREFIX_NAMESPACE_00;

		// 2. prefix == null, namespace != null
		if (prefixIsNull && !nsIsNull)
			return QNAME_PREFIX_NAMESPACE_01;

		// 3. prefix != null, namespace == null
		if (!prefixIsNull && nsIsNull)
			return QNAME_PREFIX_NAMESPACE_10;

		// 4. prefix != null, namespace != null
		if (!prefixIsNull && !nsIsNull)
			return QNAME_PREFIX_NAMESPACE_11;

		return -1;
	}

	/**
	 * Get the full string of qname
	 * 
	 * @param qName
	 * @return the string of namespaceUri + localtPart + prefix
	 */
	public static String getString(QName qName) {
		if (qName == null)
			throw new NullPointerException("argument is null");

		StringBuffer sb = new StringBuffer();
		sb.append("NamespaceUri:" + qName.getNamespaceURI() + " ");
		sb.append("LocalPart:" + qName.getLocalPart() + " ");
		sb.append("Prefix:" + qName.getPrefix());
		return sb.toString();
	}

	/**
	 * Resolve the message link in the topology with the message link name given
	 * 
	 * @param topology
	 * @param messageLinkName
	 * @return the found messageLink or null
	 */
	public static org.bpel4chor.model.topology.impl.MessageLink resolveTopologyMessageLinkByName(
			Topology topology, String messageLinkName) {

		if (topology == null || messageLinkName == null)
			throw new NullPointerException();

		List<org.bpel4chor.model.topology.impl.MessageLink> msgLinks = topology.getMessageLinks();

		for (org.bpel4chor.model.topology.impl.MessageLink msgLink : msgLinks) {
			if (msgLink.getName().equals(messageLinkName))
				return msgLink;
		}
		return null;
	}

	/**
	 * Resolve the message link in the topology with the sendActivity given
	 * 
	 * @param topology
	 * @param sendAct
	 * @return
	 */
	public static org.bpel4chor.model.topology.impl.MessageLink resolveTopologyMessageLinkBySendAct(
			Topology topology, String sendAct) {
		if (topology == null || sendAct == null)
			throw new NullPointerException();

		List<org.bpel4chor.model.topology.impl.MessageLink> msgLinks = topology.getMessageLinks();

		for (org.bpel4chor.model.topology.impl.MessageLink msgLink : msgLinks) {
			if (msgLink.getSendActivity().equals(sendAct))
				return msgLink;
		}
		return null;
	}

	/**
	 * Resolve the message link in the grounding with the name given
	 * 
	 * @param grounding
	 * @param msgLinkName
	 * @return
	 */
	public static org.bpel4chor.model.grounding.impl.MessageLink resolveGroundingMessageLinkByName(
			Grounding grounding, String msgLinkName) {
		if (grounding == null || msgLinkName == null)
			throw new NullPointerException();

		List<org.bpel4chor.model.grounding.impl.MessageLink> msgLinks = grounding.getMessageLinks();

		for (org.bpel4chor.model.grounding.impl.MessageLink msgLink : msgLinks) {
			if (msgLink.getName().equals(msgLinkName))
				return msgLink;
		}
		return null;
	}

	/**
	 * Resolve the participant that is with the participant name given
	 * 
	 * @param topology
	 * @param participantName
	 * @return
	 */
	public static org.bpel4chor.model.topology.impl.Participant resolveParticipant(
			Topology topology, String participantName) {

		if (topology == null || participantName == null)
			throw new NullPointerException();

		List<org.bpel4chor.model.topology.impl.Participant> participants = topology
				.getParticipants();

		for (org.bpel4chor.model.topology.impl.Participant p : participants) {
			if (p.getName().equals(participantName))
				return p;
		}

		return null;
	}
}
