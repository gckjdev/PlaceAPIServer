package com.orange.place.analysis.filter;

import java.util.List;

import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.dao.User;

public interface PostFilter {

	List<CompactPost> filter(User user, List<CompactPost> candidates);
}
