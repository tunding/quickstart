package org.springside.fi.service.running;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.fi.entity.Activity;
import org.springside.fi.entity.GpsActivityInfo;
import org.springside.fi.entity.Participate;
import org.springside.fi.entity.Runner;
import org.springside.fi.map.common.Geohash;
import org.springside.fi.map.module.LatLng;
import org.springside.fi.map.utils.DistanceUtil;
import org.springside.fi.repository.ActivityDao;
import org.springside.fi.repository.GpsActivityInfoDao;
import org.springside.fi.repository.ParticipateDao;
import org.springside.fi.repository.RunnerDao;



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
	
	@Autowired
	private RunnerDao runnerDao;
	
	private Date now = new Date();
	
	/**
	 * @param distance 活动发起地点距离的最大范围
	 * @param time 活动开始时间
	 * @param sort 排序字段，一般都为空，自动以距离作为返回
	 * @return 附近活动列表
	 */
	public HashMap<String, Object> getAllActivity(long user_id, String longitude, String latitude, int distance, int pageNumber, int pageSize, String time, String sort){
		
		Runner runner = getRunner(user_id);
		/*
		 * 目标地址geohash值
		 */
		String actGeoHash = getGps(longitude, latitude);
		/*
		 * 获取时间标志设置为Date类型，供后续有效时间调用
		 */
		Date starttime = getStartTime(time);
		/*
		 * geohash值取得附近九块地区的时间有效所有活动的gps信息（包括当前用户创建到活动）
		 */
		List<GpsActivityInfo> gpsactinfos = getGeoHash(actGeoHash, starttime);
		/*
		 * 遍历geohash返回的附近活动信息，筛选符合条件的插入到activities
		 */
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activityAdd(longitude, latitude, distance, runner, gpsactinfos, activities);
		/*
		 * 返回结果中包含，附近活动数量
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", activities.size());
		//调用重写的比较器，根据distance进行升序排序
		sortByDistance(activities);
		//返回规定范围内的记录
		return activitySub(pageNumber, pageSize, activities, map);
	}
	/**
	 * @param pageNumber
	 * @param pageSize
	 * @param activities
	 * @param map
	 * @return 返回排序后目标页的信息map
	 */
	private HashMap<String, Object> activitySub(int pageNumber, int pageSize,
			ArrayList<Activity> activities, HashMap<String, Object> map) {
		int start = (pageNumber-1) * pageSize;
		int end   = pageNumber*pageSize - 1;
		int size  = activities.size();
		if(end<size){
			map.put("acts", activities.subList(start, end));
			return map;
		}else if(start<size){
			map.put("acts", activities.subList(start, size));
			return map;
		}else{
			return null;
		}
	}
	/**
	 * @description 赋值activities对象
	 */
	private void activityAdd(String longitude, String latitude, int distance,
			Runner runner, List<GpsActivityInfo> gpsactinfos,
			ArrayList<Activity> activities) {
		for(GpsActivityInfo gpsactivityinfo : gpsactinfos){
			String actuuid = gpsactivityinfo.getActuuid();
			Activity act = activityDao.findByACTUUID(actuuid).get(0);
			/*
			 * 计算activity和给定地点的实际距离，只有在给定distance范围内的活动才会被插入到activities
			 */
			double apart_distance = getDistance(latitude, longitude, gpsactivityinfo.getLatitude(), gpsactivityinfo.getLongitude());
			act.setDistance(apart_distance);
			if(distance<apart_distance){
				continue;
			}
			/*
			 * 统计有效活动的参与人数participateCount
			 */
			Integer participateCount = participateDao.findUuidByActuuid(actuuid).size();
			act.setParticipateCount(participateCount);
			
			if(runner.getUuid().equals(act.getUuid())){
				act.setState(1);
			}else{
				act.setState(0);
			}
			activities.add(act);
		}
	}
	/**
	 * @param time
	 * @return 获取时间标志
	 */
	private Date getStartTime(String time) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date starttime = new Date();
		if(!StringUtils.isEmpty(time)){
			try {
				starttime = df.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return starttime;
	}
	/**
	 * @param longitude
	 * @param latitude
	 * @return 取得目标地址对geohash值
	 */
	private String getGps(String longitude, String latitude) {
		if(StringUtils.isEmpty(longitude)){
			return null;
		}
		if(StringUtils.isEmpty(latitude)){
			return null;
		}
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String actGeoHash = new Geohash().encode(lat, lon);
		return actGeoHash;
	}
	/**
	 * @param cen_latitude
	 * @param cen_longitude
	 * @param run_latitude
	 * @param run_longitude
	 * @return 调用距离计算方法返回两点间的距离
	 */
	private double getDistance(String cen_latitude, String cen_longitude, String run_latitude, String run_longitude){
		LatLng cen_latlng = new LatLng(Double.valueOf(cen_latitude), Double.valueOf(cen_longitude));
		LatLng run_latlng = new LatLng(Double.valueOf(run_latitude), Double.valueOf(run_longitude));
		return DistanceUtil.getDistance(cen_latlng, run_latlng);
	}
	/**
	 * @description 重写比较器
	 */
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
	/**
	 * @param actGeoHash
	 * @param time
	 * @return 获取geohash前四位相同的附近活动，并且time有效
	 */
	private List<GpsActivityInfo> getGeoHash(String actGeoHash, Date time){
		return gpsActivityInfoDao.findByGeohash(actGeoHash.subSequence(0, 4).toString(), time);
	}
	
	
	/**
	 * @param uuid
	 * @param actuuid
	 * @param opt in参与,out取消参与,真删除
	 */
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
	/**
	 * @param uuid
	 * @param actuuid
	 * @return 返回participate对象,无此对象则新建participate对象
	 */
	private Participate getParticipate(String uuid, String actuuid){
		List<Participate> parts = participateDao.findpart(uuid, actuuid);
		if(parts.size()>0){
			return parts.get(0);
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
	/**
	 * @param uuid
	 * @param time
	 * @return
	 * @description 判断未来某一天当前用户是否已经发布活动，发布则返回false，不允许用户再次发布活动 
	 * 需要将time表示成yyyy-MM-dd，判断同一天。
	 * 数据库TO_DAYS函数
	 */
	public boolean findDayActivityByUUID(String uuid, String time){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(date==null || activityDao.findTodayByUUID(uuid, date).size()>0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 获取用户当前对象
	 */
	private Runner getRunner(Long id){
		return runnerDao.findOne(id);
	}

}
