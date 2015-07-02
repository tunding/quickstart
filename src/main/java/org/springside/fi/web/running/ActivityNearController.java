package org.springside.fi.web.running;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Activity;
import org.springside.fi.service.running.ActivityService;
import org.springside.modules.mapper.JsonMapper;

/**
* @author tunding:wzc@tcl.com
* @project running
* @version 1.0
* @date 创建时间：2015年7月2日 下午1:26:26
* @name ActivityNearController.java
* @description 附近活动列表、附近活动参与/取消参与
*/
@Controller
@RequestMapping(value="/activity/near")
public class ActivityNearController extends BaseController{
	@Autowired
	private ActivityService activityService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	/**
	 * @param time 传入时间串，如"2015-07-01 21:00:00",也可以为空
	 * @return
	 * @description sort 作为保留字段
	 */
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
		Long user_id = getCurrentUserId();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			HashMap<String, Object> res = activityService.getAllActivity(user_id, longitude, latitude, distance, pageNumber, pageSize, time, sort);
			@SuppressWarnings("unchecked")
			List<Activity> activities = (List<Activity>)res.get("acts");
			map.put("result", "success");
			if(activities!=null&&!activities.isEmpty()){
				map.put("total", res.get("total"));
				map.put("data", activities);
			}else{
				map.put("total", 0);
				map.put("data", null);
			}
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		
		return jsonMapper.toJson(map);
	}
	
	/**
	 * @param uuid
	 * @param actuuid
	 * @param opt in参与，out取消参与
	 * @description 参与或取消附近的活动
	 */
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
