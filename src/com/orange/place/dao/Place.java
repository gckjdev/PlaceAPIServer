package com.orange.place.dao;

import java.util.List;
import java.util.Map;

import com.orange.place.constant.DBConstants;
import me.prettyprint.hector.api.beans.HColumn;

public class Place extends CommonData {

	public Place(List<HColumn<String, String>> columnValues) {
		super(columnValues);
	}

	public Place(Map<String, String> mapColumnValues){
		super(mapColumnValues);
	}

	public String getPlaceId() {
		return getKey(DBConstants.F_PLACEID);
	}

	public String getCreateDate() {
		return getKey(DBConstants.F_CREATE_DATE);
	}
	
	public String getRadius() {
		return getKey(DBConstants.F_RADIUS);
	}

	public String getPostType() {
		return getKey(DBConstants.F_POST_TYPE);
	}

	public String getDesc() {
		return getKey(DBConstants.F_DESC);
	}
	
	public String getName() {
		return getKey(DBConstants.F_NAME);
	}
	
	public String getLatitude() {
		return getKey(DBConstants.F_LATITUDE);
	}
	
	public String getLongitude() {
		return getKey(DBConstants.F_LONGITUDE);
	}
	
	public String getCreateUserId() {
		return getKey(DBConstants.F_USERID);
	}
	
}
