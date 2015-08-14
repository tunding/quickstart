package org.springside.fi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springside.fi.entity.Participate;


@Repository
public interface ParticipateDao extends 
	PagingAndSortingRepository<Participate, Long>,
	JpaSpecificationExecutor<Participate>{
	
	@Query("from Participate where uuid=?1 and actuuid=?2")
	public List<Participate> findpart(String uuid, String activityId);
	
	@Query("from Participate where actuuid=?1 and delFlag=1")
	public List<Participate> findByActuuid(String actuuid);
	
	@Query(value="select * from activity_participate where uuid=?1 and del_flag=1 order by create_time desc limit ?2, ?3", nativeQuery=true)
	public List<Participate> findByUuid(String uuid, int start, int size);
}
