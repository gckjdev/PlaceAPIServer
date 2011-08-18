package com.orange.common.utils.geohash;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class GeoHashUtilTest {

	private static final double ACCURANCY_DELTA_DEGREE = 0.0000001;

	private Random seed;

	private GeoHashUtil geohashUtil;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() {
		geohashUtil = new GeoHashUtil();
		seed = new Random();
	}

	@Test
	public void testDecode() {
		double lat = getLat();
		double lon = getLong();

		String geohash = geohashUtil.encode(lat, lon);
		double[] result = geohashUtil.decode(geohash);

		System.out.println(lat + ":" + result[0]);
		System.out.println(lon + ":" + result[1]);

		Assert.assertEquals("lat equals", lat, result[0], 0.00001);
		Assert.assertEquals("lon equals", lon, result[1], 0.00001);
	}

	@Test
	public void testEncode() {
		double lat = getLat();
		double lon = getLong();

		String result = geohashUtil.encode(lat, lon);
		System.out.println(result);
		Assert.assertNotNull(result);
	}

	@Test
	public void testDecodeEncode_Difference() {
		double r = 6356.755 * 1000;
		double delta = ACCURANCY_DELTA_DEGREE;
		double distance = 2 * Math.PI * r * delta / 360;

		System.out.println("distance : " + distance);
	}

	@Test
	public void testEncode_Accuracy() {
		int count = 1000;
		int failure = 0;
		for (int i = 0; i < count; i++) {
			double lat = getLat();
			double lon = getLong();

			String geohash = geohashUtil.encode(lat, lon);
			Assert.assertNotNull(geohash);

			double[] result = geohashUtil.decode(geohash);

			if (Math.abs(lat - result[0]) > 0.00001
					|| Math.abs(lon - result[1]) > 0.00001) {
				System.out.println("============");
				System.out.println(lat + ":" + result[0]);
				System.out.println(lon + ":" + result[1]);
				failure++;
			}
			Assert.assertEquals("lat equals", lat, result[0],
					ACCURANCY_DELTA_DEGREE);
			Assert.assertEquals("lon equals", lon, result[1],
					ACCURANCY_DELTA_DEGREE);
		}
		System.out.println("failure :" + failure);
	}

	@Test
	public void testEncode_Accuracy_Mistake() {
		// -77.9614596407155:70.14700636267662
		// -139.96324813171648:-83.69167774915695
		double lat = -77.9614596407155;
		double lon = -139.96324813171648;

		String geohash = geohashUtil.encode(lat, lon);
		Assert.assertNotNull(geohash);

		double[] result = geohashUtil.decode(geohash);
		if (Math.abs(lat - result[0]) > 0.00001
				|| Math.abs(lon - result[1]) > 0.00001) {
			System.out.println("============");
			System.out.println(lat + ":" + result[0]);
			System.out.println(lon + ":" + result[1]);
		}
	}

	@Test
	public void testEncode_PerformanceTest() {
		int[] countArray = new int[] { 10000, 100000, 1000000 };

		for (int count : countArray) {
			long total = 0;

			for (int i = 0; i < count; i++) {
				double lat = getLat();
				double lon = getLong();

				long start = System.currentTimeMillis();
				String result = geohashUtil.encode(lat, lon);
				Assert.assertNotNull(result);
				long end = System.currentTimeMillis();
				total += end - start;
			}
			System.out.println("============");
			System.out.println("result for :" + count);
			System.out.println("total:" + total);
			double average = (double) total / (double) count;
			System.out.println("average:" + average);
			double countPerSec = 1000d / average;
			System.out.println("count in one seconds " + countPerSec);
		}
	}

	private double getRanageValue(double dist, double range) {
		double result = 0;
		result = (dist - 1.0d) * range;
		return result;
	}

	private double getLong() {
		return getRanageValue(seed.nextDouble(), 180);
	}

	private double getLat() {
		return getRanageValue(seed.nextDouble(), 90);
	}
}
