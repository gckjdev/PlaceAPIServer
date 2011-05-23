package com.orange.place.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

public class IdGenerator {

	
	public static String generateId(){
		Date date = new Date();
		UUID uuid = TimeUUIDUtils.getTimeUUID(date.getTime());
		return uuid.toString();
	}
}
