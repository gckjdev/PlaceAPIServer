package com.orange.place.analysis.recommender.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.domain.ScorablePost;
import com.orange.place.analysis.filter.PostFilter;
import com.orange.place.analysis.recommender.PostRecommender;
import com.orange.place.analysis.score.ScoreCalculator;
import com.orange.place.dao.User;

public class PostRecommenderImpl implements PostRecommender {

	private ScoreCalculator scoreCalculator;

	private PostFilter postFilter;

	@Override
	public List<CompactPost> getTopPost(ParseResult parseResult, User user,
			List<CompactPost> candidates) {

		// filter some story, like have related.
		List<CompactPost> scoreCandidates = postFilter.filter(user, candidates);

		List<ScorablePost> scoreList = new LinkedList<ScorablePost>();
		for (CompactPost story : scoreCandidates) {
			double score = scoreCalculator.calculateScore(parseResult, user,
					story);
			ScorablePost scorable = new ScorablePost(story, score);
			scoreList.add(scorable);
		}

		Collections.sort(scoreList);
		Collections.reverse(scoreList);
		return convert(scoreList);
	}

	public List<CompactPost> convert(List<ScorablePost> scoreList) {
		List<CompactPost> storyList = new LinkedList<CompactPost>();
		for (ScorablePost scorable : scoreList) {
			storyList.add(scorable.getPost());
		}
		return storyList;

	}

	public void setScoreCalculator(ScoreCalculator scoreCalculator) {
		this.scoreCalculator = scoreCalculator;
	}

	public void setPostFilter(PostFilter postFilter) {
		this.postFilter = postFilter;
	}

}
