package orange.place.api.systemtest;

import static orange.place.api.systemtest.SystemTestHelper.createPlace;
import static orange.place.api.systemtest.SystemTestHelper.createPost;
import static orange.place.api.systemtest.SystemTestHelper.createReply;
import static orange.place.api.systemtest.SystemTestHelper.registerUser;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class AnalysisFlowTest {

	private static String serverURL;

	private static String logFilePath;

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

	public String generateValue() {
		long time = System.currentTimeMillis();
		long random_id = new Random().nextLong();
		return "_" + String.valueOf(time) + "_" + String.valueOf(random_id);
	}

	// date;user_id_1;post_id_1;place_id;1
	// date;user_id_1;post_id_2;place_id;1
	// date;user_id_2;post_id_1;place_id;3
	// date;user_id_3;post_id_2;place_id;3
	@Test
	public void createPostsForAnalysis() {

		// create user
		int userCount = 3;
		List<String> userList = new ArrayList<String>();

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

		// create post
		List<String> postList = new ArrayList<String>();
		// date;user_id_1;post_id_1;place_id;1
		String postId = createPost(serverURL, userList.get(0), placeId, "post1");
		postList.add(postId);

		// date;user_id_1;post_id_2;place_id;1
		postId = createPost(serverURL, userList.get(0), placeId, "post2");
		postList.add(postId);
		// date;user_id_2;post_id_1;place_id;3
		postId = createReply(serverURL, userList.get(1), placeId,
				postList.get(0), "reply_post1");
		postList.add(postId);
		// date;user_id_3;post_id_2;place_id;3
		postId = createReply(serverURL, userList.get(2), placeId,
				postList.get(1), "reply_post2");
		postList.add(postId);
	}
}
