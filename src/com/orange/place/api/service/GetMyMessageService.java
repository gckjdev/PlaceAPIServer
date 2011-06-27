package com.orange.place.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Message;
import com.orange.place.manager.MessageManager;

public class GetMyMessageService extends CommonService {
   
	String userId;
	String appId;
	String afterTimeStamp;
	String maxCount;
	
	@Override
	public void handleData() {
		List<Message> messageList = MessageManager.getMyMessage(cassandraClient,
				userId, afterTimeStamp, maxCount);
		if (messageList == null) {
			log.info("fail to get user message, userId=" + userId);
			resultCode = ErrorCode.ERROR_GET_MY_MESSAGE;
			return;
		}
//need to add into json
		resultData = CommonServiceUtils.messageListToJSON(messageList,afterTimeStamp);
		//date avatar nickname

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
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		afterTimeStamp = request
				.getParameter(ServiceConstant.PARA_AFTER_TIMESTAMP);
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
