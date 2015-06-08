package org.springside.fi.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.fi.entity.Runner;

public interface RunnerDao extends PagingAndSortingRepository<Runner, Long>, JpaSpecificationExecutor<Runner> {
	@Query("from Runner where uuid=?1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public Runner findByUUID(String uuid);
}
