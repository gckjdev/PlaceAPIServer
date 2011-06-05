package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Message;
import com.orange.place.manager.MessageManager;

public class SendMessageService extends CommonService {

	String userId;
	String appId;
	String toUserId;
	String messageContent;

	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		Message message = MessageManager.createMessage(cassandraClient, appId,
				userId, toUserId, messageContent);
		String messageId = message.getMessageId();
		MessageManager.createUserPostIndex(cassandraClient, userId, toUserId,
				messageId);
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_MESSAGE_ID, messageId);
		resultData = obj;
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
		toUserId = request.getParameter(ServiceConstant.PARA_TO_USERID);
		messageContent = request
				.getParameter(ServiceConstant.PARA_TEXT_CONTENT);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(toUserId, ErrorCode.ERROR_PARAMETER_TOUSERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_TOUSERID_NULL))
			return false;

		if (!check(messageContent,
				ErrorCode.ERROR_PARAMETER_MESSAGECONTENT_EMPTY,
				ErrorCode.ERROR_PARAMETER_MESSAGECONTENT_NULL))
			return false;

		return true;
	}

}
