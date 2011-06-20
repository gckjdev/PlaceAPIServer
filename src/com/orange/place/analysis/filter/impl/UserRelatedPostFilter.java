package com.orange.place.analysis.filter.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.orange.place.analysis.dao.PostDao;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.filter.PostFilter;
import com.orange.place.dao.User;

public class UserRelatedPostFilter implements PostFilter {

	private PostDao postDao;

	@Override
	public List<CompactPost> filter(User user, List<CompactPost> candidates) {
		Set<String> relatedSet = getRelatedPostId(user);

		Iterator<CompactPost> it = candidates.iterator();
		while (it.hasNext()) {
			CompactPost candidate = it.next();
			if (relatedSet.contains(candidate.getPostId())) {
				it.remove();
			}
		}

		return candidates;
	}

	private Set<String> getRelatedPostId(User user) {
		List<String> relatedPost = postDao.findRelatedPostByUserId(user
				.getUserId());
		// construct a map for better performance
		Set<String> relatedSet = new HashSet<String>();
		for (String related : relatedPost) {
			relatedSet.add(related);
		}
		return relatedSet;
	}

	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}

}
