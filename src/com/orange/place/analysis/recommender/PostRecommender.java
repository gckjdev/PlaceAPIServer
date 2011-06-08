package com.orange.place.analysis.recommender;

import java.util.List;

import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.dao.Post;
import com.orange.place.dao.User;

public interface PostRecommender {

	List<Post> getTopPost(ParseResult parseResult, User user,
			List<Post> candidates);

}
