package org.springside.fi.web.running;

import java.util.List;

import javax.validation.Validator;

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

public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected static final String DEFAULT_PAGE_NUMBER="1";
	protected static final String DEFAULT_PAGE_SIZE="10";
	protected static final String DEFAULT_DISTANCE="10000";
	
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
	
	public void bindErrorRes(BindingResult bindResult, BaseVo baseVo){
		FieldError fieldError = bindResult.getFieldError();
		List<ObjectError> errorList = bindResult.getAllErrors();
    	String errormsg = "[" + fieldError.getField() + "] "+errorList.get(0).getDefaultMessage();
    	baseVo.setResult(RestExceptionCode.REST_PARAMETER_ERROR_CODE);
    	baseVo.setData(errormsg);
	}
}
