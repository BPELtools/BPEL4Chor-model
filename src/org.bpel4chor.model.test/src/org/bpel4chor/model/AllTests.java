package org.bpel4chor.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for BPEL4Chor Implementation
 * 
 * @since Oct 30, 2011
 * @author Daojun.Cui
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    org.bpel4chor.model.topology.TopologyImplTest.class,
    org.bpel4chor.model.grounding.GroundingImplTest.class,
    org.bpel4chor.model.utils.BPEL4ChorNamespaceMapTest.class,
    org.bpel4chor.model.utils.BPEL4ChorUtilTest.class,
    org.bpel4chor.model.utils.BPEL4ChorReaderTest.class,
    org.bpel4chor.model.utils.BPEL4ChorWriterTest.class,
    org.bpel4chor.model.utils.WSUIDGeneratorTest.class,
    org.bpel4chor.model.utils.ZipUtilTest.class
})

public class AllTests {

//	public static Test suite() {
//		TestSuite suite = new TestSuite(AllTests.class.getName());
//		//$JUnit-BEGIN$
//
//		//$JUnit-END$
//		return suite;
//	}

}
