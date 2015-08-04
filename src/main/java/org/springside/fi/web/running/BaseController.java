package org.springside.fi.web.running;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springside.fi.service.account.ShiroDbRealm.ShiroUser;
import org.springside.fi.web.exception.RestExceptionCode;
import org.springside.fi.web.vo.BaseVo;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.JsonMapper;

public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected static final String DEFAULT_PAGE_NUMBER="1";
	protected static final String DEFAULT_PAGE_SIZE="10";
	protected static final String DEFAULT_DISTANCE="10000";
	protected JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
    @Autowired
    protected Validator validator;
    /**
     * javabean对象验证，验证失败抛出异常，交由ExceptionHandler处理
     */
    protected void validateObj(Object object, Class<?>... groups) {
        BeanValidators.validateWithException(validator, object, groups);
    }
    /**
     * 添加Model消息
     * 
     * @param message
     */
    protected void addMessage(Model model, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        model.addAttribute("message", sb.toString());
    }
    
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	protected Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	/**
	 * @param bindResult
	 * @param baseVo
	 * @description 出现参数绑定异常，写入baseVo中的result和data
	 */
	public void bindErrorRes(BindingResult bindResult, BaseVo baseVo){
		FieldError fieldError = bindResult.getFieldError();
		List<ObjectError> errorList = bindResult.getAllErrors();
    	String errormsg = "[" + fieldError.getField() + "] "+errorList.get(0).getDefaultMessage();
    	baseVo.setResult(RestExceptionCode.REST_PARAMETER_ERROR_CODE);
    	baseVo.setData(errormsg);
	}
	
	/**
	 * @param timeStr
	 * @return 传入时间为空或者在当前时间之前，返回当前时间。
	 * @throws ParseException
	 */
	public Date validDateParamByNow(String timeStr) throws ParseException{
		Date now = new Date();
		if(!StringUtils.isBlank(timeStr)){
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMddhhmmss");
			Date timeDate = df.parse(timeStr);
			if( now.after(timeDate) ){//传入时间在当前时间之前则以当前时间为准
				timeDate = now;
			}
			return timeDate;
		}else{//时间串为空，返回当前时间
			return now;
		}
	}
	
	/**
	 * @param timeStr
	 * @return 返回时间串解析后的日期
	 * @throws ParseException
	 */
	public Date validDateParam(String timeStr) throws ParseException{
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddhhmmss");
		Date timeDate = df.parse(timeStr);
		return timeDate;
	}
}
