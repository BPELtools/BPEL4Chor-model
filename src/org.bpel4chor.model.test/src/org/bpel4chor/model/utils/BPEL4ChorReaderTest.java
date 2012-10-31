package org.bpel4chor.model.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.wsdl.WSDLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.bpel4chor.model.grounding.impl.Grounding;
import org.bpel4chor.model.topology.impl.Topology;
import org.bpel4chor.utils.BPEL4ChorConstants;
import org.bpel4chor.utils.BPEL4ChorReader;
import org.bpel4chor.utils.BPEL4ChorWriter;
import org.bpel4chor.utils.MyBPELReader;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELExtensibleElement;
import org.eclipse.bpel.model.BPELPlugin;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.impl.BPELFactoryImpl;
import org.eclipse.bpel.model.resource.BPELReader;
import org.eclipse.bpel.model.resource.BPELResource;
import org.eclipse.bpel.model.resource.BPELResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_stuttgart.iaas.bpel.model.utilities.MyWSDLUtil;

public class BPEL4ChorReaderTest {
	
	private File testFileDir;// where the test files locate
	
	private File outputDir;// where to write the output files
	
	private Document document;
	
	
	@Before
	public void setup() throws ParserConfigurationException {
		File projectDir = new File("");
		this.testFileDir = new File(projectDir.getAbsolutePath(), "files");
		
		this.outputDir = new File(BPEL4ChorConstants.BPEL4CHOR_DEFAULT_WRITE_DIR);
		if (!this.outputDir.exists()) {
			this.outputDir.mkdirs();
		}
		
		final DocumentBuilderFactory documentBuilderFactory =
		// DocumentBuilderFactory.newInstance();
		// new org.apache.crimson.jaxp.DocumentBuilderFactoryImpl();
		new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
		
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setValidating(false);
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
		this.document = builder.newDocument();
	}
	
	@Test
	public void testScan() throws FileNotFoundException {
		String expected;
		String actual;
		
		// init
		BPELPlugin bpelPlugin = new BPELPlugin();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new BPELResourceFactoryImpl());
		
		// load bpel resource
		boolean loadOnDemand = false;// whether to create and load the resource
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI(this.testFileDir.getAbsolutePath() + File.separator + "Agency.bpel");
		BPELResource resource = (BPELResource) resourceSet.createResource(uri);
		
		// prepare the inputStream
		FileInputStream inputStream = new FileInputStream(new File(this.testFileDir, "Agency.bpel"));
		
		// get bpel process
		BPELReader reader = new BPELReader();
		reader.read(resource, inputStream);
		
		org.eclipse.bpel.model.Process process = (org.eclipse.bpel.model.Process) reader.getResource().getContents().get(0);
		
		// prepare PBDReader
		MyBPELReader pbdReader = new MyBPELReader();
		// now scan
		pbdReader.scan(process.getActivity().getElement());
		// get the map
		Map<String, Set<Element>> name2ElemMap = pbdReader.getName2ElementMap();
		
		// Test Case 0: size == 9
		Assert.assertEquals(9, name2ElemMap.size());
		
		// Test Case 1: ReceiveTripOrder -> receive
		expected = "receive";
		actual = name2ElemMap.get("ReceiveTripOrder").iterator().next().getLocalName();
		Assert.assertEquals(expected, actual);
		
		// Test Case 2: scope -> forEach
		expected = "forEach";
		actual = name2ElemMap.get("scope").iterator().next().getLocalName();
		Assert.assertEquals(expected, actual);
		
		// Test Case 3: ReceivePrice -> receive
		expected = "receive";
		actual = name2ElemMap.get("ReceivePrice").iterator().next().getLocalName();
		Assert.assertEquals(expected, actual);
		
		// now read another bpel process shippingService.bpel
		uri = URI.createFileURI(this.testFileDir.getAbsolutePath() + File.separator + "shippingService.bpel");
		resource = (BPELResource) resourceSet.createResource(uri);
		inputStream = new FileInputStream(new File(this.testFileDir, "shippingService.bpel"));
		reader = new BPELReader();
		reader.read(resource, inputStream);
		process = (org.eclipse.bpel.model.Process) reader.getResource().getContents().get(0);
		pbdReader = new MyBPELReader();
		// now scan
		pbdReader.scan(process.getActivity().getElement());
		// get the map
		name2ElemMap = pbdReader.getName2ElementMap();
		
		// Test Case 4: no activity has 'name=...' attribute
		Assert.assertTrue(name2ElemMap.size() == 0);
	}
	
	@Test
	public void testInsertWsuId() throws FileNotFoundException, ParserConfigurationException {
		
		// init
		BPELPlugin bpelPlugin = new BPELPlugin();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new BPELResourceFactoryImpl());
		
		// load bpel resource
		boolean loadOnDemand = false;// whether to create and load the resource
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI(this.testFileDir.getAbsolutePath() + File.separator + "Agency.bpel");
		BPELResource resource = (BPELResource) resourceSet.createResource(uri);
		
		// prepare the inputStream
		FileInputStream inputStream = new FileInputStream(new File(this.testFileDir, "Agency.bpel"));
		
		// get bpel process
		BPELReader reader = new BPELReader();
		reader.read(resource, inputStream);
		
		org.eclipse.bpel.model.Process process = (org.eclipse.bpel.model.Process) reader.getResource().getContents().get(0);
		
		// prepare PBDReader
		MyBPELReader pbdReader = new MyBPELReader();
		
		//
		// Test Case 1: activity 'name=...' == null
		//
		
		// create bpel element, and insert 'wsu:id' into the element
		BPELExtensibleElement activity = pbdReader.insertWsuId(this.createBPELActivity("invoke", null));
		Assert.assertEquals("invoke1", activity.getElement().getAttribute("wsu:id"));
		
		//
		// Test Case 2: activity 'name=...' != null, name->elemSet.size == 1
		//
		BPELExtensibleElement act = this.createBPELActivity("invoke", "getTicket");
		Set<Element> elemSet = new HashSet<Element>();
		elemSet.add(act.getElement());
		pbdReader.getName2ElementMap().put("getTicket", elemSet);
		activity = pbdReader.insertWsuId(act);
		Assert.assertEquals("getTicket", activity.getElement().getAttribute("wsu:id"));
		
		//
		// Test Case 3: activity 'name=...' != null, name->elemSet.size >= 2
		//
		BPELExtensibleElement act1 = this.createBPELActivity("invoke", "getTicket");
		BPELExtensibleElement act2 = this.createBPELActivity("invoke", "getTicket");
		elemSet = new HashSet<Element>();
		elemSet.add(act1.getElement());
		elemSet.add(act2.getElement());
		pbdReader.getName2ElementMap().put("getTicket", elemSet);
		activity = pbdReader.insertWsuId(act1);
		Assert.assertEquals("getTicket1", activity.getElement().getAttribute("wsu:id"));
	}
	
	/**
	 * Create an activity with the given localname, and the given attribute
	 * 'name=...' if the param is not null.
	 * 
	 * @param localname
	 * @param attrName
	 * @return
	 * @throws ParserConfigurationException
	 */
	BPELExtensibleElement createBPELActivity(String localname, String attrName) throws ParserConfigurationException {
		Activity bpelActivity = BPELFactoryImpl.eINSTANCE.createActivity();
		Element actElement = this.document.createElement(localname);
		if ((attrName != null) && !attrName.isEmpty()) {
			actElement.setAttribute("name", attrName);
		}
		bpelActivity.setElement(actElement);
		return bpelActivity;
	}
	
	@Test
	public void testReadBPEL() throws IOException {
		// init
		BPELPlugin bpelPlugin = new BPELPlugin();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new BPELResourceFactoryImpl());
		
		// load bpel resource
		ResourceSet resourceSet = new ResourceSetImpl();
		// URI uri =
		// URI.createFileURI(testFileDir.getAbsolutePath()+File.separator+"Agency.bpel");
		URI uri = URI.createFileURI(this.testFileDir.getAbsolutePath() + File.separator + "OrderingProcess.bpel");
		BPELResource resource = (BPELResource) resourceSet.createResource(uri);
		
		// prepare the inputStream
		// FileInputStream inputStream = new FileInputStream(new
		// File(testFileDir, "Agency.bpel"));
		FileInputStream inputStream = new FileInputStream(new File(this.testFileDir, "OrderingProcess.bpel"));
		Process process = BPEL4ChorReader.readBPEL(resource, inputStream);
		
		// create output stream
		File saveFile = new File(this.outputDir, process.getName() + "-" + Calendar.getInstance().getTimeInMillis() + ".bpel");
		FileOutputStream outputStream = new FileOutputStream(saveFile);
		
		// write BPEL process to outputStream
		BPEL4ChorWriter.writeBPEL(resource, outputStream);
		
		System.out.println("Result: " + saveFile.getAbsolutePath());
	}
	
	@Test
	public void testReadWSDL() throws WSDLException, IOException {
		
		String wsdlURI_1 = this.testFileDir.getAbsolutePath() + File.separator + "loanServicePT.wsdl";
		String wsdlURI_2 = this.testFileDir.getAbsolutePath() + File.separator + "ProcessBlaArtifacts.wsdl";
		String wsdlURI_3 = this.testFileDir.getAbsolutePath() + File.separator + "ProcessOrder.wsdl";
		String wsdlURI_4 = this.testFileDir.getAbsolutePath() + File.separator + "OrderingProcessSimple3.wsdl";
		MyWSDLUtil.readWSDL(wsdlURI_1);
		MyWSDLUtil.readWSDL(wsdlURI_2);
		MyWSDLUtil.readWSDL(wsdlURI_3);
		MyWSDLUtil.readWSDL(wsdlURI_4);
	}
	
	@Test
	public void testReadTopology() throws IOException, XMLStreamException {
		// init
		XMLInputFactory factory = XMLInputFactory.newInstance();
		FileInputStream fis = null;
		File file = new File(this.testFileDir.getAbsolutePath() + File.separator + "topology.xml");
		Topology top = null;
		
		fis = new FileInputStream(file);
		top = BPEL4ChorReader.readTopology(fis);
		
		// create output stream
		File saveFile = new File(this.outputDir, "topology-" + Calendar.getInstance().getTimeInMillis() + ".xml");
		FileOutputStream outputStream = new FileOutputStream(saveFile);
		
		// write Topology to outputStream
		BPEL4ChorWriter.writeTopology(top, outputStream);
		
		System.out.println("Result: " + saveFile.getAbsolutePath());
	}
	
	@Test
	public void testReadTopologyAndGrounding() throws IOException, XMLStreamException {
		// init
		XMLInputFactory factory = XMLInputFactory.newInstance();
		FileInputStream fis = null;
		FileInputStream fis2 = null;
		File file = new File(this.testFileDir.getAbsolutePath() + File.separator + "topology.xml");
		File file2 = new File(this.testFileDir.getAbsolutePath() + File.separator + "grounding.xml");
		Topology top = null;
		Grounding grounding = null;
		
		fis = new FileInputStream(file);
		fis2 = new FileInputStream(file2);
		top = BPEL4ChorReader.readTopology(fis);
		grounding = BPEL4ChorReader.readGrounding(fis2, top);
		
		// create output streams
		File saveFile = new File(this.outputDir, "topology-" + Calendar.getInstance().getTimeInMillis() + ".xml");
		FileOutputStream outputStream = new FileOutputStream(saveFile);
		File saveFile2 = new File(this.outputDir, "grounding-" + Calendar.getInstance().getTimeInMillis() + ".xml");
		FileOutputStream outputStream2 = new FileOutputStream(saveFile2);
		
		// write Topology to outputStream
		BPEL4ChorWriter.writeTopology(top, outputStream);
		// write Grounding to outputStream
		BPEL4ChorWriter.writeGrounding(grounding, outputStream2);
		
		System.out.println("ResultTopology: " + saveFile.getAbsolutePath());
		System.out.println("ResultGrounding: " + saveFile2.getAbsolutePath());
	}
}
