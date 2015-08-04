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
		boolean isPublish = false;//默认此活动没有发布,true为此活动已发布，不能修改actuuid，part，gps等非参数传递的信息
		
		//活动开始时间早于当前北京时间，建议修改
		if(startTime.before(new Date())){
			return "活动开始时间早于当前北京时间，建议修改";
		}
		/*
		 * 查看开始时间是否用户已经发布活动，false没有，true已发布活动
		 */
		isPublish = getDayActivity(uuid, startTime);
		//不能修改活动，只能新建活动
		if(isPublish==true){
			return "当天已发布活动，请删除后重新发布";
		}
		Activity activity = new Activity();
		/*
		 * 设置act的属性信息
		 */
		saveActInfo(name, uuid, lon, lat, address, info, kilometer, limitCount, tag, startTime, activity);
		/*
		 * 设置活动gps属性信息
		 */
		GpsActivityInfo gpsactivityinfo = new GpsActivityInfo();
		saveActGpsInfo(actParam.getLongitude(), actParam.getLatitude(), startTime, activity,
				gpsactivityinfo);
		/*
		 * 设置参与part属性信息
		 */
		Participate part = new Participate();
		savePartInfo(uuid, activity, part);
		try{
			partDao.save(part);
		}catch(RuntimeException e){
			e.printStackTrace();
			return "保存活动参与者时出现错误";
		}
		try{
			actDao.save(activity);
			gpsActInfoDao.save(gpsactivityinfo);
		}catch(RuntimeException e){
			e.printStackTrace();;
			return "保存活动信息出现错误";
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
	 * @param uuid
	 * @return 返回time当天发布的活动，否则返回新建的活动对象
	 */
	private boolean getDayActivity(String uuid, Date time){
		List<Activity> activities = actDao.findDayByUUID(uuid, time);
		if(activities.size()>0){
			return true;
		}else{
			return false;
		}
	}
}
