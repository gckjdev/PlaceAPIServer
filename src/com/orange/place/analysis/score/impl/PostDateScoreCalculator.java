package com.orange.place.analysis.score.impl;

import com.orange.common.utils.DateUtil;
import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.score.ScoreCalculator;
import com.orange.place.dao.User;

public class PostDateScoreCalculator implements ScoreCalculator {

	/**
	 * 1 minitues
	 */
	private static final int BOTTOM_THRESHOLD = 1000 * 60;

	/**
	 * 1 hour
	 */
	private static final double HOUR_PERIOR = 1000 * 60 * 60;

	@Override
	public double calculateScore(ParseResult parseResult, User user,
			CompactPost post) {

		long currentTime = parseResult.getRequestTime().getTime();
		long postTime = DateUtil.dateFromString(post.getCreateDate()).getTime();

		double timeSpan = (double) (currentTime - postTime);

		if (timeSpan < BOTTOM_THRESHOLD) {
			timeSpan = BOTTOM_THRESHOLD;
		}
		double result = HOUR_PERIOR / timeSpan;
		return result;
	}
}
