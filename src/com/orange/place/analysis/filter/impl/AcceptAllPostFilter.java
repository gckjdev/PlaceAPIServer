package com.orange.place.analysis.filter.impl;

import java.util.List;

import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.filter.PostFilter;
import com.orange.place.dao.User;

public class AcceptAllPostFilter implements PostFilter {

	@Override
	public List<CompactPost> filter(User user, List<CompactPost> candidates) {
		return candidates;
	}

}
