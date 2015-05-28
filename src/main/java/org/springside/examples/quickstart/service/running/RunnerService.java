package org.springside.examples.quickstart.service.running;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.Runner;
import org.springside.examples.quickstart.map.common.Geohash;
import org.springside.examples.quickstart.repository.RunnerDao;

@Service
@Transactional("transactionManager")
public class RunnerService {
	@Autowired
	private RunnerDao runnerDao;
	
	public List<Runner> getAllRunner(String longitude, String latitude, int distance, int pageNumber, int pageSize, String sex, String age, String time, String sort){
		Map<String, String> searchParams = new HashMap<String, String>();
		if(StringUtils.isEmpty(longitude)){
			return null;
		}
		if(StringUtils.isEmpty(latitude)){
			return null;
		}
		double lat = Double.valueOf(latitude);
		double lon = Double.valueOf(longitude);
		String userGeoHash = new Geohash().encode(lat, lon);
		
		searchParams.put("geohash", "geohash_code like "+userGeoHash.subSequence(0, userGeoHash.length()-1).toString()+"%");
		
		if(StringUtils.isNotEmpty(sex)){
			searchParams.put("sex", "sex="+sex);
		}
		if(StringUtils.isNotEmpty(age)){
			String[] rangeAge = age.split(" ");
			searchParams.put("age", " age>="+rangeAge[0]+" and "+"age<="+rangeAge[0]);
		}
		if(StringUtils.isNotEmpty(time)){
			Date now = new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = df.format(new Date(now.getTime()- Long.valueOf(time) * 60 * 1000));
			searchParams.put("last_update_time", "last_update_time>="+time);
		}
		if(StringUtils.isNotEmpty(sort)){
			String[] sortStr = sort.split(" ");
			String sortField = sortStr[0];
			String sortOrder = sortStr[1];
			searchParams.put("sort", "order by "+sortField+" "+sortOrder);
		}
		//查询记录范围
		int start = (pageNumber-1) * pageSize;
		int end   = pageNumber*pageSize - 1;
		
		List<Runner> runners = runnerDao.findRunner(userGeoHash, searchParams);
		return runners;
	}
	
}
