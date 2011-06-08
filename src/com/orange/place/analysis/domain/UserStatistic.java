package com.orange.place.analysis.domain;

public class UserStatistic {

	private String userId;

	private int interactionCount;

	public UserStatistic(String userId, int interactionCount) {
		setUserId(userId);
		setInteractionCount(interactionCount);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getInteractionCount() {
		return interactionCount;
	}

	public void setInteractionCount(int interactionCount) {
		this.interactionCount = interactionCount;
	}

}
