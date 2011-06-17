package com.orange.common.weibo;

public class QQWeiboConsumer implements Runnable {

	@Override
	public void run() {
		try {
			while (true) {
				PlaceWeibo weibo = WeiboManager.getQQWeibo();
				if (weibo == null)
					continue;
				WeiboManager.publishQQWeibo(weibo);
			}
		} catch (InterruptedException e) {
			WeiboLog.logError("<QQWeiboConsumer.run>: Thread is Interrupted");
		}
	}

}
