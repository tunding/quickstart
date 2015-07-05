package org.springside.fi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springside.fi.entity.Participate;


@Repository
public interface ParticipateDao extends 
	PagingAndSortingRepository<Participate, Long>,
	JpaSpecificationExecutor<Participate>{
	
	@Modifying
	@Query("from Participate where uuid=?1 and actuuid=?2")
	public List<Participate> findpart(String uuid, String activityId);
	
	@Modifying
	@Query("from Participate where actuuid=?1")
	public List<Participate> findByActuuid(String actuuid);
	
	@Modifying
	@Query("from Participate where uuid=?1")
	public List<Participate> findByUuid(String uuid);
}
