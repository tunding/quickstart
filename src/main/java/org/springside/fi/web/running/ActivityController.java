package org.springside.fi.web.running;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Activity;
import org.springside.fi.entity.Runner;
import org.springside.fi.service.running.ActivityService;
import org.springside.fi.service.running.RunnerService;
import org.springside.modules.mapper.JsonMapper;

/**
 * 创建时间：2015年7月3日 下午10:05:17  
 * 项目名称：running  
 * @author wangzhichao  
 * @version 1.0   
 * 文件名称：ActivityController.java  
 * 类说明： 约跑活动的创建、查询已发布的活动、查询历史发布活动、删除发布的活动、检测某天是否已经发布过活动（一天一次）
 *
 */
@Controller
@RequestMapping(value="/activity/info")
public class ActivityController extends BaseController{
	@Autowired
	private ActivityService activityService;
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
	
	/**
	 * @param time time表示的日期，作为判断某天发布活动没有的时间依据
	 * @return true表示time当天没有活动发布，可以创建time时间当天的活动
	 */
	@ResponseBody
	@RequestMapping(value="/checkActivity")
	public boolean checkActivity(@RequestParam(value="time") String time){
		Long user_id = getCurrentUserId();
		Runner runner = runnerService.getRunner(user_id);
		String uuid = runner.getUuid();
		return activityService.findDayActivityByUUID(uuid, time);
	}
}
