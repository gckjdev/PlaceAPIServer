package com.orange.place.upload;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.ImageUtil;

public class ImageUploadManager extends AbstractUploadManager {

	public String uploadImageWithCompression(HttpServletRequest request) {
		String filePath = uploadFile(request);
		String localPath = getLocalFilePath();
		File imageFile = new File(localPath);
		if (!imageFile.exists()){
			log.info("Error:Could not find the upload image!");
			return null;
		}
		if (!imageFile.exists()){
			log.info("Error:Could not find the upload image!");
			return null;
		}
		File smallImage = ImageUtil.setImageSize(imageFile, 100);
		if (smallImage == null || smallImage.exists()){
			log.info("Error:Could not compress the upload image!");
			return null;
		}

		System.out.println(smallImage.getAbsolutePath());
		return filePath;
	}

	public String uploadImage(HttpServletRequest request) {
		return uploadFile(request);
	}
	

	@Override
	public int getResultCode() {
		// TODO Auto-generated method stub
		return resultCode;
	}
}
