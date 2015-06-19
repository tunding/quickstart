package org.springside.fi.service.running;


import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.fi.common.SystemGlobal;
import org.springside.fi.common.httpclient.HttpClientTemplate;
import org.springside.fi.entity.Relationship;
import org.springside.fi.entity.Runner;
import org.springside.fi.repository.RelationshipDao;
import org.springside.fi.service.third.BaseThirdService;
import org.springside.fi.service.third.object.RongCloudBlacklist;
import org.springside.modules.mapper.JsonMapper;

@Service
@Transactional("transactionManager")
public class RelationshipService extends BaseThirdService{
	private final String RongHOSTBlacklistAdd = SystemGlobal.getConfig("RongCloudAPI")+"user/blacklist/add.json";
	private final String RongHOSTBlacklistRemove = SystemGlobal.getConfig("RongCloudAPI")+"user/blacklist/remove.json";
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@Autowired
	private HttpClientTemplate httpClientTemplate;
	@Autowired
	private RunnerService runnerService;
	@Autowired
	private RelationshipDao relationshipDao;
	
	public String updateRelationship(long id, String opt, String passiveAttentionUuid) {
		String attentionUuid = getUuid(id);
		if(opt.equals("add")){
			if(isFriend(id, passiveAttentionUuid)){
				
			}else if(isblack(id, passiveAttentionUuid)){
				
			}else{
				attention(attentionUuid, passiveAttentionUuid);
			}
		}else if(opt.equals("remove")){
			try{
				if(isblack(id, passiveAttentionUuid)){
					removeBlackList(attentionUuid, passiveAttentionUuid);
				}
				if(reverseIsBlack(id, passiveAttentionUuid)){
					removeBlackList(passiveAttentionUuid, attentionUuid);
				}
				removeAttention(attentionUuid, passiveAttentionUuid);
			}catch(RuntimeException e){
				e.printStackTrace();
			}
		}
	}
	
	public String submitBlack(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		List<Relationship> relationships = relationshipDao.findRelationshipFriend(attentionUuid, passiveAttentionUuid);
		String res = addBlackList(attentionUuid, passiveAttentionUuid);
		try{
			RongCloudBlacklist blacklist = jsonMapper.fromJson(res, RongCloudBlacklist.class);
			if(blacklist.getCode().equals("200")){
				Relationship relationship = relationships.get(0);
				relationship.setState(0);
				relationshipDao.save(relationship);
			}
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		return res;
	}
	
	public boolean removeBlack(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		List<Relationship> relationships = relationshipDao.findRelationshipBlack(attentionUuid, passiveAttentionUuid);
		String res = removeBlackList(attentionUuid, passiveAttentionUuid);
		try{
			RongCloudBlacklist blacklist = jsonMapper.fromJson(res, RongCloudBlacklist.class);
			if(blacklist.getCode().equals("200")){
				Relationship relationship = relationships.get(0);
				relationship.setState(1);
				relationshipDao.save(relationship);
			}
		}catch(RuntimeException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean isblack(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		List<Relationship> relationships = relationshipDao.findRelationshipBlack(attentionUuid, passiveAttentionUuid);
		if(relationships.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean reverseIsBlack(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		List<Relationship> relationships = relationshipDao.findRelationshipBlack(passiveAttentionUuid, attentionUuid);
		if(relationships.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isFriend(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		List<Relationship> relationships = relationshipDao.findRelationshipFriend(attentionUuid, passiveAttentionUuid);
		if(relationships.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	private void removeAttention(String attentionUuid, String passiveAttentionUuid){
		Relationship relationship = getRelationship(attentionUuid, passiveAttentionUuid);
		relationshipDao.delete(relationship);
		relationship = getRelationship(passiveAttentionUuid, attentionUuid);
		relationshipDao.delete(relationship);
	}
	
	private String addBlackList(String userId, String blackUserId){
		String reqParams = "userId="+userId+"&blackUserId="+blackUserId;
		StringRequestEntity requestEntity = null;
		try{
			requestEntity = new StringRequestEntity(reqParams, "application/x-www-form-urlencoded", "UTF-8");
			return convertResult(httpClientTemplate.executePostMethod(RongHOSTBlacklistAdd, requestEntity, null));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private String removeBlackList(String userId, String blackUserId){
		String reqParams = "userId="+userId+"&blackUserId="+blackUserId;
		StringRequestEntity requestEntity = null;
		try{
			requestEntity = new StringRequestEntity(reqParams, "application/x-www-form-urlencoded", "UTF-8");
			return convertResult(httpClientTemplate.executePostMethod(RongHOSTBlacklistRemove, requestEntity, null));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private void attention(String attentionUuid, String passiveAttentionUuid){
		Relationship relationship = getRelationship(attentionUuid, passiveAttentionUuid);
		relationship.setAttentionUuid(attentionUuid);
		relationship.setPassiveAttentionUuid(passiveAttentionUuid);
		relationshipDao.save(relationship);
	}

	
	
	/**
	 * @param attentionUuid 主动uuid
	 * @param passiveAttentionUuid 被动uuid
	 * 获取好友对象
	 */
	private Relationship getRelationship(String attentionUuid, String passiveAttentionUuid){
		List<Relationship> relationships = relationshipDao.findRelationship(attentionUuid, passiveAttentionUuid);
		if(relationships.size()>0){
			return relationships.get(0);
		}else{
			return new Relationship();
		}
	}
	/**
	 * @param id
	 * 获取uuid
	 */
	private String getUuid(long id){
		Runner runner = runnerService.getRunner(id);
		return runner.getUuid();
	}
	
}
