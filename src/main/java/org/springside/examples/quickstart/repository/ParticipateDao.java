package org.springside.examples.quickstart.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springside.examples.quickstart.entity.Participate;


@Repository
public interface ParticipateDao extends 
	PagingAndSortingRepository<Participate, Long>,
	JpaSpecificationExecutor<Participate>{
	
	@Modifying
	@Query("delete from Participate where uuid=?1 and activityId=?2")
	public void delParticipate(String uuid, String activityId);
}
