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
	/**
	 * @description 根据geohash值返回某时刻之后开始的有效活动 
	 */
	@Query("from GpsActivityInfo where delFlag=1 and geohash like ?1% and start_time>?2")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<GpsActivityInfo> findByGeohash(String geohash, Date strtTime);
	
	@Query("from GpsActivityInfo where actuuid=?1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<GpsActivityInfo> findByActUUID(String actuuid);
}
