package com.orange.place.manager;

import java.util.List;

import com.orange.place.dao.App;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import com.orange.common.cassandra.CassandraClient;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ServiceConstant;

public class AppManager extends CommonManager {

	public AppManager() {
		// TODO Auto-generated constructor stub
	}

	static public List<App> getRecommendApps(CassandraClient cassandraClient) {
		List<HColumn<String, String>> apps = cassandraClient.getAllColumns(
				DBConstants.UPDATE, DBConstants.R_RECOMMEND_APPS);
		List<App> appList = new ArrayList<App>();
		String[] appIds = new String[apps.size()];
		int i = 0;
		for (HColumn<String, String> column : apps) {
			if (column != null) {
				String appId = column.getValue();
				if (appId != null && appId.length() > 0) {
					appIds[i++] = appId;
				}
			}
		}
		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.APP, appIds);
		for (Row<String, String, String> row : rows) {
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			List<HColumn<String, String>> columns = columnSlice.getColumns();
			if (columns != null) {
				App app = new App(columns);
				appList.add(app);
			}
		}
		return appList;
	}

	static public List<App> adjustRecommendApps(List<App> appList,
			String appId, String language, CassandraClient cassandraClient) {
		List<App> list = new ArrayList<App>();
		for (App app : appList) {
			if (app != null) {
				String appIdTemp = app.getAppId();
				if (!appIdTemp.equals(appId)) {
					app.printData();
					String name = null;
					String desc = null;
					String appName = app.getAppName();
					String appDesc = app.getAppDesc();
					name = LocalizeDictionaryManager.transition(appName,
							language, cassandraClient);

					if (name != null && name.length() > 0)
						app.editData(DBConstants.F_NAME, name);

					desc = LocalizeDictionaryManager.transition(appDesc,
							language, cassandraClient);

					if (desc != null && desc.length() > 0)
						app.editData(DBConstants.F_DESC, desc);
					list.add(app);
				}
			}
		}
		return list;
	}

	public static Map<String, String> getUpdates(
			CassandraClient cassandraClient, String appId) {
		List<HColumn<String, String>> appColumns = cassandraClient
				.getAllColumns(DBConstants.APP, appId);
		Map<String, String> map = new HashMap<String, String>();
		final String values[] = { ServiceConstant.PARA_VERSION,
				ServiceConstant.PARA_APPURL, ServiceConstant.PARA_SINA_APPKEY,
				ServiceConstant.PARA_SINA_APPSECRET,
				ServiceConstant.PARA_QQ_APPKEY,
				ServiceConstant.PARA_QQ_APPSECRET,
				ServiceConstant.PARA_RENREN_APPKEY,
				ServiceConstant.PARA_RENREN_APPSECRET, };

		final String columns[] = { DBConstants.F_VERSION, DBConstants.F_APPURL,
				DBConstants.F_SINA_APPKEY, DBConstants.F_SINA_APPSECRET,
				DBConstants.F_QQ_APPKEY, DBConstants.F_QQ_APPSECRET,
				DBConstants.F_RENREN_APPKEY, DBConstants.F_RENREN_APPSECRET, };

		Map<String, String> columnMap = new HashMap<String, String>();
		for (int i = 0; i < columns.length; i++)
			columnMap.put(columns[i], values[i]);

		for (HColumn<String, String> column : appColumns) {
			if (column != null) {
				String name = column.getName();
				if (columnMap.containsKey(name)) {
					String value = column.getValue();
					String key = columnMap.get(name);
					map.put(key, value);
				}
			}
		}
		return map;
	}

}
