package org.springside.fi.web.running;

import java.text.ParseException;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.service.running.ActivityParticipateService;
import org.springside.fi.service.running.ActivityService;
import org.springside.fi.web.exception.RestExceptionCode;
import org.springside.fi.web.params.NearActivityListParam;
import org.springside.fi.web.params.acitvity.PartParam;
import org.springside.fi.web.vo.NearActivityListVo;
import org.springside.fi.web.vo.activity.PartVo;

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
	private ActivityService actService;
	@Autowired
	private ActivityParticipateService actPartService;
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
				Date startTime = validDateParamByNow(nearActListParam.getStart_time());//验证传入的时间格式,返回Date类型时间表示此刻之后点活动才能被筛选出来
				nearActListVo.setData(actService.getAllActivity(getCurrentUserId(), nearActListParam, startTime));
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
	 * @param partIn
	 * @desctiption 参与附近的活动
	 */
	@ResponseBody
	@RequestMapping(value="/participate/in")
	public String partActivityIn(@Valid PartParam partIn, BindingResult bindResult){
		PartVo partVo = new PartVo();
		if(bindResult.hasErrors()){
			bindErrorRes(bindResult, partVo);
		}else{
			String uuid = partIn.getUuid();
			String actuuid = partIn.getActuuid();
			try{
				actPartService.participateIn(uuid, actuuid);
			}catch(RuntimeException e){
				partVo.setResult(RestExceptionCode.REST_SYSTEM_ERROR_CODE);
				partVo.setData(RestExceptionCode.REST_SYSTEM_ERROR_MSG);
			}
		}
		return jsonMapper.toJson(partVo);
	}
	/**
	 * @param partOut
	 * @desctiption 取消参与附近的活动
	 */
	@ResponseBody
	@RequestMapping(value="/participate/out")
	public String partActivityOut(@Valid PartParam partOut, BindingResult bindResult){
		PartVo partVo = new PartVo();
		if(bindResult.hasErrors()){
			bindErrorRes(bindResult, partVo);
		}else{
			String uuid = partOut.getUuid();
			String actuuid = partOut.getActuuid();
			try{
				actPartService.participateOut(uuid, actuuid);
			}catch(RuntimeException e){
				partVo.setResult(RestExceptionCode.REST_SYSTEM_ERROR_CODE);
				partVo.setData(RestExceptionCode.REST_SYSTEM_ERROR_MSG);
			}
		}
		return jsonMapper.toJson(partVo);
	}
	
}
