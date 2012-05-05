package org.bpel4chor.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.wsdl.WSDLException;

import org.bpel4chor.model.grounding.impl.Grounding;
import org.bpel4chor.model.topology.impl.Topology;
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
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("bpel",
				new BPELResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl",
				new WSDLResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd",
				new XSDResourceFactoryImpl());

	}

	/**
	 * Read from the given input stream into the given resource
	 * 
	 * <p>
	 * It will be delegate to {@link MyBPELReader}.
	 * 
	 * @param resource
	 *            The BPELResource that relates to the BPEL File URI
	 * @param inputStream
	 *            The InputStream that relates to the BPEL File
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

		if (filePath == null)
			throw new NullPointerException();
		if (filePath.isEmpty())
			throw new IllegalStateException("file path is empty.");

		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI(filePath);
		boolean loadOnDemand = true;
		Resource resource = resourceSet.getResource(uri, loadOnDemand);
		return (Process) resource.getContents().get(0);
	}

	/**
	 * Read the WSDL document into a definition
	 * 
	 * @param wsdlURI
	 *            The WSDL URI
	 * @return
	 * @throws WSDLException
	 * @throws IOException
	 */
	public static Definition readWSDL(String wsdlURI) throws WSDLException, IOException {

		if (wsdlURI == null || wsdlURI.isEmpty())
			throw new IllegalArgumentException();

		if (!wsdlURI.endsWith(".wsdl"))
			throw new IllegalArgumentException("invalid wsdl uri. " + wsdlURI);

		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("wsdl", new WSDLResourceFactoryImpl());
		Resource resource = rs.createResource(URI.createFileURI(wsdlURI));
		resource.load(null);
		Definition root = (Definition) resource.getContents().iterator().next();
		return root;
	}

	/**
	 * Read in the topology file from the given InputStream
	 * 
	 * @param inputStream
	 * @return
	 * 
	 */
	public static Topology readTopology(InputStream inputStream) {
		throw new IllegalStateException("unimplemented function");
	}

	/**
	 * Read in the grounding file from the given InputStream
	 * 
	 * @param inputStream
	 * @return
	 * 
	 */
	public static Grounding readGrounding(InputStream inputStream) {
		throw new IllegalStateException("unimplemented function");
	}

}
