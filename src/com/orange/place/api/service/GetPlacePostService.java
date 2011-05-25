package com.orange.place.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import me.prettyprint.hector.api.beans.HColumn;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Post;
import com.orange.place.manager.PostManager;

public class GetPlacePostService extends CommonService {

	String userId;
	String appId;
	String placeId;
	String beforeTimeStamp;	
	String maxCount;
	
	@Override
	public void handleData() {
		// TODO Auto-generated method stub

		List<Post> resultList = PostManager.getPostByPlace(cassandraClient, placeId, beforeTimeStamp, maxCount);
		if (resultList == null){
			resultCode = ErrorCode.ERROR_GET_POST_BY_PLACE;
			log.info("fail to get post by place("+placeId+")");
			return;
		}
		
		// set result data, return postArray
		JSONArray obj = new JSONArray();
		resultData = obj;
		for (Post post : resultList){
			JSONObject json = new JSONObject();
			json.put(ServiceConstant.PARA_POSTID, post.getPostId());
//			json.put(ServiceConstant.PARA_USERID, post.getUserId());
//			json.put(ServiceConstant.PARA_PLACEID, post.getPlaceId());
//			json.put(ServiceConstant.PARA_LONGTITUDE, post.getLongitude());
//			json.put(ServiceConstant.PARA_LATITUDE, post.getLatitude());
//			json.put(ServiceConstant.PARA_USER_LONGITUDE, post.getUserLongitude());
//			json.put(ServiceConstant.PARA_USER_LATITUDE, post.getUserLatitude());
//			json.put(ServiceConstant.PARA_TEXT_CONTENT, post.getTextContent());
//			json.put(ServiceConstant.PARA_CONTENT_TYPE, post.getContentType());
//			json.put(ServiceConstant.PARA_IMAGE_URL, post.getImageURL());
//			json.put(ServiceConstant.PARA_TOTAL_VIEW, post.getTotalView());
//			json.put(ServiceConstant.PARA_TOTAL_FORWARD, post.getTotalForward());
//			json.put(ServiceConstant.PARA_TOTAL_QUOTE, post.getTotalQuote());
//			json.put(ServiceConstant.PARA_TOTAL_VIEW, post.getTotalView());
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
		log.info(String.format("userId=%s, appId=%s, placeId=%s,"
				+"beforeTimeStamp=%s, maxCount=%s",
				userId, appId, placeId, beforeTimeStamp, maxCount));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		placeId = request.getParameter(ServiceConstant.PARA_PLACEID);
		beforeTimeStamp = request.getParameter(ServiceConstant.PARA_BEFORE_TIMESTAMP);
		maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY, ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(placeId, ErrorCode.ERROR_PARAMETER_PLACEID_EMPTY, ErrorCode.ERROR_PARAMETER_PLACEID_NULL))
			return false;
		
		// TODO need to check maxCount is valid decimal/number 
		
		return true;
	}

}
