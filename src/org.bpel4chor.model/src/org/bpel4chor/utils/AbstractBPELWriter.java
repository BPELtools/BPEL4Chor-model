package org.bpel4chor.utils;

import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.resource.BPELWriter;
import org.eclipse.bpel.model.util.BPELConstants;
import org.w3c.dom.Element;

/**
 * Writer to extend the standard BPEL-Writer for abstract BPEL Processes without
 * Copyright 2013 IAAS University of Stuttgart <br>
 * <br>
 * 
 * @author Peter.Debicki
 * 
 */
public class AbstractBPELWriter extends BPELWriter {
	
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
		
		return procElement;
	}
	
}
