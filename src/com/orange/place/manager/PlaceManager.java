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
import com.orange.place.constant.DBConstants;
import com.orange.place.dao.IdGenerator;
import com.orange.place.dao.Place;

public class PlaceManager extends CommonManager {

	public static Place createPlace(CassandraClient cc, String userId,
			String appId, String longitude, String latitude, String name,
			String radius, String postType, String desc) {

		String placeId = IdGenerator.generateId();

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(DBConstants.F_PLACEID, placeId);
		map.put(DBConstants.F_USERID, userId);
		map.put(DBConstants.F_LONGITUDE, longitude);
		map.put(DBConstants.F_LATITUDE, latitude);
		map.put(DBConstants.F_NAME, name);
		map.put(DBConstants.F_RADIUS, radius);
		map.put(DBConstants.F_POST_TYPE, postType);
		map.put(DBConstants.F_DESC, desc);
		map.put(DBConstants.F_AUTH_FLAG, DBConstants.AUTH_FLAG_NONE);
		map.put(DBConstants.F_PLACE_TYPE, DBConstants.PLACE_TYPE_UNKNOWN);
		map.put(DBConstants.F_CREATE_DATE, DateUtil.currentDate());
		map.put(DBConstants.F_CREATE_SOURCE_ID, appId);
		map.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);

		log.info("<createPlace> placeId=" + placeId + ", userId=" + userId);
		cc.insert(DBConstants.PLACE, placeId, map);

		return new Place(map);
	}

	public static List<Place> getAllPlaces(CassandraClient cassandraClient) {

		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.PLACE, UNLIMITED_COUNT);
		if (rows == null) {
			return null;
		}

		// convert rows to List<Place>
		// TODO the code below can be better
		List<Place> placeList = new ArrayList<Place>();
		for (Row<String, String, String> row : rows) {
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			List<HColumn<String, String>> columns = columnSlice.getColumns();
			if (columns != null) {
				Place place = new Place(columns);
				placeList.add(place);
			}
		}

		return placeList;
	}

	public static void createUserOwnPlaceIndex(CassandraClient cassandraClient,
			String userId, String placeId) {

		UUID uuid = UUID.fromString(placeId);
		cassandraClient.insert(DBConstants.INDEX_USER_OWN_PLACE, userId, uuid,
				"");
		return;
	}

	public static void createUserFollowPlaceIndex(
			CassandraClient cassandraClient, String userId, String placeId) {
		UUID uuid = UUID.fromString(placeId);
		cassandraClient.insert(DBConstants.INDEX_USER_FOLLOW_PLACE, userId,
				uuid, "");
	}

	public static void createPlaceFollowedUserIndex(
			CassandraClient cassandraClient, String userId, String placeId) {

		UUID uuid = UUID.fromString(userId);
		cassandraClient.insert(DBConstants.INDEX_PLACE_FOLLOWED_USERS, placeId,
				uuid, "");
	}

	public static void userFollowPlace(CassandraClient cassandraClient,
			String userId, String placeId) {

		createUserFollowPlaceIndex(cassandraClient, userId, placeId);
		createPlaceFollowedUserIndex(cassandraClient, userId, placeId);
	}

}
