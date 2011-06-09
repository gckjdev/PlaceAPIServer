package com.orange.place.manager;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import com.orange.place.api.PlaceAPIServer;

public class CommonManager {

	public static final Logger log = Logger.getLogger(PlaceAPIServer.class
			.getName());

	public static final long MAX_FOLLOWPLACE_POSTS_TIMEOFFSET = 24 * 60 * 60 * 1000;

	public static final int MAX_COUNT = 30;
	public static final int UNLIMITED_COUNT = Integer.MAX_VALUE;
	public static final int MAX_DELETE_UNFOLLOWPLACE_COUNT = 3;
	public static final int MAX_ADD_FOLLOWPLACE_COUNT = 30;

	public static int getMaxCount(String maxCount) {
		int max = MAX_COUNT;
		if (maxCount != null) {
			max = Integer.parseInt(maxCount);
		}
		return max;
	}

	public static UUID getStartUUID(String beforeTimeStamp) {
		UUID startUUID = null;
		if (beforeTimeStamp != null && beforeTimeStamp.length() > 0) {
			startUUID = UUID.fromString(beforeTimeStamp);
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
