package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.analysis.domain.AnalysisLogContent;
import com.orange.place.analysis.domain.PostType;
import com.orange.place.analysis.log.AnalysisLogUtil;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.manager.PostManager;

public class ActionOnPostService extends CommonService {
	String userId;
	String postId;
	String postActionType;
	String appId;
	String placeId;
	String latitude;
	String longitude;

	@Override
	public String toString() {
		return "ActionOnPostService [appId=" + appId + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", placeId=" + placeId
				+ ", postActionType=" + postActionType + ", postId=" + postId
				+ ", userId=" + userId + "]";
	}		

	@Override
	public void handleData() {
		
		// write to post action counter column family
		long count = PostManager.actionOnPost(cassandraClient, postId, userId,
				postActionType);
		
		//  log for analysis
		AnalysisLogContent content = new AnalysisLogContent().setLatitude(latitude).setLongitude(longitude).setPlaceId(placeId)
				.setPostId(postId).setPostType(PostType.ACTION).setUserId(userId);
		AnalysisLogUtil.log(content);
		
		JSONObject obj = new JSONObject();
		obj.put(postActionType, Long.toString(count));
		resultData = obj;
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printData() {
		log.info(toString());

	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		postId = request.getParameter(ServiceConstant.PARA_POSTID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		postActionType = request
				.getParameter(ServiceConstant.PARA_POST_ACTION_TYPE);
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		latitude = request.getParameter(ServiceConstant.PARA_LATITUDE);
		longitude = request.getParameter(ServiceConstant.PARA_LONGTITUDE);
		placeId = request.getParameter(ServiceConstant.PARA_PLACEID);		
		
		if (!check(postId, ErrorCode.ERROR_PARAMETER_POSTID_EMPTY,
				ErrorCode.ERROR_PARAMETER_POSTID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(postActionType,
				ErrorCode.ERROR_PARAMETER_POSTACTIONTYPE_EMPTY,
				ErrorCode.ERROR_PARAMETER_POSTACTIONTYPE_NULL))
			return false;

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		return true;
	}

}
