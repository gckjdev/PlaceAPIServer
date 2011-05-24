package com.orange.place.manager;

import java.util.HashMap;
import java.util.UUID;

import org.eclipse.jetty.util.log.Log;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.StringUtil;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.IdGenerator;
import com.orange.place.dao.User;

public class UserManager extends CommonManager {

	public static boolean isLoginIdExist(CassandraClient cc, String loginId, String loginIdType){
		if (Integer.parseInt(loginIdType) != DBConstants.LOGINID_OWN)
			return false;
		String value = cc.getColumnValue(DBConstants.INDEX_USER, DBConstants.KEY_LOGINID, loginId);		
		return (value != null);
	}
	
	public static boolean isDeviceIdExist(CassandraClient cc, String deviceId){
		String value = cc.getColumnValue(DBConstants.INDEX_USER, DBConstants.KEY_DEVICEID, deviceId);				
		return (value != null);
	}
	
	public static boolean createUserLoginIdIndex(CassandraClient cc, String userId, String loginId, String loginIdType){
		if (Integer.parseInt(loginIdType) != DBConstants.LOGINID_OWN)
			return false;
		log.info("<createUserLoginIdIndex> loginId="+loginId+", userId="+userId);
		return cc.insert(DBConstants.INDEX_USER, DBConstants.KEY_LOGINID, loginId, userId);		
	}

	public static boolean createUserDeviceIdIndex(CassandraClient cc, String userId, String deviceId){
		log.info("<createUserDeviceIdIndex> deviceId="+deviceId+", userId="+userId);
		return cc.insert(DBConstants.INDEX_USER, DBConstants.KEY_DEVICEID, deviceId, userId);				
	}
	
	public static User createUser(CassandraClient cc, String loginId, String loginIdType, String appId,
			String deviceModel, String deviceId, String deviceOS,
			String deviceToken, String language, String countryCode,
			String password, String nickName,
			String accessToken, String accessTokenSecret){
		
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
		map.put(DBConstants.F_ACCESSTOKEN, accessToken);
		map.put(DBConstants.F_ACCESSTOKEN_SECRET, accessTokenSecret);
				
		// set loginID, sina ID, qqID by loginIdType...
		switch (Integer.parseInt(loginIdType)){
			case DBConstants.LOGINID_OWN:
				map.put(DBConstants.F_LOGINID, loginId);
				break;
			case DBConstants.LOGINID_SINA:
				map.put(DBConstants.F_SINAID, loginId);
				break;
			case DBConstants.LOGINID_QQ:
				map.put(DBConstants.F_QQID, loginId);
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
		
		log.info("<createUser> loginId="+loginId+", userId="+userId);
		cc.insert(DBConstants.USER, userId, map);
		
		return new User(map);
	}

	public static String getUserNickName(CassandraClient cc, String userId) {
		return cc.getColumnValue(DBConstants.USER, userId, DBConstants.F_NICKNAME);
	}
}
