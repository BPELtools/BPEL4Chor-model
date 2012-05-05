package org.bpel4chor.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.bpel4chor.interfaces.INamespaceMap;

/**
 * BPEL4Chor Namespace map of (Prefix,Namespace), which also holds the reverse
 * map of (Namespace,Prefix)
 * 
 * <p>
 * This class borrows the implementation of INamespaceMap in Eclipse BPEL model,
 * and some methods are added to enable the map to collect namspaces from the
 * new element.
 *
 * @since Oct 26, 2011
 * @author Daojun Cui
 * 
 * @see {@link org.eclipse.bpel.model.adapters.BasicEObjectaAdapter}
 */
public class BPEL4ChorNamespaceMap implements INamespaceMap<String, String> {

	/** Forward, prefix to namespace map. */
	Map<String, String> prefix2Namespace = null;

	/** Reverse, Namespace to list of prefixes */
	Map<String, List<String>> namespace2Prefix = null;

	@Override
	public void clear() {
		if (namespace2Prefix != null) {
			namespace2Prefix.clear();
		}
		if (prefix2Namespace != null) {
			prefix2Namespace.clear();
		}
	}

	@Override
	public boolean containsKey(Object key) {
		return prefix2Namespace != null ? prefix2Namespace.containsKey(key) : false;
	}

	@Override
	public boolean containsValue(Object value) {

		return prefix2Namespace != null ? prefix2Namespace.containsValue(value) : false;
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {

		return prefix2Namespace != null ? prefix2Namespace.entrySet() : null;
	}

	@Override
	public String get(Object key) {

		return prefix2Namespace != null ? prefix2Namespace.get(key) : null;
	}

	@Override
	public boolean isEmpty() {
		return prefix2Namespace != null ? prefix2Namespace.isEmpty() : true;
	}

	@Override
	public Set<String> keySet() {
		if (prefix2Namespace == null) {
			return Collections.emptySet();
		}
		return prefix2Namespace.keySet();
	}

	/**
	 * @param key
	 *            the key to set
	 * @param value
	 *            the value to set.
	 * @return the old value, if set
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public String put(String key, String value) {
		prefix2Namespace = getPrefix2NamespaceMap();
		String oldValue = prefix2Namespace.put(key, value);

		// when we put something in prefix2NamespaceMap
		// it might not exist in the reverse map yet
		List<String> ns2pfx = getReverse4(value);
		if (ns2pfx.contains(key) == false) {
			ns2pfx.add(key);
		}

		return oldValue;
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {

		getPrefix2NamespaceMap().putAll(m);

	}

	@Override
	public String remove(Object key) {

		String value = prefix2Namespace.remove(key);

		// the key did not exist.
		if (value == null) {
			return value;
		}

		// if oldValue is not null, remove in the reverse map too,
		// in this case , the entry in reverse map must exist.
		namespace2Prefix.get(value).remove(key);

		return value;
	}

	@Override
	public int size() {

		return prefix2Namespace != null ? prefix2Namespace.size() : 0;
	}

	@Override
	public Collection<String> values() {
		if (prefix2Namespace != null)
			return prefix2Namespace.values();
		else
			return Collections.emptyList();
	}

	/**
	 * @param key
	 *            the namespace to get the reverse mapping for
	 * @return The reverse mapping of the Namespace to namespace prefixes.
	 */
	@Override
	public List<String> getReverse(String key) {

		return getReverse4(key);
	}

	/**
	 * Get the prefix2Namespace map, initial capacity is 5.
	 * 
	 * @return
	 */
	Map<String, String> getPrefix2NamespaceMap() {
		if (prefix2Namespace == null) {
			prefix2Namespace = new HashMap<String, String>(5);
		}
		return prefix2Namespace;
	}

	/**
	 * Get the reverse map, initial capacity is 5.
	 * 
	 * @param key
	 * @return
	 */
	List<String> getReverse4(String key) {
		if (namespace2Prefix == null) {
			namespace2Prefix = new HashMap<String, List<String>>(5);
		}
		List<String> prefixes = namespace2Prefix.get(key);
		if (prefixes == null) {
			prefixes = new ArrayList<String>();
			namespace2Prefix.put(key, prefixes);
		}
		return prefixes;
	}

	/**
	 * Add the namespaceURI into map
	 * 
	 * <p>
	 * It does something more then the <code>put</code> method, you just needs
	 * to provide a namespaceURI and any prefixBase, the prefix will be created
	 * for you.
	 * 
	 * <p>
	 * <ol>
	 * <li>As default the prefix blank ("") will be assigned to
	 * "http://docs.oasis-open.org/wsbpel/2.0/process/abstract"
	 * 
	 * <li>if the namespaceURI is a new one, a prefix for it will be created.
	 * the key,value pair is inserted and then the created prefix is returned.
	 * 
	 * <li>if the namespaceURI already exists, it just returns the first one of
	 * the found corresponding prefixes.
	 * </ol>
	 * 
	 * @param namespaceURI
	 *            the namespaceURI to add
	 * @param prefixBase
	 *            the base string for creating prefix
	 * @return the prefix assigned to the given namespaceURI
	 */
	@Override
	public String addNamespace(String namespaceURI, String prefixBase) {

		if (namespaceURI == null || prefixBase == null)
			throw new NullPointerException("argument is null.");

		String pfxBase = prefixBase;
		if (namespaceURI == null || namespaceURI.isEmpty())
			return null;
		if (pfxBase == null || pfxBase.isEmpty()) {
			pfxBase = BPEL4ChorConstants.CHOREOGRAPHY_PREFIX_BASE;
		}

		// retrieve the matches for this namespaces
		List<String> prefixes = this.getReverse(namespaceURI);

		if (prefixes.isEmpty()) {
			int i = 1;
			String newPrefix = pfxBase;
			while (this.containsKey(newPrefix)) {
				newPrefix = pfxBase + i;
				i++;
			}
			this.put(newPrefix, namespaceURI);
			return newPrefix;
		} else {
			String firstFoundPrefix = prefixes.get(0);
			return firstFoundPrefix;
		}
	}

	/**
	 * Collect the namespace into map
	 * 
	 * <p>
	 * <ol>
	 * <li>Case 1: Prefix==null, NSUri==null, nothing to collect
	 * <li>Case 2: Prefix==null, NSUri!=null, add the nsUri into map anyway, if
	 * the nsUri exists already, it just returns the first one of the associated
	 * prefixes. if the namespace does not exists yet, it is inserted in the
	 * namespaceMap and then returns the created prefix.
	 * <li>Case 3: Prefix!=null, NSUri==null, nothing to collect but check the
	 * prefix anyway, in case the prefix pointed to a non-existed one in the
	 * map.
	 * <li>Case 4: Prefix!=null, NSUri!=null, check the prefix first, if the
	 * prefix exists, the get(prefix) does not conform to this nsUri, then
	 * create one new prefix for this nsUri, if the prefix does not exist, add
	 * the prefix,namespace pair into the namespace map.
	 * 
	 * 
	 * @param qName
	 * @return the new version of the qName
	 * @throws Exception
	 */
	@Override
	public QName collectNamespace(QName qName) {

		if (qName == null)
			throw new NullPointerException("argument is null.");

		QName resQName = qName;

		int analRes = BPEL4ChorUtil.analyseQName(qName);

		String nsUri = qName.getNamespaceURI();
		String localPart = qName.getLocalPart();
		String prefix = qName.getPrefix();

		switch (analRes) {
		case BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_00: {
			break;// nothing to collect
		}
		case BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_01: {

			// add namespace to map anyway,
			// if the namespace exists in the namespaceMap already, it just
			// returns the first one of the associated prefixes.
			// if the namespace does not exists yet, it is inserted in the
			// namespaceMap and then returns the created prefix.
			prefix = this.addNamespace(nsUri, BPEL4ChorConstants.CHOREOGRAPHY_PREFIX_BASE);

			// should Update QName
			resQName = new QName(nsUri, localPart, prefix);
			break;
		}
		case BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_10: {

			// nothing to collect but check the prefix anyway
			if (!this.containsPrefix(prefix)) {
				throw new RuntimeException("Illegal Prefix: " + prefix + " namespaceMap(" + prefix2Namespace.keySet()
						+ ")");
			}
			break;
		}
		case BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_11: {

			// check the prefix first
			boolean prefixExists = containsPrefix(prefix);
			if (prefixExists) {
				// if the prefix exists, check whether the retrieved namespace
				// conforms to this nsUri
				String prestoredNS = get(prefix);
				if (!prestoredNS.equalsIgnoreCase(nsUri)) {

					// the namespace is not the same to the one pre-stored in
					// the map,
					// then add the namespace into map and create a
					// corresponding new prefix.
					prefix = addNamespace(nsUri, BPEL4ChorConstants.CHOREOGRAPHY_PREFIX_BASE);

					// should Update QName
					resQName = new QName(nsUri, localPart, prefix);
				}
			} else {
				// if the prefix does not exist, add the prefix,namespace pair
				// into the namespace map
				put(prefix, nsUri);
			}

			break;
		}
		default:
			throw new RuntimeException("illegal QName(" + nsUri + ", " + localPart + ", " + prefix + ")");
		}

		return resQName;
	}

	/**
	 * Check out whether the prefix exists in the keySet of (prefix,namespace)
	 * map
	 * 
	 * @param prefix
	 * @return whether the prefix exists
	 */
	@Override
	public boolean containsPrefix(String prefix) {
		if (prefix == null)
			throw new NullPointerException("argument is null.");
		return this.containsKey(prefix);
	}

	/**
	 * Check out whether the namespaceUri exist in the namespaceMap
	 * 
	 * @param namespace
	 *            The namespace of a QName
	 * @param namespaceMap
	 *            The namespaceMap of the root
	 * @return <tt>true</tt> if namespace exist, or <tt>false</tt>.
	 */
	public boolean containsNamespace(String namespace) {
		if (namespace == null)
			throw new NullPointerException("argument is null.");
		return this.containsValue(namespace);
	}
}
