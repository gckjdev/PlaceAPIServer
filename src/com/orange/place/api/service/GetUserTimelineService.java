package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Post;
import com.orange.place.manager.PostManager;

import java.util.List;

public class GetUserTimelineService extends CommonService {

	String userId;
	String appId;
	String beforeTimeStamp;
	String maxCount;

	@Override
	public void handleData() {

		List<Post> postList = PostManager.getUserTimeline(cassandraClient,
				userId, beforeTimeStamp, maxCount);
		if (postList == null) {
			log.info("fail to get user post timeline, userId=" + userId);
			resultCode = ErrorCode.ERROR_GET_USER_TIMELINE;
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
		log.info(String.format("userId=%s, appId=%s, "
				+ "beforeTimeStamp=%s, maxCount=%s", userId, appId,
				beforeTimeStamp, maxCount));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		beforeTimeStamp = request
				.getParameter(ServiceConstant.PARA_BEFORE_TIMESTAMP);
		maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		// TODO need to check maxCount is valid decimal/number

		return true;
	}

}
