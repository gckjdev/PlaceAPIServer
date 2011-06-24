package com.orange.place.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.geohash.GeoHashUtil;
import com.orange.place.constant.DBConstants;
import com.orange.place.dao.IdGenerator;
import com.orange.place.dao.Place;
import com.orange.place.dao.Post;

public class PostManager extends CommonManager {

	public static Post createPost(CassandraClient cassandraClient,
			String userId, String appId, String placeId, String longitude,
			String latitude, String userLongitude, String userLatitude,
			String textContent, String contentType, String srcPostId,
			String replyPostId, String imageURL) {

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
		
		if (imageURL != null){
			map.put(DBConstants.F_IMAGE_URL, imageURL);
		}

		if (srcPostId != null && srcPostId.length() > 0) {
			map.put(DBConstants.F_SRC_POSTID, srcPostId);
		} else {
			map.put(DBConstants.F_SRC_POSTID, postId);
		}

		if (replyPostId != null && replyPostId.length() > 0) {
			map.put(DBConstants.F_REPLY_POSTID, replyPostId);
		} else {
			map.put(DBConstants.F_REPLY_POSTID, replyPostId);
		}

		map.put(DBConstants.F_CREATE_DATE, DateUtil.currentDate());
		map.put(DBConstants.F_CREATE_SOURCE_ID, appId);
		map.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);

		log.info("<createPost> postId=" + postId + ", userId=" + userId);
		cassandraClient.insert(DBConstants.POST, postId, map);

		return new Post(map);
	}

	public static List<Post> getPostList(CassandraClient cassandraClient,
			List<HColumn<UUID, String>> postIdIndexList) {

		int size = postIdIndexList.size();

		String[] postIds = new String[size];
		int i = 0;
		for (HColumn<UUID, String> result : postIdIndexList) {
			String postId = result.getName().toString();
			postIds[i++] = postId;
		}

		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.POST, postIds);
		if (rows == null) {
			return null;
		}

		String[] userIds = new String[size];
		String[] placeIds = new String[size];

		// convert rows to List<Post>
		// change the implementation to sort the return result in right order
		List<Post> postList = new ArrayList<Post>();
		int count = postIds.length;
		for (i = 0; i < count; i++) {
			Row<String, String, String> row = rows.getByKey(postIds[i]);
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			if (columnSlice != null) {
				List<HColumn<String, String>> columns = columnSlice
						.getColumns();
				if (columns != null) {
					Post post = new Post(columns);
					userIds[i] = post.getUserId();
					placeIds[i] = post.getPlaceId();
					postList.add(post);
				}
			}
		}
		
		// get user nickname and avatar
		Rows<String, String, String> userRows = cassandraClient.getMultiRow(
				DBConstants.USER, userIds, DBConstants.F_USERID,
				DBConstants.F_NICKNAME, DBConstants.F_AVATAR);

		// get place Id & name
		Rows<String, String, String> placeRows = cassandraClient.getMultiRow(
				DBConstants.PLACE, placeIds,
				DBConstants.F_NAME);

		
		for (Post post : postList) {
			// set user nickname and avatar to post
			String userId = post.getUserId();
			if (userId != null) {
				Row<String, String, String> userRow = userRows.getByKey(userId);
				ColumnSlice<String, String> columnSlice = userRow.getColumnSlice();
				if (columnSlice != null) {
					List<HColumn<String, String>> columns = columnSlice
							.getColumns();
					if (columns != null) {
						post.addValues(columns);
					}
				}
			}
			
			// set place name to post
			String placeId = post.getPlaceId();
			if (placeId != null){
				Row<String, String, String> placeRow = placeRows.getByKey(placeId);
				ColumnSlice<String, String> columnSlice = placeRow.getColumnSlice();
				if (columnSlice != null) {
					List<HColumn<String, String>> columns = columnSlice
							.getColumns();
					if (columns != null) {
						post.addValues(columns);
					}
				}				
			}

			// set related post number to post
			// could be low performance, TODO wait for a better API or solution
			int relatedPostCount = cassandraClient.getColumnCount(
					DBConstants.INDEX_POST_RELATED_POST, post.getSrcPostId());
			post.addValues(DBConstants.C_TOTAL_RELATED, relatedPostCount);
		}

		return postList;
	}
	
	public static Post getPostById(CassandraClient cassandraClient,
			String postId){
		List<HColumn<String, String>> columns = cassandraClient.getAllColumns(DBConstants.POST, postId);
		if (columns == null)
			return null;
		
		return new Post(columns); 
	}

	public static List<Post> getPostByPlace(CassandraClient cassandraClient,
			String placeId, String beforeTimeStamp, String maxCount) {

		UUID startUUID = getStartUUID(beforeTimeStamp);
		int max = getMaxCount(maxCount);

		List<HColumn<UUID, String>> resultList = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_PLACE_POST, placeId,
						startUUID, max);
		if (resultList == null) {
			return null;
		}

		List<Post> postList = getPostList(cassandraClient, resultList);
		return postList;
	}

	public static void createPlacePostIndex(CassandraClient cassandraClient,
			String placeId, String postId) {

		UUID uuid = UUID.fromString(postId);
		cassandraClient.insert(DBConstants.INDEX_PLACE_POST, placeId, uuid, "");
	}
	
	public static void createUserPostIndex(CassandraClient cassandraClient,
			String userId, String postId) {
		UUID uuid = UUID.fromString(postId);
		cassandraClient.insert(DBConstants.INDEX_USER_POST, userId, uuid, "");
	}

	public static void createPostRelatedPostIndex(
			CassandraClient cassandraClient, String postId, String srcPostId) {

		UUID uuid = UUID.fromString(postId);
		cassandraClient.insert(DBConstants.INDEX_POST_RELATED_POST, srcPostId,
				uuid, "");
	}

	public static void createPostLocationIndex(CassandraClient cassandraClient,
			String postId, String createDate, String latitude, String longitude) {
		UUID uuid = UUID.fromString(postId);
		GeoHashUtil util = new GeoHashUtil();
		String geoHash = util.encode(latitude, longitude);
		cassandraClient.insert(DBConstants.INDEX_POST_LOCATION, geoHash, uuid, createDate);
	}
	
	public static void createUserViewPostIndex(CassandraClient cassandraClient,
			String placeId, String postId, String createDate) {

		// TODO this method could take a long time, so maybe it shall be run in
		// another thread or process

		// get users who follow the place
		List<HColumn<UUID, String>> columnValues = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_PLACE_FOLLOWED_USERS,
						placeId, null, UNLIMITED_COUNT);
		if (columnValues == null) {
			return;
		}

		// insert postId for each user
		UUID postUUID = UUID.fromString(postId);
		for (HColumn<UUID, String> columnValue : columnValues) {
			String userId = columnValue.getName().toString();
			cassandraClient.insert(DBConstants.INDEX_USER_VIEW_POSTS, userId,
					postUUID, createDate);
		}
	}

	public static List<Post> getUserTimeline(CassandraClient cassandraClient,
			String userId, String beforeTimeStamp, String maxCount) {

		UUID startUUID = getStartUUID(beforeTimeStamp);
		int max = getMaxCount(maxCount);

		List<HColumn<UUID, String>> resultList = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_USER_VIEW_POSTS, userId,
						startUUID, max);
		if (resultList == null) {
			return null;
		}

		List<Post> postList = getPostList(cassandraClient, resultList);
		return postList;
	}

	public static List<Post> getUserPosts(CassandraClient cassandraClient,
			String userId, String beforeTimeStamp,
			String maxCount) {
		UUID startUUID = getStartUUID(beforeTimeStamp);
		int max = getMaxCount(maxCount);
		List<HColumn<UUID, String>> resultList = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_USER_POST, userId,
						startUUID, max);
		if (resultList == null) {
			return null;
		}
		List<Post> postList = getPostList(cassandraClient, resultList);
		return postList;
	}

	public static List<Post> getAllPosts(CassandraClient cassandraClient) {

		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.POST, UNLIMITED_COUNT);
		if (rows == null) {
			return null;
		}
		// convert rows to List<Post>
		// TODO the code below can be better
		List<Post> postList = new ArrayList<Post>();
		for (Row<String, String, String> row : rows) {
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			List<HColumn<String, String>> columns = columnSlice.getColumns();
			if (columns != null) {
				Post post = new Post(columns);
				postList.add(post);
			}
		}
		return postList;
	}

	public static List<Post> getRelatedPostByPost(
			CassandraClient cassandraClient, String postId,
			String beforeTimeStamp, String maxCount) {

		UUID startUUID = getStartUUID(beforeTimeStamp);
		int max = getMaxCount(maxCount);

		List<HColumn<UUID, String>> resultList = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_POST_RELATED_POST,
						postId, startUUID, max);
		if (resultList == null) {
			return null;
		}

		List<Post> postList = getPostList(cassandraClient, resultList);
		return postList;
	}

	public static void deleteUnFollowPlacePosts(
			CassandraClient cassandraClient, String userId, String placeId,
			int size) {
		List<HColumn<UUID, String>> list = cassandraClient.getColumnKeyByRange(
				DBConstants.INDEX_PLACE_POST, placeId, IdGenerator
						.generateUUId(), size);
		UUID[] columnNames = new UUID[list.size()];
		int i = 0;
		for (HColumn<UUID, String> name : list) {
			columnNames[i++] = name.getName();
		}
		cassandraClient.deleteMultipleColumns(
				DBConstants.INDEX_USER_VIEW_POSTS, userId, columnNames);
	}

	public static void addFollowPlacePosts(CassandraClient cassandraClient,
			String userId, String placeId, UUID end, int size) {
		List<HColumn<UUID, String>> list = cassandraClient.getColumnKeyByRange(
				DBConstants.INDEX_PLACE_POST, placeId, null, end, size);
		UUID[] keys = new UUID[list.size()];
		int i = 0;
		for (HColumn<UUID, String> name : list) {
			keys[i++] = name.getName();
		}
		cassandraClient.insert(DBConstants.INDEX_USER_VIEW_POSTS, userId, keys,
				null);
	}

	public static void createUserMePostIndex(CassandraClient cassandraClient,
			String userId, String replyPostId) {
		UUID uuid = UUID.fromString(replyPostId);
		cassandraClient.insert(DBConstants.INDEX_ME_POST, userId, uuid, "");
	}

	public static List<Post> getMePosts(CassandraClient cassandraClient,
			String userId, String beforeTimeStamp, String maxCount) {
		UUID startUUID = getStartUUID(beforeTimeStamp);
		int max = getMaxCount(maxCount);
		List<HColumn<UUID, String>> resultList = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_ME_POST, userId,
						startUUID, max);
		if (resultList == null) {
			return null;
		}
		List<Post> postList = getPostList(cassandraClient, resultList);
		return postList;
	}
}
