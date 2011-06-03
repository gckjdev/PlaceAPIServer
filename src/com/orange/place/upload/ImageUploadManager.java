package com.orange.place.upload;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.orange.place.constant.ErrorCode;

public class ImageUploadManager extends AbstractUploadManager {

		
	
	@Override
	public String uploadFile(HttpServletRequest request,
			HttpServletResponse response) {
		
		String filepath = uploadFile(request);
		return filepath;
		
		// TODO Auto-generated method stub
//		List items = getFileItems(request);
//		Iterator itr = items.iterator();
//		while (itr.hasNext()) {
//			FileItem fileItem = (FileItem) itr.next();
//			if (!fileItem.isFormField()) {
//				if (fileItem.getName().length() > 0) {
//					File tempFile = new File(System.currentTimeMillis()
//							+ fileItem.getName());// java.io.file
//					log.info("<uploadFile> get file name "+fileItem.getName()+", write to "+tempFile.getAbsolutePath());
//					try {
//						fileItem.write(tempFile);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						log.info("<uploadFile> get file name "+fileItem.getName()+", write to "+tempFile.getAbsolutePath()+" but catch exception="+e.toString());
//						resultCode = ErrorCode.ERROR_UPLOAD_FILE;
//						return;
//					}
//					setFilePath(tempFile.getAbsolutePath());
//					setFileSize(fileItem.getSize());
//					setFileType(fileItem.getContentType());
//					log.info("<uploadFile> get file name "+fileItem.getName()+", write to "+tempFile.getAbsolutePath());
//				} else {
//					log.info("<uploadFile> but file name is empty");
//				}
//			}
//			else{
//				log.info("<uploadFile> other form field="+fileItem.getFieldName()+",value="+fileItem.getString());
//			}
//		}

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
