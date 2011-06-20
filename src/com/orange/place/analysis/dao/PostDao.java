package com.orange.place.analysis.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.beans.HColumn;

import com.orange.common.utils.geohash.GeoRange;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.constant.DBConstants;

public class PostDao extends AbstractCassandraDao {

	/**
	 * @param latitude
	 * @param longitude
	 * @param radius
	 *            radius for the specified point
	 * @param sinceAfter
	 *            story happened after specified time
	 */
	public List<CompactPost> findPostByLocation(List<GeoRange> geoRanges,
			Date sinceAfter, int limitation) {
		//
		List<CompactPost> posts = new ArrayList<CompactPost>();
		for(GeoRange range: geoRanges){
			
		}
		return posts;
	}

	public List<String> findRelatedPostByUserId(String userId) {
		checkStringParameter(userId, "userId");
		UUID startUUID = null;
		int max = DBConstants.UNLIMITED_COUNT;

		List<HColumn<UUID, String>> resultList = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_USER_POST, userId,
						startUUID, max);

		return getColumnNames(resultList);
	}
}
