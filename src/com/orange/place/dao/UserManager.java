package com.orange.place.dao;

import java.util.UUID;

import org.eclipse.jetty.util.log.Log;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.StringUtil;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;

public class UserManager {

	String loginId;
	String loginIdType;
	String appId;
	String deviceModel;
	String deviceId;
	String deviceOS;
	String deviceToken;	

	String language;
	String countryCode;
	String nickName;
	
	String password;
	public static String createUser(CassandraClient cc, String loginId, String loginIdType, String appId,
			String deviceModel, String deviceId, String deviceOS,
			String deviceToken, String language, String countryCode,
			String password){
		
		String uuid = UUID.randomUUID().toString();
		String[] names = StringUtil.getStringList(
				ServiceConstant.PARA_LOGINID,
				ServiceConstant.PARA_LOGINIDTYPE,
				ServiceConstant.PARA_APPID,
				ServiceConstant.PARA_DEVICEMODEL,
				ServiceConstant.PARA_DEVICEID,
				ServiceConstant.PARA_DEVICEOS,
				ServiceConstant.PARA_DEVICETOKEN,
				ServiceConstant.PARA_LANGUAGE,
				ServiceConstant.PARA_COUNTRYCODE,
				ServiceConstant.PARA_PASSWORD				
				);
		
		String[] values = StringUtil.getStringList(
				loginId, loginIdType, appId,
				deviceModel, deviceId, deviceOS,
				deviceToken, language, countryCode,
				password
				);
		
		if (names.length != values.length){
			Log.warn("<createUser> but name and value size not the same");
			return null;
		}
		
		cc.insert(DBConstants.USER, uuid, names, values);
		
		return uuid;
	}
}
