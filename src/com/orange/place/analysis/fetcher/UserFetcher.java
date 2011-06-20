package com.orange.place.analysis.fetcher;

import com.orange.common.cassandra.CassandraClient;
import com.orange.place.dao.User;
import com.orange.place.manager.UserManager;

public class UserFetcher {

	private UserManager userManager;
	
	private CassandraClient cassandraClient;
	
	//TODO: better not use static 
	@SuppressWarnings("static-access")
	public User fetchUserById(String userId) {
		return userManager.getUserById(cassandraClient, userId);
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCassandraClient(CassandraClient cassandraClient) {
		this.cassandraClient = cassandraClient;
	}
}
