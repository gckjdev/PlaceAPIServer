package com.orange.place.analysis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.place.analysis.domain.CompactPost;
import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.domain.Request;
import com.orange.place.analysis.fetcher.PostFetcher;
import com.orange.place.analysis.fetcher.UserFetcher;
import com.orange.place.analysis.parser.RequestParser;
import com.orange.place.analysis.recommender.PostRecommender;
import com.orange.place.dao.User;

public class RequestHandler {

	private static final Logger log = LoggerFactory
			.getLogger(RequestHandler.class);

	private RequestParser requestParser;

	private PostFetcher postFetcher;

	private UserFetcher userFetcher;

	private PostRecommender postRecommender;

	private int fetchPostDaysBefore = 30;

	private int fetchPostLimitation = 1000;

	public List<CompactPost> execute(Request request) {
		List<CompactPost> result = new ArrayList<CompactPost>();

		ParseResult parseResult = requestParser.parse(request);
		if (!parseResult.isSuccess()) {
			// empty list if parse failed.
			return new ArrayList<CompactPost>();
		}

		try {
			List<CompactPost> candaidatePost = postFetcher.fetchStroy(
					parseResult.getPlaceRange(), getDateSince(),
					fetchPostLimitation);
			User user = userFetcher.fetchUserById(parseResult.getUserId());

			// TODO: consider put the user/candidate story/parse result to
			// context;
			result = postRecommender.getTopPost(parseResult, user,
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

	public void setPostFetcher(PostFetcher postFetcher) {
		this.postFetcher = postFetcher;
	}

	public void setUserFetcher(UserFetcher userFetcher) {
		this.userFetcher = userFetcher;
	}

	public void setPostRecommender(PostRecommender postRecommender) {
		this.postRecommender = postRecommender;
	}

	public void setFetchPostDaysBefore(int fetchPostDaysBefore) {
		this.fetchPostDaysBefore = fetchPostDaysBefore;
	}

	public void setFetchPostLimitation(int fetchPostLimitation) {
		this.fetchPostLimitation = fetchPostLimitation;
	}
}
