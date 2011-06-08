package com.orange.place.analysis.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;

import org.apache.commons.lang.StringUtils;

import com.orange.common.cassandra.CassandraClient;

public class AbstractCassandraDao {

	protected CassandraClient cassandraClient;

	public void setCassandraClient(CassandraClient cassandraClient) {
		this.cassandraClient = cassandraClient;
	}

	protected List<String> getColumnNames(List<HColumn<UUID, String>> resultList) {
		List<String> postList = new ArrayList<String>();
		if (resultList != null) {
			for (HColumn<UUID, String> result : resultList) {
				String postId = result.getName().toString();
				postList.add(postId);
			}
		}

		return postList;
	}

	protected List<String> getKeys(Rows<String, String, String> rows) {
		List<String> userList = new ArrayList<String>();
		for (Row<String, String, String> row : rows) {
			String userId = row.getKey();
			userList.add(userId);
		}
		return userList;
	}

	protected void checkStringParameter(String parameter, String name) {
		if (StringUtils.isEmpty(parameter)) {
			throw new IllegalArgumentException(name + " should not be empty");
		}
	}

}
