package orange.place.api.systemtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.Random;

import junit.framework.Assert;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.orange.place.constant.ServiceConstant;

public class BasicFlowTest {

	private static String serverURL;
	private static String userId;		// userId registered
	private static String placeId;		// placeId created
	private static String postId;		// postId for replied

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serverURL = "http://127.0.0.1:8000";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	public boolean getResultCode(String jsonString){
		JSONObject json = JSONObject.fromObject(jsonString);
		if (json == null){
			return false;
		}
		
		return (json.getInt(ServiceConstant.RET_CODE) == 0);	
	}
	
	public JSONObject getResultData(String jsonString){
		JSONObject json = JSONObject.fromObject(jsonString);
		if (json == null){
			return null;
		}
		
		return json.getJSONObject(ServiceConstant.RET_DATA);			
	}

	public String readStringFromStream(InputStream inputStream) throws IOException{
		
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));              
        StringBuffer stringBuffer = new StringBuffer();   
        String tempbf;   
        while((tempbf = br.readLine())!=null){   
        	stringBuffer.append(tempbf);   
        }   
        
        return stringBuffer.toString();
	}
	
	public JSONObject sendHttpGetRequest(String url) {
		try{
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod=new GetMethod(url);
			int statusCode = httpClient.executeMethod(getMethod);
			
			
			String response = readStringFromStream(getMethod.getResponseBodyAsStream());
			getMethod.releaseConnection();

			JSONObject json = JSONObject.fromObject(response); 
			Assert.assertEquals(statusCode, 200);
			Assert.assertTrue(json != null);
			Assert.assertTrue(json.getInt(ServiceConstant.RET_CODE) == 0);				
			
			return json.getJSONObject(ServiceConstant.RET_DATA);
		}
		catch (HttpException e){
			Assert.fail();
			return null;
		}
		catch (IOException e){
			Assert.fail();
			return null;
		}
		
	}

	@Before
	public void doSomething() throws Exception {
	}

	public String registerUser(String serverURL, String loginId, String deviceId, String nickName){
			
		// send request
		String url = String.format("%s/api/i?&m=reg&lid=%s&lty=1&did=%s&dm=iPhone&dos=1&cc=US&lang=en&app=PLACE&dto=&nn=%s&av=&at=&ats=&pro=0&ci=0&lo=&ge=&bi=&sn=&sd=&qn=&qd=&ts=1307860672&mac=aQnLk0uhiXPDDIji5DIyLw%%3D%%3D&v=0090"
				,serverURL, loginId, deviceId, nickName);		
		JSONObject data = sendHttpGetRequest(url);
		
		// get user Id, must be not null
		String returnUserId = data.getString(ServiceConstant.PARA_USERID);
		Assert.assertTrue(returnUserId != null);
		
		return returnUserId;
	}
	
	public String createPlace(String serverURL, String uid, String placeName){
		// send request
		String url = String.format("%s/api/i?&m=cpl&uid=%s&app=PLACE&lo=113.273073&lat=23.129666&ra=500&pt=0&na=%s&de=new_place_desc"
				,serverURL, uid, placeName);		
		JSONObject data = sendHttpGetRequest(url);
		
		// get place Id, must be not null
		String id = data.getString(ServiceConstant.PARA_PLACEID);
		Assert.assertTrue(id != null);
		
		return id;
		
	}
	
	public String createPost(String serverURL, String uid, String pid, String content){
		// send request
		String url = String.format("%s/api/i?&m=cp&uid=%s&app=PLACE&ct=1&t=%s&lat=23.129666&lo=113.273073&ula=153.220001&ulo=113.110001&ss=0&pid=%s"
				,serverURL, uid, content, pid);		
		JSONObject data = sendHttpGetRequest(url);
		
		// get place Id, must be not null
		String id = data.getString(ServiceConstant.PARA_POSTID);
		Assert.assertTrue(id != null);
		
		return id;		
	}
	
	public String generateValue(){
		long time = System.currentTimeMillis();		
		long random_id = new Random().nextLong(); 
		return "_" + String.valueOf(time) + "_" + String.valueOf(random_id);
	}
	
	@Test
	public void testRegisterUser() {
		
		userId = null;
		
		// send registration user request		
		String value = generateValue();
		String userName = "testLoginId" + value;
		String nickName = "testNickName" + value;
		String deviceId = "testDeviceId" + value;
			
		userId = registerUser(serverURL, userName, deviceId, nickName);				
	}
	
	@Test
	public void testCreatePlace() {
		
		placeId = null;
		Assert.assertTrue(userId != null);
		
		String value = generateValue();
		String placeName = "testPlaceName" + value;
		placeId = createPlace(serverURL, userId, placeName);
	}

	@Test
	public void testCreatePost() {
		
		postId = null;		
		Assert.assertTrue(userId != null && placeId != null);
		
		String value = generateValue();
		String postContent = "testPostContent" + value;
		postId = createPost(serverURL, userId, placeId, postContent);
	}

	@Test
	public void testReplyPost() {
		Assert.assertEquals(1, 1);
	}
	
	@Test
	public void testGetPlacePost() {
		Assert.assertEquals(1, 1);
	}
	
}
