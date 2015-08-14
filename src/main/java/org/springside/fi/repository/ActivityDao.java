package org.springside.fi.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.fi.entity.Activity;

public interface ActivityDao extends JpaSpecificationExecutor<Activity>,
		PagingAndSortingRepository<Activity, Long> {
	@Query("from Activity where actuuid=?1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Activity> findByACTUUID(String actuuid);
	
	@Query("from Activity where uuid=?1 and delFlag=1 and TO_DAYS(time)=TO_DAYS(?2)")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Activity> findDayByUUID(String uuid, Date starttime);
	
//	@Query("from Activity where uuid=?1 and time<?2")
//	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
//	public List<Activity> findHistoryByUUID(String uuid, Date now);
	@Query(value="select * from activity_info where uuid=?1 and del_flag=1 order by time desc limit ?2, ?3", nativeQuery=true)
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Activity> findHistoryByUUID(String uuid, int start, int size);
}
