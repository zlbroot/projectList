package com.bos.rest.m;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bos.kit.ext.SztQueryCard;
import com.bos.service.commons.ParamsService;

/**
 * Params的Restful API的Controller.
 */
@RestController
@RequestMapping(value = "/api/v1/params")
public class ParamsRestController {
	@Autowired
	private ParamsService paramsService ;
	
	@RequestMapping("/byType")
	public Map<String, String> getParamsByType(@Param(value ="type") String type){
		return paramsService.getParams2MapByType(type);
	}
	
	@RequestMapping("/szt")
	public Map<String, Object> szt(String loginName){
		Map<String, Object> result = new HashMap<>();
		Map<String, String> t =  paramsService.getParams2MapByType("szt_" + loginName);
		String cardno = t.get("cardNo");
		if(!StringUtils.isEmpty(cardno)){
			try {
				Map<String, String> data = SztQueryCard.query(cardno);
				result.put("status", 0);
				result.put("msg", "查询成功");
				result.put("data", data);
			} catch (IOException e) {
				result.put("status", 1);
				result.put("msg", "账号["+loginName+"]没有配置！");
			}
		}else{
			result.put("status", 1);
			result.put("msg", "账号["+loginName+"]没有配置！");
		}
		return result;
	}
}
