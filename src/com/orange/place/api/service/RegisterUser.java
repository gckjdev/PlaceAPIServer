package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.UserManager;

public class RegisterUser extends CommonService {

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
	String accessToken;
	String accessTokenSecret;
	
	String password;
	
	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		
		boolean isLoginIdExist = UserManager.isLoginIdExist(cassandraClient, loginId, loginIdType);
		boolean isDeviceIdExist = UserManager.isDeviceIdExist(cassandraClient, deviceId);
		if (isLoginIdExist && isDeviceIdExist){
			log.info("<registerUser> user loginId("+loginId+") and deviceId("+deviceId+") exist");
			resultCode = ErrorCode.ERROR_LOGINID_DEVICE_BOTH_EXIST;
			return;
		}
		else if (isLoginIdExist){
			resultCode = ErrorCode.ERROR_LOGINID_EXIST;
			log.info("<registerUser> user loginId exist, loginId="+loginId);
			return;
		}
		else if (isDeviceIdExist){
			resultCode = ErrorCode.ERROR_DEVICEID_EXIST;
			log.info("<registerUser> user deviceId exist, deviceId="+deviceId);
			return;
		}		
		
		String userId = UserManager.createUser(cassandraClient, loginId, loginIdType, appId, 
				deviceModel, deviceId, deviceOS, deviceToken, 
				language, countryCode, password, nickName, accessToken, accessTokenSecret);
		if (userId == null){
			resultCode = ErrorCode.ERROR_CREATE_USER;
			log.info("<registerUser> fail to create user, loginId="+loginId);
			return;
		}

		UserManager.createUserDeviceIdIndex(cassandraClient, userId, deviceId);
		UserManager.createUserLoginIdIndex(cassandraClient, userId, loginId, loginIdType);
		
		// set result data, return userId
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_USERID, userId);
		resultData = obj;
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printData() {
		// TODO Auto-generated method stub
		log.info(String.format("loginId=%s, loginIdType=%s, appId=%s, deviceModel=%s, " +
				"deviceId=%s, deviceOS=%s, deviceToken=%s, " +
				"language=%s, countryCode=%s, nickName=%s", loginId, loginIdType,
				appId, deviceModel, deviceId, deviceOS, deviceToken,
				language, countryCode, nickName));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		loginId = request.getParameter(ServiceConstant.PARA_LOGINID);
		loginIdType = request.getParameter(ServiceConstant.PARA_LOGINIDTYPE);
		deviceId = request.getParameter(ServiceConstant.PARA_DEVICEID);
		deviceModel = request.getParameter(ServiceConstant.PARA_DEVICEMODEL);
		deviceOS = request.getParameter(ServiceConstant.PARA_DEVICEOS);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		countryCode = request.getParameter(ServiceConstant.PARA_COUNTRYCODE);
		language = request.getParameter(ServiceConstant.PARA_LANGUAGE);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		nickName = request.getParameter(ServiceConstant.PARA_NICKNAME);
		deviceToken = request.getParameter(ServiceConstant.PARA_DEVICETOKEN);
		accessToken = request.getParameter(ServiceConstant.PARA_ACCESS_TOKEN);
		accessTokenSecret = request.getParameter(ServiceConstant.PARA_ACCESS_TOKEN_SECRET);
		
		if (!check(loginId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;
		
		if (!check(loginIdType, ErrorCode.ERROR_PARAMETER_USERTYPE_EMPTY, ErrorCode.ERROR_PARAMETER_USERTYPE_NULL))
			return false;
		
		if (!check(deviceId, ErrorCode.ERROR_PARAMETER_DEVICEID_EMPTY, ErrorCode.ERROR_PARAMETER_DEVICEID_NULL))
			return false;

		if (!check(deviceModel, ErrorCode.ERROR_PARAMETER_DEVICEMODEL_EMPTY, ErrorCode.ERROR_PARAMETER_DEVICEMODEL_NULL))
			return false;

		if (!check(deviceOS, ErrorCode.ERROR_PARAMETER_DEVICEOS_EMPTY, ErrorCode.ERROR_PARAMETER_DEVICEOS_NULL))
			return false;

//		if (!check(countryCode, ErrorCode.ERROR_PARAMETER_COUNTRYCODE_EMPTY, ErrorCode.ERROR_PARAMETER_COUNTRYCODE_NULL))
//			return false;
//
//		if (!check(language, ErrorCode.ERROR_PARAMETER_LANGUAGE_EMPTY, ErrorCode.ERROR_PARAMETER_LANGUAGE_NULL))
//			return false;

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY, ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		// set nick name the same as login Id if it doesn't exist
		if (nickName == null || nickName.length() == 0){
			nickName = loginId;
		}
		
		return true;
	}

}
