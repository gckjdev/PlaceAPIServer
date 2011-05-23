package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.UserManager;

public class RegisterUser extends CommonServiceObject {

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
	
	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		String userId = UserManager.createUser(cassandraClient, loginId, loginIdType, appId, deviceModel, deviceId, deviceOS, deviceToken, language, countryCode, password);
		if (userId == null){
			resultCode = ErrorCode.ERROR_CASSANDRA;
		}
		else{
			JSONObject obj = new JSONObject();
			obj.put(ServiceConstant.PARA_USERID, userId);
			resultData = obj;
		}
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

		return true;
	}

}
