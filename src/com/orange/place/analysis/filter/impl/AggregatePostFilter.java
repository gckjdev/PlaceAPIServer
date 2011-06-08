package com.orange.place.analysis.filter.impl;

import java.util.List;

import com.orange.place.analysis.filter.PostFilter;
import com.orange.place.dao.Post;
import com.orange.place.dao.User;

public class AggregatePostFilter implements PostFilter {

	private List<PostFilter> filters;

	@Override
	public List<Post> filter(User user, List<Post> candidates) {
		List<Post> reslut = candidates;
		for (PostFilter filter : filters) {
			reslut = filter.filter(user, reslut);
		}
		return reslut;
	}

	public void setFilters(List<PostFilter> filters) {
		this.filters = filters;
	}

}
