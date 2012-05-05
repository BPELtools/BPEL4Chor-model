package org.bpel4chor.interfaces;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * Namespace map of K,V which also holds the reverse map of V,K
 *  
 * @param <K>
 * @param <V>
 */

public interface INamespaceMap <K,V> extends Map<K,V> {
    
    /**
     * Get the entry under key V (which is the value).
     * 
     * @param key the value key
     * @return the list of prefix names 
     */
    public List<K> getReverse ( V key );
    
//    /**
//     * Get the V whose K ends with the given "key"-suffix
//     * 
//     * @param <V>
//     * @param key
//     */
//    public V getWithSuffix(String key);
//    
    /**
     * Collect the namespace into map
     * @param qName
     * @return the new version of the qName
     */
    public QName collectNamespace(QName qName);
    
    /**
     * Whether the prefix already exists
     * @param key
     * @return
     */
    public boolean containsPrefix(K prefix);
    
    /**
     * Whether the val already exists
     * @param val
     * @return
     */
    public boolean containsNamespace(V namespace);
    
    /**
     * Add the namespace into map
     * 
     * @param namespace the namespace to add
     * @param prefixBase the base string for creating Entry.Key of the (prefix, namespace) map.
     * @return
     */
    public String addNamespace(String namespace, String prefixBase);
}