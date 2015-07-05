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
		int end   = pageNumber*pageSize;
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
			Integer participateCount = participateDao.findByActuuid(actuuid).size();
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
	 * @description 重写距离比较器
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
	
	
	/**
	 * @param uuid
	 * @param longitude
	 * @param latitude
	 * @param address
	 * @param time
	 * @param info
	 * @param kilometer
	 * @description 存储发布的活动，同时当前用户被默认为第一个参与者
	 */
	public String saveActivity(String uuid, String longitude, String latitude, String address, String time, String info, int kilometer){
		/*
		 * time转换为Date类型，赋值到starttime
		 */
		Date starttime = TransferDate(time);
		/*
		 * String转换Date失败，则返回null
		 */
		if(starttime == null){
			return null;
		}
		/*
		 * 获取当前用户发布的活动，如果time日期没有发布活动则返回新建的Activity
		 */
		Activity activity = getDayActivity(uuid, starttime);
		/*
		 * 设置act的属性信息
		 */
		saveActInfo(uuid, longitude, latitude, address, info, kilometer,
				starttime, activity);
		/*
		 * 通过活动actuuid获取活动经纬度信息对象
		 */
		GpsActivityInfo gpsactivityinfo = getGpsActivityInfo(activity.getActuuid());
		/*
		 * 设置活动gps属性信息
		 */
		saveActGpsInfo(longitude, latitude, starttime, activity,
				gpsactivityinfo);
		/*
		 * 通过活动uuid和用户uuid获得参与活动对象
		 */
		Participate part = getParticipate(uuid, activity.getActuuid());
		/*
		 * part如果原本就存在则不需要修改
		 */
		if(StringUtils.isBlank(part.getActuuid())){
			savePartInfo(uuid, activity, part);
			try{
				participateDao.save(part);
			}catch(RuntimeException e){
				e.printStackTrace();
				return "save participate failed";
			}
		}
		
		try{
			activityDao.save(activity);
			gpsActivityInfoDao.save(gpsactivityinfo);
		}catch(RuntimeException e){
			e.printStackTrace();;
			return "save failed";
		}
		return "200";
		
	}
	/**
	 * @description participate信息设置完毕
	 */
	private void savePartInfo(String uuid, Activity activity, Participate part) {
		part.setActuuid(activity.getActuuid());
		part.setUuid(uuid);
	}
	/**
	 * @description actGps信息设置
	 */
	private void saveActGpsInfo(String longitude, String latitude,
			Date starttime, Activity activity, GpsActivityInfo gpsactivityinfo) {
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		gpsactivityinfo.setActuuid(activity.getActuuid());
		gpsactivityinfo.setLongitude(longitude);
		gpsactivityinfo.setLatitude(latitude);
		gpsactivityinfo.setGeohash(userGeoHash);
		gpsactivityinfo.setTime(starttime);
	}
	/**
	 * @description activity属性信息设置完毕
	 */
	private void saveActInfo(String uuid, String longitude, String latitude,
			String address, String info, int kilometer, Date starttime,
			Activity activity) {
		activity.setUuid(uuid);
		activity.setActuuid(UUID.randomUUID().toString().replace("-", ""));
		activity.setAddress(address);
		activity.setLongitude(longitude);
		activity.setLatitude(latitude);
		activity.setInfo(info);
		activity.setKilometer(kilometer);
		activity.setTime(starttime);
	}
	/**
	 * @param time
	 * @return 将String类型的time转换成Date类型
	 */
	public Date TransferDate(String time){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date starttime = null;
		try {
			starttime = df.parse(time);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return starttime;
	}
	
	/**
	 * @param actuuid
	 * @return 根据活动uuid返回活动的gps信息，没有则新建
	 */
	private GpsActivityInfo getGpsActivityInfo(String actuuid){
		List<GpsActivityInfo> gpsactivities = gpsActivityInfoDao.findByActUUID(actuuid);
		if(gpsactivities.size()>0){
			return gpsactivities.get(0);
		}else{
			return new GpsActivityInfo();
		}
	}
	
	/**
	 * @param uuid
	 * @return 返回time当天发布的活动，否则返回新建的活动对象
	 */
	private Activity getDayActivity(String uuid, Date time){
		List<Activity> activities = activityDao.findDayByUUID(uuid, time);
		if(activities.size()>0){
			return activities.get(0);
		}else{
			return new Activity();
		}
	}
	
	/**
	 * @param actuuid
	 * @return 直接通过actuuid返回act
	 */
	public Activity getActUuidActivity(String uuid, String actuuid){
		List<Activity> activities = activityDao.findByACTUUID(actuuid);
		Integer participateCount = participateDao.findByActuuid(actuuid).size();
		Activity act = activities.get(0);
		act.setParticipateCount(participateCount);
		if(uuid.equals(act.getUuid())){
			act.setState(1);
		}else{
			act.setState(0);
		}
		return act;
	}
	
	/**
	 * @param uuid
	 * @param pageNumber
	 * @param pageSize
	 * @return 用户所有发布的活动
	 */
	public List<Activity> getPublicHistoryActivity(String uuid, int pageNumber, int pageSize){
//		List<Activity> activities = activityDao.findHistoryByUUID(uuid, now);
		int start = (pageNumber-1) * pageSize;
		int size  = pageSize;
		List<Activity> activities = activityDao.findHistoryByUUID(uuid, start, size);
		return activities;
	}
	
	/**
	 * @param uuid
	 * @param pageNumber
	 * @param pageSize
	 * @return 用户所有参与活动，按时间排序
	 */
	public List<Activity> getParticipateHistoryActivity(String uuid, int pageNumber, int pageSize){
		int start = (pageNumber-1) * pageSize;
		int end   = pageNumber*pageSize;
		ArrayList<Activity> activities = getActByPart(uuid);
		int size  = activities.size();
		if(end<size){
			return activities.subList(start, end);
		}else if(start<size){
			return activities.subList(start, size);
		}else{
			return null;
		}
	}
	/**
	 * @param uuid
	 * @return 返回当前用户参与过的所有活动，按时间排序
	 */
	private ArrayList<Activity> getActByPart(String uuid) {
		ArrayList<Activity> activities = new ArrayList<Activity>();
		List<Participate> parts= participateDao.findByUuid(uuid);
		for(Participate part : parts){
			String actuuid = part.getActuuid();
			activities.add(activityDao.findByACTUUID(actuuid).get(0));
		}
		sortByTime(activities);
		return activities;
	}
	
	/**
	 * @description 时间排序
	 */
	private void sortByTime(ArrayList<Activity> acts){
		Comparator<Activity> comparator = new Comparator<Activity>() {
			
			@Override
			public int compare(Activity a1, Activity a2) {
				// TODO Auto-generated method stub
				if(a1.getTime().after(a2.getTime())){
					return -1;
				}if(a1.getTime().before(a2.getTime())){
					return 0;
				}else{
					return 1;
				}
			}
		};
		Collections.sort(acts,comparator);
	}
	
	
	/**
	 * @param actuuid
	 * 删除发布的有效活动，删除活动gps信息，删除活动所有参与人的信息
	 */
	public boolean delActivity(String actuuid){
		List<Activity> activities = activityDao.findByACTUUID(actuuid);
		if(activities.size()>0){
			Activity act = activities.get(0);
			try{
				deletePGA(actuuid, act);
				return true;
			}catch(RuntimeException e){
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * @param actuuid
	 * @param act
	 * @description 删除 参与者 part、活动gps gps、活动 act
	 */
	private void deletePGA(String actuuid, Activity act) {
		participateDao.delete(participateDao.findByActuuid(actuuid));
		gpsActivityInfoDao.delete(gpsActivityInfoDao.findByActUUID(actuuid).get(0));
		activityDao.delete(act);
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
		if(date==null || activityDao.findDayByUUID(uuid, date).size()>0){
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
