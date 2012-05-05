package org.bpel4chor.model.utils;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.bpel4chor.interfaces.INamespaceMap;
import org.bpel4chor.utils.BPEL4ChorConstants;
import org.bpel4chor.utils.BPEL4ChorNamespaceMap;
import org.junit.Before;
import org.junit.Test;

public class BPEL4ChorNamespaceMapTest {

	QName qName1, qName2, qName3, qName4;
	
	INamespaceMap<String, String> map;

	
	@Before
	public void setup()
	{
		qName1 = new QName("qName1");                        // Non Prefixed AttrName(prefix, nsUri = 00)
		qName2 = new QName("www.example.com", "qName2");     // Non Prefixed AttrName(prefix, nsUri = 01)
		qName3 = new QName("", "qName3", "p");               // Prefixed AttrName_(prefix, nsUri = 10)
		qName4 = new QName("www.example.com", "qName4", "p");// Prefixed AttrName_(prefix, nsUri = 11)
		
		map = new BPEL4ChorNamespaceMap();
		map.put("", "http://docs.oasis-open.org/wsbpel/2.0/process/abstract");
		map.put("ns", "urn:choreograhy:topology");
		map.put("ns1", "http://www.example.com");

	}
	
//	@Test
//	public void getWithSuffixTest()
//	{
//		String prefix1 = "ns1";
//		String prefix2 = "xmlns";
//		String prefix3 = "xzy";
//		
//		Assert.assertEquals("http://www.example.com", map.getWithSuffix(prefix1));
//		Assert.assertEquals("http://docs.oasis-open.org/wsbpel/2.0/process/abstract", map.getWithSuffix(prefix2));
//		Assert.assertEquals(true, map.getWithSuffix(prefix3)==null || map.getWithSuffix(prefix3).isEmpty());
//	}
	
	@Test
	public void containsPrefixTest()
	{
		String prefix1 = "notExist"; // prefix exist == false
		String prefix2 = "ns1";      // prefix exist == true
		String prefix3 = "ns"; // prefix exist == true
		
		Assert.assertEquals(false, map.containsPrefix(prefix1));
		Assert.assertEquals(true, map.containsPrefix(prefix2));
		Assert.assertEquals(true, map.containsPrefix(prefix3));
	}
	
	@Test
	public void containsNamspaceTest()
	{
		String ns1 = "http://docs.oasis-open.org/wsbpel/2.0/process/abstract"; // exist == true
		String ns2 = "http://not-exist.com"; // exist == false
		String nullNS = null;
		Assert.assertEquals(true, map.containsNamespace(ns1));
		Assert.assertEquals(false, map.containsNamespace(ns2));
		try{
			map.containsNamespace(nullNS);
			Assert.fail();
		} catch(Exception e) {
			Assert.assertEquals(true, e instanceof NullPointerException);
		}
	}
	
	@Test
	public void addNamespaceStringStringTest()
	{
		String prefixBase = BPEL4ChorConstants.CHOREOGRAPHY_PREFIX_BASE; 
		
		String existNS1 = "http://docs.oasis-open.org/wsbpel/2.0/process/abstract"; // namespace exist == true && it is the default namespace
		String actPrefix1 = map.addNamespace(existNS1, prefixBase);
		Assert.assertEquals("", actPrefix1);
		
		String existNS2 = "urn:choreograhy:topology";
		String actPrefix2 = map.addNamespace(existNS2, prefixBase);
		Assert.assertEquals("ns", actPrefix2);
		
		String nonExistNS3 = "http://www.eclipse.org/bpel";// this one does not exists, a new prefix should be created
		String actPrefix3 = map.addNamespace(nonExistNS3, prefixBase);
		Assert.assertEquals("cns", actPrefix3);
		
		String nonExistNS4 = "http://www.eclipse.org/emf";// this one does not neither, a new prefix should be created,too.
		String actPrefix4 = map.addNamespace(nonExistNS4, prefixBase);
		Assert.assertEquals("cns1", actPrefix4);
	}
	
	@Test
	public void collectNamespaceQNameTest() throws Exception
	{
		map = new BPEL4ChorNamespaceMap();
		map.put("", "http://docs.oasis-open.org/wsbpel/2.0/process/abstract");
		map.put("ns", "urn:choreograhy:topology");
		map.put("ns1", "http://www.example.com");
		
		Set<String> set = new HashSet<String>(); 
		set.addAll(map.keySet());
		
		String oldStr = null;
		String newStr = null;
		
		int expectedSize;
		int actualSize;
		
		// Test case 1. QNAME_PREFIX_NAMESPACE_00
		// prefix == null, namespace == null
		// expected result : nothing is collected
		QName qName1 = new QName("travler");                          // PREFIX_NAMESPACE_00
		
		map.collectNamespace(qName1);
		boolean noChange1 = (map.size() == 3);
		boolean noChange2 = (map.keySet().containsAll(set));
		Assert.assertEquals(true, noChange1 && noChange2);
		
		// Test case 2. QNAME_PREFIX_NAMESPACE_01
		// prefix == null, namespace != null and not exist
		// expected 
		QName qName2 = new QName("http://www.w3c.com", "travler", "");// PREFIX_NAMESPACE_01
		expectedSize = map.size() + 1;
		map.collectNamespace(qName2);
		actualSize = map.size();
		String prefix = map.getReverse("http://www.w3c.com").get(0);
		Assert.assertEquals("cns", prefix);
		Assert.assertEquals(expectedSize, actualSize);
		
		// Test case 3. QNAME_PREFIX_NAMESPACE_10
		// prefix != null, namespace == null
		// expected : qName3 exist, nothing is added to map, 
		//            qName4 does not exist, it raise an exception
		QName qName3 = new QName("", "travler", "ns1");               // PREFIX_NAMESPACE_10
		QName qName4 = new QName("", "travler", "ns11111");           // PREFIX_NAMESPACE_10, it is suppose to be exceptional 
		expectedSize = map.size();
		map.collectNamespace(qName3);// nothing is supposed to be added to the map
		Assert.assertEquals(expectedSize, map.size());
		
		boolean thrown = false;
		try{
			// expected an exception
			map.collectNamespace(qName4);
		} catch (Exception e){
			thrown = true;
		}
		Assert.assertEquals(true, thrown);
		
		// Test case 4. QNAME_PREFIX_NAMESPACE_11
		// prefix != null, namespace != null
		// expected : qName5 nothing changed, because the prefix and namespace both are present in namespaceMap
		//            qName6 , namespace added, prefix is changed.
		//            qName7 , namespace added, 
		QName qName5 = new QName("urn:choreograhy:topology", "travler", "ns");// prefix exist, namespace ==
		QName qName6 = new QName("http://www.w3c.com/schema", "travler", "ns");// prefix exist, namespace !=
		QName qName7 = new QName("http://www.w3c.com/schema", "travler", "xsd");// prefix not exist,  
		
		Set<Entry<String, String>> expectedSet = map.entrySet();
		map.collectNamespace(qName5);
		Set<Entry<String, String>> actualSet = map.entrySet();
		Assert.assertEquals(true, actualSet.equals(expectedSet));
		
		expectedSize = map.size()+1;
		oldStr = qName6.getPrefix();
		qName6 = map.collectNamespace(qName6);
		actualSize = map.size();
		newStr = qName6.getPrefix();
		Assert.assertEquals(expectedSize, actualSize);
		Assert.assertEquals(true, !map.getReverse(qName6.getNamespaceURI()).get(0).isEmpty());
		Assert.assertFalse(oldStr.equals(newStr));
		
		expectedSize = map.size()+1;
		map.collectNamespace(qName7);
		actualSize = map.size();
		Assert.assertEquals(expectedSize, actualSize);
		Assert.assertEquals(qName7.getNamespaceURI(), map.get("xsd"));
	}
	
	
}
