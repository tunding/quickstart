package org.springside.fi.service.running;

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
import org.springside.fi.repository.ActivityDao;
import org.springside.fi.repository.GpsActivityInfoDao;
import org.springside.fi.repository.ParticipateDao;
import org.springside.fi.web.params.SaveActivityParam;

/**
 * @author tunding:wzc@tcl.com
 * @description
 * @version 1.0
 * @date 创建时间：2015年8月2日 上午11:14:32
 */
@Service
@Transactional("transactionManager")
public class ActivityInfoService extends BaseService{
	@Autowired
	private ActivityDao actDao;
	@Autowired
	private GpsActivityInfoDao gpsActInfoDao;
	@Autowired
	private ParticipateDao partDao;
	@Autowired
	private ParticipateActivityService partActService;
	/**
	 * @param uuid
	 * @param act
	 * @description 存储发布的活动，同时当前用户被默认为第一个参与者
	 */
	public String saveActivity(SaveActivityParam actParam, String uuid, Date startTime){
		String name = actParam.getName();
		String lon  = actParam.getLongitude();
		String lat  = actParam.getLatitude();
		String address = actParam.getAddress();
		String info = actParam.getInfo();
		int kilometer  = actParam.getKilometer();
		int limitCount = actParam.getLimitCount();
		String tag  = actParam.getTag();
		/*
		 * 获取当前用户发布的活动，如果time日期没有发布活动则返回新建的Activity
		 */
		Activity activity = getDayActivity(uuid, startTime);
		/*
		 * 设置act的属性信息
		 */
		saveActInfo(name, uuid, lon, lat, address, info, kilometer, limitCount, tag, startTime, activity);
		/*
		 * 通过活动actuuid获取活动经纬度信息对象
		 */
		GpsActivityInfo gpsactivityinfo = getGpsActivityInfo(activity.getActuuid());
		/*
		 * 设置活动gps属性信息
		 */
		saveActGpsInfo(act.getLongitude(), act.getLatitude(), starttime, activity,
				gpsactivityinfo);
		/*
		 * 通过活动uuid和用户uuid获得参与活动对象
		 */
		Participate part = partActService.getParticipate(uuid, activity.getActuuid());
		/*
		 * part如果原本就存在则不需要修改
		 */
		if(StringUtils.isBlank(part.getActuuid())){
			savePartInfo(uuid, activity, part);
			try{
				partDao.save(part);
			}catch(RuntimeException e){
				e.printStackTrace();
				return "save participate failed";
			}
		}
		
		try{
			actDao.save(activity);
			gpsActInfoDao.save(gpsactivityinfo);
		}catch(RuntimeException e){
			e.printStackTrace();;
			return "save activity failed";
		}
		return "200";
	}
	/**
	 * @description participate信息设置完毕
	 */
	private void savePartInfo(String uuid, Activity activity, Participate part) {
		part.setActuuid(activity.getActuuid());
		part.setUuid(uuid);
		part.setDelFlag(1);
	}
	/**
	 * @description actGps信息设置
	 */
	private void saveActGpsInfo(String longitude, String latitude,
			Date startTime, Activity activity, GpsActivityInfo gpsactivityinfo) {
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		gpsactivityinfo.setActuuid(activity.getActuuid());
		gpsactivityinfo.setLongitude(longitude);
		gpsactivityinfo.setLatitude(latitude);
		gpsactivityinfo.setGeohash(userGeoHash);
		gpsactivityinfo.setStart_time(startTime);
		gpsactivityinfo.setDelFlag(1);
	}
	/**
	 * @description activity属性信息设置完毕
	 */
	private void saveActInfo(String name, String uuid, String longitude, String latitude,
			String address, String info, int kilometer, int limitCount, String tag, Date starttime,
			Activity activity) {
		activity.setName(name);
		activity.setUuid(uuid);
		activity.setActuuid(UUID.randomUUID().toString().replace("-", ""));
		activity.setAddress(address);
		activity.setLongitude(longitude);
		activity.setLatitude(latitude);
		activity.setInfo(info);
		activity.setKilometer(kilometer);
		activity.setLimitCount(limitCount);
		activity.setTag(tag);
		activity.setTime(starttime);
		activity.setDelFlag(1);
	}
	/**
	 * @param actuuid
	 * @return 根据活动uuid返回活动的gps信息，没有则新建
	 */
	private GpsActivityInfo getGpsActivityInfo(String actuuid){
		List<GpsActivityInfo> gpsactivities = gpsActInfoDao.findByActUUID(actuuid);
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
		List<Activity> activities = actDao.findDayByUUID(uuid, time);
		if(activities.size()>0){
			return activities.get(0);
		}else{
			return new Activity();
		}
	}
}
