
package com.orange.place.api;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.place.api.service.ServiceHandler;

public class PlaceAPIServer extends AbstractHandler
{
	public static final Logger log = Logger.getLogger(PlaceAPIServer.class.getName());
	
	public static AtomicInteger uniqueId = new AtomicInteger();
	
	public static final String SPRING_CONTEXT_FILE = "classpath:/com/orange/place/api/applicationContext.xml";
	
	public static final String LOG4J_FLE = "classpath:/log4j.properties";
	

	public int getPort(){		
		return 8000;
	}

	public void readConfig(String filename){

		   InputStream inputStream = null;		   
		   try {
			   inputStream = new FileInputStream(filename);
		   } catch (FileNotFoundException e) {
			   log.info("configuration file "+filename+"not found exception");
			   e.printStackTrace();
		   }
		   Properties p = new Properties();   
		   try {   
			   p.load(inputStream);   
		   } catch (IOException e1) {   
			   log.info("read configuration file exception");
			   e1.printStackTrace();   
		   }   		   		   
	}
		
	public static void initSpringContext(String... context){
		try {
			new ClassPathXmlApplicationContext(
					context );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void initLog4j(){
		log.info("Initializing log4j with: " + LOG4J_FLE);
		//PropertyConfigurator.configure(LOG4J_FLE);
	}
	
	public void printLog(long requestId, String logString){
		Date date = new Date();
		log.info(String.format("%s [%010d] %s", date.toString(), requestId, logString));
	}
	
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("application/json;charset=utf-8");
        baseRequest.setHandled(true);
        
        int requestId = uniqueId.incrementAndGet();                        
		try{			
			
	        //response.setStatus(responseCode);	   
	        ServiceHandler.getServiceHandler().handlRequest(request, response);
                       
		} catch (Exception e){
			printLog(requestId, "catch Exception="+e.toString());
			e.printStackTrace();
		} finally {
		}		
    }
 
    public static void main(String[] args) throws Exception
    {
    	String VERSION_STRING = "Install Place Application, Version 1.0 - 20110301.001";
    	System.out.println(VERSION_STRING);
    	
    	String filename = "config.properties";
    	
    	if (args.length > 0){
    		filename = args[0];
    	}
    	
    	PlaceAPIServer handler = new PlaceAPIServer();    	
    	//handler.readConfig(filename);
    	
    	//init the spring context
    	String[] springFiles = new String[]{SPRING_CONTEXT_FILE};
    	initSpringContext(springFiles);
    	
        Server server = new Server(handler.getPort());
        server.setHandler(handler);
        QueuedThreadPool threadPool = new QueuedThreadPool();  
        threadPool.setMaxThreads(100);
        threadPool.setMinThreads(25);
        server.setThreadPool(threadPool);  
        
        server.setStopAtShutdown(true);
        server.start();
        server.join();
    }
}
