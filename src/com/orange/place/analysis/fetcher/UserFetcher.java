package com.orange.place.analysis.fetcher;

import java.util.Iterator;

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
	
	public Iterator<String> findAllUserId() {
		return userManager.findAllUserId(cassandraClient);

	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCassandraClient(CassandraClient cassandraClient) {
		this.cassandraClient = cassandraClient;
	}
}
