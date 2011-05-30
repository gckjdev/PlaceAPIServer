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
	public static final String INDEX_USER_OWN_PLACE = "idx_user_own_places";
	public static final String INDEX_USER_FOLLOW_PLACE = "idx_user_follow_places";
	public static final String INDEX_USER_POST = "idx_user_posts";
	public static final String INDEX_PLACE_FOLLOWED_USERS = "idx_place_followed_users";
	public static final String INDEX_USER_VIEW_POSTS = "idx_user_timeline";
	public static final String INDEX_POST_RELATED_POST = "idx_post_related_posts";

	
	public static final String KEY_LOGINID = "loginId";
	public static final String KEY_DEVICEID = "deviceId";
	
	// index place post
	public static final String INDEX_PLACE_POST = "idx_place_posts";
	
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
	public static final String F_AVATAR = "avatar";
	public static final String F_SINAID = "sinaID";
	public static final String F_QQID = "qqID";
	public static final String F_RENRENID = "renrenID";
	public static final String F_FACEBOOKID = "facebookID";
	public static final String F_TWITTERID = "twitterID";
	
	public static final String F_SINA_ACCESS_TOKEN 			= "sinaAT";
	public static final String F_SINA_ACCESS_TOKEN_SECRET 	= "sinaATS";
	public static final String F_QQ_ACCESS_TOKEN 			= "qqAT";
	public static final String F_QQ_ACCESS_TOKEN_SECRET 	= "qqATS";
	
	public static final String F_PROVINCE			= "province";
	public static final String F_CITY				= "city";
	public static final String F_LOCATION			= "location";
	public static final String F_GENDER				= "gender";
	public static final String F_BIRTHDAY			= "birthday";
	public static final String F_SINA_NICKNAME		= "sinaNickName";
	public static final String F_SINA_DOMAIN		= "sinaDomain";
	public static final String F_QQ_NICKNAME		= "qqNickName";
	public static final String F_QQ_DOMAIN			= "qqDomain";
	
	// DB Place Fields
	public static final String F_PLACEID = "placeId";	
	public static final String F_LONGITUDE = "long";
	public static final String F_LATITUDE = "lat";
	public static final String F_NAME = "name";
	public static final String F_RADIUS = "radius";
	public static final String F_POST_TYPE = "postType";
	public static final String F_DESC = "desc";
	public static final String F_PLACE_TYPE = "type";
	public static final String F_AUTH_FLAG = "auth";

	
	// DB Post Fields
	public static final String F_POSTID = "postId";
	public static final String F_USER_LONGITUDE = "userLong";
	public static final String F_USER_LATITUDE = "userLat";
	public static final String F_TEXT_CONTENT = "text";
	public static final String F_CONTENT_TYPE = "type";
	public static final String F_IMAGE_URL = "image";
	public static final String F_TOTAL_VIEW = "totalView";
	public static final String F_TOTAL_FORWARD = "totalForward";
	public static final String F_TOTAL_QUOTE = "totalQuote";
	public static final String F_TOTAL_REPLY = "totalReply";	
	public static final String F_SRC_POSTID = "srcPostId";

	
	
	// Value
	public static final String STATUS_NORMAL = "1";
	public static final String STATUS_SUSPEND = "2";
	
	// Constants
	public static final int LOGINID_OWN 		= 1;
	public static final int LOGINID_SINA 		= 2;
	public static final int LOGINID_QQ 			= 3;
	public static final int LOGINID_RENREN 		= 4;
	public static final int LOGINID_FACEBOOK 	= 5;
	public static final int LOGINID_TWITTER 	= 6;
	
	
	public static final int CONTENT_TYPE_TEXT		= 1;
	public static final int CONTENT_TYPE_TEXT_PHOTO = 2;
	
	public static final String AUTH_FLAG_NONE = "0";
	
	public static final String PLACE_TYPE_UNKNOWN = "0";

}
