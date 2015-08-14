package org.springside.fi.web.third;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Runner;
import org.springside.fi.service.running.RunnerService;
import org.springside.fi.service.third.RongCloudService;
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
	@Autowired
	private RongCloudService rongCloudService;
	
	@ResponseBody
	@RequestMapping(value="/gettoken")
	public String getToken(){
		long id = getCurrentUserId();
		Runner runner = runnerService.getRunner(id);
		String uuid = runner.getUuid();
		String loginName = runner.getLoginName();
		String portraitUri = runner.getPortraitUri();
		try {
			String res = rongCloudService.getToken(uuid, loginName, portraitUri);
			runnerService.saveCloudToken(id, res);
			return res;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
