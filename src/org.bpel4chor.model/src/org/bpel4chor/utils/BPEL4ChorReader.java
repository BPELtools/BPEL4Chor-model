package org.bpel4chor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.bpel4chor.interfaces.INamespaceMap;
import org.bpel4chor.model.grounding.impl.Grounding;
import org.bpel4chor.model.grounding.impl.ParticipantRef;
import org.bpel4chor.model.grounding.impl.Property;
import org.bpel4chor.model.topology.impl.MessageLink;
import org.bpel4chor.model.topology.impl.Participant;
import org.bpel4chor.model.topology.impl.ParticipantSet;
import org.bpel4chor.model.topology.impl.ParticipantType;
import org.bpel4chor.model.topology.impl.Topology;
import org.bpel4chor.utils.exceptions.MalformedTLGLSyntaxException;
import org.eclipse.bpel.model.BPELPlugin;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.resource.BPELReader;
import org.eclipse.bpel.model.resource.BPELResource;
import org.eclipse.bpel.model.resource.BPELResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

/**
 * BPEL4ChorReader is useful for reading bpel, wsdl, topology and grounding from
 * document to Java.
 * 
 * @since Oct 21, 2011
 * @author Daojun Cui
 * 
 *         TODO the read of topology and grounding
 */
public class BPEL4ChorReader {
	
	static {
		// init
		BPELPlugin bpelPlugin = new BPELPlugin();
		// setup the extension to factory map, so that the proper
		// ResourceFactory can be used to read the file.
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("bpel", new BPELResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("pbd", new BPELResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());
		
	}
	
	
	/**
	 * Read from the given input stream into the given resource
	 * 
	 * <p>
	 * It will be delegate to {@link MyBPELReader}.
	 * 
	 * @param resource The BPELResource that relates to the BPEL File URI
	 * @param inputStream The InputStream that relates to the BPEL File
	 * @return
	 */
	public static Process readBPEL(BPELResource resource, InputStream inputStream) {
		Process process = null;
		
		BPELReader myReader = new BPELReader();
		myReader.read(resource, inputStream);
		
		process = (Process) myReader.getResource().getContents().get(0);
		
		return process;
	}
	
	/**
	 * Read in the BPEL process with the given uri string
	 * 
	 * @param filePath
	 * @return
	 */
	public static Process readBPEL(String filePath) {
		
		if (filePath == null) {
			throw new NullPointerException();
		}
		if (filePath.isEmpty()) {
			throw new IllegalStateException("file path is empty.");
		}
		
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI(filePath);
		boolean loadOnDemand = true;
		Resource resource = resourceSet.getResource(uri, loadOnDemand);
		return (Process) resource.getContents().get(0);
	}
	
	/**
	 * Read the WSDL document into a definition
	 * 
	 * @param wsdlURI The WSDL URI
	 * @return
	 * @throws WSDLException
	 * @throws IOException
	 */
	public static Definition readWSDL(String wsdlURI) throws WSDLException, IOException {
		
		if ((wsdlURI == null) || wsdlURI.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		if (!wsdlURI.endsWith(".wsdl")) {
			throw new IllegalArgumentException("invalid wsdl uri. " + wsdlURI);
		}
		
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
		Resource resource = rs.createResource(URI.createFileURI(wsdlURI));
		resource.load(null);
		Definition root = (Definition) resource.getContents().iterator().next();
		return root;
	}
	
	/**
	 * Read in the topology file from the given InputStream
	 * 
	 * @param inputStream The {@link InputStream} for reading
	 * @return read {@link Topology}
	 * 
	 */
	public static Topology readTopology(InputStream inputStream) {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser;
		Topology topology = null;
		try {
			parser = factory.createXMLStreamReader(inputStream);
			if ((parser.getEventType() == XMLStreamConstants.START_DOCUMENT)) {
				parser.nextTag();
			}
			
			// Read Topology Head
			if (parser.getLocalName().equals("topology") && !(parser.getEventType() == XMLStreamConstants.END_ELEMENT)) {
				
				topology = new Topology();
				for (int i = 0; i < parser.getNamespaceCount(); i++) {
					if (parser.getNamespacePrefix(i) != null) {
						topology.getNamespaceMap().addNamespace(parser.getNamespaceURI(i), parser.getNamespacePrefix(i));
					}
				}
				
				topology.setName(BPEL4ChorReader.getStrAttribute(parser, "name", true).toString());
				
				topology.setTargetNamespace(BPEL4ChorReader.getStrAttribute(parser, "targetNamespace", true).toString());
				
				parser.nextTag();
			}
			
			// Read participantTypes
			if (parser.getLocalName().equals("participantTypes")) {
				
				parser.nextTag();
				
				// Read participantType(s)
				while (parser.hasNext() && parser.getLocalName().equals("participantType")) {
					if (!(parser.getEventType() == XMLStreamConstants.END_ELEMENT)) {
						
						ParticipantType participantType = BPEL4ChorReader.readInParticipantType(parser, topology.getNamespaceMap());
						topology.getParticipantTypes().add(participantType);
					}
					parser.nextTag();
				}
				parser.nextTag();
			}
			
			// Read participants
			if (parser.getLocalName().equals("participants")) {
				
				parser.nextTag();
				
				// Read participant(s)
				if (parser.hasNext() && parser.getLocalName().equals("participant")) {
					while (parser.hasNext() && parser.getLocalName().equals("participant")) {
						Participant participant = BPEL4ChorReader.readInParticipant(parser, topology.getNamespaceMap(), false);
						topology.getParticipants().add(participant);
					}
					if (parser.getLocalName() != "participantSet") {
						parser.nextTag();
					}
				}
				// Read participantSet(s)
				if (parser.hasNext() && parser.getLocalName().equals("participantSet")) {
					while (parser.hasNext() && parser.getLocalName().equals("participantSet")) {
						ParticipantSet participantSet = BPEL4ChorReader.readInParticipantSet(parser, topology.getNamespaceMap());
						topology.getParticipantSets().add(participantSet);
					}
					parser.nextTag();
				}
			}
			
			// Read messageLinks
			if (parser.getLocalName().equals("messageLinks")) {
				
				parser.nextTag();
				
				// Read messageLink(s)
				while (parser.hasNext() && parser.getLocalName().equals("messageLink")) {
					if (!(parser.getEventType() == XMLStreamConstants.END_ELEMENT)) {
						
						MessageLink messageLink = BPEL4ChorReader.readInMessageLinkTL(parser, topology.getNamespaceMap());
						
						topology.getMessageLinks().add(messageLink);
					}
					parser.nextTag();
				}
				parser.nextTag();
			}
			
			int event = parser.next();
			if (event == XMLStreamConstants.END_DOCUMENT) {
				parser.close();
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (MalformedTLGLSyntaxException e) {
			e.printStackTrace();
		}
		
		return topology;
	}
	
	/**
	 * Read in the grounding file from the given InputStream
	 * 
	 * @param inputStream The {@link InputStream} for reading
	 * @param topology The {@link Topology} belonging to the Grounding
	 * @return The read {@link Grounding}
	 * 
	 */
	public static Grounding readGrounding(InputStream inputStream, Topology topology) {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser;
		Grounding grounding = null;
		
		try {
			parser = factory.createXMLStreamReader(inputStream);
			
			if ((parser.getEventType() == XMLStreamConstants.START_DOCUMENT)) {
				parser.nextTag();
			}
			
			if (parser.getLocalName().equals("grounding") && !(parser.getEventType() == XMLStreamConstants.END_ELEMENT)) {
				
				grounding = new Grounding(topology);
				for (int i = 0; i < parser.getNamespaceCount(); i++) {
					if (parser.getNamespacePrefix(i) != null) {
						grounding.getNamespaceMap().addNamespace(parser.getNamespaceURI(i), parser.getNamespacePrefix(i));
					}
				}
				
				grounding.setTargetNamespace(BPEL4ChorReader.getStrAttribute(parser, "targetNamespace", true).toString());
				
				parser.nextTag();
			}
			if (parser.getLocalName().equals("messageLinks")) {
				
				parser.nextTag();
				while (parser.hasNext() && parser.getLocalName().equals("messageLink")) {
					if (!(parser.getEventType() == XMLStreamConstants.END_ELEMENT)) {
						
						grounding.getMessageLinks().add(BPEL4ChorReader.readInMessageLinkGD(parser, grounding.getNamespaceMap(), topology));
					}
					parser.nextTag();
				}
				parser.nextTag();
			}
			
			if (parser.getLocalName().equals("participantRefs")) {
				parser.nextTag();
				while (parser.hasNext() && parser.getLocalName().equals("participantRef")) {
					if (!(parser.getEventType() == XMLStreamConstants.END_ELEMENT)) {
						
						grounding.getParticipantRefs().add(BPEL4ChorReader.readInParticipantRef(parser, grounding.getNamespaceMap()));
						
					}
					parser.nextTag();
				}
				parser.nextTag();
				
			}
			
			if (parser.getLocalName().equals("properties")) {
				parser.nextTag();
				while (parser.hasNext() && parser.getLocalName().equals("property")) {
					
					if (!(parser.getEventType() == XMLStreamConstants.END_ELEMENT)) {
						
						grounding.getProperties().add(BPEL4ChorReader.readInProperty(parser, grounding.getNamespaceMap()));
						
					}
					parser.nextTag();
				}
				parser.nextTag();
				
			}
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
			
		} catch (MalformedTLGLSyntaxException e) {
			e.printStackTrace();
		}
		
		return grounding;
	}
	
	/**
	 * Helper Method to check for existence of given attribute
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param attrName The searched attributes Names
	 * @param needed Flag indicating if attribute is needed or not
	 * @return found Object
	 * @throws MalformedTLGLSyntaxException
	 */
	private static Object getStrAttribute(XMLStreamReader r, String attrName, boolean needed) throws MalformedTLGLSyntaxException {
		if (r.getAttributeValue(null, attrName) != null) {
			return r.getAttributeValue(null, attrName);
		} else {
			if (needed) {
				throw new MalformedTLGLSyntaxException("Attribute " + attrName + " is not set !!");
			} else {
				return null;
			}
			
		}
	}
	
	/**
	 * Helper Method to check for existence of given attribute
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param attrName The searched attributes Names
	 * @param needed Flag indicating if attribute is needed or not
	 * @return found List<String>
	 * @throws MalformedTLGLSyntaxException
	 */
	private static List<String> getStrsAttribute(XMLStreamReader r, String attrName, boolean needed) throws MalformedTLGLSyntaxException {
		if (r.getAttributeValue(null, attrName) != null) {
			return Arrays.asList(r.getAttributeValue(null, attrName).split("[ ]+"));
		} else {
			if (needed) {
				throw new MalformedTLGLSyntaxException("Attribute " + attrName + " is not set !!");
			} else {
				return null;
			}
			
		}
	}
	
	/**
	 * Helper Method to check for existence of given attribute
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param attrName The searched attributes Names
	 * @param nsMap The nsMap to search in
	 * @param needed Flag indicating if attribute is needed or not
	 * @return found QName
	 * @throws MalformedTLGLSyntaxException
	 */
	private static QName getQNAttribute(XMLStreamReader r, String attrName, INamespaceMap<String, String> nsMap, boolean needed) throws MalformedTLGLSyntaxException {
		if (r.getAttributeValue(null, attrName) != null) {
			List<String> splitted = Arrays.asList(r.getAttributeValue(null, attrName).split(":"));
			
			QName newQName = new QName(nsMap.get(splitted.get(0)), splitted.get(1), splitted.get(0));
			return newQName;
		} else {
			if (needed) {
				throw new MalformedTLGLSyntaxException("Attribute " + attrName + " is not set !!");
			} else {
				return null;
			}
			
		}
	}
	
	/**
	 * Helper Method to check for existence of given attribute
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param attrName The searched attributes Names
	 * @param nsMap The nsMap to search in
	 * @param needed Flag indicating if attribute is needed or not
	 * @return found List<QName>
	 * @throws MalformedTLGLSyntaxException
	 */
	private static List<QName> getQNsAttribute(XMLStreamReader r, String attrName, INamespaceMap<String, String> nsMap, boolean needed) throws MalformedTLGLSyntaxException {
		if (r.getAttributeValue(null, attrName) != null) {
			List<String> qnStrings = Arrays.asList(r.getAttributeValue(null, attrName).split("[ ]+"));
			List<QName> qNames = new ArrayList<QName>();
			for (String string : qnStrings) {
				List<String> splitted = Arrays.asList(string.split(":"));
				QName newQName = new QName(nsMap.get(splitted.get(0)), splitted.get(1), splitted.get(0));
				qNames.add(newQName);
			}
			return qNames;
		} else {
			if (needed) {
				throw new MalformedTLGLSyntaxException("Attribute " + attrName + " is not set !!");
			} else {
				return null;
			}
			
		}
	}
	
	/**
	 * Read ParticipantSet from given parser (also recursive)
	 * 
	 * @param r The given {@link XMLStreamReader}
	 * @param nsMap The used namespacemap
	 * @return ParticipantSet
	 * @throws MalformedTLGLSyntaxException
	 * @throws XMLStreamException
	 */
	private static ParticipantSet readInParticipantSet(XMLStreamReader r, INamespaceMap<String, String> nsMap) throws MalformedTLGLSyntaxException, XMLStreamException {
		ParticipantSet participantSet = null;
		
		String psName = null;
		String psType = null;
		if (!(r.getEventType() == XMLStreamConstants.END_ELEMENT)) {
			
			psName = BPEL4ChorReader.getStrAttribute(r, "name", true).toString();
			psType = BPEL4ChorReader.getStrAttribute(r, "type", true).toString();
			
			participantSet = new ParticipantSet(psName, psType);
			
			QName scope = BPEL4ChorReader.getQNAttribute(r, "scope", nsMap, false);
			if (scope != null) {
				participantSet.setScope(scope);
			}
			
			List<QName> forEachs = BPEL4ChorReader.getQNsAttribute(r, "forEach", nsMap, false);
			if ((forEachs != null) && (forEachs.size() > 0)) {
				participantSet.setForEach(forEachs);
			}
		}
		
		r.nextTag();
		// Read (sub)participantSet(s)
		if (r.hasNext() && r.getLocalName().equals("participantSet")) {
			while (r.hasNext() && r.getLocalName().equals("participantSet")) {
				ParticipantSet participantSetSub = BPEL4ChorReader.readInParticipantSet(r, nsMap);
				participantSet.getParticipantSetList().add(participantSetSub);
			}
			r.nextTag();
		}
		
		// Read (sub)participant(s)
		if (r.hasNext() && r.getLocalName().equals("participant")) {
			while (r.hasNext() && r.getLocalName().equals("participant")) {
				
				Participant participant = BPEL4ChorReader.readInParticipant(r, nsMap, true);
				participantSet.getParticipantList().add(participant);
			}
			r.nextTag();
		}
		return participantSet;
	}
	
	/**
	 * Read Participant from given {@link XMLStreamReader}
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param nsMap the Namespace map with all needed data
	 * @param contained Flag indicating if participant is contained in
	 *            {@link ParticipantSet}
	 * @return {@link Participant}
	 * @throws MalformedTLGLSyntaxException
	 * @throws XMLStreamException
	 */
	private static Participant readInParticipant(XMLStreamReader r, INamespaceMap<String, String> nsMap, boolean contained) throws MalformedTLGLSyntaxException, XMLStreamException {
		Participant participant = new Participant();
		if (!(r.getEventType() == XMLStreamConstants.END_ELEMENT)) {
			participant.setName(BPEL4ChorReader.getStrAttribute(r, "name", true).toString());
			
			participant.setType(BPEL4ChorReader.getStrAttribute(r, "type", true).toString());
			if (!contained) {
				
				List<String> selects = BPEL4ChorReader.getStrsAttribute(r, "selects", false);
				if (selects != null) {
					participant.setSelects(selects);
				}
			} else {
				// Our Participant is contained in a Participant Set
				List<QName> forEachs = BPEL4ChorReader.getQNsAttribute(r, "forEach", nsMap, false);
				if (forEachs != null) {
					participant.setForEach(forEachs);
				}
				
				ContainmentValue value = (ContainmentValue) BPEL4ChorReader.getStrAttribute(r, "containment", false);
				if (value != null) {
					participant.setContainment(value);
				}
			}
			
			QName scope = BPEL4ChorReader.getQNAttribute(r, "scope", nsMap, false);
			if (scope != null) {
				participant.setScope(scope);
			}
			
		}
		r.nextTag();
		r.nextTag();
		return participant;
	}
	
	/**
	 * Read the ParticipantType from the given {@link XMLStreamReader}
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param nsMap The {@link INamespaceMap} holding all data
	 * @return The read {@link ParticipantType}
	 * @throws MalformedTLGLSyntaxException
	 */
	private static ParticipantType readInParticipantType(XMLStreamReader r, INamespaceMap<String, String> nsMap) throws MalformedTLGLSyntaxException {
		ParticipantType participantType = new ParticipantType();
		participantType.setName(BPEL4ChorReader.getStrAttribute(r, "name", true).toString());
		
		QName pbd = BPEL4ChorReader.getQNAttribute(r, "participantBehaviorDescription", nsMap, false);
		if (pbd != null) {
			participantType.setParticipantBehaviorDescription(pbd);
		}
		
		Object pLang = BPEL4ChorReader.getStrAttribute(r, "processLanguage", false);
		if (pLang != null) {
			participantType.setProcessLanguage(pLang.toString());
		}
		return participantType;
	}
	
	/**
	 * Read the MessageLink from the given {@link XMLStreamReader}
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param nsMap The {@link INamespaceMap} holding all data
	 * @return The {@link MessageLink}
	 * @throws MalformedTLGLSyntaxException
	 */
	private static MessageLink readInMessageLinkTL(XMLStreamReader r, INamespaceMap<String, String> nsMap) throws MalformedTLGLSyntaxException {
		MessageLink messageLink = new MessageLink();
		
		Object temp = BPEL4ChorReader.getStrAttribute(r, "name", false);
		if (temp != null) {
			messageLink.setName(temp.toString());
		}
		temp = null;
		temp = BPEL4ChorReader.getStrAttribute(r, "sender", false);
		if (temp != null) {
			messageLink.setSender(temp.toString());
		}
		
		List<String> tempList = BPEL4ChorReader.getStrsAttribute(r, "senders", false);
		if (tempList != null) {
			messageLink.setSenders(tempList);
		}
		temp = null;
		temp = BPEL4ChorReader.getStrAttribute(r, "sendActivity", false);
		if (temp != null) {
			messageLink.setSendActivity(temp.toString());
		}
		
		messageLink.setReceiver(BPEL4ChorReader.getStrAttribute(r, "receiver", true).toString());
		temp = null;
		temp = BPEL4ChorReader.getStrAttribute(r, "receiveActivity", false);
		if (temp != null) {
			messageLink.setReceiveActivity(temp.toString());
		}
		temp = null;
		temp = BPEL4ChorReader.getStrAttribute(r, "bindSenderTo", false);
		if (temp != null) {
			messageLink.setBindSenderTo(temp.toString());
		}
		
		messageLink.setMessageName(BPEL4ChorReader.getStrAttribute(r, "messageName", true).toString());
		
		tempList = BPEL4ChorReader.getStrsAttribute(r, "participantRefs", false);
		if (tempList != null) {
			messageLink.setParticipantRefs(tempList);
		}
		
		tempList = BPEL4ChorReader.getStrsAttribute(r, "copyParticipantRefsTo", false);
		if (tempList != null) {
			messageLink.setCopyParticipantRefsTo(tempList);
		}
		return messageLink;
	}
	
	/**
	 * Read the {@link org.bpel4chor.model.grounding.impl.MessageLink} from the
	 * given {@link XMLStreamReader}
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param nsMap The {@link INamespaceMap} holding all data
	 * @param topology The {@link Topology}
	 * @return The {@link org.bpel4chor.model.grounding.impl.MessageLink}
	 * @throws MalformedTLGLSyntaxException
	 */
	private static org.bpel4chor.model.grounding.impl.MessageLink readInMessageLinkGD(XMLStreamReader r, INamespaceMap<String, String> nsMap, Topology topology) throws MalformedTLGLSyntaxException {
		String mlName = null;
		QName mlportType = null;
		String mlOperationName = null;
		
		mlName = BPEL4ChorReader.getStrAttribute(r, "name", true).toString();
		
		mlOperationName = BPEL4ChorReader.getStrAttribute(r, "operation", true).toString();
		
		mlportType = BPEL4ChorReader.getQNAttribute(r, "portType", nsMap, true);
		
		org.bpel4chor.model.grounding.impl.MessageLink messageLink = new org.bpel4chor.model.grounding.impl.MessageLink(BPEL4ChorUtil.resolveTopologyMessageLinkByName(topology, mlName), mlportType, mlOperationName);
		return messageLink;
	}
	
	/**
	 * Read the {@link ParticipantRef} from the given {@link XMLStreamReader}
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param nsMap The {@link INamespaceMap} holding all data
	 * @return The {@link ParticipantRef}
	 * @throws MalformedTLGLSyntaxException
	 */
	private static ParticipantRef readInParticipantRef(XMLStreamReader r, INamespaceMap<String, String> nsMap) throws MalformedTLGLSyntaxException {
		String prName = null;
		QName prWSDLproperty = null;
		
		prName = BPEL4ChorReader.getStrAttribute(r, "name", true).toString();
		
		prWSDLproperty = BPEL4ChorReader.getQNAttribute(r, "WSDLproperty", nsMap, true);
		
		ParticipantRef participantRef = new ParticipantRef(prName, prWSDLproperty);
		
		return participantRef;
	}
	
	/**
	 * Read the {@link Property} from the given {@link XMLStreamReader}
	 * 
	 * @param r The {@link XMLStreamReader}
	 * @param nsMap The {@link INamespaceMap} holding all data
	 * @return The {@link Property}
	 * @throws MalformedTLGLSyntaxException
	 */
	private static Property readInProperty(XMLStreamReader r, INamespaceMap<String, String> nsMap) throws MalformedTLGLSyntaxException {
		String prName = null;
		QName prWSDLproperty = null;
		
		prName = BPEL4ChorReader.getStrAttribute(r, "name", true).toString();
		
		prWSDLproperty = BPEL4ChorReader.getQNAttribute(r, "WSDLproperty", nsMap, true);
		
		Property property = new Property(prName, prWSDLproperty);
		return property;
	}
}
