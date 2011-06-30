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
	String gender;

	// post & return
	String avatar;	
	boolean hasAvatar = false;

	@Override
	public String toString() {
		return "UpdateUserService [appId=" + appId + ", eMail=" + eMail
				+ ", gender=" + gender + ", mobile=" + mobile + ", nickName="
				+ nickName + ", password=" + password + ", userId=" + userId
				+ "]";
	}

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
				nickName, password, avatar, gender);
		
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
		log.info(toString());
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
		gender = request.getParameter(ServiceConstant.PARA_GENDER);

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
