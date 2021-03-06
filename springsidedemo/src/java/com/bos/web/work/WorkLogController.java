/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.bos.web.work;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.bos.entity.User;
import com.bos.entity.WorkLog;
import com.bos.service.account.ShiroDbRealm.ShiroUser;
import com.bos.service.work.WorkLogService;
@Controller
@RequestMapping(value = "/workLog")
public class WorkLogController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = new HashMap<String, String> (2);
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private WorkLogService workLogService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<WorkLog> workLogs = workLogService.getUserWorkLog(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("workLogs", workLogs);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "workLog/workLogList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("workLog", new WorkLog());
		model.addAttribute("action", "create");
		return "workLog/workLogForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid WorkLog entity, RedirectAttributes redirectAttributes) {
		User user = new User(getCurrentUserId());
		entity.setUser(user);
		workLogService.saveWorkLog(entity);
		redirectAttributes.addFlashAttribute("message", "创建任务成功");
		return "redirect:/workLog/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("workLog", workLogService.getWorkLog(id));
		model.addAttribute("action", "update");
		return "workLog/workLogForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("workLog") WorkLog workLog, RedirectAttributes redirectAttributes) {
		workLogService.saveWorkLog(workLog);
		redirectAttributes.addFlashAttribute("message", "更新任务成功");
		return "redirect:/workLog/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		workLogService.deleteWorkLog(id);
		redirectAttributes.addFlashAttribute("message", "删除任务成功");
		return "redirect:/workLog/";
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getTask(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("workLog", workLogService.getWorkLog(id));
		}
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
