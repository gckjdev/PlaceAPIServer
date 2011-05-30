package com.orange.place.manager;

import java.util.logging.Logger;

import com.orange.place.api.PlaceAPIServer;

public class CommonManager {

	public static final Logger log = Logger.getLogger(PlaceAPIServer.class.getName());
	
	public static int MAX_COUNT = 30;
	public static int UNLIMITED_COUNT = 9999999;
	public static int MAX_DELETE_UNFOLLOWPLACE_COUNT = 3;
}
