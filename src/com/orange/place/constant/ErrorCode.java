package com.orange.place.constant;

public class ErrorCode {

	
	static public final int ERROR_SUCCESS = 0;

	// Parameter Error
	static public final int ERROR_PARAMETER                 = 10001;
	static public final int ERROR_PARA_METHOD_NOT_FOUND     = 10002;
	static public final int ERROR_PARAMETER_TIMESTAMP_EMPTY = 10003;
	static public final int ERROR_PARAMETER_TIMESTAMP_NULL 	= 10004;
	static public final int ERROR_PARAMETER_MAC_EMPTY 		= 10005;
	static public final int ERROR_PARAMETER_MAC_NULL 		= 10006;		
	static public final int ERROR_PARAMETER_USERID_NULL 	= 10007;  
	static public final int ERROR_PARAMETER_USERID_EMPTY 	= 10008;  
	static public final int ERROR_PARAMETER_APPID_NULL 		= 10009;  
	static public final int ERROR_PARAMETER_APPID_EMPTY 	= 10010;
	static public final int ERROR_PARAMETER_STATUS_NULL 	= 10011;
	static public final int ERROR_PARAMETER_USERTYPE_EMPTY 	= 10012;
	static public final int ERROR_PARAMETER_USERTYPE_NULL 	= 10013;
	static public final int ERROR_PARAMETER_DEVICEID_EMPTY 	= 10014;
	static public final int ERROR_PARAMETER_DEVICEID_NULL 	= 10015;
	static public final int ERROR_PARAMETER_DEVICEMODEL_EMPTY = 10016;
	static public final int ERROR_PARAMETER_DEVICEMODEL_NULL = 10017;
	static public final int ERROR_PARAMETER_DEVICEOS_EMPTY = 10018;
	static public final int ERROR_PARAMETER_DEVICEOS_NULL	 = 10019;
	static public final int ERROR_PARAMETER_PASSWORD_EMPTY 	 = 10020;
	static public final int ERROR_PARAMETER_PASSWORD_NULL 	 = 10021;
	static public final int ERROR_PARAMETER_COUNTRYCODE_EMPTY = 10022;
	static public final int ERROR_PARAMETER_COUNTRYCODE_NULL = 10023;
	static public final int ERROR_PARAMETER_LANGUAGE_EMPTY 	 = 10024;
	static public final int ERROR_PARAMETER_LANGUAGE_NULL 	 = 10025;
	public static final int ERROR_PARAMETER_LONGITUDE_EMPTY  = 10026;
	public static final int ERROR_PARAMETER_LONGITUDE_NULL 	 = 10027;
	public static final int ERROR_PARAMETER_LATITUDE_EMPTY 	 = 10028;
	public static final int ERROR_PARAMETER_LATITUDE_NULL 	 = 10029;
	public static final int ERROR_PARAMETER_NAME_EMPTY 		 = 10030;
	public static final int ERROR_PARAMETER_NAME_NULL 		 = 10031;
	public static final int ERROR_PARAMETER_RADIUS_NULL 	 = 10032;
	public static final int ERROR_PARAMETER_RADIUS_EMPTY 	 = 10033;
	public static final int ERROR_PARAMETER_POSTTYPE_EMPTY 	 = 10034;
	public static final int ERROR_PARAMETER_POSTTYPE_NULL 	 = 10035;
	public static final int ERROR_PARAMETER_DESC_NULL 		 = 10036;
	public static final int ERROR_PARAMETER_DESC_EMPTY 		 = 10037;
	public static final int ERROR_PARAMETER_USER_LONGITUDE_EMPTY = 10038;
	public static final int ERROR_PARAMETER_USER_LONGITUDE_NULL = 10039;
	public static final int ERROR_PARAMETER_USER_LATITUDE_EMPTY = 10040;
	public static final int ERROR_PARAMETER_USER_LATITUDE_NULL 	= 10041;
	public static final int ERROR_PARAMETER_TEXTCONTENT_NULL 	= 10042;
	public static final int ERROR_PARAMETER_TEXTCONTENT_EMPTY 	= 10043;
	public static final int ERROR_PARAMETER_CONTENTTYPE_EMPTY 	= 10044;
	public static final int ERROR_PARAMETER_CONTENTTYPE_NULL 	= 10045;
	public static final int ERROR_PARAMETER_PLACEID_NULL 		= 10046;
	public static final int ERROR_PARAMETER_PLACEID_EMPTY 		= 10047;
	public static final int ERROR_PARAMETER_POSTID_EMPTY 		= 10048;
	public static final int ERROR_PARAMETER_POSTID_NULL 		= 10049;
	public static final int ERROR_PARAMETER_TOUSERID_NULL 		= 10050;
	public static final int ERROR_PARAMETER_TOUSERID_EMPTY 		= 10051;
	public static final int ERROR_PARAMETER_MESSAGECONTENT_EMPTY = 10052;
	public static final int ERROR_PARAMETER_MESSAGECONTENT_NULL = 10053;
	public static final int ERROR_PARAMETER_MESSAGEID_EMPTY 	= 10054;
	public static final int ERROR_PARAMETER_MESSAGEID_NULL 		= 10055;
	// User Errors
	static public final int ERROR_LOGINID_EXIST             = 20001;
	static public final int ERROR_DEVICEID_BIND            	= 20002;
	public static final int ERROR_DEVICE_NOT_BIND 			= 20003;
	static public final int ERROR_LOGINID_DEVICE_BOTH_EXIST = 20004; 
	static public final int ERROR_USERID_NOT_FOUND          = 20005;
	static public final int ERROR_CREATE_USER				= 20006;
	public static final int ERROR_USER_GET_NICKNAME 		= 20007;


	// Place Error
	static public final int ERROR_PLACE_NAME_EXIST          = 30001;
	static public final int ERROR_PLACE_NOT_FOUND           = 30002;
	public static final int ERROR_CREATE_PLACE 				= 30003;
	public static final int ERROR_GET_NEARBY_PLACES 		= 30004;
	public static final int ERROR_GET_NEARBY_POSTS	 		= 30005;

	// Post Error
	static public final int ERROR_POST_NOT_FOUND            = 40001;
	public static final int ERROR_CREATE_POST 				= 40002;
	public static final int ERROR_GET_POST_BY_PLACE 		= 40003;
	public static final int ERROR_GET_USER_TIMELINE 		= 40004;
	public static final int ERROR_GET_USER_FOLLOW_PLACE		= 40005;
	public static final int ERROR_GET_RELATED_POST_BY_POST 	= 40006;
	public static final int ERROR_GET_ME_MESSAGE 			= 40007;
	//Message Error
	
	public static final int ERROR_GET_MY_MESSAGE 			= 50001;
	
	// DB Error
	static public final int ERROR_CASSANDRA                 = 80001;
	static public final int ERROR_CASSANDRA_UNAVAILABLE     = 80002;

	// System Error
	static public final int ERROR_SYSTEM                    = 90001;
	static public final int ERROR_NOT_GET_METHOD            = 90002;
	static public final int ERROR_INVALID_SECURITY			= 90003;
	static public final int ERROR_NAME_VALUE_NOTMATCH		= 90004;
	static public final int ERROR_JSON 						= 90005;
	static public final int ERROR_UPLOAD_FILE 				= 90006;
	public static final int ERROR_CREATE_THUNMB_FILEPATH 	= 90007;
	public static final int ERROR_CREATE_THUNMB_FILE 		= 90008;



















	
		
	static public String getJSONByErrorCode(int errorCode){
		return String.format("{\"%s\":%d}", ServiceConstant.RET_CODE, errorCode);		
	}
}
