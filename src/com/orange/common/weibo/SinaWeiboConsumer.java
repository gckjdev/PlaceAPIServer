package com.orange.common.weibo;

public class SinaWeiboConsumer implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				PlaceWeibo weibo = WeiboManager.getSianWeibo();
				if (weibo == null)
					continue;
				WeiboManager.publishSinaWeibo(weibo);
			}
		} catch (InterruptedException e) {
			WeiboLog.logError("<SinaWeiboConsumer.run>: Thread is Interrupted");
		}
	}
}