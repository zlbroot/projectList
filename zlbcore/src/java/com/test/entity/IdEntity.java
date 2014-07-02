package com.test.entity;

import com.zlb.core.annos.dao.entity.GeneratedValue;
import com.zlb.core.annos.dao.entity.Id;
import com.zlb.core.consts.GenerationType;

/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * @author calvin
 */
public abstract class IdEntity {
	
	protected Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
