package com.orange.place.api.service;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.StringUtil;
import com.orange.place.api.PlaceAPIServer;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;

public abstract class CommonService {
	// response data
	int resultCode = ErrorCode.ERROR_SUCCESS;
	Object resultData = null;
	CassandraClient cassandraClient = null;

	static private Map<String, CommonService> methodMap = null;

	static private void initMethodMap() {
		if (methodMap != null)
			return;
		methodMap.put(ServiceConstant.METHOD_REGISTRATION,
				new RegisterUserService());
		methodMap.put(ServiceConstant.METHOD_CREATEPLACE,
				new CreatePlaceService());
		methodMap.put(ServiceConstant.METHOD_CREATEPOST,
				new CreatePostService());
		methodMap.put(ServiceConstant.METHOD_GETPLACEPOST,
				new GetPlacePostService());
		methodMap.put(ServiceConstant.METHOD_GETNEARBYPLACE,
				new GetNearbyPlaceService());
		methodMap.put(ServiceConstant.METHOD_USERFOLLOWPLACE,
				new UserFollowPlaceService());
		methodMap.put(ServiceConstant.METHOD_GETUSERFOLLOWPOSTS,
				new GetUserTimelineService());
		methodMap.put(ServiceConstant.METHOD_GETNEARBYPOSTS,
				new GetNearbyPostService());
	}

	public CassandraClient getCassandraClient() {
		return cassandraClient;
	}

	public void setCassandraClient(CassandraClient cassandraClient) {
		this.cassandraClient = cassandraClient;
	}

	public static final Logger log = Logger.getLogger(PlaceAPIServer.class
			.getName());

	public static CommonService createServiceObjectByMethod(String method) {
		initMethodMap();
		CommonService obj = methodMap.get(method);
		if (obj == null) {
			log.warning("Cannot find service object for METHOD = " + method);
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

	public String getResponseString() {
		JSONObject resultObject = new JSONObject();
		if (resultData != null) {
			resultObject.put(ServiceConstant.RET_DATA, resultData);
		}
		resultObject.put(ServiceConstant.RET_CODE, resultCode);

		String retString = resultObject.toString();
		return retString;
	}

	public boolean check(String value, int errorCodeEmpty, int errorCodeNull) {
		if (value == null) {
			resultCode = errorCodeNull;
			return false;
		}
		if (value.length() == 0) {
			resultCode = errorCodeEmpty;
			return false;
		}
		return true;
	}

	static final String SHARE_KEY = "NetworkRequestShareKey";

	public boolean validateSecurity(HttpServletRequest request) {

		if (needSecurityCheck() == false) {
			log.warning("<validateSecurity> skip security check");
			return true;
		}

		String timeStamp = request.getParameter(ServiceConstant.PARA_TIMESTAMP);
		String mac = request.getParameter(ServiceConstant.PARA_MAC);

		// if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
		// ErrorCode.ERROR_PARAMETER_USERID_NULL))
		// return false;

		if (!check(timeStamp, ErrorCode.ERROR_PARAMETER_TIMESTAMP_EMPTY,
				ErrorCode.ERROR_PARAMETER_TIMESTAMP_NULL))
			return false;

		if (!check(mac, ErrorCode.ERROR_PARAMETER_MAC_EMPTY,
				ErrorCode.ERROR_PARAMETER_MAC_NULL))
			return false;

		String input = timeStamp + SHARE_KEY;
		String encodeStr = StringUtil.md5base64encode(input);
		if (encodeStr == null) {
			log.warning("<validateSecurity> failure, input=" + input
					+ ",client mac=" + mac + ",server mac=null");
			return false;
		}

		if (encodeStr.equals(mac)) {
			log.info("<validateSecurity> OK, input=" + input + ",client mac="
					+ mac + ",server mac=" + encodeStr);
			return true;
		} else {
			log.warning("<validateSecurity> failure, input=" + input
					+ ",client mac=" + mac + ",server mac=" + encodeStr);
			return false;
		}
	}

}
