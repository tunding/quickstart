package org.springside.fi.web.third;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Runner;
import org.springside.fi.service.running.RunnerService;
import org.springside.fi.web.running.BaseController;

/**
 * @author wangzhichao
 * 调用第三方融云解决即时通讯
 */
@Controller
@RequestMapping(value="/rongcloud")
public class RongCloudController extends BaseController{
	
	@Autowired
	private RunnerService runnerService;
	
	@ResponseBody
	@RequestMapping(value="/gettocken")
	public String getToken(){
		long id = getCurrentUserId();
		Runner runner = runnerService.getRunner(id);
		String uuid = runner.getUuid();
		String loginName = runner.getLoginName();
		return runnerService.getToken(uuid, loginName);
	}
	
}
