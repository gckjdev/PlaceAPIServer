package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.analysis.domain.AnalysisLogContent;
import com.orange.place.analysis.domain.PostType;
import com.orange.place.analysis.log.AnalysisLogUtil;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.dao.Post;
import com.orange.place.manager.PostManager;
import com.orange.place.manager.UserManager;
import com.orange.place.upload.ImageUploadManager;

public class CreatePostService extends CommonService {

	String userId;
	String appId;
	String placeId;
	String longitude;
	String latitude;
	String userLatitude;
	String userLongitude;
	String textContent;
	String contentType;
	String syncSNS;
	// Source Post
	String srcPostId;
	// Empty -> New, Create
	String replyPostId;

	@Override
	public void handleData() {

		// get user nickname for return
		String nickName = UserManager.getUserNickName(cassandraClient, userId);
		if (nickName == null) {
			log.info("fail to get user nick name, userId=" + userId);
			resultCode = ErrorCode.ERROR_USER_GET_NICKNAME;
			return;
		}

		// upload image content if needed
		int contenTypeInt = Integer.parseInt(contentType);
		String imageURL = null;
		if (contenTypeInt != DBConstants.CONTENT_TYPE_TEXT) {
			// AbstractUploadManager uploadManager = null;
			switch (contenTypeInt) {
			case DBConstants.CONTENT_TYPE_TEXT_PHOTO: {
				ImageUploadManager uploadManager = new ImageUploadManager();
				imageURL = uploadManager.uploadImageWithCompression(request);
				resultCode = uploadManager.getResultCode();
				log.info("<createPost> save image URL=" + imageURL);
			}
				break;
			default:
				break;
			}
		}

		// check upload content result
		if (resultCode != ErrorCode.ERROR_SUCCESS) {
			log.info("<createPost> fail to upload/save image, result code="
					+ resultCode);
			return;
		}

		// create post in DB
		Post post = PostManager.createPost(cassandraClient, userId, appId,
				placeId, longitude, latitude, userLongitude, userLatitude,
				textContent, contentType, srcPostId, replyPostId, imageURL);
		if (post == null) {
			log.info("fail to create post, placeId=" + placeId + ", userId="
					+ userId);
			resultCode = ErrorCode.ERROR_CREATE_POST;
			return;
		}

		String postId = post.getPostId();
		String createDate = post.getCreateDate();
		// add post into index
		PostManager.createPlacePostIndex(cassandraClient, placeId, postId);
		PostManager.createUserPostIndex(cassandraClient, userId, postId);
		PostManager.createUserViewPostIndex(cassandraClient, placeId, postId,
				createDate);
		PostManager.createAppIdPostIndex(cassandraClient, postId, appId);
		// TODO: test needed
		// create index for geohash , hash : postId, createDate
		PostManager.createPostLocationIndex(cassandraClient, postId,
				createDate, latitude, longitude);

		// create post related post index
		if (srcPostId == null || srcPostId.length() == 0) {
			srcPostId = postId;
		}
		PostManager.createPostRelatedPostIndex(cassandraClient, postId,
				srcPostId);

		// if there is a reply post, add into user ME post index
		if (replyPostId != null && replyPostId.length() > 0) {
			Post replyPost = PostManager.getPostById(cassandraClient,
					replyPostId);
			if (replyPost != null) {
				PostManager.createUserMePostIndex(cassandraClient, replyPost
						.getUserId(), postId);
			} else {
				log
						.warning("<createPost> reply post doens't exist! reply post Id="
								+ replyPostId);
			}
			// log reply here.
			AnalysisLogContent content = new AnalysisLogContent().setLatitude(
					latitude).setLongitude(longitude).setPlaceId(placeId)
					.setPostId(replyPostId).setPostType(PostType.REPLY)
					.setUserId(userId);
			AnalysisLogUtil.log(content);
		}

		// TODO: test, log create post here.
		AnalysisLogContent content = new AnalysisLogContent().setLatitude(
				latitude).setLongitude(longitude).setPlaceId(placeId)
				.setPostId(postId).setPostType(PostType.CREATE).setUserId(
						userId);
		AnalysisLogUtil.log(content);

		// set result data, return postId, nick name, and create date
		JSONObject obj = new JSONObject();
		obj.put(ServiceConstant.PARA_POSTID, postId);
		obj.put(ServiceConstant.PARA_NICKNAME, nickName);
		obj.put(ServiceConstant.PARA_CREATE_DATE, createDate);
		resultData = obj;
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printData() {
		// TODO Auto-generated method stub
		log
				.info(String
						.format(
								"userId=%s, appId=%s, placeId=%s,"
										+ "longitude=%s, latitude=%s, userLongitude=%s, userLatitude=%s,"
										+ "textContent=%s, contentType=%s, syncSNS=%s",
								userId, appId, placeId, longitude, latitude,
								userLongitude, userLatitude, textContent,
								contentType, syncSNS));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		appId = request.getParameter(ServiceConstant.PARA_APPID);
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		placeId = request.getParameter(ServiceConstant.PARA_PLACEID);
		longitude = request.getParameter(ServiceConstant.PARA_LONGTITUDE);
		latitude = request.getParameter(ServiceConstant.PARA_LATITUDE);
		userLongitude = request
				.getParameter(ServiceConstant.PARA_USER_LONGITUDE);
		userLatitude = request.getParameter(ServiceConstant.PARA_USER_LATITUDE);
		textContent = request.getParameter(ServiceConstant.PARA_TEXT_CONTENT);
		contentType = request.getParameter(ServiceConstant.PARA_CONTENT_TYPE);
		syncSNS = request.getParameter(ServiceConstant.PARA_SYNC_SNS);
		srcPostId = request.getParameter(ServiceConstant.PARA_SRC_POSTID);
		replyPostId = request.getParameter(ServiceConstant.PARA_REPLY_POSTID);

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(placeId, ErrorCode.ERROR_PARAMETER_PLACEID_EMPTY,
				ErrorCode.ERROR_PARAMETER_PLACEID_NULL))
			return false;

		if (!check(longitude, ErrorCode.ERROR_PARAMETER_LONGITUDE_EMPTY,
				ErrorCode.ERROR_PARAMETER_LONGITUDE_NULL))
			return false;

		if (!check(latitude, ErrorCode.ERROR_PARAMETER_LATITUDE_EMPTY,
				ErrorCode.ERROR_PARAMETER_LATITUDE_NULL))
			return false;

		if (!check(userLongitude,
				ErrorCode.ERROR_PARAMETER_USER_LONGITUDE_EMPTY,
				ErrorCode.ERROR_PARAMETER_USER_LONGITUDE_NULL))
			return false;

		if (!check(userLatitude, ErrorCode.ERROR_PARAMETER_USER_LATITUDE_EMPTY,
				ErrorCode.ERROR_PARAMETER_USER_LATITUDE_NULL))
			return false;

		// no content check due to may only contain image
		// if (!check(textContent, ErrorCode.ERROR_PARAMETER_TEXTCONTENT_EMPTY,
		// ErrorCode.ERROR_PARAMETER_TEXTCONTENT_NULL))
		// return false;

		if (!check(contentType, ErrorCode.ERROR_PARAMETER_CONTENTTYPE_EMPTY,
				ErrorCode.ERROR_PARAMETER_CONTENTTYPE_NULL))
			return false;

		// TODO need to check latitude, longitude, contentType, syncSNS is valid
		// decimal/number
		return true;
	}

}
