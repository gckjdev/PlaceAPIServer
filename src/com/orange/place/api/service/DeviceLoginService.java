package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.User;
import com.orange.place.manager.UserManager;

public class DeviceLoginService extends CommonService {

	String 	deviceId;
	String 	appId;
	boolean needReturnUser;
	
	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		deviceId = request.getParameter(ServiceConstant.PARA_DEVICEID);
		String needReturnUserStr = request.getParameter(ServiceConstant.PARA_NEED_RETURN_USER);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(deviceId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (needReturnUserStr == null){
			needReturnUser = false;
		}
		else{
			needReturnUser = ( Integer.parseInt(needReturnUserStr) == 1 );
		}
		
		return true;

	}

	@Override
	public void printData() {
		// TODO Auto-generated method stub
		log.info(String.format("deviceId=%s, appId=%s, "
				+"needReturnUser=%b",
				deviceId, appId, needReturnUser));

	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleData() {
		
		User user = UserManager.getUserByDevice(cassandraClient, deviceId);
		if (user == null){
			log.info("<DeviceLogin> deviceId("+deviceId+") is not bind to any user");
			resultCode = ErrorCode.ERROR_DEVICE_NOT_BIND;
			return;
		}
		
		if (needReturnUser){
			resultData = CommonServiceUtils.userToJSON(user);
		}
	}

}
