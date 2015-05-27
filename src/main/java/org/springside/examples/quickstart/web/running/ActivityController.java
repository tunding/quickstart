package org.springside.examples.quickstart.web.running;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.quickstart.entity.Activity;
import org.springside.examples.quickstart.service.running.ActivityService;
import org.springside.modules.mapper.JsonMapper;


@Controller
@RequestMapping(value="/activity/me")
public class ActivityController {
	
	@Autowired
	private ActivityService activityService;
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@ModelAttribute("act")
	public Activity get(@RequestParam(required=false) Long id, Model model, HttpServletRequest request){
		if(id!=null){
			return activityService.getActivity(id);
		}else{
			return activityService.getNewActivity();
		}
	}
	
	@RequestMapping(value="/save")
	public String saveActivity(@RequestParam("address") String address,
			@RequestParam("time") Date time,
			@RequestParam("info") String info,
			@RequestParam("longitude") String lon,
			@RequestParam("latitude") String lat,
			@RequestParam("kilometer") int kilometer){
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
/*			act.setAddress(address);
			act.setTime(time);
			act.setInfo(info);
			act.setLongitude(lon);
			act.setLatitude(lat);
			act.setKilometer(kilometer);
			String geohashCode = new Geohash().encode(Double.valueOf(lat), Double.valueOf(lon));
			act.setGeohashCode(geohashCode);*/
			activityService.saveActivity(address, time, info, lon, lat, kilometer);
			map.put("result", "success");
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	@RequestMapping(value="/delete")
	public String delActivity(@RequestParam("activityId")String ids){
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			
			map.put("result", "success");
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "error");
			map.put("msg", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
}
