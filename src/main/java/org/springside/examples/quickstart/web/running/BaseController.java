package org.springside.examples.quickstart.web.running;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springside.modules.beanvalidator.BeanValidators;

public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected static final String DEFAULT_PAGE_NUMBER="1";
	protected static final String DEFAULT_PAGE_SIZE="10";
	
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
}
