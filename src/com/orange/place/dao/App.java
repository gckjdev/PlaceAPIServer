package com.orange.place.dao;

import java.util.List;
import java.util.Map;

import com.orange.place.constant.DBConstants;

import me.prettyprint.hector.api.beans.HColumn;

public class App extends CommonData {

	public App(List<HColumn<String, String>> columnValues) {
		super(columnValues);
		// TODO Auto-generated constructor stub
	}

	public App(Map<String, String> mapColumnValues) {
		super(mapColumnValues);
		// TODO Auto-generated constructor stub
	}

	public String getAppId() {
		return getKey(DBConstants.F_APPID);
	}

	public String getAppUrl() {
		return getKey(DBConstants.F_APPURL);
	}

	public String getAppName() {
		return getKey(DBConstants.F_NAME);
	}

	public String getAppDesc() {
		return getKey(DBConstants.F_DESC);
	}

	public String getAppIcon() {
		return getKey(DBConstants.F_ICON);
	}

	public String getAppVersion() {
		return getKey(DBConstants.F_VERSION);
	}

	public String getSinaAppKey() {
		return getKey(DBConstants.F_SINA_APPKEY);
	}

	public String getQQAppKey() {
		return getKey(DBConstants.F_QQ_APPKEY);
	}

	public String getRenrenAppKey() {
		return getKey(DBConstants.F_RENREN_APPKEY);
	}

	public String getSinaAppSecret() {
		return getKey(DBConstants.F_SINA_APPSECRET);
	}

	public String getQQAppSecret() {
		return getKey(DBConstants.F_QQ_APPSECRET);
	}

	public String getRenrenAppSecret() {
		return getKey(DBConstants.F_RENREN_APPSECRET);
	}
}
