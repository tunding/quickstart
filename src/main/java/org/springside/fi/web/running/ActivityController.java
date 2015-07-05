package org.springside.fi.web.running;

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
	
	/**
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param address 活动地址
	 * @param time 活动发起时间
	 * @param info 活动具体信息
	 * @param kilometer 活动跑步距离，以km单位
	 * @return
	 * @description 现阶段只要求存储活动是一个，而不是数组形式的多个活动存储
	 */
	@ResponseBody
	@RequestMapping(value="/saveactivity")
	public String saveActivity(@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "address") String address,
			@RequestParam(value = "time") String time,
			@RequestParam(value = "info") String info,
			@RequestParam(value = "kilometer") int kilometer){
		HashMap<String, Object> map = new HashMap<String, Object>();
		String uuid = getRunnerUuid();
		try{
			String code = activityService.saveActivity(uuid, longitude, latitude, address, time, info, kilometer);
			if("200".equals(code)){
				map.put("result", "success");
			}else{
				map.put("result", "failed");
			}
			map.put("data", "");
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	/**
	 * @param actuuid
	 * @return 返回活动详情，包含活动已参加的人数，当前用户是否参加
	 */
	@ResponseBody
	@RequestMapping(value="/getactivity")
	public String getActivity(@RequestParam(value="actuuid") String actuuid){
		HashMap<String, Object> map = new HashMap<String, Object>();
		String uuid = getRunnerUuid();
		try{
			Activity activity = activityService.getActUuidActivity(uuid, actuuid);
			map.put("result", "success");
			map.put("data", activity);
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	/**
	 * @return 返回当前用户的所有发布的活动，包括未开始和已结束的活动，按活动开启时间排序
	 */
	@ResponseBody
	@RequestMapping(value="/public/gethistoryactivity")
	public String getPublicHistoryActivity(@RequestParam(value = "pageNum", defaultValue=DEFAULT_PAGE_NUMBER) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue=DEFAULT_PAGE_SIZE) int pageSize){
		HashMap<String, Object> map = new HashMap<String, Object>();
		String uuid = getRunnerUuid();
		try{
			List<Activity> activities = activityService.getPublicHistoryActivity(uuid, pageNumber, pageSize);
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
	@RequestMapping(value="/participate/gethistoryactivity")
	public String getParticipateHistoryActivity(@RequestParam(value = "pageNum", defaultValue=DEFAULT_PAGE_NUMBER) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue=DEFAULT_PAGE_SIZE) int pageSize){
		HashMap<String, Object> map = new HashMap<String, Object>();
		String uuid = getRunnerUuid();
		try{
			List<Activity> activities = activityService.getParticipateHistoryActivity(uuid, pageNumber, pageSize);
			map.put("result", "success");
			map.put("data", activities);
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	/**
	 * @param actuuid
	 * @description 删除发布的任意活动，需要删除默认参加活动，不能删除已发生的活动
	 */
	@ResponseBody
	@RequestMapping(value="/deleteactivity")
	public boolean delActivity(@RequestParam("actuuid") String actuuid){
		return activityService.delActivity(actuuid);
	}
	
	/**
	 * @param time time表示的日期，作为判断某天发布活动没有的时间依据
	 * @return true表示time当天没有活动发布，可以创建time时间当天的活动
	 */
	@ResponseBody
	@RequestMapping(value="/checkActivity")
	public boolean checkActivity(@RequestParam(value="time") String time){
		String uuid = getRunnerUuid();
		return activityService.findDayActivityByUUID(uuid, time);
	}
	
	/**
	 * @return 获取当前用户uuid
	 */
	private String getRunnerUuid(){
		Long user_id = getCurrentUserId();
		Runner runner = runnerService.getRunner(user_id);
		return runner.getUuid();
	}
}
