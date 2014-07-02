package com.zlb.core.dao;

import java.io.Serializable;

public interface CrudRepository<T, ID extends Serializable> {
	/**
	 * 保存
	 * 
	 * @param entity
	 * @return
	 */
	<S extends T> S save(S entity);

	/**
	 * 查找
	 * 
	 * @param id
	 * @return
	 */
	T findOne(ID id);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	int delete(ID id);

	/**
	 * 分页查找
	 * 
	 * @param page
	 * @return
	 */
	Pager<T> findAll(Pager<T> page);
}
