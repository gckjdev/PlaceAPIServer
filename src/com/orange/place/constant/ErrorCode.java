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
	
	// User Errors
	static public final int ERROR_LOGINID_EXIST             = 20001;
	static public final int ERROR_DEVICEID_EXIST            = 20002;
	static public final int ERROR_LOGINID_DEVICE_BOTH_EXIST = 20003; 
	static public final int ERROR_USERID_NOT_FOUND          = 20004;

	// Place Error
	static public final int ERROR_PLACE_NAME_EXIST          = 30001;
	static public final int ERROR_PLACE_NOT_FOUND           = 30002;

	// Post Error
	static public final int ERROR_POST_NOT_FOUND            = 40001;

	// DB Error
	static public final int ERROR_CASSANDRA                 = 80001;
	static public final int ERROR_CASSANDRA_UNAVAILABLE     = 80002;

	// System Error
	static public final int ERROR_SYSTEM                    = 90001;
	static public final int ERROR_NOT_GET_METHOD            = 90002;
	static public final int ERROR_INVALID_SECURITY			= 90003;
	static public final int ERROR_NAME_VALUE_NOTMATCH		= 90004;
	
		
	static public String getJSONByErrorCode(int errorCode){
		return String.format("{\"%s\":%d}", ServiceConstant.RET_CODE, errorCode);		
	}
}
