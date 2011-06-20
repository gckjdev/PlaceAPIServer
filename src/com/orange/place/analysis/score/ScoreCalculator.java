package com.orange.place.analysis.score;

import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.dao.User;

public interface ScoreCalculator {

	double calculateScore(ParseResult parseResult, User user, CompactPost post);
}
