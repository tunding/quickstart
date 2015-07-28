
package org.springside.fi.web.running;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Activity;
import org.springside.fi.entity.Runner;
import org.springside.fi.service.running.ActivityService;
import org.springside.fi.service.running.RunnerService;
import org.springside.fi.web.exception.RestExceptionCode;
import org.springside.fi.web.params.ActuuidParam;
import org.springside.fi.web.params.CheckActivityParam;
import org.springside.fi.web.params.SaveActivityParam;
import org.springside.fi.web.vo.CheckACtivityVo;
import org.springside.fi.web.vo.GetActivityVo;
import org.springside.fi.web.vo.SaveActivityVo;
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
	 * @param actParam 发布活动信息详情
	 * @return
	 * @description 现阶段只要求存储活动是一个，而不是数组形式的多个活动存储
	 */
	@ResponseBody
	@RequestMapping(value="/saveactivity")
	public String saveActivity(@Valid SaveActivityParam actParam,
			BindingResult bindResult){
		SaveActivityVo saveActVo = new SaveActivityVo();
		if(bindResult.hasErrors()){
			bindErrorRes(bindResult, saveActVo);
		}else{
			String uuid = getRunnerUuid();
			try{
				String code = activityService.saveActivity(actParam, uuid);
				if("200".equals(code)){
					saveActVo.setResult(RestExceptionCode.REST_SUCCESS_CODE);
					saveActVo.setData(RestExceptionCode.REST_SUCCESS_MSG);
				}else{
					saveActVo.setResult(RestExceptionCode.REST_SYSTEM_ERROR_CODE);
					saveActVo.setData(code);
				}
			}catch(RuntimeException e){
				e.printStackTrace();
				saveActVo.setResult(RestExceptionCode.REST_SYSTEM_ERROR_CODE);
				saveActVo.setData(RestExceptionCode.REST_SYSTEM_ERROR_MSG);
			}
		}
		return jsonMapper.toJson(saveActVo);
	}
	
	/**
	 * @param actuuid
	 * @return 返回活动详情，包含活动已参加的人数，当前用户是否参加
	 */
	@ResponseBody
	@RequestMapping(value="/getactivity")
	public String getActivity(@Valid ActuuidParam actuuidparam,
			BindingResult bindResult){
		GetActivityVo getActivityVo = new GetActivityVo();
		if(bindResult.hasErrors()){
			bindErrorRes(bindResult, getActivityVo);
		}else{
			String uuid = getRunnerUuid();
			try{
				Activity activity = activityService.getActUuidActivity(uuid, actuuidparam.getActuuid());
				getActivityVo.setResult(RestExceptionCode.REST_SUCCESS_CODE);
				getActivityVo.setData(activity);
			}catch(RuntimeException e){
				e.printStackTrace();
				getActivityVo.setResult(RestExceptionCode.REST_SYSTEM_ERROR_CODE);
				getActivityVo.setData(RestExceptionCode.REST_SYSTEM_ERROR_MSG);
			}
		}
		return jsonMapper.toJson(getActivityVo);
	}
	
	/**
	 * @return 返回当前用户的所有发布的活动，包括未开始和已结束的活动，按活动开启时间排序
	 */
	@ResponseBody
	@RequestMapping(value="/public/gethistoryactivity")
	public String getPublicHistoryActivity(@RequestParam(value = "pageNum", defaultValue=DEFAULT_PAGE_NUMBER) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue=DEFAULT_PAGE_SIZE) int pageSize){
		GetActivityVo getActivityVo = new GetActivityVo();
		String uuid = getRunnerUuid();
		try{
			List<Activity> activities = activityService.getPublicHistoryActivity(uuid, pageNumber, pageSize);
			getActivityVo.setResult(RestExceptionCode.REST_SUCCESS_CODE);
			getActivityVo.setData(activities);
		}catch(RuntimeException e){
			e.printStackTrace();
			getActivityVo.setResult(RestExceptionCode.REST_SYSTEM_ERROR_CODE);
			getActivityVo.setData(RestExceptionCode.REST_SYSTEM_ERROR_MSG);
		}
		return jsonMapper.toJson(getActivityVo);
	}
	
	/**
	 * @param pageNumber
	 * @param pageSize
	 * @return 返回当前用户参加的活动
	 */
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
	public boolean delActivity(@RequestParam(value = "actuuid") String actuuid,
			@RequestParam(value = "msg", defaultValue="活动组织者取消活动") String msg){
		String uuid = getRunnerUuid();
		return activityService.delActivity(uuid, actuuid, msg);
	}
	
	/**
	 * @param actTime 判断某天发布活动没有的时间依据
	 * @return false表示time当天没有活动发布，可以创建time时间当天的活动
	 * 数据库中只能保持一天当前用户最多一个有效活动和一个已经删除多活动
	 * 需要将actTime表示成yyyyMMddhhmmss
	 */
	@ResponseBody
	@RequestMapping(value="/checkActivity")
	public String checkActivity(@Valid CheckActivityParam actTime,
			BindingResult bindResult){
		CheckACtivityVo actVo = new CheckACtivityVo();
		if(bindResult.hasErrors()){
			bindErrorRes(bindResult, actVo);
		}else{
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMddhhmmss");
			Date date = null;
			try {
				date = df.parse(actTime.getActTime());
			} catch (ParseException e) {
				actVo.setResult(RestExceptionCode.REST_PARAMETER_ERROR_CODE);
				actVo.setData("日期格式错误");
				return jsonMapper.toJson(actVo);
			}
			String uuid = getRunnerUuid();
			actVo.setResult(RestExceptionCode.REST_SUCCESS_CODE);
			actVo.setData(activityService.findDayActivityByUUID(uuid, date));
		}
		return jsonMapper.toJson(actVo);
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