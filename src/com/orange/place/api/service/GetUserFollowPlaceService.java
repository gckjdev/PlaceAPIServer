package com.orange.place.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Place;
import com.orange.place.manager.PlaceManager;

public class GetUserFollowPlaceService extends CommonService {
	String userId;
	String appId;
	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		List<Place> placeList = PlaceManager.getUserFollowPlace(cassandraClient,userId);
		if (placeList == null){
			log.info("fail to get user follow place, userId="+userId);
			resultCode = ErrorCode.ERROR_GET_USER_FOLLOW_PLACE;
			return;
		}
		
		resultData = CommonServiceUtils.placeListToJSON(placeList);
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printData() {
		// TODO Auto-generated method stub
		log.info(String.format("userId=%s, appId=%s, ",
				userId, appId));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY, ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;
		
		// TODO need to check maxCount is valid decimal/number 
		
		return true;
	}

}
