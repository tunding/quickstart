package org.springside.examples.quickstart.service.running;

import java.text.DateFormat;
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
import org.springside.examples.quickstart.entity.Activity;
import org.springside.examples.quickstart.map.common.FilterOperator;
import org.springside.examples.quickstart.map.common.FilterOperator.Operator;
import org.springside.examples.quickstart.map.common.Geohash;
import org.springside.examples.quickstart.map.module.LatLng;
import org.springside.examples.quickstart.map.utils.DistanceUtil;
import org.springside.examples.quickstart.repository.ActivityDao;
import org.springside.examples.quickstart.repository.ParticipateDao;



@Service
public class ActivityService extends BaseService{
	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private ParticipateDao participateDao;
	
	public List<Activity> getAllActivity(int pageNumber, int pageSize, Map<String, Object> searchParams, String longitude, String latitude){
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		String queryWhere = genQueryWhere(searchParams);
		//5位的编码能表示10平方千米范围的矩形区域
		List<Activity> result = activityDao.findActivity(userGeoHash.subSequence(0, userGeoHash.length()-1).toString(), queryWhere);
		sortActivity(result, lat, lon);
		//查询记录范围,先不分页
		int start = (pageNumber-1) * pageSize;
		int end   = pageNumber*pageSize - 1;
		return result;
	}
	
	public void participate(String uuid, String activityId, String opt){
		if("in".equals(opt)){
			String now = genCurrentTime();
			//participateDao.newParticipate(uuid, activityId, now);
		}else if("out".equals(opt)){
			participateDao.delParticipate(uuid, activityId);
		}
	}
	
	public void saveActivity(String address, Date time, String info, String lon, String lat, int kilometer){
/*		act.setAddress(address);
		act.setTime(time);
		act.setInfo(info);
		act.setLongitude(lon);
		act.setLatitude(lat);
		act.setKilometer(kilometer);
		String geohashCode = new Geohash().encode(Double.valueOf(lat), Double.valueOf(lon));
		act.setGeohashCode(geohashCode);
		activityDao.saveActivity(act);*/
	}
	
	public Activity getActivity(Long id){
		return activityDao.findById(id);
	}
	public Activity getNewActivity(){
		return new Activity();
	}
	
	private String genQueryWhere(Map<String, Object> searchParams){

		StringBuffer queryString = new StringBuffer();
		for (Entry<String, Object> entry : searchParams.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.isBlank((String) value)) {
				continue;
			}
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);
			FilterOperator filter = new FilterOperator(filedName, operator, value);
			queryString.append(filter.querySubString + " and ");
		}
		if(queryString.length()>0){
			queryString = queryString.delete(queryString.length()-5, queryString.length());
		}
		//当前系统时间
		String time = genCurrentTime();

		return " where time > "+time +" and "+queryString;
	}
	private String genCurrentTime(){
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	private void sortActivity(List<Activity> result, double lat, double lon){
		LatLng latlng_center = new LatLng(lat, lon);
		for(Activity act : result){
			String latitude = act.getLatitude();
			String longitude= act.getLongitude();
			LatLng latlng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
			
			double distance = DistanceUtil.getDistance(latlng_center, latlng);
			act.setDistance(distance);
		}
		Collections.sort(result, new Comparator<Activity>() {
			@Override
			public int compare(Activity o1, Activity o2) {
				// TODO Auto-generated method stub
				if(o1.getDistance()>o2.getDistance()){
					return 1;
				}else{
					return 0;
				}
			}
		});
	}
}
