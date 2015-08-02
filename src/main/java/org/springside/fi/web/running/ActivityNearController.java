package org.springside.fi.web.running;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Activity;
import org.springside.fi.service.running.ActivityService;
import org.springside.fi.web.exception.RestExceptionCode;
import org.springside.fi.web.params.NearActivityListParam;
import org.springside.fi.web.vo.NearActivityListVo;
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
	 * @param NearActivityListParam
	 * @description 获取附近所有有效活动，默认由近及远排序
	 */
	@ResponseBody
	@RequestMapping(value={"/list", "/", ""})
	public String getNearActivityList(@Valid NearActivityListParam nearActListParam,
			BindingResult bindResult){
		NearActivityListVo nearActListVo = new NearActivityListVo();
		if(bindResult.hasErrors()){
			bindErrorRes(bindResult, nearActListVo);//参数绑定异常，经纬度为空
		}else{
			try{
				Date startTime = validDateParam(nearActListParam.getStart_time());//验证传入的时间格式,返回Date类型时间表示此刻之后点活动才能被筛选出来
				activityService.getAllActivity(getCurrentUserId(), nearActListParam, startTime);
			}catch(ParseException e){//时间格式解析验证错误抛出异常
				nearActListVo.setResult(RestExceptionCode.REST_PARAMETER_ERROR_CODE);
				nearActListVo.setData("日期格式错误");
			}catch(RuntimeException e){
				nearActListVo.setResult(RestExceptionCode.REST_SYSTEM_ERROR_CODE);
				nearActListVo.setData(RestExceptionCode.REST_SYSTEM_ERROR_MSG);
			}
		}
		return jsonMapper.toJson(nearActListVo);
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
