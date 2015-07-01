package org.springside.fi.service.running;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.fi.entity.Activity;
import org.springside.fi.entity.GpsActivityInfo;
import org.springside.fi.entity.Participate;
import org.springside.fi.map.common.Geohash;
import org.springside.fi.map.module.LatLng;
import org.springside.fi.map.utils.DistanceUtil;
import org.springside.fi.repository.ActivityDao;
import org.springside.fi.repository.GpsActivityInfoDao;
import org.springside.fi.repository.ParticipateDao;



/**
 * 创建时间：2015年7月1日 下午10:04:49  
 * 项目名称：running  
 * @author wangzhichao  
 * @version 1.0   
 * 文件名称：ActivityService.java  
 * 类说明： 获取附近的活动
 *
 */
@Service
@Transactional("transactionManager")
public class ActivityService extends BaseService{
	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private GpsActivityInfoDao gpsActivityInfoDao;
	
	@Autowired
	private ParticipateDao participateDao;
	
	private Date now = new Date();
	
	/**
	 * @param longitude
	 * @param latitude
	 * @param distance 活动发起地点距离的最大范围
	 * @param pageNumber
	 * @param pageSize
	 * @param time 活动开始时间
	 * @param sort 排序字段，一般都为空，自动以距离作为返回
	 * @return 附近活动列表
	 */
	public List<Activity> getAllActivity(String longitude, String latitude, int distance, int pageNumber, int pageSize, String time, String sort){
		if(StringUtils.isEmpty(longitude)){
			return null;
		}
		if(StringUtils.isEmpty(latitude)){
			return null;
		}
		/*
		 * 取得目标地址对geohash值
		 */
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String actGeoHash = new Geohash().encode(lat, lon);
		/*
		 * geohash值取得附近九块地区的有效所有活动的gps信息（包括当前用户创建到活动）
		 */
		List<GpsActivityInfo> gpsactinfos = getGeoHash(actGeoHash);
		/*
		 * 遍历geohash返回的附近活动信息，筛选符合条件的插入到activities
		 */
		ArrayList<Activity> activities = new ArrayList<Activity>();
		for(GpsActivityInfo gpsactivityinfo : gpsactinfos){
			String actuuid = gpsactivityinfo.getActuuid();
			Activity act = activityDao.findByACTUUID(actuuid).get(0);
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(StringUtils.isNotEmpty(time)){
				try {
					if(df.parse(time).after(act.getTime())){
						continue;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				Date now = new Date();
				if(now.after(act.getTime())){
					continue;
				}
			}
			
			double apart_distance = getDistance(latitude, longitude, gpsactivityinfo.getLatitude(), gpsactivityinfo.getLongitude());
			act.setDistance(apart_distance);
			if(distance<apart_distance){
				continue;
			}
			activities.add(act);
		}
		//排序distance，升序
		sortByDistance(activities);
		//查询记录范围
		int start = (pageNumber-1) * pageSize;
		int end   = pageNumber*pageSize - 1;
		int size  = activities.size();
		if(end<size){
			return activities.subList(start, end);
		}else if(start<size){
			return activities.subList(start, size);
		}else{
			return null;
		}
	}
	private double getDistance(String cen_latitude, String cen_longitude, String run_latitude, String run_longitude){
		LatLng cen_latlng = new LatLng(Double.valueOf(cen_latitude), Double.valueOf(cen_longitude));
		LatLng run_latlng = new LatLng(Double.valueOf(run_latitude), Double.valueOf(run_longitude));
		return DistanceUtil.getDistance(cen_latlng, run_latlng);
	}
	private void sortByDistance(ArrayList<Activity> acts){
		Comparator<Activity> comparator = new Comparator<Activity>() {
			
			@Override
			public int compare(Activity a1, Activity a2) {
				// TODO Auto-generated method stub
				if(a1.getDistance()<a2.getDistance()){
					return -1;
				}if(a1.getDistance()==a2.getDistance()){
					return 0;
				}else{
					return 1;
				}
			}
		};
		Collections.sort(acts,comparator);
	}
	private List<GpsActivityInfo> getGeoHash(String actGeoHash){
		return gpsActivityInfoDao.findByGeohash(actGeoHash.subSequence(0, 4).toString());
	}
	
	public void participate(String uuid, String actuuid, String opt){
		Participate part = getParticipate(uuid, actuuid);
		if("in".equals(opt)){
			part.setActuuid(actuuid);
			part.setUuid(uuid);
			participateDao.save(part);
		}else if("out".equals(opt)){
			participateDao.delete(part);
		}
	}
	private Participate getParticipate(String uuid, String actuuid){
		if(participateDao.findpart(uuid, actuuid).size()>0){
			return participateDao.findpart(uuid, actuuid).get(0);
		}else{
			return new Participate();
		}
	}
	
	public void saveActivity(String uuid, String longitude, String latitude, String address, String time, String info, int kilometer){
		
		Activity activity = getActivity(uuid);
		GpsActivityInfo gpsactivityinfo = getGpsActivityInfo(activity.getActuuid());
		
		activity.setUuid(uuid);
		activity.setActuuid(UUID.randomUUID().toString().replace("-", ""));
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
		
		
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		gpsactivityinfo.setActuuid(activity.getActuuid());
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
	
	public GpsActivityInfo getGpsActivityInfo(String actuuid){
		if(null==actuuid){
			return new GpsActivityInfo();
		}else{
			List<GpsActivityInfo> gpsactivities = gpsActivityInfoDao.findByActUUID(actuuid);
			return gpsactivities.get(0);
		}
	}
	
	public Activity getActivity(String uuid){
		List<Activity> activities = activityDao.findTodayByUUID(uuid, now);
		if(activities.size()>0){
			return activities.get(0);
		}else{
			return new Activity();
		}
	}
	
	public List<Activity> getHistoryActivity(String uuid){
		List<Activity> activities = activityDao.findHistoryByUUID(uuid, now);
		return activities;
	}
	
	public boolean delActivity(String uuid, String actuuid){
		List<Activity> activities = activityDao.findSelfTodayByUUID(uuid);
		try{
			for(Activity activity:activities){
				if(activity.getActuuid().equals(actuuid)){
					gpsActivityInfoDao.delete(gpsActivityInfoDao.findByActUUID(actuuid).get(0));
					activityDao.delete(activity);
					return true;
				}
			}
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean findSelfTodayActivityByUUID(String uuid){
		if(activityDao.findSelfTodayByUUID(uuid).size()>0){
			return false;
		}else{
			return true;
		}
	}
	public boolean findTodayActivityByUUID(String uuid){
		if(activityDao.findTodayByUUID(uuid, now).size()>0){
			return false;
		}else{
			return true;
		}
	}

}
