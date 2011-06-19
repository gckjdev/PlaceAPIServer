package com.orange.place.analysis.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.common.utils.geohash.GeoRange;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.constant.DBConstants;

public class PostDao extends AbstractCassandraDao {

	private Logger log = LoggerFactory.getLogger(PostDao.class);

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
		List<CompactPost> posts = new ArrayList<CompactPost>();
		for (GeoRange range : geoRanges) {
			// TODO: time limitation should exist.
			String start = null;
			String end = null;
			Rows<String, String, String> rows = cassandraClient
					.getMultiRowByRange(DBConstants.INDEX_POST_LOCATION, range.getMin(),
							range.getMax(), start, end, limitation);
			if (rows != null) {
				Iterator<Row<String, String, String>> it = rows.iterator();
				while (it.hasNext()) {
					Row<String, String, String> row = it.next();
					if (row.getColumnSlice()!=null && row.getColumnSlice().getColumns() != null) {
						Iterator<HColumn<String, String>> columnIter = row
								.getColumnSlice().getColumns().iterator();

						String geohash = row.getKey();
						while(columnIter.hasNext()){
							HColumn<String, String> column = columnIter.next();
							
							String postId = column.getName();
							String createDate  = column.getValue();

							CompactPost post = createCompactPost(geohash, postId,
									createDate);
							posts.add(post);
						}
					}
				}
			}
		}
		return posts;
	}

	private CompactPost createCompactPost(String geohash, String postId,
			String createDate) {
		CompactPost post = new CompactPost();
		post.setGeohash(geohash);
		post.setCreateDate(createDate);
		post.setPostId(postId);
		if (log.isDebugEnabled()) {
			log.debug(
					"CompactPost created, postId : {}, geohash : {},  createDate : {}",
					new Object[] { postId, geohash, createDate });
		}
		return post;
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
