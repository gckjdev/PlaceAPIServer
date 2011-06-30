package orange.place.api.systemtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import junit.framework.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.orange.place.constant.ServiceConstant;

public class SystemTestHelper {
	public static JSONObject sendHttpGetRequest(String url) {
		try {
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(url);
			int statusCode = httpClient.executeMethod(getMethod);

			String response = readStringFromStream(getMethod
					.getResponseBodyAsStream());
			getMethod.releaseConnection();

			JSONObject json = JSONObject.fromObject(response);
			Assert.assertEquals(statusCode, 200);
			Assert.assertTrue(json != null);
			Assert.assertTrue(json.getInt(ServiceConstant.RET_CODE) == 0);

			return json.getJSONObject(ServiceConstant.RET_DATA);
		} catch (HttpException e) {
			Assert.fail();
			return null;
		} catch (IOException e) {
			Assert.fail();
			return null;
		}

	}

	public static JSONArray sendHttpGetRequestGetArray(String url) {
		try {
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(url);
			int statusCode = httpClient.executeMethod(getMethod);

			String response = readStringFromStream(getMethod
					.getResponseBodyAsStream());
			getMethod.releaseConnection();

			JSONObject json = JSONObject.fromObject(response);
			Assert.assertEquals(statusCode, 200);
			Assert.assertTrue(json != null);
			Assert.assertTrue(json.getInt(ServiceConstant.RET_CODE) == 0);

			return json.getJSONArray(ServiceConstant.RET_DATA);
		} catch (HttpException e) {
			Assert.fail();
			return null;
		} catch (IOException e) {
			Assert.fail();
			return null;
		}

	}

	public static String registerUser(String serverURL, String loginId,
			String deviceId, String nickName) {

		// send request
		String url = String
				.format("%s/api/i?&m=reg&lid=%s&lty=1&did=%s&dm=iPhone&dos=1&cc=US&lang=en&app=PLACE&dto=&nn=%s&av=&at=&ats=&pro=0&ci=0&lo=&ge=&bi=&sn=&sd=&qn=&qd=&ts=1307860672&mac=aQnLk0uhiXPDDIji5DIyLw%%3D%%3D&v=0090",
						serverURL, loginId, deviceId, nickName);
		JSONObject data = sendHttpGetRequest(url);

		// get user Id, must be not null
		String returnUserId = data.getString(ServiceConstant.PARA_USERID);
		Assert.assertTrue(returnUserId != null);

		System.out.println("create user successfully. " + returnUserId);
		return returnUserId;
	}

	public static String createPlace(String serverURL, String uid,
			String placeName) {
		// send request
		String url = String
				.format("%s/api/i?&m=cpl&uid=%s&app=PLACE&lo=113.273073&lat=23.129666&ra=500&pt=0&na=%s&de=new_place_desc",
						serverURL, uid, placeName);
		JSONObject data = sendHttpGetRequest(url);

		// get place Id, must be not null
		String id = data.getString(ServiceConstant.PARA_PLACEID);
		Assert.assertTrue(id != null);

		System.out.println("create place successfully. " + id);
		return id;

	}

	public static String createPost(String serverURL, String uid, String pid,
			String content) {
		// send request
		String url = String
				.format("%s/api/i?&m=cp&uid=%s&app=PLACE&ct=1&t=%s&lat=23.129666&lo=113.273073&ula=153.220001&ulo=113.110001&ss=0&pid=%s",
						serverURL, uid, content, pid);
		JSONObject data = sendHttpGetRequest(url);

		// get place Id, must be not null
		String id = data.getString(ServiceConstant.PARA_POSTID);
		Assert.assertTrue(id != null);

		System.out.println("create post successfully. " + id);
		return id;
	}

	public static String createReply(String serverURL, String uid, String pid,
			String rpi, String content) {
		// send request
		String url = String
				.format("%s/api/i?&m=cp&uid=%s&app=PLACE&ct=1&t=%s&lat=23.129666&lo=113.273073&ula=153.220001&ulo=113.110001&ss=0&pid=%s&rpi=%s",
						serverURL, uid, content, pid, rpi);
		JSONObject data = sendHttpGetRequest(url);

		// get place Id, must be not null
		String id = data.getString(ServiceConstant.PARA_POSTID);
		Assert.assertTrue(id != null);

		System.out.println("create reply successfully. " + id + " for post :"
				+ rpi);
		return id;
	}

	public static String getNearByPost(String serverURL, String uid) {
		// send request
		// TODO: remove ula, ulo?
		String url = String
				.format("%s/api/i?&m=gne&uid=%s&app=PLACE&lat=23.129666&lo=113.273073&ula=153.220001&ulo=113.110001",
						serverURL, uid);
		JSONArray list = sendHttpGetRequestGetArray(url);

		// get place Id, must be not null
		JSONObject data = list.getJSONObject(0);
		String id = data.getString(ServiceConstant.PARA_POSTID);
		Assert.assertTrue(id != null);

		System.out.println("get neary by post.");
		return id;
	}

	public static String generateValue() {
		long time = System.currentTimeMillis();
		long random_id = new Random().nextLong();
		return "_" + String.valueOf(time) + "_" + String.valueOf(random_id);
	}

	public boolean getResultCode(String jsonString) {
		JSONObject json = JSONObject.fromObject(jsonString);
		if (json == null) {
			return false;
		}

		return (json.getInt(ServiceConstant.RET_CODE) == 0);
	}

	public static JSONObject getResultData(String jsonString) {
		JSONObject json = JSONObject.fromObject(jsonString);
		if (json == null) {
			return null;
		}

		return json.getJSONObject(ServiceConstant.RET_DATA);
	}

	public static String readStringFromStream(InputStream inputStream)
			throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuffer stringBuffer = new StringBuffer();
		String tempbf;
		while ((tempbf = br.readLine()) != null) {
			stringBuffer.append(tempbf);
		}

		return stringBuffer.toString();
	}
}
