package com.orange.common.weibo;

import java.io.File;

public class PlaceWeibo {
	private String tokenKey = null;
	private String tokenSecret = null;
	private String text = null;
	private File imageFile = null;

	public PlaceWeibo(String tokenKey, String tokenSecrect, String text,
			File iamgeFile) {
		super();
		this.tokenKey = tokenKey;
		this.tokenSecret = tokenSecrect;
		this.text = text;
		this.imageFile = iamgeFile;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecrect(String tokenSecrect) {
		this.tokenSecret = tokenSecrect;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File iamgeFile) {
		this.imageFile = iamgeFile;
	}

	@Override
	public String toString() {
		return "PlaceWeibo [iamgeFile=" + imageFile + ", text=" + text
				+ ", tokenKey=" + tokenKey + ", tokenSecret=" + tokenSecret
				+ "]";
	}
}
