package org.springside.examples.quickstart.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.Activity;

public interface ActivityDao extends JpaSpecificationExecutor<Activity>,
		PagingAndSortingRepository<Activity, Long> {
	@Query("from Activity where uuid=?1 and state=1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Activity> findSelfTodayByUUID(String uuid);
	
	@Query("from Activity where uuid=?1 and state=1 and time>?2")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Activity> findTodayByUUID(String uuid, Date now);
	
	@Query("from Activity where uuid=?1 and time<?2")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Activity> findHistoryByUUID(String uuid, Date now);
}
