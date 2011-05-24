package com.orange.place.dao;

import java.util.List;
import java.util.Map;

import com.orange.common.utils.DateUtil;
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
}
