package com.orange.place.analysis.recommender;

import java.util.List;

import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.dao.User;

public interface PostRecommender {

	List<CompactPost> getTopPost(ParseResult parseResult, User user,
			List<CompactPost> candidates);

}
