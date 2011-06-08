package com.orange.place.analysis.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orange.place.analysis.domain.GeoRange;
import com.orange.place.dao.Post;

public class NativeDao {

	/**
	 * @param latitude
	 * @param longitude
	 * @param radius
	 *            radius for the specified point
	 * @param sinceAfter
	 *            story happened after specified time
	 */
	public List<Post> findStoryByLocation(List<GeoRange> geoRange,
			Date sinceAfter, int limitation) {
		return new ArrayList<Post>();
	}

	public List<String> findRelatedPostByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
}
