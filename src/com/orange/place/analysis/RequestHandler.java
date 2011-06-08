package com.orange.place.analysis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.domain.Request;
import com.orange.place.analysis.fetcher.PostFetcher;
import com.orange.place.analysis.fetcher.UserFetcher;
import com.orange.place.analysis.parser.RequestParser;
import com.orange.place.analysis.recommender.PostRecommender;
import com.orange.place.dao.Post;
import com.orange.place.dao.User;

public class RequestHandler {

	private static final Logger log = LoggerFactory
			.getLogger(RequestHandler.class);

	private RequestParser requestParser;

	private PostFetcher storyFetcher;

	private UserFetcher userFetcher;

	private PostRecommender rankingGenerator;

	private final int fetchPostDaysBefore = 30;

	private final int fetchPostLimitation = 1000;

	public List<Post> execute(Request request) {
		List<Post> result = new ArrayList<Post>();

		ParseResult parseResult = requestParser.parse(request);
		if (!parseResult.isSuccess()) {
			// empty list if parse failed.
			return new ArrayList<Post>();
		}

		try {
			List<Post> candaidatePost = storyFetcher.fetchStroy(
					parseResult.getPlaceRange(), getDateSince(),
					fetchPostLimitation);
			User user = userFetcher.fetchUserById(parseResult.getUserId());

			// TODO: consider put the user/candidate story/parse result to
			// context;
			result = rankingGenerator.getTopPost(parseResult, user,
					candaidatePost);

		} catch (Exception e) {
			// TODO: some exception handling here.
			log.error("RequestHandler#execute", e);
		}
		return result;
	}

	private Date getDateSince() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1 * fetchPostDaysBefore);
		return cal.getTime();
	}

	public void setRequestParser(RequestParser requestParser) {
		this.requestParser = requestParser;
	}

	public void setPostFetcher(PostFetcher storyFetcher) {
		this.storyFetcher = storyFetcher;
	}

	public void setUserFetcher(UserFetcher userFetcher) {
		this.userFetcher = userFetcher;
	}

	public void setRankingGenerator(PostRecommender rankingGenerator) {
		this.rankingGenerator = rankingGenerator;
	}
}
