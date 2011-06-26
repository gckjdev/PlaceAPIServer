package com.orange.place.analysis.parser.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.orange.common.utils.geohash.GeoRange;
import com.orange.common.utils.geohash.GeoRangeUtil;
import com.orange.common.utils.geohash.ProximitySearchUtil;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.domain.Request;
import com.orange.place.analysis.parser.RequestParser;

public class RequestParserImpl implements RequestParser {

	private ProximitySearchUtil proximitySearchUtil;

	private GeoRangeUtil geoRangeUtil;

	@Override
	public ParseResult parse(Request request) {
		ParseResult result = new ParseResult();
		try {
			result.setRequest(request);
			result.setRequestTime(new Date());
			List<String> geohashList = proximitySearchUtil.getNearBy(
					request.getLatitude(), request.getLongitude(),
					request.getRadius());
			List<GeoRange> geoRanges = geoRangeUtil.getGeoRange(geohashList);
			result.setPlaceRange(geoRanges);

			result.setSuccess(true);
		} catch (Exception e) {
			// TODO: log here?
			result.setSuccess(false);
		}

		return result;
	}

	@Required
	public void setProximitySearchUtil(ProximitySearchUtil proximitySearchUtil) {
		this.proximitySearchUtil = proximitySearchUtil;
	}

	@Required
	public void setGeoRangeUtil(GeoRangeUtil geoRangeUtil) {
		this.geoRangeUtil = geoRangeUtil;
	}

}
