package com.orange.place.manager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Rows;

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

	public static List<Post> getPostByPlace(CassandraClient cassandraClient,
			String placeId, String beforeTimeStamp, String maxCount) {
	
		int max = MAX_COUNT; 		
		if (maxCount != null){
			max = Integer.parseInt(maxCount);
		}
				
		UUID startUUID = null;
		if (beforeTimeStamp != null){
			startUUID = UUID.fromString(beforeTimeStamp);
		}
		
		List<HColumn<UUID, String>> resultList = cassandraClient.getColumnKeyByRange(DBConstants.INDEX_PLACE_POST, placeId, startUUID, max);
		if (resultList == null){
			return null;
		}
		
		String[] postIds = new String[resultList.size()];
		int i=0;
		for (HColumn<UUID, String> result : resultList){
			String postId = result.getName().toString();
			postIds[i++] = postId;
		}
		
		Rows<String, String, String> rows = cassandraClient.getMultiRow(DBConstants.POST, postIds);
		if (rows == null){
			return null;
		}
		
		// convert rows to List<Post>
		
		return null;
	}

	public static void createPlacePostIndex(CassandraClient cassandraClient,
			String placeId, String postId) {

		UUID uuid = UUID.fromString(postId);		
		cassandraClient.insert(DBConstants.INDEX_PLACE_POST, placeId, uuid, "");
	}

}
