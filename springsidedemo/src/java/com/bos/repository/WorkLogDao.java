/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.bos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bos.entity.Task;
import com.bos.entity.WorkLog;

public interface WorkLogDao extends PagingAndSortingRepository<WorkLog, Long>, JpaSpecificationExecutor<WorkLog> {

	Page<Task> findByUserId(Long id, Pageable pageRequest);

	@Modifying
	@Query("delete from WorkLog work where work.user.id=?1")
	void deleteByUserId(Long id);
}
