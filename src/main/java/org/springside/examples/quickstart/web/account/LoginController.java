package org.springside.examples.quickstart.web.account;

import java.util.HashMap;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.service.account.AccountService;
import org.springside.modules.mapper.JsonMapper;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {
	@Autowired
	private AccountService accountService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	protected HashMap<String, Object> map = new HashMap<String, Object>();

	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		return "account/login";
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
		System.out.println("fail method");
		map.put("result", "failed");
		if (accountService.findUserByLoginName(userName) == null) {
			map.put("data", "用户名不存在");
		} else {
			map.put("data", "密码不正确");
		}
		return jsonMapper.toJson(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/success", method = RequestMethod.GET)
	public String success() {
		System.out.println("success method");
		map.put("result", "success");
		return jsonMapper.toJson(map);
	}
	
	@RequestMapping(value="/menu", method = RequestMethod.GET)
	public String test() {
		return "account/menu";
	}

}
