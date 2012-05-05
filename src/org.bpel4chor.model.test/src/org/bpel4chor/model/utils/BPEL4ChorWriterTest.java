package org.bpel4chor.model.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.bpel4chor.model.grounding.impl.Grounding;
import org.bpel4chor.model.grounding.impl.ParticipantRef;
import org.bpel4chor.model.topology.impl.MessageLink;
import org.bpel4chor.model.topology.impl.Participant;
import org.bpel4chor.model.topology.impl.ParticipantSet;
import org.bpel4chor.model.topology.impl.ParticipantType;
import org.bpel4chor.model.topology.impl.Topology;
import org.bpel4chor.utils.BPEL4ChorConstants;
import org.bpel4chor.utils.BPEL4ChorFactory;
import org.bpel4chor.utils.BPEL4ChorWriter;
import org.eclipse.bpel.model.BPELPlugin;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.resource.BPELResource;
import org.eclipse.bpel.model.resource.BPELResourceFactoryImpl;
import org.eclipse.bpel.model.resource.BPELWriter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Test;

public class BPEL4ChorWriterTest {

	private Topology topology;

	private Grounding grounding;

	private File testFileDir;// where the test files locate

	private File outputDir;// where to write the output files

	@Before
	public void setup() throws Exception {
		File projectDir = new File("");
		testFileDir = new File(projectDir.getAbsolutePath(), "files");

		outputDir = new File(BPEL4ChorConstants.BPEL4CHOR_DEFAULT_WRITE_DIR);
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		initTopology();
		initGrounding();
	}

	/**
	 * create a sample topology
	 * 
	 * @throws Exception
	 */
	public void initTopology() throws Exception {
		// create sample topology
		topology = BPEL4ChorFactory.createTopology("ExmpleTopology", "urn:exampletopology");

		// participantTypes
		ParticipantType ptAgency = BPEL4ChorFactory.createParticipantType("Agency", new QName(
				"http://example.org", "agency", "agency"), null);
		ParticipantType ptTravler = BPEL4ChorFactory.createParticipantType("Travler", new QName(
				"http://example.org", "travler", "travler"), null);
		ParticipantType ptAirline = BPEL4ChorFactory.createParticipantType("Airline", new QName(
				"http://example.org", "airline", "airline"), null);

		topology.add(ptAgency);
		topology.add(ptTravler);
		topology.add(ptAirline);

		// participants
		Participant travler = BPEL4ChorFactory.createParticipant("travler", "Travler");
		Participant agency = BPEL4ChorFactory.createParticipant("agency", "Agency");

		topology.add(travler);
		topology.add(agency);

		// participantSets
		Participant airline1 = BPEL4ChorFactory.createParticipant("airline1", "Airline");
		Participant ariline2 = BPEL4ChorFactory.createParticipant("airline2", "Airline");
		ParticipantSet pSet = BPEL4ChorFactory.createTopologyParticipantSet("airline", "Airline",
				null, null);
		pSet.add(airline1);
		pSet.add(ariline2);

		topology.add(pSet);

		// (topology)messageLinks
		MessageLink ml = new MessageLink("eTicketLink", "airline", "SendTickets", "travler",
				"receiveETicket", "eTicket");
		MessageLink m2 = new MessageLink("tripOrder", "travler", "SubmitTripOrder", "Agency",
				"ReceiveTripOrder", "tripOrder");

		topology.add(ml);
		topology.add(m2);
	}

	/** create a sample grounding associated to the topology */
	public void initGrounding() {
		// create a sample grounding
		grounding = BPEL4ChorFactory.createGrounding(topology);

		// messageLinks - use the messageLinks from the topology
		for (org.bpel4chor.model.topology.impl.MessageLink topoML : topology.getMessageLinks()) {
			org.bpel4chor.model.grounding.impl.MessageLink grouML = new org.bpel4chor.model.grounding.impl.MessageLink(
					topoML, new QName("http://example.org", topoML.getName() + "PortType", "wsdl"),
					topoML.getName() + "WSDLOperation");
			grounding.add(grouML);
		}

		// participantRefs
		ParticipantRef pRefTravler = BPEL4ChorFactory.createGroundingParticipantRef("travler",
				new QName("http://example.org", "travlerWSDL"));
		ParticipantRef pRefAgency = BPEL4ChorFactory.createGroundingParticipantRef("agency",
				new QName("http://example.org", "agencyWSDL"));
		ParticipantRef pRefAirline = BPEL4ChorFactory.createGroundingParticipantRef("airline",
				new QName("http://example.org", "airlineWSDL"));
		grounding.add(pRefTravler);
		grounding.add(pRefAirline);
		grounding.add(pRefAgency);

		// properties
		org.bpel4chor.model.grounding.impl.Property property = BPEL4ChorFactory
				.createGroundingProperty("travlerProperty", new QName("http://example.org",
						"travlerWSDLProperty"));
		grounding.add(property);
	}

	@Test
	public void writeTopologyFileNameTest() throws FileNotFoundException, XMLStreamException {
		BPEL4ChorWriter.writeTopology(topology, "topology-" + topology.getName() + "-"
				+ Calendar.getInstance().getTimeInMillis() + ".xml");
	}

	@Test
	public void writeTopologyOutputstreamTest() throws FileNotFoundException, XMLStreamException {
		String fileName = "topology-" + topology.getName() + "-"
				+ Calendar.getInstance().getTimeInMillis() + ".xml";
		FileOutputStream outputStream = new FileOutputStream(new File(outputDir, fileName));
		BPEL4ChorWriter.writeTopology(topology, outputStream);
		System.out.println("Result: " + outputDir.getAbsolutePath() + File.separator + fileName);
	}

	@Test
	public void writeGroundingFileNameTest() throws FileNotFoundException, XMLStreamException {
		BPEL4ChorWriter.writeGrounding(grounding, "grounding-"
				+ grounding.getTopology().getLocalPart() + "-"
				+ Calendar.getInstance().getTimeInMillis() + ".xml");
	}

	@Test
	public void writeGroundingOutputStreamTest() throws FileNotFoundException, XMLStreamException {
		String fileName = "grounding-" + grounding.getTopology().getLocalPart() + "-"
				+ Calendar.getInstance().getTimeInMillis() + ".xml";
		FileOutputStream outputStream = new FileOutputStream(new File(outputDir, fileName));
		BPEL4ChorWriter.writeGrounding(grounding, outputStream);
		System.out.println("Result: " + outputDir.getAbsolutePath() + File.separator + fileName);
	}

	@Test
	public void BPELWriterTest() throws IOException {

		// init
		BPELPlugin bpelPlugin = new BPELPlugin();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
				new BPELResourceFactoryImpl());

		// load process
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI(testFileDir.getAbsolutePath() + File.separator + "Agency.bpel");
		BPELResource resource = (BPELResource) resourceSet.getResource(uri, true);
		org.eclipse.bpel.model.Process process = (org.eclipse.bpel.model.Process) resource
				.getContents().get(0);

		// write process

		boolean UseNSPrefix = false;
		File saveFile = new File(outputDir, process.getName() + "-"
				+ Calendar.getInstance().getTimeInMillis() + ".bpel");
		FileOutputStream outputStream = new FileOutputStream(saveFile);
		resource.setOptionUseNSPrefix(UseNSPrefix);
		// resource.save(outputStream, null);
		// this line is aquivalent to the following 2 lines ==>
		BPEL4ChorWriter.writeBPEL(resource, outputStream);
		System.out.println("BPELWriterTest result: " + saveFile.getAbsolutePath());

	}

	@Test
	public void writeBPELResourceOutputStreamTest() throws IOException {
		// init
		BPELPlugin bpelPlugin = new BPELPlugin();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
				new BPELResourceFactoryImpl());

		// load process resource
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI(testFileDir.getAbsolutePath() + File.separator + "Agency.bpel");
		BPELResource resource = (BPELResource) resourceSet.getResource(uri, true);

		// get bpel process
		org.eclipse.bpel.model.Process process = (org.eclipse.bpel.model.Process) resource
				.getContents().get(0);

		// create output stream
		File saveFile = new File(outputDir, process.getName() + "-"
				+ Calendar.getInstance().getTimeInMillis() + ".bpel");
		FileOutputStream outputStream = new FileOutputStream(saveFile);

		// write BPEL process to outputStream
		BPEL4ChorWriter.writeBPEL(resource, outputStream);

		System.out.println("Write BPEL Result: " + saveFile.getAbsolutePath());
	}

	@Test
	public void testWritePBD() throws IOException {
		// init
		BPELPlugin bpelPlugin = new BPELPlugin();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
				new BPELResourceFactoryImpl());

		// load process resource
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI(testFileDir.getAbsolutePath() + File.separator
				+ "OrderingProcess.bpel");
		BPELResource resource = (BPELResource) resourceSet.getResource(uri, true);

		// get bpel process
		Process process = (Process) resource
				.getContents().get(0);

		// create output stream
		File saveFile = new File(outputDir, process.getName() + "-"
				+ Calendar.getInstance().getTimeInMillis() + ".pbd");
		FileOutputStream outputStream = new FileOutputStream(saveFile);

		// work-around to prevent the Import Exception
		process.getImports().clear();
		process.setPartnerLinks(null);
		// write BPEL process to outputStream
		BPEL4ChorWriter.writePBD(process, outputStream);

		System.out.println("Write PBD Result: " + saveFile.getAbsolutePath());
	}
}
