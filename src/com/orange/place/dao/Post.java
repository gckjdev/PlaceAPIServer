package com.orange.place.dao;

import java.util.List;
import java.util.Map;

import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ServiceConstant;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;

public class Post extends CommonData {

	public Post(List<HColumn<String, String>> columnValues) {
		super(columnValues);
	}
	
	public Post(Map<String, String> mapColumnValues){
		super(mapColumnValues);
	}

	public String getPostId() {
		return getKey(DBConstants.F_POSTID);
	}

	public String getCreateDate() {
		return getKey(DBConstants.F_CREATE_DATE);
	}

	public String getUserId() {		
		return getKey(DBConstants.F_USERID);
	}

	public String getPlaceId() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_PLACEID);
	}
	
	public String getLongitude() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_LONGITUDE);
	}
	
	public String getLatitude() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_LATITUDE);
	}

	public String getUserLongitude() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_USER_LONGITUDE);
	}

	public String getUserLatitude() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_USER_LATITUDE);
	}

	public String getTextContent() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_TEXT_CONTENT);
	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_CONTENT_TYPE);
	}

	public String getImageURL() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_IMAGE_URL);
	}

	public String getTotalView() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_TOTAL_VIEW);
	}

	public String getTotalForward() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_TOTAL_FORWARD);
	}

	public String getTotalQuote() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_TOTAL_QUOTE);
	}

	public String getTotalReply() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_TOTAL_REPLY);
	}

	public String getSrcPostId() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_SRC_POSTID);
	}

	public String getUserNickName() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_NICKNAME);
	}

	public String getUserAvatar() {
		// TODO Auto-generated method stub
		return getKey(DBConstants.F_AVATAR);
	}
	
}
