package com.orange.place.dao;

import java.util.UUID;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

public class IdGenerator {

	
	public static String generateId(){
		
		UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
		return uuid.toString();
	}		
}
