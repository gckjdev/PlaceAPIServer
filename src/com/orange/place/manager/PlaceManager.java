package com.orange.place.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.model.HColumnImpl;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.StringSimilarityUtil;
import com.orange.common.utils.geohash.ProximitySearchUtil;
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

	public static boolean isPlaceExist(CassandraClient cassandraClient,
			String placeId) {
		int count = cassandraClient.getColumnCount(DBConstants.PLACE, placeId);
		if (count > 0)
			return true;
		return false;
	}

	public static String getCreator(CassandraClient cassandraClient,
			String placeId) {
		String creator = cassandraClient.getColumnValue(DBConstants.PLACE,
				placeId, DBConstants.F_USERID);
		return creator;
	}

	private static List<Place> getPlaceListFromRows(
			Rows<String, String, String> rows) {
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
			System.out.println("**********class: " + columns.get(0).toString());
		}

		return placeList;
	}

	public static Place getPlaceById(CassandraClient cassandraClient,
			String placeId) {
		List<HColumn<String, String>> columns = cassandraClient.getAllColumns(
				DBConstants.PLACE, placeId);
		if (columns == null)
			return null;

		return new Place(columns);
	}

	public static List<Place> getAllPlaces(CassandraClient cassandraClient) {

		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.PLACE, UNLIMITED_COUNT);
		return getPlaceListFromRows(rows);
	}

	public static void createUserOwnPlaceIndex(CassandraClient cassandraClient,
			String userId, String placeId) {

		UUID uuid = UUID.fromString(placeId);
		cassandraClient.insert(DBConstants.INDEX_USER_OWN_PLACE, userId, uuid,
				"");
		return;
	}

	public static void createUserFollowPlaceIndex(
			CassandraClient cassandraClient, String userId, String placeId,
			String dateUuid) {
		UUID uuid = UUID.fromString(placeId);
		cassandraClient.insert(DBConstants.INDEX_USER_FOLLOW_PLACE, userId,
				uuid, dateUuid);
	}

	public static void createPlaceFollowedUserIndex(
			CassandraClient cassandraClient, String userId, String placeId,
			String dateUuid) {
		UUID uuid = UUID.fromString(userId);
		cassandraClient.insert(DBConstants.INDEX_PLACE_FOLLOWED_USERS, placeId,
				uuid, dateUuid);
	}

	public static void getUserFollowStatusByPlace(
			CassandraClient cassandraClient, String userId, String[] placeId,
			boolean[] followStatusResult) {

	}

	public static void userFollowPlace(CassandraClient cassandraClient,
			String userId, String placeId) {
		String uuid = IdGenerator.generateId();
		// String createDate = DateUtil.currentDate();
		createUserFollowPlaceIndex(cassandraClient, userId, placeId, uuid);
		createPlaceFollowedUserIndex(cassandraClient, userId, placeId, uuid);
	}

	public static void deleteUserFollowPlaceIndex(
			CassandraClient cassandraClient, String userId, String placeId) {
		UUID uuid = UUID.fromString(placeId);
		cassandraClient.deleteUUIDColumn(DBConstants.INDEX_USER_FOLLOW_PLACE,
				userId, uuid);
	}

	public static void deletePlaceFollowedUserIndex(
			CassandraClient cassandraClient, String userId, String placeId) {
		UUID uuid = UUID.fromString(userId);
		cassandraClient.deleteUUIDColumn(
				DBConstants.INDEX_PLACE_FOLLOWED_USERS, placeId, uuid);
	}

	public static void userUnFollowPlace(CassandraClient cassandraClient,
			String userId, String placeId) {
		deletePlaceFollowedUserIndex(cassandraClient, userId, placeId);
		deleteUserFollowPlaceIndex(cassandraClient, userId, placeId);
	}

	public static List<Place> getPostList(CassandraClient cassandraClient,
			List<HColumn<UUID, String>> placeIdIndexList) {
		int size = placeIdIndexList.size();

		String[] PlaceIds = new String[size];
		int i = 0;
		for (HColumn<UUID, String> result : placeIdIndexList) {
			String placeId = result.getName().toString();
			PlaceIds[i++] = placeId;
		}

		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.PLACE, PlaceIds);
		if (rows == null) {
			return null;
		}

		List<Place> placeList = new ArrayList<Place>();
		int count = PlaceIds.length;
		for (i = 0; i < count; i++) {
			Row<String, String, String> row = rows.getByKey(PlaceIds[i]);
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			if (columnSlice != null) {
				List<HColumn<String, String>> columns = columnSlice
						.getColumns();
				if (columns != null) {
					placeList.add(new Place(columns));
				}
			}
		}
		return placeList;
	}

	public static List<Place> getUserFollowPlace(
			CassandraClient cassandraClient, String userId) {
		// TODO Auto-generated method stub

		List<HColumn<UUID, String>> resultList = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_USER_FOLLOW_PLACE,
						userId, null, CommonManager.UNLIMITED_COUNT);
		if (resultList == null) {
			log
					.info("<getUserFollowPlace> cannot find any places followed by user("
							+ userId + ")");
			return null;
		}
		return getPostList(cassandraClient, resultList);
	}

	public static void updatePlace(CassandraClient cassandraClient,
			String placeId, String longitude, String latitude, String name,
			String radius, String postType, String desc) {
		HashMap<String, String> map = new HashMap<String, String>();
		safePutMap(map, DBConstants.F_LONGITUDE, longitude);
		safePutMap(map, DBConstants.F_LATITUDE, latitude);
		safePutMap(map, DBConstants.F_NAME, name);
		safePutMap(map, DBConstants.F_RADIUS, radius);
		safePutMap(map, DBConstants.F_POST_TYPE, postType);
		safePutMap(map, DBConstants.F_DESC, desc);
		cassandraClient.insert(DBConstants.PLACE, placeId, map);
	}

	private static List<HColumn<String, String>> getNearyPlaceColumn(
			CassandraClient cassandraClient, double latitude, double longitude,
			double radius) {
		ProximitySearchUtil proximitySearchUtil = new ProximitySearchUtil();
		proximitySearchUtil
				.setPrecision(CommonManager.NEARBY_PLACE_GEOHASH_PRECISION);
		List<String> result = proximitySearchUtil.getNearBy(latitude,
				longitude, radius);

		String resultArray[] = new String[result.size()];
		resultArray = result.toArray(resultArray);

		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.INDEX_GEOHASH6_PLACEID, resultArray);
		int count = result.size();
		if (count < 1)
			return null;
		List<HColumn<String, String>> columnList = new ArrayList<HColumn<String, String>>();

		for (int i = 0; i < count; i++) {
			Row<String, String, String> row = rows.getByKey(result.get(i));
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			if (columnSlice != null) {
				List<HColumn<String, String>> columns = columnSlice
						.getColumns();
				if (columns != null && !columns.isEmpty()) {
					columnList.addAll(columns);
				}
			}
		}
		return columnList;
	}

	public static List<String> getNearbyPlaceId(
			CassandraClient cassandraClient, double latitude, double longitude,
			double radius) {
		List<HColumn<String, String>> columnList = getNearyPlaceColumn(
				cassandraClient, latitude, longitude, radius);
		return getNearbyPlaceId(columnList);
	}

	public static List<String> getNearbyPlaceName(
			CassandraClient cassandraClient, double latitude, double longitude,
			double radius) {
		List<HColumn<String, String>> columnList = getNearyPlaceColumn(
				cassandraClient, latitude, longitude, radius);
		return getNearbyPlaceName(columnList);
	}

	private static List<String> getNearbyPlaceName(
			List<HColumn<String, String>> columnList) {
		if (columnList != null && !columnList.isEmpty()) {
			List<String> placeNameList = new ArrayList<String>();
			for (HColumn<String, String> column : columnList) {
				placeNameList.add(column.getValue());
			}
			return placeNameList;
		}
		return null;
	}

	private static List<String> getNearbyPlaceId(
			List<HColumn<String, String>> columnList) {
		if (columnList != null && !columnList.isEmpty()) {
			List<String> placeIdList = new ArrayList<String>();
			for (HColumn<String, String> column : columnList) {
				placeIdList.add(column.getName());
			}
			return placeIdList;
		}
		return null;
	}

	private static List<Place> getPlaceByPlaceId(
			CassandraClient cassandraClient, String[] placeIds) {
		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.PLACE, placeIds);
		List<Place> placeList = new ArrayList<Place>();
		int count = placeIds.length;
		for (int i = 0; i < count; i++) {
			Row<String, String, String> row = rows.getByKey(placeIds[i]);
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			if (columnSlice != null) {
				List<HColumn<String, String>> columns = columnSlice
						.getColumns();
				if (columns != null) {
					placeList.add(new Place(columns));
				}
			}
		}
		return placeList;
	}

	public static List<Place> getNearbyPlace(CassandraClient cassandraClient,
			double latitude, double longitude, double radius) {
		List<String> placeIds = getNearbyPlaceId(cassandraClient, latitude,
				longitude, radius);
		if (placeIds != null && placeIds.size() > 0) {
			String[] placeIdsArr = new String[placeIds.size()];
			placeIdsArr = placeIds.toArray(placeIdsArr);
			return getPlaceByPlaceId(cassandraClient, placeIdsArr);
		}
		return null;
	}

	public static boolean isPlaceNameUnique(CassandraClient cassandraClient,
			String placeName, double latitude, double longitude, double radius) {
		List<String> placeNames = getNearbyPlaceName(cassandraClient, radius,
				radius, radius);
		for (String name : placeNames) {
			if (name.equals(placeName)) {
				return false;
			}
		}
		return true;
	}

	public static List<HColumn<String, String>> getNearbySimilarPlace(
			CassandraClient cassandraClient, String placeName, double latitude,
			double longitude, double radius) {
		if (placeName == null || placeName.length() < 1)
			return null;

		List<HColumn<String, String>> columnList = getNearyPlaceColumn(
				cassandraClient, latitude, longitude, radius);

		List<String> placeIds = getNearbyPlaceId(columnList);
		List<String> placeNames = getNearbyPlaceName(columnList);

		if (placeIds == null || placeIds.isEmpty() || placeName == null
				|| placeNames.isEmpty())
			return null;

		Map<String, String> nameToId = new HashMap<String, String>();
		int length = Math.min(placeIds.size(), placeNames.size());
		for (int i = 0; i < length; ++i) {
			nameToId.put(placeNames.get(i), placeIds.get(i));
		}
		System.out.println(nameToId);
		if (placeNames == null || placeNames.isEmpty())
			return null;
		String[] srcs = new String[placeNames.size()];
		srcs = placeNames.toArray(srcs);
		String[] result = null;
		result = StringSimilarityUtil.getSortedLCSArray(srcs, placeName,
				CommonManager.MIN_PLACE_NAME_SIMILARITY,
				CommonManager.MAX_SIMILARITY_PLACE_COUNT);
		if (result == null || result.length < 1)
			return null;

		List<HColumn<String, String>> placeList = new ArrayList<HColumn<String, String>>();
		for (String name : result) {
			if (nameToId.containsKey(name)) {
				HColumn<String, String> column = new HColumnImpl<String, String>(
						nameToId.get(name), name, 0);
				placeList.add(column);
			}
		}
		System.out.println(placeList);
		return placeList;
	}
}
