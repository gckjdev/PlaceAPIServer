package com.orange.place.analysis;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.common.context.SpringContextUtil;
import com.orange.place.analysis.domain.Request;
import com.orange.place.dao.IdGenerator;

public class RequestHandlerTest {

	public static final String SPRING_CONTEXT_FILE = "classpath:/com/orange/place/api/applicationContext.xml";

	private static RequestHandler requestHandler;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			new ClassPathXmlApplicationContext(
					new String[] { SPRING_CONTEXT_FILE });
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		requestHandler = SpringContextUtil.getBean("requestHandler");
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() {
		String userId = IdGenerator.generateId();
		Request request = new Request();
		request.setLatitude(120.345);
		request.setLatitude(-5.345);
		request.setRadius(200);
		
		request.setUserId(userId);
		requestHandler.execute(request);
		
	}

}
