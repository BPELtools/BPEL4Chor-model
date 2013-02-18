package org.bpel4chor.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.OnMessage;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Receive;
import org.eclipse.bpel.model.Reply;
import org.eclipse.bpel.model.resource.BPELResource;
import org.eclipse.bpel.model.resource.BPELWriter;
import org.eclipse.bpel.model.util.BPELConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * MyBPELWriter will adapt the BPELWriter to write out the BPEL process in term
 * of Participant Behavior Description.
 * 
 * <p>
 * It adds an extra attribute 'wsu:id" into the communication activity element,
 * so that all element in PBD can be recognized again.
 * 
 * 
 * <p>
 * <b>Code Snippet to save a BPEL process</b>:<br>
 * <code><pre>
 * 
 * // init
	BPELPlugin bpelPlugin = new BPELPlugin();
	Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new BPELResourceFactoryImpl());
		
		
	// load process
	ResourceSet resourceSet = new ResourceSetImpl();
	URI uri = URI.createFileURI("D:\\DA-Doc\\files\\Agency.bpel");
	boolean loadOnDemand = true;
	BPELResource resource = (BPELResource) resourceSet.getResource(uri, loadOnDemand);
	org.eclipse.bpel.model.Process process = 
			(org.eclipse.bpel.model.Process)resource.getContents().get(0);
		
		
	// save process
	try {
		boolean UseNSPrefix = false;
		File saveFile = new File("D:\\DA-Doc\\files\\"+process.getName()+"-"+Calendar.getInstance().getTimeInMillis()+".bpel");
		saveFile.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(saveFile);
		resource.setOptionUseNSPrefix(UseNSPrefix);
		//	resource.save(outputStream, null);
		//  this line is aquivalent to the following 2 lines ==>
		//
		BPELWriter writer = new BPELWriter();
		writer.write(resource, outputStream, null);
		System.out.println("result: " + saveFile.getAbsolutePath());
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
 * </pre></code>
 * 
 * 
 * @since Oct 30, 2011
 * @author Daojun Cui
 */
public class PBDWriter extends BPELWriter {
	
	/** counter for wsu:id */
	protected static int wsuIdCounter = 0;
	
	
	@Override
	public void write(BPELResource resource, OutputStream out, Map<?, ?> args) throws IOException {
		super.write(resource, out, args);
	}
	
	@Override
	protected Document resource2XML(BPELResource resource) {
		
		return super.resource2XML(resource);
	}
	
	/**
	 * Extend to assure that the process namespaces must contain the namespace
	 * for 'wsu:id'
	 * 
	 * @param process
	 * @return
	 */
	@Override
	protected Element process2XML(Process process) {
		
		Element procElement = super.process2XML(process);
		String attrAbstProcProf = procElement.getAttribute("abstractProcessProfile");
		if ((attrAbstProcProf == null) || attrAbstProcProf.isEmpty()) {
			procElement.setAttribute("abstractProcessProfile", BPEL4ChorConstants.PBD_ABSTRACT_PROCESS_PROFILE);
		}
		// Quick hack to add correct namespace to element
		// We assume here that the elements are written without any prefix
		// The Eclipse BPEL writer does NOT add any "xmlns" prefix
		// In "normal operation mode", there is a "bpel"-prefix written by the
		// designer
		procElement.setAttribute("xmlns", BPELConstants.NAMESPACE_ABSTRACT_2007);
		
		// Set the namespace for the wsu:id element
		procElement.setAttribute("xmlns:wsu", BPEL4ChorConstants.XMLNS_WSU);
		
		return procElement;
	}
	
	/**
	 * Make sure the the community activities contain the attribute 'wsu:id'
	 * 
	 * @param activity
	 * @return
	 */
	@Override
	public Element activity2XML(Activity activity) {
		return super.activity2XML(activity);
	}
	
	@Override
	protected Element invoke2XML(Invoke activity) {
		
		Element element = super.invoke2XML(activity);
		if (activity.getPartnerLink() != null) {
			element.removeAttribute("partnerLink");
		}
		if (activity.getPortType() != null) {
			element.removeAttribute("portType");
		}
		if (activity.getOperation() != null) {
			element.removeAttribute("operation");
		}
		// get wsu:id
		element.setAttribute("wsu:id", WSUIDGenerator.getId());
		return element;
	}
	
	@Override
	protected Element receive2XML(Receive activity) {
		
		Element element = super.receive2XML(activity);
		if (activity.getPartnerLink() != null) {
			element.removeAttribute("partnerLink");
		}
		if (activity.getPortType() != null) {
			element.removeAttribute("portType");
		}
		if (activity.getOperation() != null) {
			element.removeAttribute("operation");
		}
		// get wsu:id
		element.setAttribute("wsu:id", WSUIDGenerator.getId());
		return element;
	}
	
	@Override
	protected Element reply2XML(Reply activity) {
		Element element = super.reply2XML(activity);
		if (activity.getPartnerLink() != null) {
			element.removeAttribute("partnerLink");
		}
		if (activity.getPortType() != null) {
			element.removeAttribute("portType");
		}
		if (activity.getOperation() != null) {
			element.removeAttribute("operation");
		}
		// get wsu:id
		element.setAttribute("wsu:id", WSUIDGenerator.getId());
		return element;
	}
	
	@Override
	protected Element onMessage2XML(OnMessage onMsg) {
		
		Element element = super.onMessage2XML(onMsg);
		element.setAttribute("wsu:id", WSUIDGenerator.getId());
		return element;
		
	}
}
