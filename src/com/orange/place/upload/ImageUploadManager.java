package com.orange.place.upload;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.orange.common.utils.ImageUtil;
import com.orange.place.constant.ErrorCode;

public class ImageUploadManager extends AbstractUploadManager {

	private String thumbImage = null;

	public String uploadImageWithCompression(HttpServletRequest request) {
		String httpPath = uploadFile(request);
		if (httpPath == null) {
			resultCode = ErrorCode.ERROR_UPLOAD_FILE;
			return null;
		}

		String localPath = getLocalFilePath();
		File imageFile = new File(localPath);
		if (!imageFile.exists()) {
			resultCode = ErrorCode.ERROR_CREATE_THUNMB_FILEPATH;
			log
					.info("Error: <uploadImageWithCompression> Could not find the upload image! image file path="
							+ localPath);
			return null;
		}
		File smallImage = ImageUtil.setImageSize(imageFile, 100);
		if (smallImage == null || !smallImage.exists()) {
			resultCode = ErrorCode.ERROR_CREATE_THUNMB_FILE;
			log
					.info("Error: <uploadImageWithCompression> fail to create thumnail image, local path="
							+ localPath);
			return null;
		}

		setThumbImage(ImageUtil.getSmallImagePath(httpPath));
		log.info("<uploadImageWithCompression> create thumbnail image="
				+ getThumbImage());
		return httpPath;
	}

	public String uploadImage(HttpServletRequest request) {
		return uploadFile(request);
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}

	@Override
	public int getResultCode() {
		// TODO Auto-generated method stub
		return resultCode;
	}
}
