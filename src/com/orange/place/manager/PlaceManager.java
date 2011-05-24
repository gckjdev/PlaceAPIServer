package com.orange.place.manager;

import java.util.HashMap;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.DateUtil;
import com.orange.place.constant.DBConstants;
import com.orange.place.dao.IdGenerator;
import com.orange.place.dao.Place;

public class PlaceManager extends CommonManager {

	public static Place createPlace(CassandraClient cc, String userId, String appId,
			String longitude, String latitude, String name, String radius,
			String postType, String desc) {
		
		String placeId = IdGenerator.generateId();
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(DBConstants.F_PLACEID, placeId);
		map.put(DBConstants.F_USERID, userId);
		map.put(DBConstants.F_LONGITUDE, longitude);		
		map.put(DBConstants.F_LATITUDE, latitude);		
		map.put(DBConstants.F_NAME, name);		
		map.put(DBConstants.F_RADIUS, radius);		
		map.put(DBConstants.F_POST_TYPE, postType);		
		map.put(DBConstants.F_DESC, desc);				
		map.put(DBConstants.F_AUTH_FLAG, DBConstants.AUTH_FLAG_NONE);		
		map.put(DBConstants.F_PLACE_TYPE, DBConstants.PLACE_TYPE_UNKNOWN);		
		map.put(DBConstants.F_CREATE_DATE, DateUtil.currentDate());
		map.put(DBConstants.F_CREATE_SOURCE_ID, appId);		
		map.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);		
				
		log.info("<createPlace> placeId="+placeId+", userId="+userId);
		cc.insert(DBConstants.PLACE, placeId, map);
		
		return new Place(map);
	}

}
