package org.springside.examples.quickstart.service.running;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.GpsInfo;
import org.springside.examples.quickstart.entity.Runner;
import org.springside.examples.quickstart.map.common.Geohash;
import org.springside.examples.quickstart.map.module.LatLng;
import org.springside.examples.quickstart.map.utils.DistanceUtil;
import org.springside.examples.quickstart.repository.GpsInfoDao;
import org.springside.examples.quickstart.repository.RunnerDao;
import org.springside.examples.quickstart.service.account.AccountService;

@Service
@Transactional("transactionManager")
public class RunnerService {
	@Autowired
	private AccountService accountService;
	@Autowired
	private RunnerDao runnerDao;
	@Autowired
	private GpsInfoDao gpsInfoDao;
	
	public List<Runner> getAllRunner(String loginName, String longitude, String latitude, int distance, int pageNumber, int pageSize, String sex, String age, String time, String sort){
		if(StringUtils.isEmpty(longitude)){
			return null;
		}
		if(StringUtils.isEmpty(latitude)){
			return null;
		}
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		List<GpsInfo> gpsinfos = getGeoHash(userGeoHash);
		ArrayList<Runner> runners = new ArrayList<Runner>();
		for(GpsInfo gpsinfo : gpsinfos) {
			String uuid = gpsinfo.getUuid();
			Runner runner = runnerDao.findByUUID(uuid);
			if(loginName.equals(runner.getLoginName())){
				continue;
			}
			if(StringUtils.isNotEmpty(sex)&&!sex.equals(runner.getSex())){
				continue;
			}
			if(StringUtils.isNotEmpty(age)){
				String[] rangeAge = age.split(" ");
				int runner_age = runner.getAge().intValue();
				if(runner_age<Integer.valueOf(rangeAge[0])||runner_age>Integer.valueOf(rangeAge[1])){
					continue;
				}
			}
			if(StringUtils.isNotEmpty(time)){
				Date now = new Date();
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				time = df.format(new Date(now.getTime()- Long.valueOf(time) * 60 * 1000));
				if(java.sql.Date.valueOf(time).after(java.sql.Date.valueOf(String.valueOf(runner.getLastUpdateTime())))){
					continue;
				}
			}
			double apart_distance = getDistance(latitude, longitude, gpsinfo.getLatitude(), gpsinfo.getLongitude());
			runner.setDistance(apart_distance);
			if(distance<apart_distance){
				continue;
			}
			runners.add(runner);
		}
		//排序distance，升序
		sortByDistance(runners);
		//查询记录范围
		int start = (pageNumber-1) * pageSize;
		int end   = pageNumber*pageSize - 1;
		int size  = runners.size();
		if(end<size){
			return runners.subList(start, end);
		}else if(start<size){
			return runners.subList(start, size);
		}else{
			return null;
		}
	}
	
	private void sortByDistance(ArrayList<Runner> runners){
		Comparator<Runner> comparator = new Comparator<Runner>() {
			
			@Override
			public int compare(Runner r1, Runner r2) {
				// TODO Auto-generated method stub
				if(r1.getDistance()<r2.getDistance()){
					return -1;
				}if(r1.getDistance()==r2.getDistance()){
					return 0;
				}else{
					return 1;
				}
			}
		};
		Collections.sort(runners,comparator);
	}
	
	private double getDistance(String cen_latitude, String cen_longitude, String run_latitude, String run_longitude){
		LatLng cen_latlng = new LatLng(Double.valueOf(cen_latitude), Double.valueOf(cen_longitude));
		LatLng run_latlng = new LatLng(Double.valueOf(run_latitude), Double.valueOf(run_longitude));
		return DistanceUtil.getDistance(cen_latlng, run_latlng);
	}
	
	private List<GpsInfo> getGeoHash(String userGeoHash){
		return gpsInfoDao.findByGeohash(userGeoHash.subSequence(0, 4).toString());
	}
	
	public String UpdateGeoHash(String LongName, String longitude, String latitude){
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		try{
			Runner runner = accountService.findUserByLoginName(LongName);
			GpsInfo gpsinfo = new GpsInfo();
			gpsinfo.setUuid(runner.getUuid());;
			gpsinfo.setLatitude(latitude);
			gpsinfo.setLongitude(longitude);
			gpsinfo.setGeohash(userGeoHash);
			gpsInfoDao.save(gpsinfo);
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		return userGeoHash;
	}
	
}
