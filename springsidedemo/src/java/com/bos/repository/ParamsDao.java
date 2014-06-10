/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.bos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.bos.entity.Params;

public interface ParamsDao extends PagingAndSortingRepository<Params, Long>, JpaSpecificationExecutor<Params> {

	List<Params> findByType(@Param("type")String type);
	List<Params> findByTypeAndKIn(@Param("type")String type,
			@Param("k")List<String> keys);
}
