package com.orange.place.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.orange.place.api.PlaceAPIServer;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;

public abstract class AbstractUploadManager {
	
	public static final Logger log = Logger.getLogger(PlaceAPIServer.class
			.getName());
	
	public int resultCode = ErrorCode.ERROR_SUCCESS;
	protected String fileType = null;
	protected String filePath = null;
	protected long fileSize = 0;
	public abstract String uploadFile(HttpServletRequest request,
			HttpServletResponse response);

	public abstract int getResultCode();
/*	public abstract String getFilePath();
	public abstract String getFileType();
	public abstract String getFileSzie();*/
	
	protected String uploadFile(HttpServletRequest request) {		
		List items = null;
		String filepath = null;
		try {
			request.setCharacterEncoding("UTF-8");
			ServletFileUpload upload = new ServletFileUpload();
			upload.setProgressListener(progressListener);

			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
			    String name = item.getFieldName();
			    InputStream stream = item.openStream();
			    if (item.isFormField()) {
			        log.info("<uploadFile> Form field " + name + " with value "
			            + Streams.asString(stream) + " detected.");
			    } else {
			    	log.info("<uploadFile> File field " + name + " with file name "
			            + item.getName() + " detected.");
			        
			    	String filename = "";
			    	if (item.getName() != null){
			    		filename = item.getName();
			    	}
			    	
			    	// Process the input stream
			    	// TODO the code below could be better
			        ArrayList<Integer> byteArray = new ArrayList<Integer>();
			        int tempByte;
			        do {
			        	tempByte = stream.read();
			        	byteArray.add(tempByte);
			        } while(tempByte != -1);
			        stream.close();
			        
			    	int size = byteArray.size();
			    	log.info("<uploadFile> total "+size+" bytes read");

			    	byteArray.remove(size-1);
			        byte[] bytes = new byte[size];
			        int i = 0;
			        for(Integer tByte : byteArray) {
			         bytes[i++] = tByte.byteValue();
			        }
			        
			        // write to file			        
			    	filepath = ServiceConstant.FILE_IMAGE_PATH + TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString() + "-" + filename;
			    	log.info("<uploadFile> write to file="+filepath);
			        FileOutputStream fw = new FileOutputStream(filepath);
			        fw.write(bytes);
			        fw.close();
			    }
			}
//			items = upload.parseRequest(request);
		} catch (Exception e) {
			resultCode = ErrorCode.ERROR_UPLOAD_FILE;
	    	log.info("<uploadFile> filepaht="+filepath+", but catch exception="+e.toString());	    	
			return null;
		} 
		return filepath;
	}

	
	//Create a progress listener
	ProgressListener progressListener = new ProgressListener(){
	   public void update(long pBytesRead, long pContentLength, int pItems) {
	       System.out.println("We are currently reading item " + pItems);
	       if (pContentLength == -1) {
	           System.out.println("So far, " + pBytesRead + " bytes have been read.");
	       } else {
	           System.out.println("So far, " + pBytesRead + " of " + pContentLength
	                              + " bytes have been read.");
	       }
	   }
	};
	
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
