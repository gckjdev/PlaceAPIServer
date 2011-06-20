package com.orange.common.utils.geohash;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProximitySearchUtilTest {

	/**
	 * Distance err delta : 10 meters
	 */
	private static final int DISTANCE_DELTA_METER = 10;

	private ProximitySearchUtil util;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() {
		util = new ProximitySearchUtil();
	}

	@Test
	public void testGetNearBy_MinRadius() {
		double latitude = 23.1291630;
		double longitude = 113.2644350;
		double radius = 5;

		List<String> result = util.getNearBy(latitude, longitude, radius);
		System.out.println(result.size());
		System.out.print(Arrays.asList(result));
		// Assert.assertEquals("source point is included ", 1, result.size());
	}

	@Test
	public void testGetNearBy_MiddleRadius() {
		double latitude = 23.1291630;
		double longitude = 113.2644350;
		double radius = 50;

		List<String> result = util.getNearBy(latitude, longitude, radius);
		System.out.println(result.size());
		System.out.print(Arrays.asList(result));
	}

	@Test
	public void testGetNearBy_MiddleRadius2() {
		double latitude = 23.1291630;
		double longitude = 113.2644350;
		double radius = 200;

		List<String> result = util.getNearBy(latitude, longitude, radius);
		System.out.println(result.size());
		System.out.print(Arrays.asList(result));
	}

	@Test
	public void testGetNearBy_MiddleRadius_Performance() {
		int count = 1000;
		long start = System.currentTimeMillis();
		util.setPrecision(7);
		for (int i = 0; i < count; i++) {
			double latitude = 23.1291630;
			double longitude = 113.2644350;
			double[] radiusArray = new double[] { 500, 800, 1000 };
			for (double radius : radiusArray) {
				List<String> result = util.getNearBy(latitude, longitude,
						radius);
			}
		}
		long end = System.currentTimeMillis();
		long time = end -start;
		double avg = (double)time/ (double)count;
		double countPerSec = 1000 / avg;
		
		System.out.println("time :" + time);
		System.out.println("avg :" + avg);
		System.out.println("countPerSec :" + countPerSec);
	}
	
	@Test
	public void testGetNearBy_MiddleRadius_Performance2() {
		int count = 1000;
		long start = System.currentTimeMillis();
		util.setPrecision(6);
		for (int i = 0; i < count; i++) {
			double latitude = 23.1291630;
			double longitude = 113.2644350;
			double[] radiusArray = new double[] { 1000, 2000, 4000 };
			for (double radius : radiusArray) {
				List<String> result = util.getNearBy(latitude, longitude,
						radius);
			}
		}
		long end = System.currentTimeMillis();
		long time = end -start;
		double avg = (double)time/ (double)count;
		double countPerSec = 1000 / avg;
		
		System.out.println("time :" + time);
		System.out.println("avg :" + avg);
		System.out.println("countPerSec :" + countPerSec);
	}
	
	@Test
	public void testGetNearBy_MiddleRadius_SeveralRange() {
		double latitude = 23.1291630;
		double longitude = 113.2644350;
		double[] radiusArray = new double[] { 500, 800, 1000 };
		for (double radius : radiusArray) {
			List<String> result = util.getNearBy(latitude, longitude, radius);
			System.out.println("radis :" + radius);
			System.out.println(result.size());
			System.out.println(Arrays.asList(result));
		}
	}

	@Test
	public void testGetNearBy_MaxRadius() {
		double latitude = 23.1291630;
		double longitude = 113.2644350;
		double radius = 2000;

		List<String> result = util.getNearBy(latitude, longitude, radius);
		System.out.println(result.size());
		// System.out.println(Arrays.asList(result));
	}

	@Test
	public void testIsGeohashInRange() {
		double lat1 = 23.1291630;
		double lng1 = 113.2644350;
		String sourcePoint = "ws0e96s9hskp";
		double radius = 1;

		boolean reslut = util.isGeohashInRange(lat1, lng1, radius, sourcePoint);

		Assert.assertTrue(sourcePoint + " is: " + lat1 + "," + lng1, reslut);
	}

	@Test
	public void testIsGeohashInRange_close_NotInRange() {
		double lat1 = 23.1291630;
		double lng1 = 113.2644350;
		String sourcePoint = "ws0e96s9hs";
		double radius = 1;
		boolean reslut = util.isGeohashInRange(lat1, lng1, radius, sourcePoint);
		Assert.assertFalse(sourcePoint + " is: " + lat1 + "," + lng1, reslut);
	}

	@Test
	public void testIsGeohashInRange_NotInRange() {
		// 23.1291630 113.2644350, ws0e96s9hskp3
		double lat1 = 23.1291630;
		double lng1 = 113.2644350;
		String sourcePoint = "ws0e96sd";
		double radius = 30;

		boolean reslut = util.isGeohashInRange(lat1, lng1, radius, sourcePoint);

		Assert.assertFalse(sourcePoint + " is not " + radius + " m north of: "
				+ lat1 + "," + lng1, reslut);
	}

	@Test
	public void testIsGeohashInRange_InRange() {
		// 23.1291630 113.2644350, ws0e96s9hskp3
		double lat1 = 23.1291630;
		double lng1 = 113.2644350;
		String sourcePoint = "ws0e96sd";
		double radius = 50;

		boolean reslut = util.isGeohashInRange(lat1, lng1, radius, sourcePoint);

		Assert.assertTrue(sourcePoint + " is " + radius + " m north of: "
				+ lat1 + "," + lng1, reslut);
	}

	@Test
	public void testGetDistance() {
		double expected = 4304.5270839204695;
		// Tian He Cheng
		double lat1 = 23.136097;
		double lng1 = 113.323761;

		// Nong jiang suo,,
		double lat2 = 23.132938;
		double lng2 = 113.281777;
		double result = util.getDistance(lat1, lng1, lat2, lng2);

		Assert.assertEquals(
				"distance should be correct between tian he and nong jiang suo.",
				expected, result, DISTANCE_DELTA_METER);
	}

	/**
	 * Result from http://openlocation.org/geohash/geohash-js/
	 */
	@Test
	public void getNeighbor() {
		// LEFT(geohash,8) IN
		// ('ws0e96sd','ws0e96s8','ws0e96sc','ws0e96s3','ws0e96s6','ws0e96sf','ws0e96sb','ws0e96s2','ws0e96s9')
		// 23.129, 113.264 [w:73m, h:52m] (0km2)
		String sourcePoint = "ws0e96s9";
		String actual = util
				.getNeighbor(sourcePoint, ProximitySearchUtil.NORTH);
		Assert.assertEquals("ws0e96sd", actual);

		actual = util.getNeighbor(sourcePoint, ProximitySearchUtil.WEST);
		Assert.assertEquals("ws0e96s3", actual);

		actual = util.getNeighbor(sourcePoint, ProximitySearchUtil.SOUTH);
		Assert.assertEquals("ws0e96s8", actual);

		actual = util.getNeighbor(sourcePoint, ProximitySearchUtil.EAST);
		Assert.assertEquals("ws0e96sc", actual);
	}
}
