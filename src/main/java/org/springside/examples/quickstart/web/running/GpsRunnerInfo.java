package org.springside.examples.quickstart.web.running;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.quickstart.service.running.RunnerService;

@Controller
@RequestMapping(value="/gps/info")
public class GpsRunnerInfo {
	@Autowired
	private RunnerService runnerService;
	
	@ResponseBody
	@RequestMapping(value="/putgeohash")
	public String putGeoHash(@RequestParam("loginName") String loginName,
			@RequestParam(value="lon") String lon,
			@RequestParam(value="lat") String lat){
		return runnerService.UpdateGeoHash(loginName, lon, lat);
	}
}
