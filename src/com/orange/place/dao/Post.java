package com.orange.place.dao;

import java.util.List;
import java.util.Map;

import com.orange.place.constant.DBConstants;

import me.prettyprint.hector.api.beans.HColumn;

public class Post extends CommonData {

	public Post(List<HColumn<String, String>> columnValues) {
		super(columnValues);
	}
	
	public Post(Map<String, String> mapColumnValues){
		super(mapColumnValues);
	}

	public String getPostId() {
		return getKey(DBConstants.F_POSTID);
	}

	public String getCreateDate() {
		return getKey(DBConstants.F_CREATE_DATE);
	}
}
