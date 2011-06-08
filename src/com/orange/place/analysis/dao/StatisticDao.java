package com.orange.place.analysis.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.beans.HColumn;

import org.apache.commons.lang.StringUtils;

import com.orange.place.analysis.domain.Similarity;
import com.orange.place.analysis.domain.UserPostStatistic;
import com.orange.place.analysis.domain.UserStatistic;
import com.orange.place.constant.DBConstants;

public class StatisticDao extends AbstractCassandraDao {

	public UserPostStatistic findUserPostStatistic(String userId, String postId) {
		String colFamily = DBConstants.USER_POST_STATISTIC;
		String rowKey = userId;
		String columnName = postId;

		checkStringParameter(rowKey, "rowKey");
		checkStringParameter(columnName, "columnName");

		String value = cassandraClient.getColumnValue(colFamily, rowKey,
				columnName);
		int interactionCount = readInteractionCount(value);
		return new UserPostStatistic(userId, postId, interactionCount);
	}

	public void saveUserPostStatistic(UserPostStatistic postStatistic) {
		String colFamily = DBConstants.USER_POST_STATISTIC;
		String rowKey = postStatistic.getUserId();
		String columnName = postStatistic.getPostId();
		String columnValue = String
				.valueOf(postStatistic.getInteractionCount());
		cassandraClient.insert(colFamily, rowKey, columnName, columnValue);
	}

	public UserStatistic findUserStatistic(String userId) {
		String colFamily = DBConstants.USER_POST_STATISTIC;
		String rowKey = userId;
		String columnName = DBConstants.F_USER_POST_STATISTIC_TOTOAL;

		checkStringParameter(rowKey, "rowKey");

		String value = cassandraClient.getColumnValue(colFamily, rowKey,
				columnName);
		int interactionCount = readInteractionCount(value);
		return new UserStatistic(userId, interactionCount);
	}

	public void saveUserStatistic(UserStatistic statistic) {
		String colFamily = DBConstants.USER_POST_STATISTIC;
		String rowKey = statistic.getUserId();
		String columnName = DBConstants.F_USER_POST_STATISTIC_TOTOAL;
		String columnValue = String.valueOf(statistic.getInteractionCount());
		cassandraClient.insert(colFamily, rowKey, columnName, columnValue);
	}

	public Similarity findUserSimilarity(String userId) {
		String colFamily = DBConstants.USER_SIMILARITY;
		String rowKey = userId;

		List<HColumn<String, String>> userSimilarity = cassandraClient
				.getColumnKey(colFamily, rowKey, DBConstants.UNLIMITED_COUNT);

		Map<String, Double> similarityByUser = new HashMap<String, Double>();
		if (userSimilarity != null) {
			for (HColumn<String, String> column : userSimilarity) {
				String otherUser = column.getName();
				Double simiValue = readSimilarityValue(column.getValue());
				similarityByUser.put(otherUser, simiValue);
			}
		}
		Similarity simi = new Similarity();
		simi.setUserId(userId);
		simi.setSimilarity(similarityByUser);
		return simi;
	}

	public void saveUserSimilarity(Similarity similarity) {
		String colFamily = DBConstants.USER_SIMILARITY;
		String rowKey = similarity.getUserId();
		Map<String, Double> similarityByUser = similarity.getSimilarity();

		checkStringParameter(rowKey, "rowKey");
		if (similarityByUser == null) {
			throw new IllegalArgumentException(
					"similarityByUser should not be null");
		}

		HashMap<String, String> columns = new HashMap<String, String>();
		Iterator<String> it = similarityByUser.keySet().iterator();
		while (it.hasNext()) {
			String userId = it.next();
			String similarityValue = writeSimilarityValue(similarityByUser,
					userId);
			columns.put(userId, similarityValue);
		}
		cassandraClient.insert(colFamily, rowKey, columns);

	}

	private int readInteractionCount(String value) {
		if (StringUtils.isEmpty(value)) {
			value = "0";
		}
		int interactionCount = Integer.parseInt(value);
		return interactionCount;
	}

	private String writeSimilarityValue(Map<String, Double> similarityByUser,
			String userId) {
		Double simiValue = similarityByUser.get(userId);
		if (simiValue == null) {
			simiValue = 0d;
		}

		String similarityValue = String.valueOf(simiValue);
		return similarityValue;
	}

	private Double readSimilarityValue(String value) {
		if (StringUtils.isEmpty(value)) {
			value = "0";
		}
		Double simiValue = Double.valueOf(value);
		return simiValue;
	}

	// TODO: not implement, merge with saveUserSimilarity
	public void saveClusterSimilarity(Similarity similarity) {
	}

	// TODO: not implement, merge with findUserSimilarity
	public Similarity findClusterSimilarity(String clusterId) {
		return new Similarity();
	}
}
