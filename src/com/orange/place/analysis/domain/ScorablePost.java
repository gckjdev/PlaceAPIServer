package com.orange.place.analysis.domain;

public class ScorablePost implements Comparable<ScorablePost> {

	private CompactPost post;

	private double score;

	public ScorablePost(CompactPost post, double score) {
		this.post = post;
		this.score = score;
	}

	public CompactPost getPost() {
		return post;
	}

	public void setPost(CompactPost post) {
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
