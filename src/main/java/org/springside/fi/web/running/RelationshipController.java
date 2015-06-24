package org.springside.fi.web.running;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.fi.service.running.RelationshipService;
import org.springside.modules.mapper.JsonMapper;

@Controller
@RequestMapping(value="/ralationship/friendship")
public class RelationshipController extends BaseController{
	
	protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	
	@Autowired
	private RelationshipService relationshipService;
	
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
	
	@ResponseBody
	@RequestMapping(value="/attention")
	public String AttentionFriendship(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid,
			@RequestParam(value="message") String msg){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		try{
			return relationshipService.attentionRelationship(user_id, passiveAttentionUuid, msg);
		}catch(RuntimeException e){
			e.printStackTrace();
			map.put("result", "failed");
			map.put("data", e.getMessage());
			return jsonMapper.toJson(map);
		}
	}
	
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
	
	@ResponseBody
	@RequestMapping(value="/submitblack")
	public String submitBlack(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.submitBlack(user_id, passiveAttentionUuid);
	}
	
	@ResponseBody
	@RequestMapping(value="/removeblack")
	public boolean removeBlack(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.removeBlack(user_id, passiveAttentionUuid);
	}
	
	@ResponseBody
	@RequestMapping(value="/isblack")
	public boolean isBlack(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.isblack(user_id, passiveAttentionUuid);
	}
	
	@ResponseBody
	@RequestMapping(value="/reverseisblack")
	public boolean reverseIsBlack(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.reverseIsBlack(user_id, passiveAttentionUuid);
	}
	
	@ResponseBody
	@RequestMapping(value="/isfriend")
	public boolean isFriend(@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		Long user_id = getCurrentUserId();
		return relationshipService.isFriend(user_id, passiveAttentionUuid);
	}
}
