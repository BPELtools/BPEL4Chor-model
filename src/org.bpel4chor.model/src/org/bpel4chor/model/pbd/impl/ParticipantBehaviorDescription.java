package org.bpel4chor.model.pbd.impl;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Process;

/**
 * Implementation of ParticipantBehaviorDescription
 * 
 * <p>
 * It is associated to a BPEL process via the property <tt>process</tt>.
 * 
 * @since Oct 21, 2011
 * @author Daojun.Cui
 */
public class ParticipantBehaviorDescription {

	/** the bpel process */
	private Process process;
	
//	/** activity --> id map */
//	private Map<Activity, String> act2Id;
	
	/**
	 * Constructor
	 */
	public ParticipantBehaviorDescription(){
		
	}
	
	/**
	 * Constructor
	 * @param process the abstract bpel process
	 */
	public ParticipantBehaviorDescription(Process process)
	{
		this.process = process;
	}
	
	public Process getProcess() {
		return this.process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public String getId(Activity act) {
		String wsuId = act.getElement().getAttribute("wsu:id");
		if(wsuId==null || wsuId.isEmpty()){
			wsuId = act.getElement().getAttribute("name");
		}
		return wsuId;
	}

	public String toString() {
		return "ParticipantBehaviorDescriptionImpl [process=" + process
				+ "]";
	}

}


