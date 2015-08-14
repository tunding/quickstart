package org.springside.fi.service.running;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.fi.entity.Activity;
import org.springside.fi.entity.GpsActivityInfo;
import org.springside.fi.entity.GpsRunnerInfo;
import org.springside.fi.entity.Participate;
import org.springside.fi.entity.Runner;
import org.springside.fi.map.common.Geohash;
import org.springside.fi.map.module.LatLng;
import org.springside.fi.map.utils.DistanceUtil;
import org.springside.fi.repository.ActivityDao;
import org.springside.fi.repository.GpsActivityInfoDao;
import org.springside.fi.repository.ParticipateDao;
import org.springside.fi.repository.RunnerDao;
import org.springside.fi.rest.RestErrorCode;
import org.springside.fi.service.third.RongCloudService;
import org.springside.fi.web.params.NearActivityListParam;
import org.springside.fi.web.vo.DelActivityVo;



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
	@Autowired
	private RunnerService runnerService;
	@Autowired
	private RongCloudService rongCloudService;
	/**
	 * @param distance 活动发起地点距离的最大范围
	 * @param time 活动开始时间
	 * @param sort 排序字段，一般都为空，自动以距离作为返回
	 * @return 附近活动列表
	 */
	public HashMap<String, Object> getAllActivity(long user_id, NearActivityListParam nearActListParam, Date startTime){
		int distance      = nearActListParam.getDistance();
		int pageNumber    = nearActListParam.getPageNum();
		int pageSize      = nearActListParam.getPageSize();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Runner runner = getRunner(user_id);
		runner.setLastUpdateTime(new Date());//更新runner最后一次在线时间
		String longitude  = nearActListParam.getLongitude();
		String latitude   = nearActListParam.getLatitude();
		if(StringUtils.isBlank(longitude) || StringUtils.isBlank(latitude)){
			GpsRunnerInfo gps = runnerService.getGpsRunnerInfo(runner.getUuid());
			longitude = gps.getLongitude();
			latitude = gps.getLatitude();
			if(StringUtils.isBlank(longitude) || StringUtils.isBlank(latitude)){
				map.put("total", 0);
				map.put("acts", new ArrayList<Activity>());
				return map;
			}
		}
		/*
		 * 目标地址geohash值
		 */
		String actGeoHash = getGps(longitude, latitude);
		/*
		 * geohash值取得附近九块地区的时间有效所有活动的gps信息（包括当前用户创建到活动）
		 */
		List<GpsActivityInfo> gpsactinfos = getGeoHash(actGeoHash, startTime);
		/*
		 * 遍历geohash返回的附近活动信息，筛选符合条件的插入到activities
		 */
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activityAdd(longitude, latitude, distance, runner, gpsactinfos, activities);
		/*
		 * 返回结果中包含，附近活动数量
		 */
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
			map.put("acts", new ArrayList<Activity>());
			return map;
		}
	}
	/**
	 * @description 向activities中添加有效的活动内容
	 */
	private void activityAdd(String longitude, String latitude, int distance,
			Runner runner, List<GpsActivityInfo> gpsactinfos,
			ArrayList<Activity> activities) {
		for(GpsActivityInfo gpsactinfo : gpsactinfos){
			String actuuid = gpsactinfo.getActuuid();
			Activity act = activityDao.findByACTUUID(actuuid).get(0);
			/*
			 * 计算activity和给定地点的实际距离，只有在给定distance范围内的活动才会被插入到activities
			 */
			double apart_distance = getDistance(latitude, longitude, gpsactinfo.getLatitude(), gpsactinfo.getLongitude());
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
			act.setPublishName(runnerDao.findByUUID(act.getUuid()).getName());
			activities.add(act);
		}
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
	private List<GpsActivityInfo> getGeoHash(String actGeoHash, Date startTime){
		return gpsActivityInfoDao.findByGeohash(actGeoHash.subSequence(0, 4).toString(), startTime);
	}
	
	/**
	 * @param uuid
	 * @param pageNumber
	 * @param pageSize
	 * @return 用户所有发布的活动
	 */
	public HashMap<String, Object> getPublicHistoryActivity(String uuid, int pageNumber, int pageSize){
//		List<Activity> activities = activityDao.findHistoryByUUID(uuid, now);
		int start = (pageNumber-1) * pageSize;
		int size  = pageSize;
		ArrayList<Activity> activities = (ArrayList<Activity>) activityDao.findHistoryByUUID(uuid, start, size);
		for(Activity act : activities){
			act.setParticipateFlag(1);
			List<Participate> parts = participateDao.findByActuuid(act.getActuuid());
			Integer participateCount = parts.size();
			act.setParticipateCount(participateCount);
			act.setPublishName(runnerDao.findByUUID(act.getUuid()).getName());//发布者姓名
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(activities.size()>0){
			map.put("acts", activities);
		}else{
			map.put("acts", new ArrayList<Activity>());
		}
		return map;
	}
	
	/**
	 * @param uuid
	 * @param pageNumber
	 * @param pageSize
	 * @return 用户所有参与活动，按时间排序
	 */
	public HashMap<String, Object> getParticipateHistoryActivity(String uuid, int pageNumber, int pageSize){
		int start = (pageNumber-1) * pageSize;
		int size  = pageSize;
		ArrayList<Activity> activities = getActByPart(uuid, start, size);
		for(Activity act : activities){
			act.setParticipateFlag(1);
			List<Participate> parts = participateDao.findByActuuid(act.getActuuid());
			Integer participateCount = parts.size();
			act.setParticipateCount(participateCount);
			act.setPublishName(runnerDao.findByUUID(act.getUuid()).getName());//发布者姓名
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(activities.size()>0){
			map.put("acts", activities);
		}else{
			map.put("acts", new ArrayList<Activity>());
		}
		return map;
	}
	/**
	 * @param uuid
	 * @return 返回当前用户参与过的所有活动，按时间排序
	 */
	private ArrayList<Activity> getActByPart(String uuid, int start, int size) {
		ArrayList<Activity> activities = new ArrayList<Activity>();
		List<Participate> parts= participateDao.findByUuid(uuid, start, size);
		for(Participate part : parts){
			String actuuid = part.getActuuid();
			Activity act = activityDao.findByACTUUID(actuuid).get(0);
			activities.add(act);
		}
		return activities;
	}
	
	/**
	 * @param actuuid
	 * 删除发布的有效活动，删除活动gps信息，删除活动所有参与人的信息
	 */
	public void delActivity(String uuid, String actuuid, String msg, DelActivityVo delActivityVo){
		List<Activity> activities = activityDao.findByACTUUID(actuuid);
		if(activities.size()>0){
			Activity act = activities.get(0);
			try{
				deletePGA(uuid, actuuid, act, msg);
				delActivityVo.setResult(RestErrorCode.REST_SUCCESS_CODE);
				delActivityVo.setData(RestErrorCode.REST_SUCCESS_MSG);
			}catch(RuntimeException e){
				delActivityVo.setResult(RestErrorCode.REST_INTERNAL_ERROR_CODE);
				delActivityVo.setData(RestErrorCode.REST_INTERNAL_ERROR_MSG);
			}
		}else{
			delActivityVo.setResult(RestErrorCode.REST_SUCCESS_CODE);
			delActivityVo.setData("此活动不存在");
		}
	}
	/**
	 * @param actuuid
	 * @param act
	 * @description 删除 参与者 part、活动gps gps、活动 act
	 */
	private void deletePGA(String uuid, String actuuid, Activity act, String msg) {
		List<Participate> parts = participateDao.findByActuuid(actuuid);
		for(Participate part : parts){
			part.setDelFlag(0);
			participateDao.save(part);
			if(!uuid.equals(part.getUuid())){
				rongCloudService.sendPartCancel(uuid, part.getUuid(), msg);
			}
		}
		List<GpsActivityInfo> gpsActInfo = gpsActivityInfoDao.findByActUUID(actuuid);
		if(gpsActInfo.size()>0){
			GpsActivityInfo gps = gpsActInfo.get(0);
			gps.setDelFlag(0);
			gpsActivityInfoDao.save(gps);
		}
		act.setDelFlag(0);
		activityDao.save(act);
	}
	/**
	 * @param uuid
	 * @param actTime
	 * @return
	 * @description 判断未来某一天当前用户是否已经发布活动，发布则返回true，不允许用户再次发布活动
	 * 判断同一天,数据库TO_DAYS函数
	 */
	public boolean findDayActivityByUUID(String uuid, Date date){
		if(activityDao.findDayByUUID(uuid, date).size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取用户当前对象
	 */
	private Runner getRunner(Long id){
		return runnerDao.findOne(id);
	}

}
