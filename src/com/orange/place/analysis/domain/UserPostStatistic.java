package com.orange.place.analysis.domain;

public class UserPostStatistic {

	private String userId;

	private String postId;

	private int interactionCount;

	public UserPostStatistic(String userId, String postId, int interactionCount) {
		setUserId(userId);
		setPostId(postId);
		setInteractionCount(interactionCount);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public int getInteractionCount() {
		return interactionCount;
	}

	public void setInteractionCount(int interactionCount) {
		this.interactionCount = interactionCount;
	}
}
