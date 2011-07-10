package com.orange.place.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.DateUtil;
import com.orange.place.constant.DBConstants;
import com.orange.place.dao.IdGenerator;
import com.orange.place.dao.User;

public class UserManager extends CommonManager {

	public static boolean isLoginIdExist(CassandraClient cc, String loginId,
			String loginIdType) {
		if (Integer.parseInt(loginIdType) != DBConstants.LOGINID_OWN)
			return false;
		String value = cc.getColumnValue(DBConstants.INDEX_USER,
				DBConstants.KEY_LOGINID, loginId);
		return (value != null);
	}

	public static boolean isDeviceIdExist(CassandraClient cc, String deviceId) {
		String value = cc.getColumnValue(DBConstants.INDEX_USER,
				DBConstants.KEY_DEVICEID, deviceId);
		return (value != null);
	}

	public static boolean createUserLoginIdIndex(CassandraClient cc,
			String userId, String loginId, String loginIdType) {
		if (Integer.parseInt(loginIdType) != DBConstants.LOGINID_OWN)
			return false;
		log.info("<createUserLoginIdIndex> loginId=" + loginId + ", userId="
				+ userId);
		return cc.insert(DBConstants.INDEX_USER, DBConstants.KEY_LOGINID,
				loginId, userId);
	}

	public static boolean createUserDeviceIdIndex(CassandraClient cc,
			String userId, String deviceId) {
		log.info("<createUserDeviceIdIndex> deviceId=" + deviceId + ", userId="
				+ userId);
		return cc.insert(DBConstants.INDEX_USER, DBConstants.KEY_DEVICEID,
				deviceId, userId);
	}

	public static User createUser(CassandraClient cc, String loginId, String loginIdType, String appId,
			String deviceModel, String deviceId, String deviceOS,
			String deviceToken, String language, String countryCode,
			String password, String nickName, String avatar,
			String accessToken, String accessTokenSecret,
			String province, String city, String location,
			String gender, String birthday,
			String sinaNickName, String sinaDomain,
			String qqNickName, String qqDomain){

		String userId = IdGenerator.generateId();

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(DBConstants.F_USERID, userId);
		map.put(DBConstants.F_APPID, appId);
		map.put(DBConstants.F_DEVICEMODEL, deviceModel);
		map.put(DBConstants.F_DEVICEID, deviceId);
		map.put(DBConstants.F_DEVICEOS, deviceOS);
		map.put(DBConstants.F_DEVICETOKEN, deviceToken);
		map.put(DBConstants.F_LANGUAGE, language);
		map.put(DBConstants.F_COUNTRYCODE, countryCode);
		map.put(DBConstants.F_PASSWORD, password);
		map.put(DBConstants.F_CREATE_DATE, DateUtil.currentDate());
		map.put(DBConstants.F_CREATE_SOURCE_ID, appId);
		map.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);
		map.put(DBConstants.F_NICKNAME, nickName);
		map.put(DBConstants.F_AVATAR, avatar);
		map.put(DBConstants.F_PROVINCE, province);
		map.put(DBConstants.F_CITY, city);
		map.put(DBConstants.F_LOCATION, location);
		map.put(DBConstants.F_GENDER, gender);
		map.put(DBConstants.F_BIRTHDAY, birthday);
		map.put(DBConstants.F_SINA_NICKNAME, sinaNickName);
		map.put(DBConstants.F_SINA_DOMAIN, sinaDomain);
		map.put(DBConstants.F_QQ_NICKNAME, qqNickName);
		map.put(DBConstants.F_QQ_DOMAIN, qqDomain);

		// set loginID, sina ID, qqID by loginIdType...
		switch (Integer.parseInt(loginIdType)) {
		case DBConstants.LOGINID_OWN:
			map.put(DBConstants.F_LOGINID, loginId);
			break;
		case DBConstants.LOGINID_SINA:
			map.put(DBConstants.F_SINAID, loginId);
			map.put(DBConstants.F_SINA_ACCESS_TOKEN, accessToken);
			map.put(DBConstants.F_SINA_ACCESS_TOKEN_SECRET, accessTokenSecret);

			break;
		case DBConstants.LOGINID_QQ:
			map.put(DBConstants.F_QQID, loginId);
			map.put(DBConstants.F_QQ_ACCESS_TOKEN, accessToken);
			map.put(DBConstants.F_QQ_ACCESS_TOKEN_SECRET, accessTokenSecret);
			break;
		case DBConstants.LOGINID_RENREN:
			map.put(DBConstants.F_RENRENID, loginId);
			break;
		case DBConstants.LOGINID_TWITTER:
			map.put(DBConstants.F_TWITTERID, loginId);
			break;
		case DBConstants.LOGINID_FACEBOOK:
			map.put(DBConstants.F_FACEBOOKID, loginId);
			break;
		}

		log.info("<createUser> loginId=" + loginId + ", userId=" + userId);
		cc.insert(DBConstants.USER, userId, map);

		return new User(map);
	}

	public static User bindUser(CassandraClient cc, String userId,
			String loginId, String loginIdType, String deviceId,
			String nickName, String avatar, String accessToken,
			String accessTokenSecret, String domain, String province,
			String city, String location, String gender, String birthday) {

		if (userId == null || loginId == null) {
			log.info("<bindUser> but userId or loginId is null");
			return null;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		safePutMap(map, DBConstants.F_USERID, userId);
		safePutMap(map, DBConstants.F_DEVICEID, deviceId);
		safePutMap(map, DBConstants.F_CREATE_DATE, DateUtil.currentDate());
		safePutMap(map, DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);
		safePutMap(map, DBConstants.F_NICKNAME, nickName);
		safePutMap(map, DBConstants.F_AVATAR, avatar);
		safePutMap(map, DBConstants.F_PROVINCE, province);
		safePutMap(map, DBConstants.F_CITY, city);
		safePutMap(map, DBConstants.F_LOCATION, location);
		safePutMap(map, DBConstants.F_GENDER, gender);
		safePutMap(map, DBConstants.F_BIRTHDAY, birthday);

		// set loginID, sina ID, qqID by loginIdType...
		switch (Integer.parseInt(loginIdType)) {
		case DBConstants.LOGINID_OWN:
			safePutMap(map, DBConstants.F_LOGINID, loginId);
			break;
		case DBConstants.LOGINID_SINA:
			safePutMap(map, DBConstants.F_SINAID, loginId);
			safePutMap(map, DBConstants.F_SINA_ACCESS_TOKEN, accessToken);
			safePutMap(map, DBConstants.F_SINA_ACCESS_TOKEN_SECRET,
					accessTokenSecret);
			safePutMap(map, DBConstants.F_SINA_NICKNAME, nickName);
			safePutMap(map, DBConstants.F_SINA_DOMAIN, domain);

			break;
		case DBConstants.LOGINID_QQ:
			safePutMap(map, DBConstants.F_QQID, loginId);
			safePutMap(map, DBConstants.F_QQ_ACCESS_TOKEN, accessToken);
			safePutMap(map, DBConstants.F_QQ_ACCESS_TOKEN_SECRET,
					accessTokenSecret);
			safePutMap(map, DBConstants.F_QQ_NICKNAME, nickName);
			safePutMap(map, DBConstants.F_QQ_DOMAIN, domain);

			break;
		case DBConstants.LOGINID_RENREN:
			safePutMap(map, DBConstants.F_RENRENID, loginId);
			break;
		case DBConstants.LOGINID_TWITTER:
			safePutMap(map, DBConstants.F_TWITTERID, loginId);
			break;
		case DBConstants.LOGINID_FACEBOOK:
			safePutMap(map, DBConstants.F_FACEBOOKID, loginId);
			break;
		}

		log.info("<bindUser> userId=" + userId + ", loginId=" + loginId);
		cc.insert(DBConstants.USER, userId, map);

		return new User(map);
	}

	public static String getUserNickName(CassandraClient cc, String userId) {
		return cc.getColumnValue(DBConstants.USER, userId,
				DBConstants.F_NICKNAME);
	}

	public static User getUserByDevice(CassandraClient cassandraClient,
			String deviceId) {

		String userId = cassandraClient.getColumnValue(DBConstants.INDEX_USER,
				DBConstants.KEY_DEVICEID, deviceId);
		
		User user =  getUserById(cassandraClient, userId);
		if(user==null){
			log.warn("<getUserByDevice> deviceId(" + deviceId
					+ ") is bind but userId not found");
		}
		return user;
	}

	public static User getUserById(CassandraClient cassandraClient,
			String userId) {
		if (userId == null) {
			return null;
		}

		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.USER, userId);
		if (rows == null || rows.getCount() <= 0) {
			return null;
		}

		User user = null;
		for (Row<String, String, String> row : rows) {
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			List<HColumn<String, String>> columns = columnSlice.getColumns();
			if (columns != null) {
				user = new User(columns);
			}

			break; // one row expected
		}

		return user;
	}

	public static void updateUser(CassandraClient cassandraClient,
			String userId, String mobile, String eMail, String nickName,
			String password, String avatarUrl, String gender) {
		HashMap<String, String> map = new HashMap<String, String>();
		safePutMap(map, DBConstants.F_MOBILE, mobile);
		safePutMap(map, DBConstants.F_EMAIL, eMail);

		safePutMap(map, DBConstants.F_NICKNAME, nickName);
		safePutMap(map, DBConstants.F_PASSWORD, password);
		safePutMap(map, DBConstants.F_GENDER, gender);
		safePutMap(map, DBConstants.F_AVATAR, avatarUrl);
		cassandraClient.insert(DBConstants.USER, userId, map);
		
		log.info("<updateUser> userId="+userId+",mobile="+mobile+
				",nickName="+nickName+",email="+eMail+",avatar="+avatarUrl+",gender="+gender);
	}

	public static boolean isUserExist(CassandraClient cassandraClient,
			String userId) {
		int count = cassandraClient.getColumnCount(DBConstants.USER, userId);
		if (count > 0)
			return true;
		return false;
	}

	// TODO: duplicate
	public Iterator<String> findAllUserId(CassandraClient cassandraClient) {
		int max = DBConstants.UNLIMITED_COUNT;
		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.USER, max);

		List<String> userList = getKeys(rows);
		return userList.iterator();
	}

	private List<String> getKeys(Rows<String, String, String> rows) {
		List<String> userList = new ArrayList<String>();
		for (Row<String, String, String> row : rows) {
			String userId = row.getKey();
			userList.add(userId);
		}
		return userList;
	}
}
