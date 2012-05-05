package org.bpel4chor.model.topology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.bpel4chor.interfaces.INamespaceMap;
import org.bpel4chor.model.topology.impl.Participant;
import org.bpel4chor.model.topology.impl.ParticipantSet;
import org.bpel4chor.model.topology.impl.ParticipantType;
import org.bpel4chor.model.topology.impl.Topology;
import org.bpel4chor.utils.BPEL4ChorNamespaceMap;
import org.bpel4chor.utils.BPEL4ChorUtil;
import org.junit.Before;
import org.junit.Test;

public class TopologyImplTest {

	MyTopologyImpl topo;
	
	ParticipantType pt;
	
	INamespaceMap<String, String> map;
	
	@Before
	public void setup()
	{
		topo = new MyTopologyImpl();
		pt = new ParticipantType("travler");
		
		map = new BPEL4ChorNamespaceMap();
		map.put("", "http://docs.oasis-open.org/wsbpel/2.0/process/abstract");
		map.put("ns", "urn:choreograhy:topology");
		map.put("ns1", "http://www.example.com");
		topo.setNamespaceMap(map);
		
	}
	
	
	@Test
	public void collectNamespaceTest() throws Exception
	{
		QName qName1 = new QName("travler");                          // PREFIX_NAMESPACE_00
		QName qName2 = new QName("http://www.w3c.com", "travler", "");// PREFIX_NAMESPACE_01
		QName qName3 = new QName("", "travler", "ns1");               // PREFIX_NAMESPACE_10
		QName qName4 = new QName("", "travler", "ns11111");           // PREFIX_NAMESPACE_10, it is suppose to be exceptional 
		QName qName5 = new QName("urn:choreograhy:topology", "travler", "ns");// prefix exist, namespace ==
		QName qName6 = new QName("http://www.w3c.com/schema", "travler", "ns");// prefix exist, namespace !=
		QName qName7 = new QName("http://www.w3c.com/schema", "travler", "xsd");// prefix not exist,  
		
		Set<String> set = new HashSet<String>(); 
		set.addAll(topo.getNamespaceMap().keySet());
		
		int expectedSize;
		int actualSize;
		
		// 1. BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_00
		pt.setParticipantBehaviorDescription(qName1);
		topo.myCollectNamespace(pt);
		boolean noChange1 = (topo.getNamespaceMap().size() == 3);
		boolean noChange2 = (topo.getNamespaceMap().keySet().containsAll(set));
		Assert.assertEquals(true, noChange1 && noChange2);
		
		// 2. BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_01
		expectedSize = topo.getNamespaceMap().size() + 1;
		pt.setParticipantBehaviorDescription(qName2);
		topo.myCollectNamespace(pt);
		actualSize = topo.getNamespaceMap().size();
		String prefix = topo.getNamespaceMap().getReverse("http://www.w3c.com").get(0);
		Assert.assertEquals("cns", prefix);
		Assert.assertEquals(expectedSize, actualSize);
		
		// 3. BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_10
		pt.setParticipantBehaviorDescription(qName3);
		expectedSize = topo.getNamespaceMap().size();
		topo.myCollectNamespace(pt);// nothing is supposed to be added to the map
		Assert.assertEquals(expectedSize, topo.getNamespaceMap().size());
		
		boolean thrown = false;
		pt.setParticipantBehaviorDescription(qName4);
		try{
			topo.myCollectNamespace(pt);
		} catch (Exception e){
			thrown = true;
		}
		Assert.assertEquals(true, thrown);
		
		// 4. BPEL4ChorUtil.QNAME_PREFIX_NAMESPACE_11
		Set<Entry<String, String>> expectedSet = topo.getNamespaceMap().entrySet();
		pt.setParticipantBehaviorDescription(qName5);
		topo.myCollectNamespace(pt);
		Set<Entry<String, String>> actualSet = topo.getNamespaceMap().entrySet();
		Assert.assertEquals(true, actualSet.equals(expectedSet));
		
		expectedSize = topo.getNamespaceMap().size()+1;
		pt.setParticipantBehaviorDescription(qName6);
		topo.myCollectNamespace(pt);
		actualSize = topo.getNamespaceMap().size();
		Assert.assertEquals(expectedSize, actualSize);
		Assert.assertEquals(true, topo.getNamespaceMap().getReverse(qName6.getNamespaceURI()).get(0).isEmpty()==false);
		
		expectedSize = topo.getNamespaceMap().size()+1;
		pt.setParticipantBehaviorDescription(qName7);
		topo.myCollectNamespace(pt);
		actualSize = topo.getNamespaceMap().size();
		Assert.assertEquals(expectedSize, actualSize);
		Assert.assertEquals("http://www.w3c.com/schema", topo.getNamespaceMap().get("xsd"));
		
	}
	
	
	@Test
	public void addParticipantTypeTest() throws Exception
	{
		QName pbd1 = new QName("travler");                          // PREFIX_NAMESPACE_00
		QName pbd2 = new QName("http://www.w3c.com", "travler", "");// PREFIX_NAMESPACE_01
		QName pbd3 = new QName("", "travler", "ns1");               // PREFIX_NAMESPACE_10
		QName pbd4 = new QName("", "travler", "ns11111");           // PREFIX_NAMESPACE_10, it is suppose to be exceptional 
		QName pbd5 = new QName("urn:choreograhy:topology", "travler", "ns");// prefix exist, namespace ==
		QName pbd6 = new QName("http://www.w3c.com/schema", "travler", "ns");// prefix exist, namespace !=
		QName pbd7 = new QName("http://www.w3c.com/schema", "travler", "xsd");// prefix not exist,
		
		int expectedSize;
		int actualSize;
		
		// Test Case 1 - add participantType with pbd1 to new topology
		// expected result - pt is added
		ParticipantType pt1 = new ParticipantType("travler", pbd1, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		expectedSize = topo.getParticipantTypes().size() + 1;
		topo.add(pt1);
		actualSize = topo.getParticipantTypes().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		ParticipantType pt1Copy = new ParticipantType("travler1", pbd1, null);
		expectedSize = topo.getParticipantTypes().size();
		topo.add(pt1Copy);
		actualSize = topo.getParticipantTypes().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		ParticipantType pt2 = new ParticipantType("travler", pbd2, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		expectedSize = topo.getParticipantTypes().size() + 1;
		topo.add(pt2);
		actualSize = topo.getParticipantTypes().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		
		ParticipantType pt3 = new ParticipantType("travler", pbd3, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		expectedSize = topo.getParticipantTypes().size() + 1;
		topo.add(pt3);
		actualSize = topo.getParticipantTypes().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		ParticipantType pt4 = new ParticipantType("travler", pbd4, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		expectedSize = topo.getParticipantTypes().size();
		// if you see error here, 
		// do not panic, this one is supposed to be error, due to the wrong input
		System.out.println("Do Not Panic if you see following error traceStack. It is supposed to be.");
		try{
			topo.add(pt4);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(true, e instanceof RuntimeException);
		}
		actualSize = topo.getParticipantTypes().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		ParticipantType pt5 = new ParticipantType("travler", pbd5, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		expectedSize = topo.getParticipantTypes().size() + 1;
		topo.add(pt5);
		actualSize = topo.getParticipantTypes().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		ParticipantType pt6 = new ParticipantType("travler", pbd6, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		expectedSize = topo.getParticipantTypes().size() + 1;
		topo.add(pt6);
		actualSize = topo.getParticipantTypes().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		ParticipantType pt7 = new ParticipantType("travler", pbd7, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		expectedSize = topo.getParticipantTypes().size() + 1;
		topo.add(pt7);
		actualSize = topo.getParticipantTypes().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		// Test Case 2
		// Test Input: the participantType with pbd which contains new namespace
		// Expected Result: the participantType will be added, the pbd will be updated.
		pt2 = new ParticipantType("travler", pbd2, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		topo.add(pt2); // with pt2, its pbd should be changed.
		QName actualPBD = topo.getParticipantTypes().get(0).getParticipantBehaviorDescription();
		QName oldPbd = pbd2;
		Assert.assertFalse(actualPBD.getPrefix().equals(oldPbd.getPrefix()));
		
		
		pt7 = new ParticipantType("travler", pbd7, null);
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		topo.add(pt7); // with pt7, its pbd should be changed.
		actualPBD = topo.getParticipantTypes().get(0).getParticipantBehaviorDescription();
		oldPbd = pbd7;
		Assert.assertTrue(actualPBD.getPrefix().equals(oldPbd.getPrefix()));
	}
	


	@Test
	public void addParticipantTest() throws Exception
	{
		QName qName1 = new QName("urn:choreograhy:topology", "LocalPart1", "ns");// prefix exist, namespace ==
		QName qName2 = new QName("http://www.w3c.com/schema", "LocalPart2", "ns");// prefix exist, namespace !=
		QName qName3 = new QName("http://www.w3c.com/schema", "LocalPart3", "xsd");// prefix not exist,
		
		int expectedSize;
		int actualSize;
		
		String expectedStr = null;
		String actualStr = null;
		
		// test case 1 
		// add participant with scope
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);
		Participant p1 = new Participant("travler1", "Travler");
		p1.setScope(qName1);
		expectedSize = topo.getParticipants().size()+1;
		topo.add(p1);
		actualSize = topo.getParticipants().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		expectedStr = p1.getName() + p1.getType() + BPEL4ChorUtil.getString(p1.getScope());
		Participant p1Copy = topo.getParticipants().get(0);
		actualStr = p1Copy.getName() + p1Copy.getType() + BPEL4ChorUtil.getString(p1Copy.getScope());
		Assert.assertEquals(expectedStr, actualStr);
		
		// test case 2 
		// add participant with forEach 
		Participant p2 = new Participant("travler2", "Travler");
		p2.getForEach().add(qName2);
		p2.getForEach().add(qName3);
		expectedSize = topo.getParticipants().size();
		topo.add(p2);
		actualSize = topo.getParticipants().size();
		
		expectedStr = "http://www.w3c.com/schema";
		actualStr = topo.getNamespaceMap().get("xsd");
		Assert.assertEquals(expectedStr, actualStr);
		
		// test case 3
		// add a participant with existed name
		Participant p3 = new Participant("travler1", "Travler");
		expectedSize = topo.getParticipants().size();
		topo.add(p3);
		actualSize = topo.getParticipants().size();
		
		
	}
	
	@Test
	public void addParticipantSetTest() throws Exception
	{
		QName qName1 = new QName("urn:choreograhy:topology", "LocalPart1", "ns");// prefix exist, namespace == the one in map
		QName qName2 = new QName("http://www.w3c.com/schema", "LocalPart2", "ns");// prefix exist, namespace != the one in map
		QName qName3 = new QName("http://www.w3c.com/schema", "LocalPart3", "xsd");// prefix not exist,
		QName qName4 = new QName("http://www.iaas.informatik.uni-stuttgart.de", "LocalPart4", "iaas");// prefix not exist, namespace not exist 
		QName qName5 = new QName("http://www.hlrs.de", "LocalPart5", "");
		QName qName6 = new QName("http://www.uni-stuttgart.de", "LocalPart6", "uni");
		QName qName7 = new QName("http://www.uni-stuttgart.de", "LocalPart7", "uni");
		QName qName8 = new QName("http://www.uni-stuttgart.de", "LocalPart8", "uni");

		int expectedSize;
		int actualSize;
		
	
		// add participantSet with scope
		topo = new MyTopologyImpl();
		topo.setNamespaceMap(map);

		ParticipantSet ps1 = new ParticipantSet("ParticipantSet1", "PSType1");
		ParticipantSet ps2 = new ParticipantSet("ParticipantSet2", "PSType2");
		
		Participant p1 = new Participant("Participant1", "airline");
		Participant p2 = new Participant("Participant2", "airline");
		Participant p3 = new Participant("Participant3", "airline");
		Participant p4 = new Participant("Participant4", "airline");
		
		
		p1.setScope(qName1);
		p2.setScope(qName2);
		p3.setScope(qName3);
		p4.setScope(qName4);
		
		ps1.setScope(qName5);
		ps2.setScope(qName6);
		
		List<QName> qNameList = new ArrayList<QName>();
		qNameList.add(qName7);
		qNameList.add(qName8);
		ps1.setForEach(qNameList);
		
		expectedSize = ps2.getParticipantList().size()+3;
		ps2.add(p2);
		ps2.add(p3);
		ps2.add(p4);
		actualSize = ps2.getParticipantList().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		expectedSize = ps1.getParticipantList().size() + 1;
		ps1.add(p1);
		actualSize = ps1.getParticipantList().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		expectedSize = ps1.getParticipantSetList().size() + 1;
		ps1.add(ps2);
		actualSize = ps1.getParticipantSetList().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		expectedSize = topo.getParticipantSets().size() + 1;
		topo.add(ps1);
		actualSize = topo.getParticipantSets().size();
		
		
		for(Map.Entry<String, String> entry : topo.getNamespaceMap().entrySet())
		{
			System.out.println(entry);
		}
		expectedSize = 8; // now there should be 8 entries in namespace map
//		xmlns:iaas=http://www.iaas.informatik.uni-stuttgart.de
//			xmlns:xsd=http://www.w3c.com/schema
//			xmlns:ns=urn:choreograhy:topology
//			xmlns:tns=http://www.hlrs.de
//			xmlns:tns1=http://www.w3c.com/schema
//			xmlns=http://docs.oasis-open.org/wsbpel/2.0/process/abstract
//			xmlns:ns1=http://www.example.com
//			xmlns:uni=http://www.uni-stuttgart.de
		actualSize = topo.getNamespaceMap().size();
		Assert.assertEquals(expectedSize, actualSize);
	}
}

/**
 * Extended Topology for testing protected methods
 * 
 *
 * @since Oct 28, 2011
 * @author daojun
 */
class MyTopologyImpl extends Topology
{
	public void myCollectNamespace(ParticipantType pt) throws Exception
	{
		this.collectNamespace(pt);
	}
}