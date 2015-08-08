package org.springside.fi.service.running;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.fi.entity.Relationship;
import org.springside.fi.entity.Runner;
import org.springside.fi.web.exception.RestExceptionCode;
import org.springside.fi.web.vo.attention.AttentionVo;

/**
 * @author tunding:wzc@tcl.com
 * @description 关注相关接口服务
 * @version 1.0
 * @date 创建时间：2015年8月5日 下午6:54:55
 */
@Service
public class AttentionService {
	@Autowired
	private RunnerService runnerService;
	@Autowired
	private RelationshipService relationshipService;
	 // 不发送消息，只在本地数据库关注对方,存储表明当前用户关注了哪个用户
	public void attentionRelationship(long id, String passiveAttentionUuid, AttentionVo attentionVo){
		String attentionUuid = getUuid(id);
		if(relationshipService.isAttention(id, passiveAttentionUuid)){
			attentionVo.setResult(RestExceptionCode.REST_ALREADY_ATTENTION_CODE);
			attentionVo.setData(RestExceptionCode.REST_ALREADY_ATTENTION_MSG);
		}else{
			relationshipService.saveAttention(attentionUuid, passiveAttentionUuid);
			attentionVo.setResult(RestExceptionCode.REST_ATTENTION_SUCCESS_CODE);
			attentionVo.setData(RestExceptionCode.REST_ATTENTION_SUCCESS_MSG);
		}
	}
	
	//假删除本地关注记录
	public void removeRelationshipFlag(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		removeAttentionFlag(attentionUuid, passiveAttentionUuid);
	}
	private void removeAttentionFlag(String attentionUuid, String passiveAttentionUuid){
		Relationship relationship = relationshipService.getRelationship(attentionUuid, passiveAttentionUuid);
		relationship.setDelFlag(0);
		relationshipService.removeAttention(relationship);
	}
	
	//返回我关注的好友列表
	public List<Runner> iattention(long id){
		String attentionUuid = getUuid(id);
		boolean iorme = true;
		List<Relationship> relationships = relationshipService.getRelationships(attentionUuid, iorme);
		List<Runner> friends = relationshipService.getFriends(relationships, iorme);
		return friends;
	}
	//返回关注我的好友列表
	public List<Runner> attentionme(long id){
		String attentionUuid = getUuid(id);
		boolean iorme = false;
		List<Relationship> relationships = relationshipService.getRelationships(attentionUuid, iorme);
		List<Runner> friends = relationshipService.getFriends(relationships, iorme);
		return friends;
	}
	
	//通过id获取用户的uuid
	private String getUuid(long id){
		Runner runner = runnerService.getRunner(id);
		return runner.getUuid();
	}
}
