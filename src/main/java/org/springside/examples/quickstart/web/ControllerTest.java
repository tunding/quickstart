package org.springside.examples.quickstart.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.service.account.AccountService;


/**
 * @author tunding
 * 测试的controller文件
 */
@Controller
@RequestMapping(value="/test")
public class ControllerTest {
	
	private static Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	@ResponseBody
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public void login(){
		System.out.println("123456");
		logger.info("abcdefg");
	}
}
