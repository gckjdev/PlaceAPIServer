package com.orange.common.utils.geohash;

public class GeoRange {

	private String max;

	private String min;

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	@Override
	public String toString() {
		return "min:" + min + " ;max:" + max;
	}
}
