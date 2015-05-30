package org.springside.examples.quickstart.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.Activity;

public interface ActivityDao extends JpaSpecificationExecutor<Activity>,
		PagingAndSortingRepository<Activity, Long> {
	@Query("from Activity where uuid=?1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Activity> findByUUID(String uuid);
}
