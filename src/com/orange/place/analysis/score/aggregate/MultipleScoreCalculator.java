package com.orange.place.analysis.score.aggregate;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.score.ScoreCalculator;
import com.orange.place.dao.User;

public class MultipleScoreCalculator implements ScoreCalculator {

	private List<ScoreCalculator> calculators;

	@Override
	public double calculateScore(ParseResult parseResult, User user, CompactPost story) {
		int score = 1;
		for (ScoreCalculator cal : calculators) {
			score *= cal.calculateScore(parseResult, user, story);
		}
		return score;
	}

	@Required
	public void setCalculators(List<ScoreCalculator> calculators) {
		this.calculators = calculators;
	}
}
