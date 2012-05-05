package org.bpel4chor.model.grounding;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.bpel4chor.model.grounding.impl.Grounding;
import org.bpel4chor.model.grounding.impl.MessageLink;
import org.bpel4chor.model.grounding.impl.ParticipantRef;
import org.bpel4chor.model.grounding.impl.Property;
import org.bpel4chor.model.topology.impl.Topology;
import org.bpel4chor.utils.BPEL4ChorConstants;
import org.bpel4chor.utils.BPEL4ChorUtil;
import org.eclipse.bpel.model.messageproperties.MessagepropertiesFactory;
import org.junit.Before;
import org.junit.Test;

public class GroundingImplTest {

	Topology topo;
	
	Grounding grou;
	
	@Before
	public void setup()
	{
		topo = new Topology("TicketOrderTopology", BPEL4ChorConstants.TOPOLOGY_TARGET_NAMESPACE);
		
	}
	
	@Test
	public void GroundingImplTopologyRefTest()
	{
		grou = new Grounding(topo);
		String expected = BPEL4ChorConstants.TOPOLOGY_XMLNS;
		String actual = grou.getNamespaceMap().get("tns");
		Assert.assertEquals(expected, actual);

		QName expectedQName = new QName(topo.getTargetNamespace(), topo.getName(),
				BPEL4ChorConstants.TOPOLOGY_PREFIX_BASE);
		QName actualQName = grou.getTopology();
		Assert.assertEquals(BPEL4ChorUtil.getString(expectedQName), BPEL4ChorUtil.getString(actualQName));
	}
	
	@Test
	public void addMessageLinkTest()
	{
		grou = new Grounding(topo);
		// test case 
		// input: a messageLink will be added, it's portType will contains a new namespace.
		// expected: 1) the namespace is collected in the namespaceMap
		//           2) the messageLink is added to grounding
		//           3) if new prefix is created, the qName(portType) in the messageLink should be updated.
		org.bpel4chor.model.topology.impl.MessageLink tMsgLink =	new org.bpel4chor.model.topology.impl.MessageLink();
		tMsgLink.setName("eTicket");
		
		String ns = "www.wsdl.com";
		String localpart = "input";
		String prefix = "wsdl";
		QName portType = new QName(ns, localpart, prefix);
		
		MessageLink ml = new MessageLink(tMsgLink, portType, "sendTicket");
		
		//1)
		String expectedString = ns;
		int expectedSize = grou.getNamespaceMap().size()+1;
		grou.add(ml);
		String actualString = grou.getNamespaceMap().get(prefix);
		Assert.assertEquals(expectedString, actualString);
		
		//2
		int actualSize = grou.getNamespaceMap().size();
		Assert.assertEquals(expectedSize, actualSize);
		
		//3
		org.bpel4chor.model.topology.impl.MessageLink tMsgLink2 = 
			new org.bpel4chor.model.topology.impl.MessageLink();
		tMsgLink2.setName("eTicket2");
		
		QName portType2 = new QName("http://www.example.com/receive", "localpart", "wsdl");
		MessageLink ml2 = new MessageLink(tMsgLink2, portType2, "receiveTicket");
		grou.add(ml2);
		String oldPrefix = "wsdl";
		String newPrefix = ml2.getPortType().getPrefix();
		Assert.assertFalse(newPrefix.equals(oldPrefix));
		
	}
	
	@Test
	public void addParticipantRefTest()
	{
		grou = new Grounding(topo);
		
		ParticipantRef pref1 = new ParticipantRef("Ref1", new QName("http://example.com", "localpart1", "cns"));
		
		ParticipantRef pref2 = new ParticipantRef("Ref2", new QName("http://example.com/2", "localpart2", "cns"));
		
		// 1.
		int expectedInt = grou.getParticipantRefs().size()+1;
		grou.add(pref1);
		int actualInt = grou.getParticipantRefs().size();
		Assert.assertEquals(expectedInt, actualInt);
		Assert.assertEquals("http://example.com", grou.getNamespaceMap().get("cns"));
		
		// 2.
		expectedInt = grou.getParticipantRefs().size()+1;
		grou.add(pref2);
		actualInt = grou.getParticipantRefs().size();
		Assert.assertEquals(expectedInt, actualInt);
		Assert.assertTrue(grou.getNamespaceMap().getReverse("http://example.com/2").get(0).equals("cns1"));
		
	}
	
	@Test
	public void testContainsProperty() {
		grou = new Grounding(topo);
		
		Property property1 = new Property("p1", new QName("www.uni-stuttgart.de", "wsdlproperty1"));
		Property property2 = new Property("p2", new QName("www.uni-stuttgart.de", "wsdlproperty2"));
		Property property3 = new Property("p3", new QName("www.uni-stuttgart.de", "wsdlproperty1"));
		
		grou.add(property1);
		grou.add(property2);
		
		Assert.assertTrue(grou.getProperties().size() == 2);
		Assert.assertTrue(grou.contains(property3));
		
		grou.add(property3);
		Assert.assertTrue(grou.getProperties().size() == 2);
	}
	
}
