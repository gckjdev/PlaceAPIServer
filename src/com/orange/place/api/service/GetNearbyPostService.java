package com.orange.place.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.context.SpringContextUtil;
import com.orange.place.analysis.RequestHandler;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.Request;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Post;
import com.orange.place.manager.PostManager;

public class GetNearbyPostService extends CommonService {

	private static final int DEFAULT_RADIUS_METERS = 200;
	String userId;
	String appId;
	String longitude;
	String latitude;
	String beforeTimeStamp;
	String maxCount;

	@Override
	public void handleData() {
		RequestHandler requestHandler = SpringContextUtil
				.getBean("requestHandler");

		Request request = new Request();
		request.setLatitude(Double.valueOf(latitude));
		request.setLongitude(Double.valueOf(longitude));
		request.setRadius(DEFAULT_RADIUS_METERS);
		request.setUserId(userId);

		List<CompactPost> compactPostList = requestHandler.execute(request);
		// TODO: paging
		List<Post> postList = new ArrayList<Post>();
		for (CompactPost cp : compactPostList) {
			Post post = PostManager
					.getPostById(cassandraClient, cp.getPostId());
			postList.add(post);
		}
		if (postList == null) {
			resultCode = ErrorCode.ERROR_GET_NEARBY_POSTS;
			log.info("fail to get nearby posts");
			return;
		}
		resultData = CommonServiceUtils.postListToJSON(postList,
				beforeTimeStamp);
	}

	@Override
	public boolean needSecurityCheck() {
		return false;
	}

	@Override
	public void printData() {
		log.info(String.format("userId=%s, appId=%s, "
				+ "beforeTimeStamp=%s, maxCount=%s, longitude=%s, latitude=%s",
				userId, appId, beforeTimeStamp, maxCount, longitude, latitude));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		longitude = request.getParameter(ServiceConstant.PARA_LONGTITUDE);
		latitude = request.getParameter(ServiceConstant.PARA_LATITUDE);
		beforeTimeStamp = request
				.getParameter(ServiceConstant.PARA_BEFORE_TIMESTAMP);
		maxCount = request.getParameter(ServiceConstant.PARA_MAX_COUNT);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(longitude, ErrorCode.ERROR_PARAMETER_LONGITUDE_EMPTY,
				ErrorCode.ERROR_PARAMETER_LONGITUDE_NULL))
			return false;

		if (!check(latitude, ErrorCode.ERROR_PARAMETER_LATITUDE_EMPTY,
				ErrorCode.ERROR_PARAMETER_LATITUDE_NULL))
			return false;

		// TODO need to check maxCount is valid decimal/number

		return true;
	}

}
