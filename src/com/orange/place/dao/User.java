package com.orange.place.dao;

import java.util.List;
import java.util.Map;

import com.orange.place.constant.DBConstants;

import me.prettyprint.hector.api.beans.HColumn;

public class User extends CommonData {

	public User(List<HColumn<String, String>> columnValues) {
		super(columnValues);
	}

	public User(Map<String, String> mapColumnValues){
		super(mapColumnValues);
	}

	public String getUserId() {
		return getKey(DBConstants.F_USERID);
	}
	
	public String getLoginId() {
		return getKey(DBConstants.F_LOGINID);
	}
	
	public String getNickName() {
		return getKey(DBConstants.F_NICKNAME);
	}

	public String getSinaAccessToken() {
		return getKey(DBConstants.F_SINA_ACCESS_TOKEN);
	}

	public String getSinaAccessTokenSecret() {
		return getKey(DBConstants.F_SINA_ACCESS_TOKEN_SECRET);
	}

	public String getQQAccessToken() {
		return getKey(DBConstants.F_QQ_ACCESS_TOKEN);
	}
	
	public String getQQAccessTokenSecret() {
		return getKey(DBConstants.F_QQ_ACCESS_TOKEN_SECRET);
	}

	public String getQQId() {
		return getKey(DBConstants.F_QQID);
	}
	
	public String getSinaId() {
		return getKey(DBConstants.F_SINAID);
	}

	public String getRenrenId() {
		return getKey(DBConstants.F_RENRENID);
	}

	public String getFacebookId() {
		return getKey(DBConstants.F_FACEBOOKID);
	}

	public String getTwitterId() {
		return getKey(DBConstants.F_TWITTERID);
	}
}
