package org.springside.examples.quickstart.web.running;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.entity.Activity;
import org.springside.examples.quickstart.entity.Runner;
import org.springside.examples.quickstart.service.account.AccountService;
import org.springside.examples.quickstart.service.running.ActivityService;
import org.springside.modules.mapper.JsonMapper;


@Controller
@RequestMapping(value="/activity/info")
public class ActivityController {
	@Autowired
	private ActivityService activityService;
	@Autowired
	private AccountService accountService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@ResponseBody
	@RequestMapping(value="/saveactivity")
	public String saveActivity(@RequestParam(value="loginName") String loginName,
			@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "address") String address,
			@RequestParam(value = "time") String time,
			@RequestParam(value = "info") String info,
			@RequestParam(value = "kilometer") int kilometer){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Runner runner = accountService.findUserByLoginName(loginName);
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
	public String getActivity(@RequestParam("loginName")String loginName){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Runner runner = accountService.findUserByLoginName(loginName);
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
	public String getHistoryActivity(@RequestParam("loginName")String loginName){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Runner runner = accountService.findUserByLoginName(loginName);
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
	public boolean delActivity(@RequestParam("loginName")String loginName,
			@RequestParam("actuuid") String actuuid){
		Runner runner = accountService.findUserByLoginName(loginName);
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
