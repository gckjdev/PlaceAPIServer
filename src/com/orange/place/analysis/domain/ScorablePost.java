package com.orange.place.analysis.domain;

import com.orange.place.dao.Post;

public class ScorablePost implements Comparable<ScorablePost> {

	private Post post;

	private double score;

	public ScorablePost(Post post, double score) {
		this.post = post;
		this.score = score;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public int compareTo(ScorablePost post) {
		if (post == null) {
			throw new NullPointerException("post should not be null.");
		}

		int result = 0;
		if (getScore() < post.getScore()) {
			result = -1;
		} else if (getScore() > post.getScore()) {
			result = 1;
		}
		return result;
	}
}
