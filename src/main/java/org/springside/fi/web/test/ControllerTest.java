package org.springside.fi.web.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springside.fi.web.running.BaseController;

@Controller
@RequestMapping(value="/test")
public class ControllerTest extends BaseController {
	@RequestMapping(value="/session", method=RequestMethod.GET)
	public String getSession(){
		System.out.println("test session");
		return "test/session";
	}
}
