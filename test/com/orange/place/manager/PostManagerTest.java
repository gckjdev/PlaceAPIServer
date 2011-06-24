package com.orange.place.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.httpclient.util.DateUtil;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.geohash.GeoHashUtil;
import com.orange.common.utils.geohash.GeoRange;
import com.orange.place.analysis.dao.PostDao;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.constant.DBConstants;
import com.orange.place.dao.IdGenerator;

public class PostManagerTest {

	private static CassandraClient cassandraClient;
	private static PostDao postDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cassandraClient = new CassandraClient(DBConstants.SERVER,
				DBConstants.CLUSTERNAME, DBConstants.KEYSPACE);
		
		postDao = new PostDao();
		postDao.setCassandraClient(cassandraClient);
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreatePostLocationIndex() {
		String postId = IdGenerator.generateId();
		String createDate = DateUtil.formatDate(new Date());
		String latitude = "-12.345";
		String longitude = "56.878";

		GeoHashUtil util = new GeoHashUtil();
		String geoHashValue = util.encode(latitude, longitude);
		System.out.println(geoHashValue);
		
		//cleanup
		cassandraClient.deleteMultipleRows(DBConstants.INDEX_POST_LOCATION, new String[]{geoHashValue});
		
		PostManager.createPostLocationIndex(cassandraClient, postId, createDate, latitude, longitude);
		
		
		List<GeoRange> geoRanges = new ArrayList<GeoRange>();
		GeoRange r = new GeoRange();
		geoRanges.add(r);
		r.setMax(geoHashValue);
		r.setMin(geoHashValue);
		DateTime weeksBefore = new DateTime(2011, 6, 20, 0, 0, 0, 0);
		List<CompactPost> posts = postDao.findPostByLocation(geoRanges, weeksBefore.toDate(), 100);

		Assert.assertEquals("1 value found in near.",1, posts.size());
		CompactPost post = posts.get(0);
		Assert.assertEquals("postId shoud be match",postId, post.getPostId());
	}

}
