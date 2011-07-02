package com.orange.place.dao;

import java.util.List;
import java.util.Map;

import com.orange.place.constant.DBConstants;

import me.prettyprint.hector.api.beans.HColumn;

public class Message extends CommonData {

	public Message(List<HColumn<String, String>> columnValues) {
		super(columnValues);
		// TODO Auto-generated constructor stub
	}

	public Message(Map<String, String> mapColumnValues) {
		super(mapColumnValues);
		// TODO Auto-generated constructor stub
	}

	public String getMessageId() {
		return getKey(DBConstants.F_MESSAGEID);
	}

	public String getFromUserId() {
		return getKey(DBConstants.F_FROM_USERID);
	}

	public String getToUserId() {
		return getKey(DBConstants.F_TO_USERID);
	}

	public String getCreateDate() {
		return getKey(DBConstants.F_CREATE_DATE);
	}

	public String getCreateSourceId() {
		return getKey(DBConstants.F_CREATE_SOURCE_ID);
	}

	public String getMessageContent() {
		return getKey(DBConstants.F_MESSAGE_CONTENT);
	}
	
	public String getUserNickName() {
		return getKey(DBConstants.F_NICKNAME);
	}
	
	public String getUserAvatar() {
		return getKey(DBConstants.F_AVATAR);
	}
	
	public String getMessageType() {
		return getKey(DBConstants.F_MESSAGE_TYPE);
	}

	public String getUserGender() {
		return getKey(DBConstants.F_GENDER);
	}
	
}
