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
	@RequestMapping(value="/attention")
	public String OptFriendship(@RequestParam(value="opt") String opt,
			@RequestParam(value="passiveAttentionUuid") String passiveAttentionUuid){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long user_id = getCurrentUserId();
		try{
			relationshipService.updateRelationship(user_id, opt, passiveAttentionUuid);
			map.put("result", "success");
			map.put("data", null);
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
