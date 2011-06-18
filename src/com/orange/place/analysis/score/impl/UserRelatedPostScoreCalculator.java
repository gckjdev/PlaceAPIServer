package com.orange.place.analysis.score.impl;

import java.util.Iterator;

import com.orange.common.cassandra.CassandraClient;
import com.orange.place.analysis.dao.StatisticDao;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.domain.Similarity;
import com.orange.place.analysis.domain.UserPostStatistic;
import com.orange.place.analysis.domain.UserStatistic;
import com.orange.place.analysis.score.ScoreCalculator;
import com.orange.place.dao.User;
import com.orange.place.manager.UserManager;

public class UserRelatedPostScoreCalculator implements ScoreCalculator {

	private StatisticDao statisticDao;

	private UserManager userManager;

	private CassandraClient cassandraClient;

	@Override
	public double calculateScore(ParseResult parseResult, User user, CompactPost story) {
		Iterator<String> userIds = userManager.findAllUserId(cassandraClient);
		Similarity similarity = statisticDao.findUserSimilarity(user
				.getUserId());
		double score = 0;
		while (userIds.hasNext()) {
			String userId = userIds.next();
			if (!userId.equals(user.getUserId())) {
				// TODO: user-story interaction / user interaction, user can be
				// replaced with cluster
				UserStatistic userStat = statisticDao.findUserStatistic(userId);
				UserPostStatistic storyStati = statisticDao
						.findUserPostStatistic(userId, story.getPostId());

				double interactionStatInStatObj = storyStati
						.getInteractionCount() / userStat.getInteractionCount();

				double storySimilarityInStatObj = similarity.getSimilarity()
						.get(userId);
				// TODO: what if the user id not in the similarity list.
				double storyScoreInStatObj = interactionStatInStatObj
						* storySimilarityInStatObj;

				score += storyScoreInStatObj;
			}
		}
		return score;
	}

	public void setStatisticDao(StatisticDao statisticDao) {
		this.statisticDao = statisticDao;
	}

}
