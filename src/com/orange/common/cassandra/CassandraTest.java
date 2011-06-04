package com.orange.common.cassandra;

import org.apache.cassandra.thrift.Cassandra.system_add_column_family_args;

import com.orange.place.constant.DBConstants;

public class CassandraTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CassandraClient cc = new CassandraClient(DBConstants.SERVER,
				DBConstants.CLUSTERNAME, DBConstants.KEYSPACE);
		//String key = "31d27490-8670-11e0-ba6a-0026bb0628f2";
		String key = "8a24e790-89c2-11e0-9217-0026bb0628f2";
		int count = cc.getColumnCount(DBConstants.INDEX_USER_VIEW_POSTS, key);
		System.out.println("count is :"+count);
		//count idx_user_timeline['8a24e790-89c2-11e0-9217-0026bb0628f2'];
	}

}
;