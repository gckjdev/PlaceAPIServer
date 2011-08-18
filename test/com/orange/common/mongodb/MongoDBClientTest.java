package com.orange.common.mongodb;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.place.constant.DBConstants;
import com.orange.place.manager.UserManager;

public class MongoDBClientTest {

	MongoDBClient mongoClient;
	static String userId;
	static String deviceId;
	private Random seed;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() {
		mongoClient = new MongoDBClient("localhost", "groupbuy", "", "");
		seed = new Random();
	}

	@Test
	public void testCreateDeviceUser() {
		String testDeviceId = String.format("device_id_%d", (long)(Math.random() * 1000));
		BasicDBObject user = UserManager.createDeviceUser(mongoClient, "test_app", "iphone", testDeviceId, "iOS", "token", "zh_Hans", "CN");
		Assert.assertNotNull(user);
		System.out.println("testCreateDeviceUser, user="+user.toString());
		Assert.assertNotNull(user.getString(MongoDBClient.ID));		
		userId = user.getString(MongoDBClient.ID);
		deviceId = user.getString(DBConstants.F_DEVICEID);
		System.out.println("testCreateDeviceUser, userId="+userId);
		System.out.println("testCreateDeviceUser, deviceId="+deviceId);
	}
	
	@Test
	public void testFindUserByDevice(){
		
		if (deviceId == null)
			Assert.fail();
		
		DBObject user = UserManager.findUserByDeviceId(mongoClient, deviceId);
		Assert.assertNotNull(user);
		System.out.println("testFindUserByDevice, user="+user.toString());
		Assert.assertNull(UserManager.findUserByDeviceId(mongoClient, "not_found_device_id"));
	}
}
