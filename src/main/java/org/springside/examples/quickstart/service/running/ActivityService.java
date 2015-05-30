package org.springside.examples.quickstart.service.running;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.Activity;
import org.springside.examples.quickstart.entity.GpsActivityInfo;
import org.springside.examples.quickstart.map.common.FilterOperator;
import org.springside.examples.quickstart.map.common.FilterOperator.Operator;
import org.springside.examples.quickstart.map.common.Geohash;
import org.springside.examples.quickstart.map.module.LatLng;
import org.springside.examples.quickstart.map.utils.DistanceUtil;
import org.springside.examples.quickstart.repository.ActivityDao;
import org.springside.examples.quickstart.repository.GpsActivityInfoDao;
import org.springside.examples.quickstart.repository.ParticipateDao;



@Service
@Transactional("transactionManager")
public class ActivityService extends BaseService{
	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private GpsActivityInfoDao gpsActivityInfoDao;
	
	@Autowired
	private ParticipateDao participateDao;

	
	public void participate(String uuid, String activityId, String opt){
/*		if("in".equals(opt)){
			String now = genCurrentTime();
			//participateDao.newParticipate(uuid, activityId, now);
		}else if("out".equals(opt)){
			participateDao.delParticipate(uuid, activityId);
		}*/
	}
	
	public void saveActivity(String uuid, String longitude, String latitude, String address, String time, String info, int kilometer){
		
		Activity activity = getActivity(uuid);
		activity.setAddress(address);
		activity.setLongitude(longitude);
		activity.setLatitude(latitude);
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date starttime;
		try {
			starttime = df.parse(time);
			activity.setTime(starttime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		activity.setInfo(info);
		activity.setKilometer(kilometer);
		
		GpsActivityInfo gpsactivityinfo = new GpsActivityInfo();
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		gpsactivityinfo.setUuid(uuid);
		gpsactivityinfo.setLongitude(longitude);
		gpsactivityinfo.setLatitude(latitude);
		gpsactivityinfo.setGeohash(userGeoHash);
		
		try{
			activityDao.save(activity);
			gpsActivityInfoDao.save(gpsactivityinfo);
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		
	}
	
	private Activity getActivity(String uuid){
		List<Activity> activities = activityDao.findByUUID(uuid);
		if(activities.size()>0){
			return activities.get(0);
		}else{
			return new Activity();
		}
	}
	
	public boolean delActivity(String uuid){
		List<Activity> activities = activityDao.findByUUID(uuid);
		try{
			for(Activity activity:activities){
				activityDao.delete(activity);
			}
			return true;
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean findActivityByUUID(String uuid){
		if(activityDao.findByUUID(uuid).size()>0){
			return false;
		}else{
			return true;
		}
	}

}
