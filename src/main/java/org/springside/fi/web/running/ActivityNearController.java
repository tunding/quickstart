package org.springside.fi.web.running;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Activity;
import org.springside.fi.service.running.ActivityService;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.web.Servlets;

@Controller
@RequestMapping(value="/activity/near")
public class ActivityNearController extends BaseController{
	@Autowired
	private ActivityService activityService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@ResponseBody
	@RequestMapping(value={"/list", "/", ""})
	public String getNearActivity(ServletRequest request,
			@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "distance", defaultValue=DEFAULT_DISTANCE) int distance,
			@RequestParam(value = "pageNum", defaultValue=DEFAULT_PAGE_NUMBER) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue=DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "time", defaultValue = "") String time,
			@RequestParam(value = "sort", defaultValue = "") String sort){
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			List<Activity> activities = activityService.getAllActivity(longitude, latitude, distance, pageNumber, pageSize, time, sort);
			map.put("result", "success");
			if(activities!=null&&!activities.isEmpty()){
				map.put("data", activities);
			}else{
				map.put("data", "null");
			}
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", "");
		}
		return jsonMapper.toJson(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/participate")
	public String participateActivity(@RequestParam(value="uuid") String uuid,
			@RequestParam(value="actuuid") String actuuid,
			@RequestParam(value="controller") String opt){
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			activityService.participate(uuid, actuuid, opt);
			map.put("result", "success");
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
}
