package com.orange.place.constant;

public class DBConstants {

	public static final String SERVER = "192.168.1.101:9160";
	public static final String CLUSTERNAME = "Test Cluster";
	
	public static final String KEYSPACE = "PlaceKS";
	
	// normal column family
	public static final String USER = "place_user";
	public static final String PLACE = "place_place";
	public static final String POST = "place_post";
	
	// column family for index
	public static final String INDEX_USER = "idx_user";
	public static final String KEY_LOGINID = "loginId";
	public static final String KEY_DEVICEID = "deviceId";
	
	// DB User Fields
	public static final String F_USERID = "userId";
	public static final String F_LOGINID = "loginId";
	public static final String F_APPID = "appId";
	public static final String F_DEVICEID = "deviceId";
	public static final String F_DEVICEMODEL = "deviceModel";
	public static final String F_DEVICEOS = "deviceOS";
	public static final String F_DEVICETOKEN = "deviceToken";	
	public static final String F_LANGUAGE = "language";
	public static final String F_COUNTRYCODE = "countryCode";	

	public static final String F_CREATE_DATE = "createDate";
	public static final String F_CREATE_SOURCE_ID = "createSourceId";
	public static final String F_MODIFY_DATE = "modifyDate";
	public static final String F_MODIFY_SOURCE_ID = "modifySourceId";
	
	public static final String F_EMAIL = "email";	
	public static final String F_MOBILE = "mobile";
	public static final String F_PASSWORD = "password";
	public static final String F_STATUS = "status";	
	
	public static final String F_NICKNAME = "nickName";	
	public static final String F_SINAID = "sinaID";
	public static final String F_QQID = "qqID";
	public static final String F_RENRENID = "renrenID";
	public static final String F_FACEBOOKID = "facebookID";
	public static final String F_TWITTERID = "twitterID";
	
	public static final String F_AUTHCODE = "authCode";
	public static final String F_AUTHSECRET = "authSecret";
	
	// DB Place Fields
	
	// DB Post Fields
	
	
	// Value
	public static final String STATUS_NORMAL = "1";
	public static final String STATUS_SUSPEND = "2";
	
	public static final int LOGINID_OWN 		= 1;
	public static final int LOGINID_SINA 		= 2;
	public static final int LOGINID_QQ 			= 3;
	public static final int LOGINID_RENREN 		= 4;
	public static final int LOGINID_FACEBOOK 	= 5;
	public static final int LOGINID_TWITTER 	= 6;
}
