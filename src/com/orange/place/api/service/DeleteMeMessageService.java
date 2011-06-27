package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.manager.MessageManager;

public class DeleteMeMessageService extends CommonService {
	String userId;
	String appId;
	String messageId;

	@Override
	public String toString() {
		return "DeleteMeMessageService [appId=" + appId + ", messageId="
				+ messageId + ", userId=" + userId + "]";
	}

	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		MessageManager.deleteMessage(cassandraClient, userId, messageId);
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
		// TODO Auto-generated method stub
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		messageId = request.getParameter(ServiceConstant.PARA_MESSAGE_ID);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(messageId, ErrorCode.ERROR_PARAMETER_MESSAGEID_EMPTY,
				ErrorCode.ERROR_PARAMETER_MESSAGEID_NULL))
			return false;

		return true;
	}

}
