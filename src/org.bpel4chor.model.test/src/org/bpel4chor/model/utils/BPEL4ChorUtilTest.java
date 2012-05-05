package org.bpel4chor.model.utils;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.bpel4chor.interfaces.INamespaceMap;
import org.bpel4chor.utils.BPEL4ChorNamespaceMap;
import org.bpel4chor.utils.BPEL4ChorUtil;
import org.junit.Before;
import org.junit.Test;

public class BPEL4ChorUtilTest {

	QName qName1, qName2, qName3, qName4;
	INamespaceMap<String, String> map;
	
	@Before
	public void setup()
	{
		qName1 = new QName("qName1");                        // PrefixNS_00
		qName2 = new QName("www.example.com", "qName2");     // PrefixNS_01
		qName3 = new QName("", "qName3", "p");               // PrefixNS_10
		qName4 = new QName("www.example.com", "qName4", "p");// PrefixNS_11
		
		map = new BPEL4ChorNamespaceMap();
		map.put("xmlns", "http://docs.oasis-open.org/wsbpel/2.0/process/abstract");
		map.put("ns", "urn:choreograhy:topology");
		map.put("ns1", "http://www.example.com");
	}
	
	
	@Test
	public void analyseQNameTest()
	{
		
		int res1 = BPEL4ChorUtil.analyseQName(qName1);
		int res2 = BPEL4ChorUtil.analyseQName(qName2);
		int res3 = BPEL4ChorUtil.analyseQName(qName3);
		int res4 = BPEL4ChorUtil.analyseQName(qName4);
		Assert.assertEquals(BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_00, res1);
		Assert.assertEquals(BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_01, res2);
		Assert.assertEquals(BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_10, res3);
		Assert.assertEquals(BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_11, res4);
		
	}
	
}
