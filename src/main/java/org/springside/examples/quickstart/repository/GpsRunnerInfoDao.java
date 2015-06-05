package org.springside.examples.quickstart.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.GpsRunnerInfo;

public interface GpsRunnerInfoDao extends PagingAndSortingRepository<GpsRunnerInfo, Long>, JpaSpecificationExecutor<GpsRunnerInfo> {
	@Query("from GpsRunnerInfo where geohash like ?1%")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<GpsRunnerInfo> findByGeohash(String geohash);
	
	@Query("from GpsRunnerInfo where uuid=?1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public GpsRunnerInfo findByUUID(String uuid);
}
