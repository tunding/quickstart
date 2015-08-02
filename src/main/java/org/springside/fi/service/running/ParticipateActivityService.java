package org.springside.fi.service.running;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.fi.entity.Participate;
import org.springside.fi.repository.ParticipateDao;

/**
 * @author tunding:wzc@tcl.com
 * @description
 * @version 1.0
 * @date 创建时间：2015年8月2日 上午11:22:53
 */
@Service
@Transactional("transactionManager")
public class ParticipateActivityService {
	@Autowired
	private ParticipateDao participateDao;
	/**
	 * @param uuid
	 * @param actuuid
	 * @return 返回participate对象,无此对象则新建participate对象
	 */
	public Participate getParticipate(String uuid, String actuuid){
		List<Participate> parts = participateDao.findpart(uuid, actuuid);
		if(parts.size()>0){
			return parts.get(0);
		}else{
			return new Participate();
		}
	}
}
