package org.springside.fi.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.fi.entity.Relationship;

public interface RelationshipDao extends
		PagingAndSortingRepository<Relationship, Long>,
		JpaSpecificationExecutor<Relationship>{

}
