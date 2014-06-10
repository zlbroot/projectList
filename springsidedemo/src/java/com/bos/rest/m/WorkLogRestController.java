package com.bos.rest.m;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bos.entity.User;
import com.bos.entity.WorkLog;
import com.bos.repository.UserDao;
import com.bos.service.work.WorkLogService;

/**
 * WorkLog的Restful API的Controller.
 */
@RestController
@RequestMapping(value = "/api/v1/workLog")
public class WorkLogRestController {
	@Autowired
	private WorkLogService workLogService ;
	@Autowired
	private UserDao userDao;
	
	@RequestMapping("/add")
	public Map<String, Object> add(@RequestParam(value="loginName" ,required=true)String loginName,
			@RequestParam(value="type" ,required=true)Integer type,
			@RequestParam(value="msg" ,required=true)String content){
		Map<String, Object> result = new HashMap<String, Object>();
		User user = userDao.findByLoginName(loginName);
		if(null != user){
			WorkLog entity = new WorkLog();
			entity.setContent(content);
			entity.setType(type);
			entity.setUser(user);
			entity.setCreated(new Date());
			workLogService.saveWorkLog(entity);
			result.put("status", 0);
			result.put("msg", "保存成功");
		}else{
			result.put("status", 1);
			result.put("msg", "用户["+loginName+"]没有找到！");
		}
		return result;
	}
	
	@RequestMapping("/query")
	public Map<String, Object> select(@RequestParam(value="loginName" ,required=true)String loginName,
			@RequestParam(value="type" ,required=true)Integer type,@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		Map<String, Object> result = new HashMap<String, Object>();
		User user = userDao.findByLoginName(loginName);
		if(null != user){
			Page<WorkLog> page = workLogService.getUserWorkLog(user.getId(), new HashMap<String, Object>(),
					pageNumber, pageSize, sortType);
			result.put("status", 0);
			result.put("msg", "查询成功");
			result.put("data", page);
		}else{
			result.put("status", 1);
			result.put("msg", "用户["+loginName+"]没有找到！");
		}
		return result;
	}
}
