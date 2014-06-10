/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.bos.service.work;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.bos.entity.WorkLog;
import com.bos.repository.WorkLogDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class WorkLogService {

	private WorkLogDao workLogDao;

	public WorkLog getWorkLog(Long id) {
		return workLogDao.findOne(id);
	}

	public void saveWorkLog(WorkLog entity) {
		entity.setCreated(new Date());
		workLogDao.save(entity);
	}

	public void deleteWorkLog(Long id) {
		workLogDao.delete(id);
	}

	public List<WorkLog> getAllWorkLog() {
		return (List<WorkLog>) workLogDao.findAll();
	}

	public Page<WorkLog> getUserWorkLog(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<WorkLog> spec = buildSpecification(userId, searchParams);

		return workLogDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<WorkLog> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("user.id", new SearchFilter("user.id", Operator.EQ, userId));
		Specification<WorkLog> spec = DynamicSpecifications.bySearchFilter(filters.values(), WorkLog.class);
		return spec;
	}

	@Autowired
	public void setWorkLogDao(WorkLogDao workLogDao) {
		this.workLogDao = workLogDao;
	}
}
