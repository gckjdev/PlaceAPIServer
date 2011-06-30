package com.orange.common.utils.geohash;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeoRangeUtilTest {

	private ProximitySearchUtil proximitySearchUtil;

	private GeoRangeUtil util;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() {
		proximitySearchUtil = new ProximitySearchUtil();
		util = new GeoRangeUtil();
	}

	@Test
	public void testGetGeoRange() {
		double latitude = 23.1291630;
		double longitude = 113.2644350;
		double radius = 50;

		List<String> result = proximitySearchUtil.getNearBy(latitude,
				longitude, radius);
		System.out.println(result.size());
		System.out.println(Arrays.asList(result));
		// Assert.assertEquals("source point is included ", 1, result.size());

		List<GeoRange> ranges = util.getGeoRange(result, 1);
		System.out.println(ranges.size());
	}

	@Test
	public void testGetNearBy_MiddleRadius_SeveralRange() {
		double latitude = 23.1291630;
		double longitude = 113.2644350;
		// 23.12 : 113.2
		double[] radiusArray = new double[] { 100, 200, 500, 800, 1000, 2000 };
		for (double radius : radiusArray) {
			List<String> result = proximitySearchUtil.getNearBy(latitude,
					longitude, radius);
			System.out.println("radis :" + radius);
			System.out.println(result.size());
			for (String r : result) {
				GeoHashUtil geoUtil = new GeoHashUtil();
				double[] loc = geoUtil.decode(r);
				double distan = proximitySearchUtil.getDistance(latitude,
						longitude, loc[0], loc[1]);
				System.out.println(distan + " : " + loc[0] + " : " + loc[1]);
			}
			List<GeoRange> ranges = util.getGeoRange(result, 1);
			System.out.println(ranges.size());
			System.out.println(Arrays.asList(ranges));
		}
	}
}
