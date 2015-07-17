package org.springside.fi.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.fi.entity.Relationship;

public interface RelationshipDao extends
		PagingAndSortingRepository<Relationship, Long>,
		JpaSpecificationExecutor<Relationship>{

	@Query("from Relationship where attentionUuid=?1 and passiveAttentionUuid=?2")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Relationship> findRelationship(String attentionUuid, String passiveAttentionUuid);
	
	@Query("from Relationship where attentionUuid=?1 and passiveAttentionUuid=?2 and state=0")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Relationship> findRelationshipBlack(String attentionUuid, String passiveAttentionUuid);
	
	@Query("from Relationship where attentionUuid=?1 and passiveAttentionUuid=?2 and state=1 and delFlag=1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Relationship> findRelationshipFriend(String attentionUuid, String passiveAttentionUuid);
	
	@Query("from Relationship where attentionUuid=?1 and state=1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Relationship> findListFriend(String attentionUuid);
	
	@Query("from Relationship where attentionUuid=?1 and state=1 and delFlag=1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Relationship> findIAttention(String attentionUuid);
	
	@Query("from Relationship where passiveAttentionUuid=?1 and state=1 and delFlag=1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<Relationship> findAttentionMe(String passiveAttentionUuid);
	
}
