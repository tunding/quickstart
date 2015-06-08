package org.springside.fi.web.running;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Activity;
import org.springside.fi.entity.Runner;
import org.springside.fi.service.account.AccountService;
import org.springside.fi.service.running.ActivityService;
import org.springside.fi.service.running.RunnerService;
import org.springside.modules.mapper.JsonMapper;


@Controller
@RequestMapping(value="/activity/info")
public class ActivityController extends BaseController{
	@Autowired
	private ActivityService activityService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private RunnerService runnerService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@ResponseBody
	@RequestMapping(value="/saveactivity")
	public String saveActivity(@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "address") String address,
			@RequestParam(value = "time") String time,
			@RequestParam(value = "info") String info,
			@RequestParam(value = "kilometer") int kilometer){
		System.out.println("saveractivity");
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		Runner runner = runnerService.getRunner(user_id);
		String uuid = runner.getUuid();
		try{
			activityService.saveActivity(uuid, longitude, latitude, address, time, info, kilometer);
			map.put("result", "success");
			map.put("data", "");
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/getactivity")
	public String getActivity(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		Runner runner = runnerService.getRunner(user_id);
		String uuid = runner.getUuid();
		try{
			Activity activity = activityService.getActivity(uuid);
			map.put("result", "success");
			map.put("data", activity);
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/gethistoryactivity")
	public String getHistoryActivity(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		Runner runner = runnerService.getRunner(user_id);
		String uuid = runner.getUuid();
		try{
			List<Activity> activities = activityService.getHistoryActivity(uuid);
			map.put("result", "success");
			map.put("data", activities);
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteactivity")
	public boolean delActivity(@RequestParam("actuuid") String actuuid){
		Long user_id = getCurrentUserId();
		Runner runner = runnerService.getRunner(user_id);
		String uuid = runner.getUuid();
		return activityService.delActivity(uuid, actuuid);
	}
	
	@ResponseBody
	@RequestMapping(value="/checkActivity")
	public boolean checkActivity(@RequestParam(value="loginName") String loginName){
		Runner runner = accountService.findUserByLoginName(loginName);
		String uuid = runner.getUuid();
		return activityService.findTodayActivityByUUID(uuid);
	}
}
