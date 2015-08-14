package org.springside.fi.web.account;

import java.util.HashMap;

import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Runner;
import org.springside.fi.service.account.AccountService;
import org.springside.fi.service.account.ShiroDbRealm.ShiroUser;
import org.springside.modules.mapper.JsonMapper;

/**
 * 用户修改自己资料的Controller.
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

	@Autowired
	private AccountService accountService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

	@RequestMapping(method = RequestMethod.GET)
	public String profileForm(Model model) {
		Long id = getCurrentUserId();
		model.addAttribute("user", accountService.getUser(id));
		return "account/profile";
	}
	
	@ResponseBody
	//@RequestMapping(method = RequestMethod.GET)
	@RequestMapping(value="/details")
	public String updateForm(Model model) {
		Long id = getCurrentUserId();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", "success");
		map.put("data", accountService.getUser(id));
		return jsonMapper.toJson(map);
	}

	@ResponseBody
	//@RequestMapping(method = RequestMethod.POST)
	@RequestMapping(value="/save")
	public String update(@Valid @ModelAttribute("user") Runner user) {
		accountService.updateUser(user);
		updateCurrentUserName(user.getName());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", "success");
		return jsonMapper.toJson(map);
		//return "redirect:/";
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("user", accountService.getUser(id));
		}
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	/**
	 * 更新Shiro中当前用户的用户名.
	 */
	private void updateCurrentUserName(String userName) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.name = userName;
	}
}
