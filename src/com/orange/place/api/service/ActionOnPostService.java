package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.manager.PostManager;

public class ActionOnPostService extends CommonService {
	String userId;
	String postId;
	String postActionType;

	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		boolean flag = PostManager.actionOnPost(cassandraClient, postId,
				userId, postActionType);
		if (!flag) {
			log.info("fail to do action on the post, postId=" + postId
					+ ", postActionType=" + postActionType);
			resultCode = ErrorCode.ERROR_ACTION_ON_POST;
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

	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		postId = request.getParameter(ServiceConstant.PARA_POSTID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		postActionType = request
				.getParameter(ServiceConstant.PARA_POST_ACTION_TYPE);

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

		return true;
	}

}
