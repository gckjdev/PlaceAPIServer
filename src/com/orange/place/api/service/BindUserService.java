package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.User;
import com.orange.place.manager.UserManager;

public class BindUserService extends CommonService {

	String userId;
	String loginId;
	String loginIdType;
	String appId;
	String nickName;
	String avatar;
	String accessToken;
	String accessTokenSecret;
	String province;
	String city;
	String location;
	String gender;
	String birthday;
	String domain;
	
	String password;
	
	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		
		boolean isLoginIdExist = UserManager.isLoginIdExist(cassandraClient, loginId, loginIdType);
		
		if (isLoginIdExist){
			resultCode = ErrorCode.ERROR_LOGINID_EXIST;
			log.info("<registerUser> user loginId exist, loginId="+loginId);
			return;
		}
						
		User user = UserManager.bindUser(cassandraClient, userId, loginId, loginIdType, appId, 
				nickName, avatar, accessToken, accessTokenSecret, domain,
				province, city, location, gender, birthday);
		if (user == null){
			resultCode = ErrorCode.ERROR_CREATE_USER;
			log.info("<registerUser> fail to create user, loginId="+loginId);
			return;
		}
		
		String userId = user.getUserId();
		UserManager.createUserLoginIdIndex(cassandraClient, userId, loginId, loginIdType);
		
		// set result data, return userId
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_USERID, userId);
		resultData = obj;
		
		// TODO if there is any exception, shall clean all inconsistent data and index
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printData() {
		// TODO Auto-generated method stub
		log.info(String.format("userId=%s, loginId=%s, loginIdType=%s, appId=%s, nickName=%s", 
				userId, loginId, loginIdType, appId, nickName));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		loginId = request.getParameter(ServiceConstant.PARA_LOGINID);
		loginIdType = request.getParameter(ServiceConstant.PARA_LOGINIDTYPE);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		nickName = request.getParameter(ServiceConstant.PARA_NICKNAME);
		avatar = request.getParameter(ServiceConstant.PARA_AVATAR);
		accessToken = request.getParameter(ServiceConstant.PARA_ACCESS_TOKEN);
		accessTokenSecret = request.getParameter(ServiceConstant.PARA_ACCESS_TOKEN_SECRET);
		province = request.getParameter(ServiceConstant.PARA_PROVINCE);
		city = request.getParameter(ServiceConstant.PARA_CITY);
		location = request.getParameter(ServiceConstant.PARA_LOCATION);
		gender = request.getParameter(ServiceConstant.PARA_GENDER);
		birthday = request.getParameter(ServiceConstant.PARA_BIRTHDAY);
		domain = request.getParameter(ServiceConstant.PARA_DOMAIN);
		
		if (!check(loginId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;
		
		if (!check(loginIdType, ErrorCode.ERROR_PARAMETER_USERTYPE_EMPTY, ErrorCode.ERROR_PARAMETER_USERTYPE_NULL))
			return false;
		
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY, ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		// set nick name the same as login Id if it doesn't exist
		if (nickName == null || nickName.length() == 0){
			nickName = loginId;
		}
		
		return true;
	}

}
