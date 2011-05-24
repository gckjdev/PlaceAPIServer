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
	
}
