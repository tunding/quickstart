package org.springside.examples.quickstart.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springside.examples.quickstart.entity.Activity;

@Repository
public class ActivityDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Activity> findActivity(String userGeoHash, String query){
		String QUERY_ACT = "select * from activity_info " + query +" and geohash_code like "+ userGeoHash + "%";
		return jdbcTemplate.queryForList(QUERY_ACT, Activity.class);
	}
	
	public Activity findById(Long id){
		String QUERY_ACT = "select * from activity_info where id = " + id;
		return jdbcTemplate.queryForObject(QUERY_ACT, Activity.class);
	}
	
	public void saveActivity(Activity act){
		jdbcTemplate.update("insert into activity_info(address, time, info, longitude, latitude, kilometer, geohash_code)"
				+ " values(?, ?, ?, ?, ?, ?, ?)", 
				act.getAddress(),
				act.getTime(),
				act.getInfo(),
				act.getLongitude(),
				act.getLatitude(),
				act.getKilometer(),
				act.getGeohashCode());
	}
}
