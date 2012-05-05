package org.bpel4chor.model.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.logging.Logger;

import org.bpel4chor.utils.WSUIDGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WSUIDGeneratorTest {

	//Logger logger = Logger.getLogger(WSUIDGeneratorTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetIdInt() {
		String id1 = WSUIDGenerator.getId();
		String id2 = WSUIDGenerator.getId();
		assertNotNull(id1);
		assertNotNull(id2);
		assertEquals(WSUIDGenerator.idLength(), id1.length());
		assertEquals(WSUIDGenerator.idLength(), id2.length());
		assertFalse(id1.equals(id2));
		String id3 = WSUIDGenerator.getId();
		assertNotNull(id3);
		assertFalse(id1.equals(id3));
	}

	// this test may take a while
	@Test
	public void testLimitOfTheIdGenerator() throws Exception {
		System.out.println("Begin Stress Test for limit of RandomIdGenerator, this may take a while");
		String[] usedIds = new String[(int) (WSUIDGenerator.maxSize()*0.1)];
		
		for (int i = 0; i < usedIds.length; i++) {
			String id = WSUIDGenerator.getId();
			assertNotNull(id);
			assertUniqueID(usedIds, id, i);
			assertEquals(WSUIDGenerator.idLength(), id.length());
			usedIds[i] = id;
			System.out.println("id[" + i +"]="+id);
		}

	}

	private void assertUniqueID(String[] usedIds, String id, int counter) {
		
		for(int i = 0; i < counter; i++) {
			String usedId = usedIds[i];
			assertFalse(usedId.equals(id));
		}
	}
}
