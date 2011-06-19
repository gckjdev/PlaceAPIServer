package com.orange.place.analysis.domain;

public class AnalysisLogContent {
	
	private String userId;
	
	private String placeId;
	
	private String postId;
	
	private PostType postType;
	
	private String latitude;
	
	private String longitude;

	public String getUserId() {
		return userId;
	}

	public AnalysisLogContent setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getPlaceId() {
		return placeId;
	}

	public AnalysisLogContent setPlaceId(String placeId) {
		this.placeId = placeId;
		return this;
	}

	public String getPostId() {
		return postId;
	}

	public AnalysisLogContent setPostId(String postId) {
		this.postId = postId;
		return this;
	}

	public int getPostType() {
		return postType.getValue();
	}

	public AnalysisLogContent setPostType(PostType postType) {
		this.postType = postType;
		return this;
	}

	public String getLatitude() {
		return latitude;
	}

	public AnalysisLogContent setLatitude(String latitude) {
		this.latitude = latitude;
		return this;
	}

	public String getLongitude() {
		return longitude;
	}

	public AnalysisLogContent setLongitude(String longitude) {
		this.longitude = longitude;
		return this;
	}
}
