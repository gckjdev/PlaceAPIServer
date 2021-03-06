package com.orange.place.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Post;
import com.orange.place.manager.PostManager;

public class GetPublicTimeline extends CommonService {

	String appId;
	String beforeTimeStamp;
	String maxCount;
	String userId;

	@Override
	public String toString() {
		return "GetPublicTimeline [appId=" + appId + ", beforeTimeStamp="
				+ beforeTimeStamp + ", maxCount=" + maxCount + ", userId="
				+ userId + "]";
	}

	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		List<Post> postList = PostManager.getPublicTimeLine(cassandraClient,
				appId, beforeTimeStamp, maxCount);
		if (postList == null) {
			log.info("fail to get public timeline, appId=" + appId);
			resultCode = ErrorCode.ERROR_GET_PUBLIC_POST;
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
		log.info(toString());
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		beforeTimeStamp = request
				.getParameter(ServiceConstant.PARA_BEFORE_TIMESTAMP);
		maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		
		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;
		// TODO need to check maxCount is valid decimal/number

		return true;
	}

}
