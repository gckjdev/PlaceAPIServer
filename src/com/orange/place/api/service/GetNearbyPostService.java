package com.orange.place.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Post;
import com.orange.place.manager.PostManager;

public class GetNearbyPostService extends CommonService {
	String userId;
	String appId;
	String longitude;
	String latitude;
	String beforeTimeStamp;	
	String maxCount;
	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		// Fake implementation, return all post data
		List<Post> postList = PostManager.getAllPosts(cassandraClient);
		if (postList == null){
			resultCode = ErrorCode.ERROR_GET_NEARBY_POSTS;
			log.info("fail to get nearby posts");
			return;
		}
		resultData = CommonServiceUtils.postListToJSON(postList, beforeTimeStamp);
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
		// TODO Auto-generated method stub
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
