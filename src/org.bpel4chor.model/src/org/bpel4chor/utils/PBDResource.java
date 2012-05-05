package org.bpel4chor.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.adapters.INamespaceMap;
import org.eclipse.bpel.model.resource.BPELResourceImpl;
import org.eclipse.bpel.model.util.BPELConstants;
import org.eclipse.bpel.model.util.BPELUtils;

/**
 * PBD Resource to invoke PBDWriter to write Process Behavior Description to
 * file.
 * 
 * @author Daojun Cui
 * @since Jun 13, 2012
 * 
 */
public class PBDResource extends BPELResourceImpl {

	/**
	 * do save by invoking PWDWriter instead of invoking BPELWriter
	 */
	@Override
	public void doSave(OutputStream out, Map<?, ?> args) throws IOException {
		INamespaceMap<String, String> nsMap = BPELUtils.getNamespaceMap(this.getProcess());
		if (getOptionUseNSPrefix()) {
			nsMap.remove("");
			List<String> prefix = nsMap.getReverse(getNamespaceURI());
			if (prefix.isEmpty()) {
				nsMap.put(BPELConstants.PREFIX, getNamespaceURI());
			}
		} else {
			nsMap.put("", getNamespaceURI());
		}

		PBDWriter writer = new PBDWriter();
		writer.write(this, out, args);
	}

}
