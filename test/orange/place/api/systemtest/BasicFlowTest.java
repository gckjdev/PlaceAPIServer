package orange.place.api.systemtest;

import static orange.place.api.systemtest.SystemTestHelper.createPlace;
import static orange.place.api.systemtest.SystemTestHelper.createPost;
import static orange.place.api.systemtest.SystemTestHelper.createReply;
import static orange.place.api.systemtest.SystemTestHelper.getTopNearByPost;
import static orange.place.api.systemtest.SystemTestHelper.registerUser;

import java.util.Random;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BasicFlowTest {

	private static String serverURL;
	private static String userId; // userId registered
	private static String placeId; // placeId created
	private static String postId; // postId for replied
	private static String replyPostId; // new postId for replied postId post

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serverURL = "http://127.0.0.1:8000";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void doSomething() throws Exception {
	}

	public String generateValue() {
		long time = System.currentTimeMillis();
		long random_id = new Random().nextLong();
		return "_" + String.valueOf(time) + "_" + String.valueOf(random_id);
	}

	@Test
	public void testRegisterUser() {

		userId = null;

		// send registration user request
		String value = generateValue();
		String userName = "testLoginId" + value;
		String nickName = "testNickName" + value;
		String deviceId = "testDeviceId" + value;

		userId = registerUser(serverURL, userName, deviceId, nickName);
	}

	@Test
	public void testCreatePlace() {

		placeId = null;
		Assert.assertTrue(userId != null);

		String value = generateValue();
		String placeName = "testPlaceName" + value;
		placeId = createPlace(serverURL, userId, placeName);
	}

	@Test
	public void testCreatePost() {

		postId = null;
		Assert.assertTrue(userId != null && placeId != null);

		String value = generateValue();
		String postContent = "testPostContent" + value;
		postId = createPost(serverURL, userId, placeId, postContent);
	}

	@Test
	public void testReplyPost() {
		replyPostId = null;
		Assert.assertTrue(userId != null && placeId != null && postId != null);

		String value = generateValue();
		String postContent = "testPostContent" + value;
		replyPostId = createReply(serverURL, userId, placeId, postId,
				postContent);
	}

	@Test
	public void testGetNearByPost() {
		Assert.assertTrue(userId != null);

		String nearByPostId = getTopNearByPost(serverURL, userId);

		Assert.assertNotNull(nearByPostId);
	}

	@Test
	public void testGetPlacePost() {
		Assert.assertEquals(1, 1);
	}

}
