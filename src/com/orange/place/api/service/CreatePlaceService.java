package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Place;
import com.orange.place.manager.PlaceManager;
import com.orange.place.manager.UserManager;

public class CreatePlaceService extends CommonService {

	String userId;
	String appId;
	String longitude;
	String latitude;
	String name;
	String radius;
	String postType;
	String desc;
	
	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		
		// get user nickname for return
		String nickName = UserManager.getUserNickName(cassandraClient, userId);
		if (nickName == null){
			log.info("fail to get user nick name, userId="+userId);
			resultCode = ErrorCode.ERROR_USER_GET_NICKNAME;
			return;
		}
		
		// TODO check if the name is duplicated in recent area
		
		
		// create place
		Place place = PlaceManager.createPlace(cassandraClient, userId, appId, longitude, latitude,
				name, radius, postType, desc);		
		if (place == null){
			log.info("fail to create place, place name="+name+", userId="+userId);
			resultCode = ErrorCode.ERROR_CREATE_PLACE;
			return;
		}				
		
		String placeId = place.getPlaceId();
		String createDate = place.getCreateDate();
		
		// set result data, return userId
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_PLACEID, placeId);
		obj.put(ServiceConstant.PARA_NICKNAME, nickName);
		obj.put(ServiceConstant.PARA_CREATE_DATE, createDate);
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
		log.info(String.format("userId=%s, appId=%s, longitude=%s, latitude=%s," +
				"name=%s, radisu=%s, postType=%s, desc=%s", 
				userId, appId, longitude, latitude,
				name, radius, postType, desc));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		longitude = request.getParameter(ServiceConstant.PARA_LONGTITUDE);
		latitude = request.getParameter(ServiceConstant.PARA_LATITUDE);
		name = request.getParameter(ServiceConstant.PARA_NAME);
		radius = request.getParameter(ServiceConstant.PARA_RADIUS);
		postType = request.getParameter(ServiceConstant.PARA_POSTTYPE);
		desc = request.getParameter(ServiceConstant.PARA_DESC);		
		
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY, ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(longitude, ErrorCode.ERROR_PARAMETER_LONGITUDE_EMPTY, ErrorCode.ERROR_PARAMETER_LONGITUDE_NULL))
			return false;

		if (!check(latitude, ErrorCode.ERROR_PARAMETER_LATITUDE_EMPTY, ErrorCode.ERROR_PARAMETER_LATITUDE_NULL))
			return false;

		if (!check(name, ErrorCode.ERROR_PARAMETER_NAME_EMPTY, ErrorCode.ERROR_PARAMETER_NAME_NULL))
			return false;

		if (!check(radius, ErrorCode.ERROR_PARAMETER_RADIUS_EMPTY, ErrorCode.ERROR_PARAMETER_RADIUS_NULL))
			return false;

		if (!check(postType, ErrorCode.ERROR_PARAMETER_POSTTYPE_EMPTY, ErrorCode.ERROR_PARAMETER_POSTTYPE_NULL))
			return false;

		if (!check(desc, ErrorCode.ERROR_PARAMETER_DESC_EMPTY, ErrorCode.ERROR_PARAMETER_DESC_NULL))
			return false;

		// TODO need to check latitude, longitude, postType, radius is valid decimal/number 
		
		return true;
	}

}
