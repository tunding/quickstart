package org.springside.fi.service.running;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.fi.entity.Runner;

@Service
@Transactional("transactionManager")
public class RelationshipService {
	@Autowired
	private RunnerService runnerService;
	
	public void updateRelationship(long id, String opt, String passiveAttentionUuid) {
		Runner runner = runnerService.getRunner(id);
		String attentionUuid = runner.getUuid();
	}
}
