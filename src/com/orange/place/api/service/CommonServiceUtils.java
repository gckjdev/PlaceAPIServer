package com.orange.place.api.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Message;
import com.orange.place.dao.Place;
import com.orange.place.dao.Post;
import com.orange.place.dao.User;

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
			json.put(ServiceConstant.PARA_SRC_POSTID, post.getSrcPostId());
			json.put(ServiceConstant.PARA_NICKNAME, post.getUserNickName());
			json.put(ServiceConstant.PARA_AVATAR, post.getUserAvatar());
			json.put(ServiceConstant.PARA_TOTAL_RELATED, post.getTotalRelatedPost());
			
			obj.add(json);
		}
		
		return obj;
	}
	
	public static JSONArray postListToJSON(List<Post> postList, String excludePostId){
		// TODO if JSON value is null, don't put into the JSON object
		// set result data, return postArray
		JSONArray obj = new JSONArray();
		for (Post post : postList){
			
			String postId = post.getPostId();
			if (excludePostId != null && excludePostId.equalsIgnoreCase(postId))
				continue;
			
			JSONObject json = new JSONObject();
			json.put(ServiceConstant.PARA_POSTID, postId);
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
			json.put(ServiceConstant.PARA_SRC_POSTID, post.getSrcPostId());
			json.put(ServiceConstant.PARA_NICKNAME, post.getUserNickName());
			json.put(ServiceConstant.PARA_AVATAR, post.getUserAvatar());
			
			obj.add(json);
		}
		
		return obj;
	}
	
	public static JSONArray placeListToJSON(List<Place> placeList){
		// TODO if JSON value is null, don't put into the JSON object
		// set result data, return postArray
		JSONArray obj = new JSONArray();
		for (Place place : placeList){
			JSONObject json = new JSONObject();
			json.put(ServiceConstant.PARA_CREATE_USERID, place.getCreateUserId());
			json.put(ServiceConstant.PARA_PLACEID, place.getPlaceId());
			json.put(ServiceConstant.PARA_LONGTITUDE, place.getLongitude());
			json.put(ServiceConstant.PARA_LATITUDE, place.getLatitude());
			json.put(ServiceConstant.PARA_NAME, place.getName());
			json.put(ServiceConstant.PARA_DESC, place.getDesc());
			json.put(ServiceConstant.PARA_RADIUS, place.getRadius());
			json.put(ServiceConstant.PARA_POSTTYPE, place.getPostType());
			json.put(ServiceConstant.PARA_CREATE_DATE, place.getCreateDate());			
			obj.add(json);
		}		
		return obj;
	}

	public static JSONObject userToJSON(User user) {
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_USERID, user.getUserId());
		obj.put(ServiceConstant.PARA_NICKNAME, user.getNickName());
		obj.put(ServiceConstant.PARA_LOGINID, user.getLoginId());
		obj.put(ServiceConstant.PARA_SINA_ACCESS_TOKEN, user.getSinaAccessToken());
		obj.put(ServiceConstant.PARA_SINA_ACCESS_TOKEN_SECRET, user.getSinaAccessTokenSecret());
		obj.put(ServiceConstant.PARA_QQ_ACCESS_TOKEN, user.getQQAccessToken());
		obj.put(ServiceConstant.PARA_QQ_ACCESS_TOKEN_SECRET, user.getQQAccessTokenSecret());
		return obj;
	}

	public static Object messageListToJSON(List<Message> messageList) {
		// TODO Auto-generated method stub
		JSONArray obj = new JSONArray();
		for (Message message : messageList){
			JSONObject json = new JSONObject();
			json.put(ServiceConstant.PARA_MESSAGE_ID, message.getMessageId());
			json.put(ServiceConstant.PARA_USERID, message.getFromUserId());
			json.put(ServiceConstant.PARA_TO_USERID, message.getToUserId());
			json.put(ServiceConstant.PARA_MESSAGETEXT, message.getMessageContent());
			json.put(ServiceConstant.PARA_CREATE_DATE, message.getCreateDate());				
			obj.add(json);
		}
		
		return obj;
	}

}
