package org.bpel4chor.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELExtensibleElement;
import org.eclipse.bpel.model.BPELPlugin;
import org.eclipse.bpel.model.OnMessage;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.resource.BPELReader;
import org.eclipse.bpel.model.resource.BPELResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * MyBPELReader reads BPEL file and Participant Behavior Description(PBD) file.
 * 
 * <p>
 * MyBPELReader adapts the BPELReader to read in the BPEL file and create BPEL
 * Process, and at the mean time it attaches a wsu:id attribute in the invoke-,
 * reply- and receive-activity element.
 * 
 * @since Nov 2, 2011
 * @author Daojun Cui
 */
public class MyBPELReader extends BPELReader {

	static {
		// init
		BPELPlugin bpelPlugin = new BPELPlugin();
		// setup the extension to factory map, so that the proper ResourceFactory can be used to read the file.
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("bpel", new BPELResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());
		
	}

	/**
	 * The name to Activity-Element map, it maps the name/wsu:id to
	 * Activity-Element.
	 */
	Map<String, Set<Element>> name2ElementMap = new HashMap<String, Set<Element>>();

	/**
	 * The root activity element in the BPEL process
	 */
	Element rootActElement = null;

	/**
	 * Scan from root element to leaves element, and collect name->element pair
	 * into the name2ElementMap, if there is multiple elements that share the
	 * same name, it will be inserted in the element-set.
	 * 
	 * <p>
	 * Only the element which get 'name' attribute can be collected into the
	 * map.
	 * 
	 * <p>
	 * The purpose of this map is to tell whether a name is already assigned to
	 * an activity or not.
	 * 
	 * @param actElement
	 * @return the name to element-set map
	 */
	public void scan(Element actElement) {
		if (actElement == null)
			throw new NullPointerException();

		String name = actElement.getAttribute("name");
		if (name != null && !name.isEmpty()) {
			Set<Element> elemSet = name2ElementMap.get(name);
			if (elemSet == null) {
				elemSet = new HashSet<Element>();
			}
			elemSet.add(actElement);
			name2ElementMap.put(name, elemSet);
		}

		// recursively navigate into the child, top-down
		if (actElement.hasChildNodes()) {
			Node node = actElement.getFirstChild();
			do {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					scan((Element) node);
				}
				node = node.getNextSibling();
			} while (node != null);
		}

	}

//	/**
//	 * Check if the given name value is unique in the activities hierarchy.
//	 * 
//	 * <p>
//	 * It runs through all the activity nodes if there is any node that contains
//	 * the 'name=val' which equals the given name value, it returns
//	 * <tt>false</tt>, else <tt>true</tt>.
//	 * 
//	 * <p>
//	 * if the given nameVal is <tt>null</tt> or <tt>empty</tt>, it returns
//	 * <tt>false</tt>.
//	 * 
//	 * @param nameVal
//	 * 
//	 * @return
//	 */
//	protected boolean isUniqueName(String nameVal) {
//		if (nameVal == null || nameVal.isEmpty())
//			return false;
//
//		boolean unique = false;
//
//		String name = rootActElement.getAttribute("name");
//		if (name != null && !name.isEmpty()) {
//
//		}
//
//		return unique;
//	}

	/**
	 * Get name to Element Map
	 * 
	 * @return
	 */
	public Map<String, Set<Element>> getName2ElementMap() {
		return name2ElementMap;
	}

	/**
	 * Clean up the map
	 */
	public void clearName2ElementMap() {
		name2ElementMap.clear();
	}

	@Override
	protected Process xml2Process(Element processElement) {

		// get the root activity element
		Element actElement = getActivityElement(processElement);

		// scan all activity elements
		scan(actElement);

		return super.xml2Process(processElement);

	}

	/**
	 * Get the activity element in the children of processElement
	 * 
	 * @param processElement
	 * @return
	 */
	protected Element getActivityElement(Element processElement) {
		if (processElement == null)
			return null;
		Element actElement = null;
		String[] actLocalNames = { "receive", "reply", "invoke", "assign", "throw", "exit", "wait", "empty",
				"sequence", "if", "while", "pick", "flow", "scope", "compensate", "compensateScope", "rethrow",
				"extensionActivity", "opaqueActivity", "forEach", "repeatUntil", "validate" };

		// get the activity element of the process element
		for (int i = 0; i < processElement.getChildNodes().getLength(); i++) {
			Node node = processElement.getChildNodes().item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = ((Element) node).getLocalName();
				for (String actLocalName : actLocalNames) {
					if (localName.equals(actLocalName)) {
						actElement = (Element) node;
						return actElement;
					}
				}

			}
		}

		return actElement;
	}

	@Override
	protected OnMessage xml2OnMessage(Element onMessageElement) {
		OnMessage onMsg = super.xml2OnMessage(onMessageElement);
		onMsg = (OnMessage) insertWsuId(onMsg);
		return onMsg;
	}

	@Override
	protected Activity xml2Invoke(Element invokeElement) {
		Activity invoke = super.xml2Invoke(invokeElement);

		invoke = (Activity) insertWsuId(invoke);

		return invoke;
	}

	@Override
	protected Activity xml2Reply(Element replyElement) {
		Activity reply = super.xml2Reply(replyElement);
		reply = (Activity) insertWsuId(reply);
		return reply;
	}

	@Override
	protected Activity xml2Receive(Element receiveElement) {
		Activity receive = super.xml2Receive(receiveElement);
		receive = (Activity) insertWsuId(receive);
		return receive;
	}

	/**
	 * Insert wsu:id into the activity element
	 * <p>
	 * It will be transparent to the BPEL process and will be taken out by
	 * BPEL4ChorWriter.
	 * 
	 * @param activity
	 *            The BPEL Element
	 * @return the updated activity element
	 */
	public BPELExtensibleElement insertWsuId(BPELExtensibleElement activity) {

		String attrName = activity.getElement().getAttribute("name");
		String wsuIdBase = null;
		String wsuId = null;
		int i = 1;

		if (attrName == null || attrName.isEmpty()) {
			//
			// attrName is NULL or EMPTY means that the activity does NOT
			// contain attribute 'name=...',
			// it certainly does NOT exist in name2ElementMap neither.
			// then a new name must be created
			//
			wsuIdBase = activity.getElement().getTagName();
			wsuId = wsuIdBase + i;
			while (activity.getElement().getAttribute("wsu:id") == null
					|| activity.getElement().getAttribute("wsu:id").isEmpty()) {
				Set<Element> elemSet = name2ElementMap.get(wsuId);
				if (elemSet == null || elemSet.isEmpty()) {
					// the attrVal is not used yet, now insert into the elemSet
					// and put into name2ElementMap
					activity.getElement().setAttribute("wsu:id", wsuId);
					elemSet = new HashSet<Element>();
					elemSet.add(activity.getElement());
					name2ElementMap.put(wsuId, elemSet);

				} else {
					// the attrVal is already mapped to an element in the
					// name2ElementMap, and our current element
					// contains NO 'name=...', so this mapped element CAN'T be
					// it.
					// the attrVal must be re-created.
					i++;
					wsuId = wsuIdBase + i;
				}
			}

		} else {
			//
			// attrName is not NULL and not EMPTY means that the activity does
			// contain attribute 'name=...',
			// and it maps to one or multiple elements in the name2ElementMap
			//
			wsuIdBase = attrName;
			wsuId = wsuIdBase;
			// try to use the 'name=...' for 'wsu:id=...'
			Set<Element> elemSet = name2ElementMap.get(wsuId);
			if (elemSet == null || elemSet.isEmpty()) {
				throw new IllegalStateException(wsuId + " does not exist in name2ElementMap "
						+ name2ElementMap.toString());
			} else if (elemSet.size() == 1) {
				// this name is the only one that maps to the element,
				// it is such because this name-element mapping previously was
				// scanned in the map.
				// so the wsu:id will use the value from the 'name=...'
				activity.getElement().setAttribute("wsu:id", wsuId);
			} else {
				// elemSet.size > 1,
				// this name is mapped to multiple element,
				// a new name will be created
				wsuId = wsuIdBase + i;
				while (activity.getElement().getAttribute("wsu:id") == null
						|| activity.getElement().getAttribute("wsu:id").isEmpty()) {
					elemSet = name2ElementMap.get(wsuId);
					if (elemSet == null || elemSet.isEmpty()) {
						activity.getElement().setAttribute("wsu:id", wsuId);
						elemSet = new HashSet<Element>();
						elemSet.add(activity.getElement());
						name2ElementMap.put(wsuId, elemSet);
					} else {
						i++;
						wsuId = wsuIdBase + i;
					}
				}
			}

		}

		return activity;
	}

}