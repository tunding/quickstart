package org.springside.fi.service.third;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

@ContextConfiguration(locations = {"/applicationContext.xml"})
@DirtiesContext
@ActiveProfiles("development")
public class RongCloudServiceTest extends SpringTransactionalTestCase{
	
	@Autowired
	private RongCloudService rongcloud;
	
	@Test
	public void RongCloudServiceTest(){
		try {
			rongcloud.getToken("c362342ab6de44d0b5ec91234a54e585", "wangzhichao");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
