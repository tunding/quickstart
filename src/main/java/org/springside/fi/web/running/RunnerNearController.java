package org.springside.fi.web.running;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.entity.Runner;
import org.springside.fi.service.running.RunnerService;
import org.springside.modules.mapper.JsonMapper;

@Controller
@RequestMapping(value="/runner/near")
public class RunnerNearController extends BaseController{
	
	@Autowired
	private RunnerService runnerService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
	@ResponseBody
	@RequestMapping(value="/test", method = RequestMethod.GET)
	public String test() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("test", "test method");
		return jsonMapper.toJson(map);
	}
	
	
	@ResponseBody
	@RequestMapping(value={"/list", "/", ""})
	public String  getNearPerson(ServletRequest request,
			@RequestParam(value = "longitude", defaultValue="") String longitude,
			@RequestParam(value = "latitude", defaultValue="") String latitude,
			@RequestParam(value = "distance", defaultValue=DEFAULT_DISTANCE) int distance,
			@RequestParam(value = "pageNum", defaultValue=DEFAULT_PAGE_NUMBER) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue=DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "sex", defaultValue = "") String sex,
			@RequestParam(value = "age", defaultValue = "") String age,
			@RequestParam(value = "time", defaultValue = "") String time,
			@RequestParam(value = "sort", defaultValue = "") String sort){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		try{
			List<Runner> runners = runnerService.getAllRunner(user_id, longitude, latitude, distance, pageNumber, pageSize, sex, age, time, sort);
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
	
	/**
	 * @return 返回附近的人属性信息
	 */
	@ResponseBody
	@RequestMapping(value="/info")
	public String getRunnerInfo(@RequestParam(value="uuid") String uuid) {
		String currentUuid = getUuid();
		Runner runner = runnerService.getRunnerWithAttentionFlag(currentUuid, uuid);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", "success");
		map.put("data", runner);
		return jsonMapper.toJson(map);
	}
	/**
	 * @param id
	 * 获取uuid
	 */
	private String getUuid(){
		Long user_id = getCurrentUserId();
		Runner runner = runnerService.getRunner(user_id);
		return runner.getUuid();
	}
}
