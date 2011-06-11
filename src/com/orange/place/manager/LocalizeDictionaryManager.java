package com.orange.place.manager;

import com.orange.common.cassandra.CassandraClient;
import com.orange.place.constant.DBConstants;

//import com.orange.place.dao.App;

public class LocalizeDictionaryManager extends CommonManager {

	public static String transition(String name, String language,
			CassandraClient cassandraClient) {
		// TODO Auto-generated method stub
		String alias = null;
		alias = cassandraClient.getColumnValue(DBConstants.LOCALIZEDICT,
				language, name);
		return alias;
	}
}
