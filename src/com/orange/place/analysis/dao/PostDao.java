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
			UUID start = null;
			UUID end = null;
			Rows<String, UUID, String> rows = cassandraClient
					.getMultiRowByRange(DBConstants.INDEX_POST_LOCATION,
							range.getMin(), range.getMax(), start, end,
							limitation);
			if (rows != null) {
				Iterator<Row<String, UUID, String>> it = rows.iterator();
				while (it.hasNext()) {
					Row<String, UUID, String> row = it.next();
					if (row.getColumnSlice() != null
							&& row.getColumnSlice().getColumns() != null) {
						Iterator<HColumn<UUID, String>> columnIter = row
								.getColumnSlice().getColumns().iterator();

						String geohash = row.getKey();
						while (columnIter.hasNext()) {
							HColumn<UUID, String> column = columnIter.next();

							UUID postId = column.getName();
							String createDate = column.getValue();

							CompactPost post = createCompactPost(geohash,
									postId.toString(), createDate);
							posts.add(post);
						}
					}
				}
			}
		}
		return posts;
	}

	public List<CompactPost> findPostByLocation(String geohash,
			Date sinceAfter, int limitation) {
		List<CompactPost> posts = new ArrayList<CompactPost>();
		// TODO: time limitation should exist.
		UUID start = null;
		UUID end = null;
		List<HColumn<UUID, String>> reslut = cassandraClient
				.getColumnKeyByRange(DBConstants.INDEX_POST_LOCATION,
						geohash, start, end, limitation);

		if (reslut != null) {
			Iterator<HColumn<UUID, String>> columnIter = reslut.iterator();

			while (columnIter.hasNext()) {
				HColumn<UUID, String> column = columnIter.next();

				UUID postId = column.getName();
				String createDate = column.getValue();

				CompactPost post = createCompactPost(geohash, postId.toString(),
						createDate);
				posts.add(post);
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
