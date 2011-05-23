package com.orange.place.api.service;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.StringUtil;
import com.orange.place.api.PlaceAPIServer;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;

public abstract class CommonServiceObject {
	// response data
	int resultCode = ErrorCode.ERROR_SUCCESS;		
	Object resultData = null;
	CassandraClient cassandraClient = null;
	
	public CassandraClient getCassandraClient() {
		return cassandraClient;
	}

	public void setCassandraClient(CassandraClient cassandraClient) {
		this.cassandraClient = cassandraClient;
	}

	public static final Logger log = Logger.getLogger(PlaceAPIServer.class.getName());

	public static CommonServiceObject createServiceObjectByMethod(String method){
		
		CommonServiceObject obj = null;

		if (method.equalsIgnoreCase(ServiceConstant.METHOD_REGISTRATION)){
			obj = new RegisterUser();
		}
		
		
		return obj;
	}

	// save data from request to object fields
	public abstract boolean setDataFromRequest(HttpServletRequest request);
	
	// print object fields (for request data)
	public abstract void printData();	
	
	// return false if this method doesn't need security check
	public abstract boolean needSecurityCheck();
	
	// handle request, business logic implementation here
	// need to set responseData and resultCode and return them as JSON string
	public abstract void handleData();
	
	public String getResponseString(){
		JSONObject resultObject = new JSONObject();
		if (resultData != null){
			resultObject.put(ServiceConstant.RET_DATA, resultData);
		}
		resultObject.put(ServiceConstant.RET_CODE, resultCode);
		
		String retString = resultObject.toString();		
		return retString;
	}
	
	public boolean check(String value, int errorCodeEmpty, int errorCodeNull){
		if (value == null){
			resultCode = errorCodeNull;
			return false;
		}
		if (value.length() == 0){
			resultCode = errorCodeEmpty;
			return false;
		}
		return true;
	}
	
	
	
	static final String SHARE_KEY = "NetworkRequestShareKey"; 
	
	public boolean validateSecurity(HttpServletRequest request){
		
		if (needSecurityCheck() == false){
			log.warning("<validateSecurity> skip security check");
			return true;
		}
				
		String timeStamp = request.getParameter(ServiceConstant.PARA_TIMESTAMP);
		String mac = request.getParameter(ServiceConstant.PARA_MAC);
		
//		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY, ErrorCode.ERROR_PARAMETER_USERID_NULL))
//			return false;

		if (!check(timeStamp, ErrorCode.ERROR_PARAMETER_TIMESTAMP_EMPTY, ErrorCode.ERROR_PARAMETER_TIMESTAMP_NULL))
			return false;
		
		if (!check(mac, ErrorCode.ERROR_PARAMETER_MAC_EMPTY, ErrorCode.ERROR_PARAMETER_MAC_NULL))
			return false;
		
		String input = timeStamp + SHARE_KEY;
		String encodeStr = StringUtil.md5base64encode(input);
		if (encodeStr == null){
			log.warning("<validateSecurity> failure, input="+input+",client mac="+mac+",server mac=null");
			return false;
		}
		
		if (encodeStr.equals(mac)){
			log.info("<validateSecurity> OK, input="+input+",client mac="+mac+",server mac="+encodeStr);
			return true;
		}
		else{
			log.warning("<validateSecurity> failure, input="+input+",client mac="+mac+",server mac="+encodeStr);
			return false;
		}
	}
	
}
