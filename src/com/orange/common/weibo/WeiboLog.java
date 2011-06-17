package com.orange.common.weibo;

import java.io.*;
import java.util.logging.Logger;

import com.orange.place.api.PlaceAPIServer;

public class WeiboLog {
	private static FileWriter logFileWriter = null;
	public static final Logger log = Logger.getLogger(PlaceAPIServer.class
			.getName());

	public static void logError(String errorInfo) {
		log.info(errorInfo);
		try {
			logFileWriter.write(errorInfo, 0, errorInfo.length());
			logFileWriter.flush();
		} catch (IOException e) {
			log
					.info("<WeiboLog.logError>: fail to write error log. errorInfo = "
							+ errorInfo);
		}

	}

	public static void setFileWriter(FileWriter fileWriter) {
		logFileWriter = fileWriter;
	}

	public static void setFileWriter(String filePath) {
		try {
			logFileWriter = new FileWriter(filePath);
		} catch (IOException e) {
			log
					.info("<WeiboLog.setFileWriter>: fail to set file writer. file path = "
							+ filePath);
		}
	}
}
