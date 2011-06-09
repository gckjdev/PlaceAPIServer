package com.orange.place.analysis.fetcher;

import java.util.Date;
import java.util.List;

import com.orange.place.analysis.dao.PostDao;
import com.orange.place.analysis.domain.GeoRange;
import com.orange.place.dao.Post;

public class PostFetcher {

	private PostDao postDao;

	public List<Post> fetchStroy(List<GeoRange> geoRange, Date sinceAfter,
			int limitation) {
		List<Post> sotry = postDao.findPostByLocation(geoRange, sinceAfter,
				limitation);
		return sotry;
	}

	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}
}
