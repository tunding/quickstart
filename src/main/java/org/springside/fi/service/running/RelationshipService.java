package org.springside.fi.service.running;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
import org.springside.fi.rest.RestErrorCode;
import org.springside.fi.service.rong.models.ContactNtfMessage;
import org.springside.fi.service.rong.models.Message;
import org.springside.fi.service.third.BaseThirdService;
import org.springside.fi.service.third.object.RongCloudBlacklist;
import org.springside.modules.mapper.JsonMapper;

@Service
@Transactional("transactionManager")
public class RelationshipService extends BaseThirdService{
	private final String RongHOSTBlacklistAdd = SystemGlobal.getConfig("RongCloudAPI")+"user/blacklist/add.json";
	private final String RongHOSTBlacklistRemove = SystemGlobal.getConfig("RongCloudAPI")+"user/blacklist/remove.json";
	private final String RongHOSTPrivateMsg = SystemGlobal.getConfig("RongCloudAPI")+"/message/private/publish.json";
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@Autowired
	private HttpClientTemplate httpClientTemplate;
	@Autowired
	private RunnerService runnerService;
	@Autowired
	private RelationshipDao relationshipDao;
	
	public void removeRelationship(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		if(isblack(id, passiveAttentionUuid)){
			removeBlackList(attentionUuid, passiveAttentionUuid);
		}
		if(reverseIsBlack(id, passiveAttentionUuid)){
			removeBlackList(passiveAttentionUuid, attentionUuid);
		}
		System.out.println(attentionUuid);
		System.out.println(passiveAttentionUuid);
		removeAttention(attentionUuid, passiveAttentionUuid);
		removeAttention(passiveAttentionUuid, attentionUuid);
	}
	public String attentionRelationship(long id, String content, String passiveAttentionUuid) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String attentionUuid = getUuid(id);
		if(isFriend(id, passiveAttentionUuid)){
			map.put("code", RestErrorCode.REST_ISFRIEND_CODE);
			map.put("data", RestErrorCode.REST_ISFRIEND_MSG);
		}else if(isblack(id, passiveAttentionUuid)){
			map.put("code", RestErrorCode.REST_ISBLACK_CODE);
			map.put("data", RestErrorCode.REST_ISBLACK_MSG);
		}else{
			Message msg = new ContactNtfMessage("attention", attentionUuid,
					passiveAttentionUuid, content);
			System.out.println(attention(attentionUuid, passiveAttentionUuid, msg));
			map.put("code", RestErrorCode.REST_SUCCESS_CODE);
			map.put("data", "验证消息已发送...");
		}
		return jsonMapper.toJson(map);
	}
	
	public void agreeRelationship(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		agree(attentionUuid, passiveAttentionUuid);
		agree(passiveAttentionUuid, attentionUuid);
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
	
	/**
	 * @param userId
	 * @param blackUserId
	 * 移除黑名单
	 */
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

	/**
	 * @param fromUserId
	 * @param toUserId
	 * @param msg
	 * 发送好友请求消息
	 */
	private String attention(String fromUserId, String toUserId, Message msg){
		String reqParams = "fromUserId="+fromUserId+"&toUserId="+toUserId+"&objectName=RC:ContactNtf&content="+msg;
		System.out.println(reqParams);
		StringRequestEntity requestEntity = null;
		try{
			requestEntity = new StringRequestEntity(reqParams, "Application/json", "UTF-8");
			return convertResult(httpClientTemplate.executePostMethod(RongHOSTPrivateMsg, requestEntity, null));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param attentionUuid 
	 * @param passiveAttentionUuid
	 * attentionUuid添加passiveAttentionUuid为好友，纪录好友信息到数据库
	 */
	private void agree(String attentionUuid, String passiveAttentionUuid){
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
		System.out.println(relationships.size()>0);
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
