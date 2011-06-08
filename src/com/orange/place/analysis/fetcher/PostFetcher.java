package com.orange.place.analysis.fetcher;

import java.util.Date;
import java.util.List;

import com.orange.place.analysis.dao.NativeDao;
import com.orange.place.analysis.domain.GeoRange;
import com.orange.place.dao.Post;

public class PostFetcher {

	private NativeDao nativeDao;

	public List<Post> fetchStroy(List<GeoRange> geoRange, Date sinceAfter,
			int limitation) {
		List<Post> sotry = nativeDao.findStoryByLocation(geoRange, sinceAfter,
				limitation);
		return sotry;
	}
}
