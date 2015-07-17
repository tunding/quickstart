package org.springside.fi.service.running;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

/**
* @author tunding:wzc@tcl.com
* @project running
* @version 1.0
* @date 创建时间：2015年6月29日 上午8:37:37
* @name RelationshipService.java
* @description 好友关系service
*/
@Service
@Transactional("transactionManager")
public class RelationshipService extends BaseThirdService{
	private final static String RongHOSTBlacklistAdd = SystemGlobal.getConfig("RongCloudAPI")+"user/blacklist/add.json";
	private final static String RongHOSTBlacklistRemove = SystemGlobal.getConfig("RongCloudAPI")+"user/blacklist/remove.json";
	private final static String RongHOSTSystemMsg = SystemGlobal.getConfig("RongCloudAPI")+"message/system/publish.json";
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@Autowired
	private HttpClientTemplate httpClientTemplate;
	@Autowired
	private RunnerService runnerService;
	@Autowired
	private RelationshipDao relationshipDao;
	
	/**
	 * @param id 返回当前用户的所有好友信息列表
	 */
	public String listFriend(long id){
		String attentionUuid = getUuid(id);
		List<Relationship> relationships = relationshipDao.findListFriend(attentionUuid);
		List<Runner> friends = new ArrayList<Runner>();
		
		for(Relationship relationship : relationships){
			Runner runner = runnerService.getRunnerByUUID(relationship.getPassiveAttentionUuid());
			//创建runner,将需要返回的好友信息存储
			Runner newrunner = new Runner();
			newrunner.setId(runner.getId());
			newrunner.setName(runner.getName());
			newrunner.setLoginName(runner.getLoginName());
			newrunner.setRoles(runner.getRoles());
			newrunner.setAge(runner.getAge());
			newrunner.setSex(runner.getSex());
			newrunner.setSignature(runner.getSignature());
			
			friends.add(newrunner);
		}
		
		return jsonMapper.toJson(friends);
	}
	
	/**
	 * 本地删除好友关系
	 */
	public void removeRelationship(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		if(isblack(id, passiveAttentionUuid)){
			removeBlackList(attentionUuid, passiveAttentionUuid);
		}
		if(reverseIsBlack(id, passiveAttentionUuid)){
			removeBlackList(passiveAttentionUuid, attentionUuid);
		}
		removeAttention(attentionUuid, passiveAttentionUuid);
		removeAttention(passiveAttentionUuid, attentionUuid);
	}
	
	/**
	 * 请求添加好友
	 * 如果原本当前用户是请求对象的好友，则返回。
	 * 如果原本当前用户在请求对象的黑名单里，则返回。
	 * 否则，发送请求添加好友信息
	 */
	public String attentionRelationship(long id, String passiveAttentionUuid, String content) {
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
			String res = attention(attentionUuid, passiveAttentionUuid, msg);
			System.out.println(res);
			map.put("code", RestErrorCode.REST_SUCCESS_CODE);
			map.put("data", "验证消息已发送...");
		}
		return jsonMapper.toJson(map);
	}
	/**
	 * @param id
	 * @param passiveAttentionUuid
	 * @description 不发送消息，只在本地数据库关注双方
	 */
	public String attentionRelationship(long id, String passiveAttentionUuid){
		HashMap<String, Object> map = new HashMap<String, Object>();
		String attentionUuid = getUuid(id);
		if(isFriend(id, passiveAttentionUuid)){
			map.put("code", RestErrorCode.REST_ISFRIEND_CODE);
			map.put("data", "已经关注过对方");
		}else{
			agree(attentionUuid, passiveAttentionUuid);
			agree(passiveAttentionUuid, attentionUuid);
			map.put("code", RestErrorCode.REST_SUCCESS_CODE);
			map.put("data", "已关注对方");
		}
		return jsonMapper.toJson(map);
	}
	
	/**
	 * 添加好友需要A->B和B->A两层好友关系
	 */
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
			//解析融云返回值，200则移除本地黑名单
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
			//解析融云返回值，200则移除本地黑名单			
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
	
	/**
	 * state为1的有效好友
	 */
	public boolean isFriend(long id, String passiveAttentionUuid){
		String attentionUuid = getUuid(id);
		List<Relationship> relationships = relationshipDao.findRelationshipFriend(attentionUuid, passiveAttentionUuid);
		if(relationships.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 本地数据库中删除好友关系
	 */
	private void removeAttention(String attentionUuid, String passiveAttentionUuid){
		Relationship relationship = getRelationship(attentionUuid, passiveAttentionUuid);
		relationshipDao.delete(relationship);
	}
	
	/**
	 * @param userId
	 * @param blackUserId
	 * 添加blackUserId到userId的黑名单中
	 */
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
		System.out.println(RongHOSTSystemMsg);
		System.out.println(reqParams);
		StringRequestEntity requestEntity = null;
		try{
			requestEntity = new StringRequestEntity(reqParams, "application/x-www-form-urlencoded", "UTF-8");
			return convertResult(httpClientTemplate.executePostMethod(RongHOSTSystemMsg, requestEntity, null));
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
