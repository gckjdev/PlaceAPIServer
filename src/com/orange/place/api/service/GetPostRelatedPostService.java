package com.orange.place.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Post;
import com.orange.place.manager.PostManager;

public class GetPostRelatedPostService extends CommonService {

	String userId;
	String appId;
	String postId;
	String beforeTimeStamp;
	String maxCount;
	String excludePostId;
	
	@Override
	public void handleData() {
		List<Post> resultList = PostManager.getRelatedPostByPost(cassandraClient,
				postId, beforeTimeStamp, maxCount);
		if (resultList == null) {
			resultCode = ErrorCode.ERROR_GET_RELATED_POST_BY_POST;
			log.info("fail to get related post by post(" + postId + ")");
			return;
		}

		resultData = CommonServiceUtils.postListToJSON(resultList, excludePostId);
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printData() {
		log.info(String.format("userId=%s, appId=%s, postId=%s,"
				+ "beforeTimeStamp=%s, maxCount=%s", userId, appId, postId,
				beforeTimeStamp, maxCount));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		postId = request.getParameter(ServiceConstant.PARA_POSTID);
		excludePostId = request.getParameter(ServiceConstant.PARA_EXCLUDE_POSTID);
		beforeTimeStamp = request
				.getParameter(ServiceConstant.PARA_BEFORE_TIMESTAMP);
		maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(postId, ErrorCode.ERROR_PARAMETER_POSTID_EMPTY,
				ErrorCode.ERROR_PARAMETER_POSTID_NULL))
			return false;
		
		// TODO need to check maxCount is valid decimal/number

		return true;
	}

}
