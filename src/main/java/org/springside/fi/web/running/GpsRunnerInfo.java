package org.springside.fi.web.running;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.service.running.RunnerService;

@Controller
@RequestMapping(value="/gps/info")
public class GpsRunnerInfo extends BaseController{
	@Autowired
	private RunnerService runnerService;
	
	@ResponseBody
	@RequestMapping(value="/putgeohash")
	public String putGeoHash(@RequestParam(value="lat") String lat,
			@RequestParam(value="lon") String lon){
		Long user_id = getCurrentUserId();
		return runnerService.UpdateGeoHash(user_id, lat, lon);
	}
}
