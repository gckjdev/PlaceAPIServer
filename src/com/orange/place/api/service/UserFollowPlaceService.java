package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.IdGenerator;
import com.orange.place.manager.CommonManager;
import com.orange.place.manager.PlaceManager;
import com.orange.place.manager.PostManager;
import java.util.UUID;

public class UserFollowPlaceService extends CommonService {

	String userId;
	String placeId;
	String appId;

	@Override
	public void handleData() {
		PlaceManager.userFollowPlace(cassandraClient, userId, placeId);
		long time = System.currentTimeMillis()
				- CommonManager.MAX_FOLLOWPLACE_POSTS_TIMEOFFSET;
		UUID end = IdGenerator.getUUIDFromTime(time);
		PostManager.addFollowPlacePosts(cassandraClient, userId, placeId, end,
				CommonManager.MAX_ADD_FOLLOWPLACE_COUNT);
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
