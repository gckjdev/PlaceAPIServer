package com.orange.place.api.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Post;

public class CommonServiceUtils {

	public static JSONArray postListToJSON(List<Post> postList){
		// TODO if JSON value is null, don't put into the JSON object
		// set result data, return postArray
		JSONArray obj = new JSONArray();
		for (Post post : postList){
			JSONObject json = new JSONObject();
			json.put(ServiceConstant.PARA_POSTID, post.getPostId());
			json.put(ServiceConstant.PARA_USERID, post.getUserId());
			json.put(ServiceConstant.PARA_PLACEID, post.getPlaceId());
			json.put(ServiceConstant.PARA_LONGTITUDE, post.getLongitude());
			json.put(ServiceConstant.PARA_LATITUDE, post.getLatitude());
			json.put(ServiceConstant.PARA_USER_LONGITUDE, post.getUserLongitude());
			json.put(ServiceConstant.PARA_USER_LATITUDE, post.getUserLatitude());
			json.put(ServiceConstant.PARA_TEXT_CONTENT, post.getTextContent());
			json.put(ServiceConstant.PARA_CONTENT_TYPE, post.getContentType());
			json.put(ServiceConstant.PARA_IMAGE_URL, post.getImageURL());
			json.put(ServiceConstant.PARA_TOTAL_VIEW, post.getTotalView());
			json.put(ServiceConstant.PARA_TOTAL_FORWARD, post.getTotalForward());
			json.put(ServiceConstant.PARA_TOTAL_QUOTE, post.getTotalQuote());
			json.put(ServiceConstant.PARA_TOTAL_REPLY, post.getTotalReply());
			json.put(ServiceConstant.PARA_CREATE_DATE, post.getCreateDate());
			
			obj.add(json);
		}
		
		return obj;
	}

}