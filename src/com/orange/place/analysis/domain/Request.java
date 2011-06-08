package com.orange.place.analysis.domain;

/**
 * @author echnlee
 * 
 */
public class Request {

	private String userId;

	/**
	 * Jing du
	 */
	private double longitude;

	private double latitude;

	/**
	 * Calculate in meters;
	 */
	private double radius;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
