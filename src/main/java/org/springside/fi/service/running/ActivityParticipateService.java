package org.springside.fi.service.running;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.fi.entity.Participate;
import org.springside.fi.repository.ParticipateDao;

/**
 * @author tunding:wzc@tcl.com
 * @description
 * @version 1.0
 * @date 创建时间：2015年8月5日 下午4:23:08
 */
@Service
@Transactional("transactionManager")
public class ActivityParticipateService  extends BaseService {
	@Autowired
	private ParticipateDao partDao;
	@Autowired
	private ParticipateActivityService partActService;
	/**
	 * @param uuid
	 * @param actuuid
	 * @description 参与活动
	 */
	public void participateIn(String uuid, String actuuid){
		Participate part = partActService.getParticipate(uuid, actuuid);
		part.setActuuid(actuuid);
		part.setUuid(uuid);
		part.setDelFlag(1);
		partDao.save(part);
	}
	/**
	 * @param uuid
	 * @param actuuid
	 * @description 取消参与活动，假删除
	 */
	public void participateOut(String uuid, String actuuid){
		Participate part = partActService.getParticipate(uuid, actuuid);
		part.setDelFlag(0);
		partDao.save(part);
	}
}
