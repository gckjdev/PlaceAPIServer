package com.orange.place.analysis.fetcher;

import java.util.Date;
import java.util.List;

import com.orange.common.utils.geohash.GeoRange;
import com.orange.place.analysis.dao.PostDao;
import com.orange.place.analysis.domain.CompactPost;

public class PostFetcher {

	private PostDao postDao;

	public List<CompactPost> fetchStroy(List<GeoRange> geoRange, Date sinceAfter,
			int limitation) {
		List<CompactPost> sotry = postDao.findPostByLocation(geoRange, sinceAfter,
				limitation);
		return sotry;
	}

	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}
}
