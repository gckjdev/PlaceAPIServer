package com.orange.place.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.geohash.GeoHashUtil;
import com.orange.common.utils.geohash.GeoRange;
import com.orange.common.utils.geohash.GeoRangeUtil;
import com.orange.common.utils.geohash.ProximitySearchUtil;
import com.orange.place.analysis.dao.PostDao;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.domain.Request;
import com.orange.place.analysis.parser.impl.RequestParserImpl;
import com.orange.place.constant.DBConstants;
import com.orange.place.dao.IdGenerator;

@Ignore
public class PostManagerTest {

	/**
	 * ProximitySearchUtil
	 */
	private static final int PROXIMITY_PRECISION = 9;
	private static CassandraClient cassandraClient;
	private static PostDao postDao;
	private static RequestParserImpl requestParser;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cassandraClient = new CassandraClient(DBConstants.SERVER,
				DBConstants.CLUSTERNAME, DBConstants.KEYSPACE);
		
		postDao = new PostDao();
		postDao.setCassandraClient(cassandraClient);

		requestParser = new RequestParserImpl();

		GeoRangeUtil geoRangeUtil = new GeoRangeUtil();
		requestParser.setGeoRangeUtil(geoRangeUtil);
		
		ProximitySearchUtil proximitySearchUtil = new ProximitySearchUtil();
		proximitySearchUtil.setPrecision(PROXIMITY_PRECISION);
		requestParser.setProximitySearchUtil(proximitySearchUtil);
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreatePostLocationIndex_oneGeohashValue() {
		String postId = IdGenerator.generateId();
		String createDate = DateUtil.dateToString(new Date());
		String latitude = "-12.345";
		String longitude = "56.878";

		GeoHashUtil util = new GeoHashUtil();
		util.setPrecision(13);
		String geoHashValue = util.encode(latitude, longitude);
		System.out.println(geoHashValue);
		
		//cleanup
		cassandraClient.deleteMultipleRows(DBConstants.INDEX_POST_LOCATION, new String[]{geoHashValue});
		
		PostManager.createPostLocationIndex(cassandraClient, postId, createDate, latitude, longitude);
		
		GeoHashUtil searchUtil = new GeoHashUtil();
		searchUtil.setPrecision(7);
		String searchGeoHash = util.encode(latitude, longitude);

		List<GeoRange> geoRanges = new ArrayList<GeoRange>();
		GeoRange r = new GeoRange();
		geoRanges.add(r);
		r.setMax(searchGeoHash);
		r.setMin(searchGeoHash);
		DateTime weeksBefore = new DateTime(2011, 6, 20, 0, 0, 0, 0);
		List<CompactPost> posts = postDao.findPostByLocation(geoRanges, weeksBefore.toDate(), 100);

		Assert.assertEquals("1 value found in near.",1, posts.size());
		CompactPost post = posts.get(0);
		Assert.assertEquals("postId shoud be match",postId, post.getPostId());
	}

	@Test
	public void testCreatePostLocationIndex_TimeLimation() {
		String postId = IdGenerator.generateId();
		String createDate = DateUtil.dateToString(new Date());
		String latitude = "-12.345";
		String longitude = "56.878";

		GeoHashUtil util = new GeoHashUtil();
		util.setPrecision(13);
		String geoHashValue = util.encode(latitude, longitude);
		System.out.println(geoHashValue);

		// cleanup
		cassandraClient.deleteMultipleRows(DBConstants.INDEX_POST_LOCATION,
				new String[] { geoHashValue });

		PostManager.createPostLocationIndex(cassandraClient, postId,
				createDate, latitude, longitude);

		GeoHashUtil searchUtil = new GeoHashUtil();
		searchUtil.setPrecision(7);
		String searchGeoHash = util.encode(latitude, longitude);

		List<GeoRange> geoRanges = new ArrayList<GeoRange>();
		GeoRange r = new GeoRange();
		geoRanges.add(r);
		r.setMax(searchGeoHash);
		r.setMin(searchGeoHash);
		List<CompactPost> posts = postDao.findPostByLocation(geoRanges,
				new Date(), 100);

		Assert.assertEquals("no post can after current date.", 0, posts.size());
	}

	@Test
	public void testCreatePostLocationIndex_simulateServer() {
		String postId = IdGenerator.generateId();
		String createDate = DateUtil.dateToString(new Date());
		String latitude = "-12.345";
		String longitude = "56.878";
		double radius = 200;

		GeoHashUtil util = new GeoHashUtil();
		util.setPrecision(13);
		String geoHashValue = util.encode(latitude, longitude);
		System.out.println(geoHashValue);

		// cleanup
		cassandraClient.deleteMultipleRows(DBConstants.INDEX_POST_LOCATION,
				new String[] { geoHashValue });

		PostManager.createPostLocationIndex(cassandraClient, postId,
				createDate, latitude, longitude);

		Request request = new Request();
		request.setUserId("mock-userId");
		request.setLatitude(Double.valueOf(latitude));
		request.setLongitude(Double.valueOf(longitude));
		request.setRadius(radius);
		ParseResult result = requestParser.parse(request);
		Assert.assertTrue("parse success.", result.isSuccess());

		DateTime weeksBefore = new DateTime(2011, 6, 20, 0, 0, 0, 0);
		List<CompactPost> posts = postDao.findPostByLocation(
				result.getPlaceRange(),
				weeksBefore.toDate(), 100);

		Assert.assertEquals("1 value found in near.", 1, posts.size());
		CompactPost post = posts.get(0);
		Assert.assertEquals("postId shoud be match", postId, post.getPostId());
	}
}
