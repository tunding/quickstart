package org.springside.fi.web.running;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.service.running.RelationshipService;
import org.springside.modules.mapper.JsonMapper;

/**
 * @date：2015年6月28日 上午10:44:53  
 * @project：quickstart  
 * @author wangzhichao  
 * @version 1.0   
 * @name：RelationshipController.java  
 * @description：	好友关系操作controller
 * 		同意添加好友｜agreeAttention
 * 		请求添加好友｜AttentionFriendship
 * 		删除好友｜RemoveFriendship
 * 		添加黑名单｜submitBlack
 * 		删除黑名单｜removeBlack
 * 		对方是否在你的黑名单｜isBlack
 * 		是否在对方的黑名单｜reverseIsBlack
 * 		是否已添加对方为好友｜isFriend
 * 		获取好友列表|listFriend
 */
@Controller
@RequestMapping(value="/ralationship/friendship")
public class RelationshipController extends BaseController{
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@Autowired
	private RelationshipService relationshipService;
	
	/**
	 * @param attentionUuid 同意加好友，主动方的uuid
	 */
	@ResponseBody
	@RequestMapping(value="/agree")
	public String agreeAttention(@RequestParam(value="attentionUuid") String attentionUuid){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		try{
			relationshipService.agreeRelationship(user_id, attentionUuid);
			map.put("result", "success");
			map.put("data", "");
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	/**
	 * @param passiveAttentionUuid 添加好友请求时，所添加的好友的uuid
	 * @param msg 添加好友请求时，所发送的消息内容
	 */
	@ResponseBody
	@RequestMapping(value="/attention")
	public String AttentionFriendship(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid,
			@RequestParam(value="message", defaultValue="") String msg){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		try{
			//return relationshipService.attentionRelationship(user_id, passiveAttentionUuid, msg);
			return relationshipService.attentionRelationship(user_id, passiveAttentionUuid);
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
			return jsonMapper.toJson(map);
		}
	}
	
	/**
	 * @param passiveAttentionUuid 所删除好友的uuid
	 */
	@ResponseBody
	@RequestMapping(value="/attentionremove")
	public String RemoveFriendship(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		try{
			relationshipService.removeRelationship(user_id, passiveAttentionUuid);
			map.put("result", "success");
			map.put("data", "");
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
		}
		return jsonMapper.toJson(map);
	}
	
	/**
	 * @param passiveAttentionUuid 向黑名单中添加好友的uuid
	 * @return 返回融云返回的结果，失败则返回null
	 */
	@ResponseBody
	@RequestMapping(value="/submitblack")
	public String submitBlack(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.submitBlack(user_id, passiveAttentionUuid);
	}
	
	/**
	 * @param passiveAttentionUuid 移除黑名单中的好友uuid
	 * @return 成功返回true，失败则返回false
	 */
	@ResponseBody
	@RequestMapping(value="/removeblack")
	public boolean removeBlack(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.removeBlack(user_id, passiveAttentionUuid);
	}
	
	/**
	 * @param passiveAttentionUuid 参数uuid的用户是否在当前用户的黑名单中
	 */
	@ResponseBody
	@RequestMapping(value="/isblack")
	public boolean isBlack(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.isblack(user_id, passiveAttentionUuid);
	}
	
	/**
	 * @param passiveAttentionUuid 当前用户是否参数uuid的用户黑名单中
	 */
	@ResponseBody
	@RequestMapping(value="/reverseisblack")
	public boolean reverseIsBlack(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.reverseIsBlack(user_id, passiveAttentionUuid);
	}
	
	/**
	 * @param passiveAttentionUuid 参数uuid的用户是否为当前用户的好友，并且不在当前用户黑名单中
	 */
	@ResponseBody
	@RequestMapping(value="/isfriend")
	public boolean isFriend(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.isFriend(user_id, passiveAttentionUuid);
	}
	
	/**
	 * 返回当前用户通讯录
	 */
	@ResponseBody
	@RequestMapping(value="/listfriend")
	public String listFriend(){
		Long user_id = getCurrentUserId();
		return relationshipService.listFriend(user_id);
	}
}
