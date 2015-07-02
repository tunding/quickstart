package org.springside.fi.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.fi.entity.GpsActivityInfo;

public interface GpsActivityInfoDao extends PagingAndSortingRepository<GpsActivityInfo, Long>,
		JpaSpecificationExecutor<GpsActivityInfo> {
	@Query("from GpsActivityInfo where geohash like ?1% and time>?2")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<GpsActivityInfo> findByGeohash(String geohash, Date time);
	
	@Query("from GpsActivityInfo where actuuid=?1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<GpsActivityInfo> findByActUUID(String actuuid);
}
