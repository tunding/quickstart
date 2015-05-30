package org.springside.examples.quickstart.service.running;

import java.text.ParseException;
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
import org.springside.examples.quickstart.entity.GpsRunnerInfo;
import org.springside.examples.quickstart.entity.Runner;
import org.springside.examples.quickstart.map.common.Geohash;
import org.springside.examples.quickstart.map.module.LatLng;
import org.springside.examples.quickstart.map.utils.DistanceUtil;
import org.springside.examples.quickstart.repository.GpsRunnerInfoDao;
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
	private GpsRunnerInfoDao gpsRunnerInfoDao;
	
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
		List<GpsRunnerInfo> gpsrunnerinfos = getGeoHash(userGeoHash);
		ArrayList<Runner> runners = new ArrayList<Runner>();
		for(GpsRunnerInfo gpsrunnerinfo : gpsrunnerinfos) {
			String uuid = gpsrunnerinfo.getUuid();
			Runner runner = runnerDao.findByUUID(uuid);
			if(loginName.equals(runner.getLoginName())){
				continue;
			}
			if(StringUtils.isNotEmpty(sex)&&!sex.equals(String.valueOf(runner.getSex()))){
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
				Date start = new Date(now.getTime()- Long.valueOf(time) * 60 * 1000);
				try {
					if(start.after(df.parse(String.valueOf(gpsrunnerinfo.getLastUpdateTime())))){
						continue;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			double apart_distance = getDistance(latitude, longitude, gpsrunnerinfo.getLatitude(), gpsrunnerinfo.getLongitude());
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
	
	private List<GpsRunnerInfo> getGeoHash(String userGeoHash){
		return gpsRunnerInfoDao.findByGeohash(userGeoHash.subSequence(0, 4).toString());
	}
	
	public String UpdateGeoHash(String LongName, String longitude, String latitude){
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		try{
			Runner runner = accountService.findUserByLoginName(LongName);
			GpsRunnerInfo gpsrunnerinfo = new GpsRunnerInfo();
			gpsrunnerinfo.setUuid(runner.getUuid());;
			gpsrunnerinfo.setLatitude(latitude);
			gpsrunnerinfo.setLongitude(longitude);
			gpsrunnerinfo.setGeohash(userGeoHash);
			gpsRunnerInfoDao.save(gpsrunnerinfo);
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		return userGeoHash;
	}
	
}
