package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Place;
import com.orange.place.dao.Post;
import com.orange.place.manager.PlaceManager;

import java.util.List;

public class GetNearbyPlaceService extends CommonService {

	String userId;
	String appId;
	String longitude;
	String latitude;
	String beforeTimeStamp;	
	String maxCount;
	
	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		
		// Fake implementation, return all place data
		List<Place> placeList = PlaceManager.getAllPlaces(cassandraClient);
		if (placeList == null){
			resultCode = ErrorCode.ERROR_GET_NEARBY_PLACES;
			log.info("fail to get nearby places");
			return;
		}
		
		// set result data, return postArray
		JSONArray obj = new JSONArray();
		resultData = obj;
		for (Place place : placeList){
			JSONObject json = new JSONObject();
			json.put(ServiceConstant.PARA_CREATE_USERID, place.getCreateUserId());
			json.put(ServiceConstant.PARA_PLACEID, place.getPlaceId());
			json.put(ServiceConstant.PARA_LONGTITUDE, place.getLongitude());
			json.put(ServiceConstant.PARA_LATITUDE, place.getLatitude());
			json.put(ServiceConstant.PARA_NAME, place.getName());
			json.put(ServiceConstant.PARA_DESC, place.getDesc());
			json.put(ServiceConstant.PARA_RADIUS, place.getRadius());
			json.put(ServiceConstant.PARA_POSTTYPE, place.getPostType());
			json.put(ServiceConstant.PARA_CREATE_DATE, place.getCreateDate());
			
			obj.add(json);
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
		log.info(String.format("userId=%s, appId=%s, "
				+"beforeTimeStamp=%s, maxCount=%s, longitude=%s, latitude=%s",
				userId, appId, beforeTimeStamp, maxCount,
				longitude, latitude));

	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		longitude = request.getParameter(ServiceConstant.PARA_LONGTITUDE);
		latitude = request.getParameter(ServiceConstant.PARA_LATITUDE);
		beforeTimeStamp = request.getParameter(ServiceConstant.PARA_BEFORE_TIMESTAMP);
		maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY, ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;
		
		if (!check(longitude, ErrorCode.ERROR_PARAMETER_LONGITUDE_EMPTY, ErrorCode.ERROR_PARAMETER_LONGITUDE_NULL))
			return false;

		if (!check(latitude, ErrorCode.ERROR_PARAMETER_LATITUDE_EMPTY, ErrorCode.ERROR_PARAMETER_LATITUDE_NULL))
			return false;

		// TODO need to check maxCount is valid decimal/number 
		
		return true;
	}

}
