package com.zlb.core.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.zlb.core.annos.Resource;
import com.zlb.core.dao.CrudRepository;
import com.zlb.core.dao.Pager;
import com.zlb.core.dao.batis.SqlSessionTemplate;
import com.zlb.core.dao.entity.EntityManager;
import com.zlb.core.dao.entity.EntityObject;
import com.zlb.core.kit.ObjectKit;

public class SimpleCrudRepository<T, ID extends Serializable> implements
		CrudRepository<T, ID> {
	protected final Class<T> entityClazz;
	protected final EntityObject entityObject;
	protected SqlSessionTemplate sqlSessionTemplate;
	@Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public SimpleCrudRepository(Class<T> entityClazz) {
		this.entityClazz = entityClazz;
		entityObject = EntityManager.getInstance().process(entityClazz);
	}

	@Override
	public <S extends T> S save(S entity) {
		Map<String, Object> params = ObjectKit.toMap(entity);
		EntityManager.SqlTx sqltx = entityObject.process(entityObject.insert_entity_sql, params);
		System.out.println(sqltx);
		return null;
	}

	@Override
	public T findOne(ID id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put("TABLE_NAME", entityObject.getTableName());
		return sqlSessionTemplate.selectOne("selectOne", params);
	}

	@Override
	public int delete(ID id) {
		return 0;
	}

	@Override
	public Pager<T> findAll(Pager<T> page) {
		return null;
	}
}
