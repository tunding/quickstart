package org.springside.examples.quickstart.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springside.examples.quickstart.entity.Runner;

@Repository
public class RunnerDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Runner> findRunner(String userGeoHash, Map<String, String> searchParams){
		String query = buildAndConjunction(searchParams);
		String QUERY_RUNNER = "select * from runner_user" + query;
		return jdbcTemplate.queryForList(QUERY_RUNNER, Runner.class);
	}
	private String buildAndConjunction(Map<String, String> searchParams){
		if(searchParams.size()==0){
			return "";
		}
		StringBuffer queryString = new StringBuffer();
		for (String key : searchParams.keySet()) {
			queryString.append(searchParams.get(key)+" and ");
		}
		if(queryString.length()>0){
			queryString = queryString.delete(queryString.length()-5, queryString.length());
		}
		return " where " + queryString;
	}
}
