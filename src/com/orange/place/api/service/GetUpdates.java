package com.orange.place.api.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.manager.AppManager;

public class GetUpdates extends CommonService {

	String appId;
	String userId;
	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		Map<String,String> updateMap = AppManager.getUpdates(cassandraClient,appId);
		if(updateMap == null || updateMap.size() < 1){
			log.info("<GetUpdateService> app "+ appId + " update information not found");
			resultCode = ErrorCode.ERROR_APP_UPDATE_NOT_FOUND;
			return;
		}
		JSONObject jObj = new JSONObject();
		jObj.accumulateAll(updateMap);
		resultData = jObj;
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printData() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;
		
		return true;
	}

}
