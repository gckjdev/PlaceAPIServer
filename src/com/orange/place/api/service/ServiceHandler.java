package com.orange.place.api.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orange.common.cassandra.CassandraClient;
import com.orange.place.api.PlaceAPIServer;
import com.orange.place.constant.DBConstants;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constant.ServiceConstant;

public class ServiceHandler {

	public static final CassandraClient cassandraClient = new CassandraClient(DBConstants.SERVER, DBConstants.KEYSPACE);
	
	private static final Logger log = Logger.getLogger(PlaceAPIServer.class.getName());
	
	public static ServiceHandler getServiceHandler(){
		ServiceHandler handler = new ServiceHandler();
		return handler;
	}
		
	public void handlRequest(HttpServletRequest request, HttpServletResponse response){
		
		printRequest(request);			
		
		String method = request.getParameter(ServiceConstant.METHOD);		
		CommonServiceObject obj = CommonServiceObject.createServiceObjectByMethod(method);		
		if (obj == null){
			sendResponseByErrorCode(response, ErrorCode.ERROR_PARA_METHOD_NOT_FOUND);
			return;			
		}
		
		obj.setCassandraClient(cassandraClient);
		
		if (!obj.validateSecurity(request)){
			sendResponseByErrorCode(response, ErrorCode.ERROR_INVALID_SECURITY);
			return;
		}
		
		try{
			
			// parse request parameters
			if (!obj.setDataFromRequest(request)){	
				sendResponseByErrorCode(response, obj.resultCode);
				return;
			}		
			obj.printData();
			
			// handle request
			obj.handleData();
		}
		finally{
		}
		
		String responseData = obj.getResponseString();
		
		// send back response
		sendResponse(response, responseData);
		
		
	}
	
	
	
	void printRequest(HttpServletRequest request){
		log.info("[RECV] request = " + request.getQueryString());
	}
	
	void printResponse(HttpServletResponse reponse, String responseData){
		log.info("[SEND] response data = " + responseData);
	}
	
	void sendResponse(HttpServletResponse response, String responseData){			
		printResponse(response, responseData);
		response.setContentType("application/json; charset=utf-8");
		try {
			response.getWriter().write(responseData);
			response.getWriter().flush();		
		} catch (IOException e) {
			log.severe("sendResponse, catch exception="+e.toString());
		}
	}	
	
	void sendResponseByErrorCode(HttpServletResponse response, int errorCode){
		String resultString = ErrorCode.getJSONByErrorCode(errorCode);
		sendResponse(response, resultString);
	}
}


