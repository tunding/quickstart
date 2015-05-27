package org.springside.examples.quickstart.service.running;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springside.examples.quickstart.entity.Runner;

@Service
public class RunnerService {
	public List<Runner> getAllRunner(int pageNumber, int pageSize, Map<String, Object> searchParams, String longitude, String latitude){
		List<Runner> result;
		return result;
	}
}
