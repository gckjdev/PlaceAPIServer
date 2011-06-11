package com.orange.place.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.App;
import com.orange.place.manager.AppManager;

public class GetAppsService extends CommonService {

	String appId;
	String language;

	@Override
	public void handleData() {
		// TODO Auto-generated method stub
		List<App> appList = AppManager.getRecommendApps(cassandraClient);
		if (appList == null) {
			log.info("There is no recommended apps.");
			return;
		}
		appList = AppManager.adjustRecommendApps(appList, appId, language,
				cassandraClient);
		if (appList == null) {
			log.info("There is no recommended apps except appId = " + appId);
			return;
		}
		resultData = CommonServiceUtils.appListToJSON(appList);
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
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		language = request.getParameter(ServiceConstant.PARA_LANGUAGE);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(language, ErrorCode.ERROR_PARAMETER_LANGUAGE_EMPTY,
				ErrorCode.ERROR_PARAMETER_LANGUAGE_NULL))
			return false;

		return true;
	}

}
