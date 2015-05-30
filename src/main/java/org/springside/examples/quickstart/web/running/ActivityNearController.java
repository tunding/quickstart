package org.springside.examples.quickstart.web.running;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.quickstart.entity.Activity;
import org.springside.examples.quickstart.service.running.ActivityService;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.web.Servlets;

@Controller
@RequestMapping(value="/activity/near")
public class ActivityNearController extends BaseController{
	@Autowired
	private ActivityService activityService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@RequestMapping(value={"/list", "/", ""})
	public String getNearActivity(ServletRequest request,
			@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "pageNum", defaultValue=DEFAULT_PAGE_NUMBER) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue=DEFAULT_PAGE_SIZE) int pageSize){
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			/*List<Activity> activities = activityService.getAllActivity(pageNumber, pageSize, searchParams, longitude, latitude);
			map.put("result", "success");
			map.put("content", activities);*/
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	@RequestMapping(value="/participate")
	public String participateActivity(@RequestParam(value="uuid") String uuid,
			@RequestParam(value="activityId") String activityId,
			@RequestParam(value="controller") String opt){
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			activityService.participate(uuid, activityId, opt);
			map.put("result", "success");
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
}
