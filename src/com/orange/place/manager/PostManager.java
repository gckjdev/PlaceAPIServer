package com.orange.place.manager;

import java.util.HashMap;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.DateUtil;
import com.orange.place.constant.DBConstants;
import com.orange.place.dao.IdGenerator;
import com.orange.place.dao.Place;
import com.orange.place.dao.Post;

public class PostManager extends CommonManager {

	public static Post createPost(CassandraClient cassandraClient,
			String userId, String appId, String placeId, String longitude,
			String latitude, String userLongitude, String userLatitude,
			String textContent, String contentType) {
		
		String postId = IdGenerator.generateId();
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(DBConstants.F_POSTID, postId);
		map.put(DBConstants.F_PLACEID, placeId);
		map.put(DBConstants.F_USERID, userId);
		map.put(DBConstants.F_LONGITUDE, longitude);		
		map.put(DBConstants.F_LATITUDE, latitude);		
		map.put(DBConstants.F_USER_LONGITUDE, userLongitude);		
		map.put(DBConstants.F_USER_LATITUDE, userLatitude);		
		map.put(DBConstants.F_TEXT_CONTENT, textContent);		
		map.put(DBConstants.F_CONTENT_TYPE, contentType);		

		map.put(DBConstants.F_CREATE_DATE, DateUtil.currentDate());
		map.put(DBConstants.F_CREATE_SOURCE_ID, appId);		
		map.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);		
				
		log.info("<createPost> postId="+postId+", userId="+userId);
		cassandraClient.insert(DBConstants.POST, postId, map);
		
		return new Post(map);
	}

}
