package org.bpel4chor.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bpel4chor.model.topology.impl.Topology;

public class BPEL4ChorNameGenerator {
	
	protected Topology topology = null;
	
	protected Set<String> existedMessageLInkNames = new HashSet<String>();
	
	public BPEL4ChorNameGenerator(Topology topology) {
		if (topology == null)
			throw new NullPointerException();

		this.topology = topology;
		this.initMessageLinkNames();
	}

	private void initMessageLinkNames() {

		List<org.bpel4chor.model.topology.impl.MessageLink> msgLinks = this.topology
				.getMessageLinks();
		
		for(org.bpel4chor.model.topology.impl.MessageLink msgLink : msgLinks) {
			if(existedMessageLInkNames.contains(msgLink.getName()) == false) {
				existedMessageLInkNames.add(msgLink.getName());
			}
		}
		
	}
	
	public String getUniqueTopoMsgLinkName(String sugguestMsgLinkName) {
		if (sugguestMsgLinkName == null || sugguestMsgLinkName.isEmpty())
			throw new IllegalArgumentException("illegal argument.");
		
		String uniqueName = new String(sugguestMsgLinkName);
		
		if (existedMessageLInkNames.contains(uniqueName) == false) {
			existedMessageLInkNames.add(uniqueName);
			return uniqueName;
		}
		
		int i = 1;
		do {
			// uniqueName = uniqueName + String.format(format, i++);
			uniqueName = uniqueName + i;
		} while (existedMessageLInkNames.contains(uniqueName));
		existedMessageLInkNames.add(uniqueName);

		return uniqueName;
	}
}
