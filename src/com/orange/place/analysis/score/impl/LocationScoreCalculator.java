package com.orange.place.analysis.score.impl;

import org.springframework.beans.factory.annotation.Required;

import com.orange.common.utils.geohash.GeoHashUtil;
import com.orange.common.utils.geohash.ProximitySearchUtil;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.score.ScoreCalculator;
import com.orange.place.dao.User;

public class LocationScoreCalculator implements ScoreCalculator {

	private ProximitySearchUtil proximitySearchUtil;

	private GeoHashUtil geoHashUtil;

	private double STANDRAD_DISTANCE = 200;

	private static final int BOTTOM_THRESHOLD = 1;

	@Override
	public double calculateScore(ParseResult parseResult, User user,
			CompactPost post) {
		double[] location = geoHashUtil.decode(post.getGeohash());

		double distance = proximitySearchUtil.getDistance(parseResult
				.getRequest().getLatitude(), parseResult.getRequest()
				.getLongitude(), location[0], location[1]);
		if (distance < BOTTOM_THRESHOLD) {
			distance = BOTTOM_THRESHOLD;
		}
		return STANDRAD_DISTANCE / distance;
	}
	
	@Required
	public void setProximitySearchUtil(ProximitySearchUtil proximitySearchUtil) {
		this.proximitySearchUtil = proximitySearchUtil;
	}

	@Required
	public void setGeoHashUtil(GeoHashUtil geoHashUtil) {
		this.geoHashUtil = geoHashUtil;
	}

}
