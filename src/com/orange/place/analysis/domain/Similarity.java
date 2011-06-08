package com.orange.place.analysis.domain;

import java.util.Iterator;
import java.util.Map;

public class Similarity {

	private String userId;

	private Map<String, Double> similarity;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, Double> getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Map<String, Double> similarity) {
		this.similarity = similarity;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("userId : ").append(userId).append('\n');
		if (similarity != null) {
			Iterator<String> it = similarity.keySet().iterator();
			sb.append("similarity : ").append("[");
			while (it.hasNext()) {
				String otherUser = it.next();
				Double value = similarity.get(otherUser);
				sb.append("user : similarity - ").append(otherUser)
						.append(" : ").append(value);
				sb.append('\n');
			}
			sb.append("]");
		} else {
			sb.append("similarity : ").append("[]");
		}
		return sb.toString();
	}
}
