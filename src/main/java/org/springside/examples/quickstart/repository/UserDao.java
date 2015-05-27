package org.springside.examples.quickstart.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.Runner;

public interface UserDao extends PagingAndSortingRepository<Runner, Long> {
	Runner findByLoginName(String loginName);
}
