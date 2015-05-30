package org.springside.examples.quickstart.web.running;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.entity.Runner;
import org.springside.examples.quickstart.service.running.RunnerService;
import org.springside.modules.mapper.JsonMapper;

@Controller
@RequestMapping(value="/runner/near")
public class RunnerNearController extends BaseController {
	
	@Autowired
	private RunnerService runnerService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@ResponseBody
	@RequestMapping(value={"/list", "/", ""})
	public String  getNearPerson(ServletRequest request,
			@RequestParam(value = "loginName") String loginName,
			@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "distance", defaultValue=DEFAULT_DISTANCE) int distance,
			@RequestParam(value = "pageNum", defaultValue=DEFAULT_PAGE_NUMBER) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue=DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "sex", defaultValue = "") String sex,
			@RequestParam(value = "age", defaultValue = "") String age,
			@RequestParam(value = "time", defaultValue = "") String time,
			@RequestParam(value = "sort", defaultValue = "") String sort){
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			List<Runner> runners = runnerService.getAllRunner(loginName, longitude, latitude, distance, pageNumber, pageSize, sex, age, time, sort);
			map.put("result", "success");
			if(runners!=null&&!runners.isEmpty()){
				map.put("data", runners);
			}else{
				map.put("data", "null");
			}
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", "");
		}
		return jsonMapper.toJson(map);
	}
	
}
