package com.orange.place.analysis.recommender.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.domain.ScorablePost;
import com.orange.place.analysis.filter.PostFilter;
import com.orange.place.analysis.recommender.PostRecommender;
import com.orange.place.analysis.score.ScoreCalculator;
import com.orange.place.dao.Post;
import com.orange.place.dao.User;

public class PostRecommenderImpl implements PostRecommender {

	private ScoreCalculator scoreCalculator;

	private PostFilter storyFilter;

	@Override
	public List<Post> getTopPost(ParseResult parseResult, User user,
			List<Post> candidates) {

		// filter some story, like have related.
		List<Post> scoreCandidates = storyFilter.filter(user, candidates);

		List<ScorablePost> scoreList = new LinkedList<ScorablePost>();
		for (Post story : scoreCandidates) {
			double score = scoreCalculator.calculateScore(parseResult, user,
					story);
			ScorablePost scorable = new ScorablePost(story, score);
			scoreList.add(scorable);
		}

		Collections.sort(scoreList);
		return convert(scoreList);
	}

	public List<Post> convert(List<ScorablePost> scoreList) {
		List<Post> storyList = new LinkedList<Post>();
		for (ScorablePost scorable : scoreList) {
			storyList.add(scorable.getPost());
		}
		return storyList;

	}

	public void setScoreCalculator(ScoreCalculator scoreCalculator) {
		this.scoreCalculator = scoreCalculator;
	}

}
