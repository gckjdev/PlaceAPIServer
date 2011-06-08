package com.orange.place.analysis.filter.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.orange.place.analysis.dao.NativeDao;
import com.orange.place.analysis.filter.PostFilter;
import com.orange.place.dao.Post;
import com.orange.place.dao.User;

public class UserRelatedPostFilter implements PostFilter {

	private NativeDao nativeDao;

	@Override
	public List<Post> filter(User user, List<Post> candidates) {
		Set<String> relatedSet = getRelatedPostId(user);

		Iterator<Post> it = candidates.iterator();
		while (it.hasNext()) {
			Post candidate = it.next();
			if (relatedSet.contains(candidate.getPostId())) {
				it.remove();
			}
		}

		return candidates;
	}

	private Set<String> getRelatedPostId(User user) {
		List<String> relatedPost = nativeDao.findRelatedPostByUserId(user
				.getUserId());
		// construct a map for better performance
		Set<String> relatedSet = new HashSet<String>();
		for (String related : relatedPost) {
			relatedSet.add(related);
		}
		return relatedSet;
	}

	public void setNativeDao(NativeDao nativeDao) {
		this.nativeDao = nativeDao;
	}

}
