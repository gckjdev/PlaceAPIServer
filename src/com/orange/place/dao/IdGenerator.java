package com.orange.place.dao;

import java.util.UUID;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

public class IdGenerator {

	
	public static String generateId(){
		
		UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
		return uuid.toString();
	}	
	public static UUID generateUUId(){		
		UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
		return uuid;
	}
	public static long getTimeFromUUID(UUID uuid){
		byte[] uuidbyte = TimeUUIDUtils.asByteArray(uuid);
		return TimeUUIDUtils.getTimeFromUUID(uuidbyte);
	}
	public static UUID getUUIDFromTime(long time){
		return TimeUUIDUtils.getTimeUUID(time);
	}
	public static String getIdFromTime(long time){
		return getUUIDFromTime(time).toString();
	}
	
	public static UUID getStartUUID(String beforeTimeStamp) {
		UUID startUUID = null;
		if (beforeTimeStamp != null && beforeTimeStamp.length() > 0) {
			startUUID = UUID.fromString(beforeTimeStamp);
		}
		return startUUID;
	}
}
