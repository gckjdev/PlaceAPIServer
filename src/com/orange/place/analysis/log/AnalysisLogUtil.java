package com.orange.place.analysis.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.place.analysis.domain.AnalysisLogContent;

//date(yyyy-MM-dd HH:mm:sss);user_id;place_id;post_id;type;(CREATE,FORWARD,REPLY);lang;long
public class AnalysisLogUtil {

	private static final Logger analysisLog = LoggerFactory
			.getLogger("com.orange.place.analysisLog");
	
	private static final String SEPARATOR = ";";

	public static void log(AnalysisLogContent content) {
		StringBuffer sb = new StringBuffer();
		sb.append(content.getUserId()).append(SEPARATOR);
		sb.append(content.getPlaceId()).append(SEPARATOR);
		sb.append(content.getPostId()).append(SEPARATOR);
		sb.append(content.getPostType()).append(SEPARATOR);
		sb.append(content.getLatitude()).append(SEPARATOR);
		sb.append(content.getLongitude()).append(SEPARATOR);

		analysisLog.info(sb.toString());
	}
}
