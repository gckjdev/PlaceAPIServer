package orange.place.api.systemtest;

import static orange.place.api.systemtest.SystemTestHelper.createPlace;
import static orange.place.api.systemtest.SystemTestHelper.createPost;
import static orange.place.api.systemtest.SystemTestHelper.createReply;
import static orange.place.api.systemtest.SystemTestHelper.getGeoHash;
import static orange.place.api.systemtest.SystemTestHelper.getTopNearByPost;
import static orange.place.api.systemtest.SystemTestHelper.registerUser;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.orange.common.cassandra.CassandraClient;
import com.orange.place.constant.DBConstants;

public class AnalysisFlowTest {

	private static String serverURL;

	private static String logFilePath;

	private List<String> postList;

	private List<String> userList;

	private static String latitude;
	private static String longitude;

	private static CassandraClient cassandraClient;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serverURL = "http://127.0.0.1:8000";
		logFilePath = "/tmp/analysis.log";

		File logFile = new File(logFilePath);
		FileWriter writer = null;
		try {
			writer = new FileWriter(logFile);
			writer.write(new char[0]);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

		// clean up
		cassandraClient = new CassandraClient(DBConstants.SERVER,
				DBConstants.CLUSTERNAME, DBConstants.KEYSPACE);
		// lat=23.129666&lo=113.273073
		// 77.06092205691345, longitude 9.041424539249771
		latitude = "23.129666";
		longitude = "113.273073";
		// latitude = String.valueOf(Math.random() * 90);
		// longitude = String.valueOf(Math.random() * 180);
		// latitude = "77.06092205691345";
		// longitude = "9.041424539249771";

		String geoHashValue = getGeoHash(latitude, longitude);
		System.out.println(geoHashValue);

		// cleanup
		cassandraClient.deleteMultipleRows(DBConstants.INDEX_POST_LOCATION,
				new String[] { geoHashValue });
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// create post
		postList = new ArrayList<String>();
		userList = new ArrayList<String>();
		createPostsForAnalysis();
		analysis();
	}

	@After
	public void tearDown() throws Exception {
	}

	public String generateValue() {
		long time = System.currentTimeMillis();
		long random_id = new Random().nextLong();
		return "_" + String.valueOf(time) + "_" + String.valueOf(random_id);
	}

	// date;user_id_1;post_id_1;place_id;1
	// date;user_id_2;post_id_1;place_id;3
	// date;user_id_3;post_id_2;place_id;1
	private void createPostsForAnalysis() {
		// create user
		int userCount = 3;
		userList = new ArrayList<String>();

		for (int i = 0; i < userCount; i++) {
			String value = generateValue();
			String userName = "testLoginId" + value;
			String nickName = "testNickName" + value;
			String deviceId = "testDeviceId" + value;

			String userId = registerUser(serverURL, userName, deviceId,
					nickName);
			userList.add(userId);
		}

		// one user create post
		String value = generateValue();
		String placeName = "testPlaceName" + value;
		String placeId = createPlace(serverURL, userList.get(0), placeName);


		// date;user_id_1;post_id_1;place_id;1
		String postId = createPost(serverURL, userList.get(0), placeId,
				"post1", latitude, longitude);
		postList.add(postId);

		// date;user_id_2;post_id_1;place_id;3
		postId = createReply(serverURL, userList.get(1), placeId,
				postList.get(0), "reply_post1", latitude, longitude);
		postList.add(postId);

		// date;user_id_3;post_id_2;place_id;3
		postId = createPost(serverURL, userList.get(2), placeId, "post2",
				latitude, longitude);
		postList.add(postId);
	}

	private static void analysis() throws Exception {
		// TODO: dependency with Place_Server_Analysis
		// AnalysisFlowTestHelper helper = new AnalysisFlowTestHelper();
		// helper.setUpBeforeClass();
		// helper.testRunJob();
	}

	// @Ignore
	@Test
	public void getPostsAfterAnalysis() {
		// user 1: p1, p2
		// String user1Top = getTopNearByPost(serverURL, userList.get(0),
		// latitude, longitude);
		// Assert.assertEquals("p2 is " + postList.get(1), postList.get(0),
		// user1Top);

		// user 2: p1, p2,p3
		String user2Top = getTopNearByPost(serverURL, userList.get(1),
				latitude, longitude);
		Assert.assertEquals(postList.toString(), postList.get(0),
				user2Top);
	}
}
