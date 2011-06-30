package com.orange.place.analysis.fetcher;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.common.utils.geohash.GeoRange;
import com.orange.place.analysis.dao.PostDao;
import com.orange.place.analysis.domain.CompactPost;

public class PostFetcher {

	private Logger log = LoggerFactory.getLogger(PostFetcher.class);
	private PostDao postDao;

	public List<CompactPost> fetchStroy(List<GeoRange> geoRange,
			Date sinceAfter, int limitation) {
		List<CompactPost> posts = postDao.findPostByLocation(geoRange,
				sinceAfter, limitation);
		log.info("postFetcher list size : {}, limitation : {}", posts.size(),
				limitation);
		return posts;
	}

	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}
}
