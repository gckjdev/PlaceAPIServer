package com.orange.common.utils.geohash;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeoHashUtilTest {

	private static GeoHashUtil util;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		util = new GeoHashUtil(7);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncodeDoubleDouble() {
		String value = util.encode(23.129666, 113.273073);
		System.out.print(value);
	}

	@Test
	public void testEncodeStringString() {
		String value = util.encode("23.129666", "113.273073");
		System.out.print(value);
	}

}
