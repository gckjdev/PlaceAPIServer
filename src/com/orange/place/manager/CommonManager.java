package com.orange.place.manager;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.place.api.PlaceAPIServer;

public class CommonManager {

	public static final Logger log = LoggerFactory
			.getLogger(PlaceAPIServer.class
			.getName());

	public static final long MAX_FOLLOWPLACE_POSTS_TIMEOFFSET = 24 * 60 * 60 * 1000;

	public static final int MAX_COUNT = 30;
	public static final int UNLIMITED_COUNT = Integer.MAX_VALUE;
	public static final int MAX_DELETE_UNFOLLOWPLACE_COUNT = 3;
	public static final int MAX_ADD_FOLLOWPLACE_COUNT = 30;
	public static final int MAX_SIMILARITY_PLACE_COUNT = 10;
	public static final double MIN_PLACE_NAME_SIMILARITY = 60;

	public static final int NEARBY_PLACE_GEOHASH_PRECISION = 6;
	public static final int DEFAULE_MESSAGE_COUNT = 200;
	public static final int DEFAULT_POST_MAX_COUNT = 50;
	
	public static final String MESSAGE_TYPE_SEND = "send";
	public static final String MESSAGE_TYPE_RECEIVE = "receive";
	
	public static int getPostCount(String count){
		int max = DEFAULT_POST_MAX_COUNT;
		if (count != null) {
			max = Integer.parseInt(count);
		}
		return max;
	}
	
	public static int getMaxCount(String maxCount) {
		int max = MAX_COUNT;
		if (maxCount != null) {
			max = Integer.parseInt(maxCount);
		}
		return max;
	}

	public static UUID getTimeStampUUID(String timeStamp) {
		UUID startUUID = null;
		if (timeStamp != null && timeStamp.length() > 0) {
			startUUID = UUID.fromString(timeStamp);
		}
		return startUUID;
	}

	public static void safePutMap(HashMap<String, String> map, String key,
			String value) {
		if (value != null && value.length() > 0) {
			map.put(key, value);
		}
	}
}
