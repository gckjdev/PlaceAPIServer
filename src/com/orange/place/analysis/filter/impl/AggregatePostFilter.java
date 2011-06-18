package com.orange.place.analysis.filter.impl;

import java.util.List;

import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.filter.PostFilter;
import com.orange.place.dao.User;

public class AggregatePostFilter implements PostFilter {

	private List<PostFilter> filters;

	@Override
	public List<CompactPost> filter(User user, List<CompactPost> candidates) {
		List<CompactPost> reslut = candidates;
		for (PostFilter filter : filters) {
			reslut = filter.filter(user, reslut);
		}
		return reslut;
	}

	public void setFilters(List<PostFilter> filters) {
		this.filters = filters;
	}

}
