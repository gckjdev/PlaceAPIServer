package com.orange.place.upload;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.eclipse.jetty.util.log.Log;

import com.orange.place.constant.ErrorCode;

public class ImageUploadManager extends AbstractUploadManager {

	@Override
	public void fileHandle(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		List items = getFileItems(request);
		Iterator itr = items.iterator();
		while (itr.hasNext()) {
			FileItem fileItem = (FileItem) itr.next();
			if (!fileItem.isFormField()) {
				if (fileItem.getName().length() > 0) {
					File tempFile = new File(System.currentTimeMillis()
							+ fileItem.getName());// java.io.file
					try {
						fileItem.write(tempFile);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						resultCode = ErrorCode.ERROR_UPLOAD_FILE;
						return;
					}
					setFilePath(tempFile.getAbsolutePath());
					setFileSize(fileItem.getSize());
					setFileType(fileItem.getContentType());
					Log.info("upload done!");
				} else {
					Log.info("upload fail!");
				}
			}
		}

	}

	@Override
	public int getResultCode() {
		// TODO Auto-generated method stub
		return resultCode;
	}

/*	public String getFilePath() {
		return null;
	}*/
	
/*	public abstract String getFileType(){
		
	}
	public abstract String getFileSzie(){
		
	}*/
}
