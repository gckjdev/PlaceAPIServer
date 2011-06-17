package com.orange.common.weibo;

import java.io.File;
import java.net.URLEncoder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import weibo4j.Status;
import weibo4j.Weibo;

import com.mime.qweibo.examples.QWeiboSyncApi;
import com.mime.qweibo.examples.QWeiboType.ResultType;

public class WeiboManager {

	public static BlockingQueue<PlaceWeibo> qqWeiboQueue = new LinkedBlockingQueue<PlaceWeibo>();
	public static BlockingQueue<PlaceWeibo> sinaWeiboQueue = new LinkedBlockingQueue<PlaceWeibo>();

	public static String SINA_CONSUMER_KEY = "1528146353";
	public static String SINA_CONSUMER_SECRET = "4815b7938e960380395e6ac1fe645a5c";
	public static String QQ_CONSUMER_KEY = "7c78d5b42d514af8bb66f0200bc7c0fc";
	public static String QQ_CONSUMER_SECRET = "6340ae28094e66d5388b4eb127a2af43";


	// static String
	public static void putQQWeibo(PlaceWeibo weibo) throws InterruptedException {
		qqWeiboQueue.put(weibo);
	}

	public static void putSinaWeibo(PlaceWeibo weibo)
			throws InterruptedException {
		sinaWeiboQueue.put(weibo);
	}

	public static void putSianWeibo(String tokenKey, String tokenSecrect,
			String text, File iamgeFile) throws InterruptedException {
		PlaceWeibo weibo = new PlaceWeibo(tokenKey, tokenSecrect, text,
				iamgeFile);
		putSinaWeibo(weibo);
	}

	public static void putQQWeibo(String tokenKey, String tokenSecrect,
			String text, File iamgeFile) throws InterruptedException {
		PlaceWeibo weibo = new PlaceWeibo(tokenKey, tokenSecrect, text,
				iamgeFile);
		putQQWeibo(weibo);
	}

	public static PlaceWeibo getSianWeibo() throws InterruptedException {
		if (sinaWeiboQueue == null)
			return null;
		return sinaWeiboQueue.take();
	}

	public static PlaceWeibo getQQWeibo() throws InterruptedException {
		if (qqWeiboQueue == null)
			return null;
		return qqWeiboQueue.take();
	}

	public static void printWeiboMessage(PlaceWeibo weibo) {
		System.out.println(weibo.toString());
	}

	public static boolean publishSinaWeibo(PlaceWeibo weibo) {
		Weibo.CONSUMER_KEY = SINA_CONSUMER_KEY;
		Weibo.CONSUMER_SECRET = SINA_CONSUMER_SECRET;
		File file = weibo.getImageFile();
		String text = weibo.getText();
		String tokenKey = weibo.getTokenKey();
		String tokenSecret = weibo.getTokenSecret();
		try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			Weibo sinaWeibo = new Weibo();

			sinaWeibo.setToken(tokenKey, tokenSecret);
			try {
				text = URLEncoder.encode(text, "UTF-8");
				Status status = null;
				if (file == null || !file.exists()) {
					status = sinaWeibo.updateStatus(text);
				} else {
					status = sinaWeibo.uploadStatus(text, file);
				}
			} catch (Exception e1) {
				WeiboLog
						.logError("<weiboManager.publishSinaWeibo>: fail to publish sina weibo = "
								+ weibo.toString());
				return false;
			}
		} catch (Exception ioe) {
			WeiboLog
					.logError("<weiboManager.publishSinaWeibo>:fail to read image file = "
							+ file.getPath());
			return false;
		}
		return true;
	}

	public static boolean publishQQWeibo(PlaceWeibo weibo) {

		QWeiboSyncApi api = new QWeiboSyncApi();
		String customKey = QQ_CONSUMER_KEY;
		String customSecrect = QQ_CONSUMER_SECRET;
		String tokenKey = weibo.getTokenKey();
		String tokenSecret = weibo.getTokenSecret();
		String text = weibo.getText();

		File imageFile = weibo.getImageFile();
		String imagePath = null;
		if (imageFile != null && imageFile.exists()) {
			imagePath = imageFile.getPath();
		}
		String result = api.publishMsg(customKey, customSecrect, tokenKey,
				tokenSecret, text, imagePath, ResultType.ResultType_Json);
		if (result == null) {
			WeiboLog
					.logError("<weiboManager.publishSinaWeibo>: fail to publish weibo = "
							+ weibo.toString());
			return false;
		}
		return true;
	}
}
