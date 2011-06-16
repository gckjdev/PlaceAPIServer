package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Place;
import com.orange.place.manager.PlaceManager;

public class GetPlaceService extends CommonService {

	String userId;
	String placeId;
	String appId;

	
	@Override
	public void handleData() {
		Place place = PlaceManager.getPlaceById(cassandraClient, placeId);
		if (place == null){
			log.info("<getPlaceById> cannot find place="+placeId);
			resultCode = ErrorCode.ERROR_PLACE_NOT_FOUND;
		}
		else{
			resultData = CommonServiceUtils.placeToJSON(place);
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
		log.info(String.format("userId=%s, appId=%s, placeId=%s,", userId,
				appId, placeId));

	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {

		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		placeId = request.getParameter(ServiceConstant.PARA_PLACEID);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(placeId, ErrorCode.ERROR_PARAMETER_PLACEID_EMPTY,
				ErrorCode.ERROR_PARAMETER_PLACEID_NULL))
			return false;

		return true;
	}

}
