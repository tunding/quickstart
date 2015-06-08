package org.springside.fi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.fi.entity.Runner;

public interface UserDao extends PagingAndSortingRepository<Runner, Long> {
	Runner findByLoginName(String loginName);
}
