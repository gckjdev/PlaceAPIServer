package com.orange.place.analysis.filter;

import java.util.List;

import com.orange.place.dao.Post;
import com.orange.place.dao.User;

public interface PostFilter {

	List<Post> filter(User user, List<Post> candidates);
}
