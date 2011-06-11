package com.orange.place.api.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;
import com.orange.place.manager.UserManager;
import com.orange.place.upload.ImageUploadManager;

public class UpdateUserService extends CommonService {

	String userId;
	String appId;

	// optional
	String mobile;
	String eMail;
	String nickName;
	String password;

	// post & return
	String avatar;	
	boolean hasAvatar = false;

	@Override
	public void handleData() {
		
		if(!UserManager.isUserExist(cassandraClient,userId)){
			log.info("fail to update user, userId="
					+ userId + ". Reason:user not found!");
			resultCode = ErrorCode.ERROR_USERID_NOT_FOUND;
			return;
		}
		
		// POST
		if (hasAvatar){
			ImageUploadManager imageUploadManager = new ImageUploadManager();
			imageUploadManager.uploadImage(request);
			avatar = imageUploadManager.getFilePath();
		}

		UserManager.updateUser(cassandraClient, userId, mobile, eMail,
				nickName, password, avatar);
		
		// return avatar
		if (avatar != null){
			JSONObject json = new JSONObject();
			json.put(ServiceConstant.PARA_AVATAR, avatar);
			resultData = json;
		}
	}

	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printData() {
		// TODO Auto-generated method stub
		log.info(String.format("userId=%s, password=%s, appId=%s, mobile=%s, avatar=%s, "
				+ "eMail=%s, nickName=%s", userId, password, appId, mobile, avatar,
				eMail, nickName, avatar));
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		userId = request.getParameter(ServiceConstant.PARA_USERID);
		appId = request.getParameter(ServiceConstant.PARA_APPID);

		nickName = request.getParameter(ServiceConstant.PARA_NICKNAME);
		password = request.getParameter(ServiceConstant.PARA_PASSWORD);
		mobile = request.getParameter(ServiceConstant.PARA_MOBILE);
		eMail = request.getParameter(ServiceConstant.PARA_EMAIL);

		String hasAvatarPara = request.getParameter(ServiceConstant.PARA_AVATAR);		
		if (hasAvatarPara != null && hasAvatarPara.length() > 0){
			hasAvatar = true;					
		}		

		if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
				ErrorCode.ERROR_PARAMETER_USERID_NULL))
			return false;

		if (!check(appId, ErrorCode.ERROR_PARAMETER_APPID_EMPTY,
				ErrorCode.ERROR_PARAMETER_APPID_NULL))
			return false;

		return true;
	}
}
