package com.orange.place.upload;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.orange.place.constant.ErrorCode;

public abstract class AbstractUploadManager {
	public int resultCode = ErrorCode.ERROR_SUCCESS;
	protected String fileType = null;
	protected String filePath = null;
	protected long fileSize = 0;
	public abstract void fileHandle(HttpServletRequest request,
			HttpServletResponse response);

	public abstract int getResultCode();
/*	public abstract String getFilePath();
	public abstract String getFileType();
	public abstract String getFileSzie();*/
	
	protected List getFileItems(HttpServletRequest request) {		
		List items = null;
		try {
			request.setCharacterEncoding("UTF-8");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			items = upload.parseRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
			resultCode = ErrorCode.ERROR_UPLOAD_FILE;
			return null;
		}
		return items;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
}
