package com.orange.place.analysis.domain;

import java.util.Date;
import java.util.List;

import com.orange.common.utils.geohash.GeoRange;

public class ParseResult {

	private Request request;

	private boolean success;

	private List<GeoRange> placeRange;

	private Date requestTime;

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public List<GeoRange> getPlaceRange() {
		return placeRange;
	}

	public void setPlaceRange(List<GeoRange> placeRange) {
		this.placeRange = placeRange;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getUserId() {
		return request.getUserId();
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

}
