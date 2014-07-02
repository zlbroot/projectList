package com.zlb.core.dao.batis.builder;

import com.zlb.core.dao.entity.EntityManager;
import com.zlb.core.dao.entity.EntityObject;

/**
 * 通过注解生成sql
 * 
 * @author david
 * 
 */
public class SqlBuilder {

	/**
	 * 由传入的对象生成insert sql语句
	 * 
	 * @param tableMapper
	 * @param dto
	 * @return sql
	 * @throws Exception
	 */
	public static String buildInsertSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException(
					"Sorry,I refuse to build sql for a null object!");
		}
		EntityObject em = EntityManager.getInstance()
				.process(object.getClass());
		return em.processSql(em.insert_entity_sql, object);
	}

	/**
	 * 由传入的对象生成update sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildUpdateSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException(
					"Sorry,I refuse to build sql for a null object!");
		}
		EntityObject em = EntityManager.getInstance()
				.process(object.getClass());
		return em.processSql(em.update_by_id_sql, object);
	}

	/**
	 * 由传入的对象生成update sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildDeleteSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException(
					"Sorry,I refuse to build sql for a null object!");
		}
		EntityObject em = EntityManager.getInstance()
				.process(object.getClass());
		return em.processSql(em.delete_by_id_sql, object);
	}

	public static String buildSelectOneSql(Object object) {
		if (null == object) {
			throw new RuntimeException(
					"Sorry,I refuse to build sql for a null object!");
		}
		EntityObject em = EntityManager.getInstance()
				.process(object.getClass());
		return em.processSql(em.select_one, object);
	}

	/**
	 * 由传入的对象生成query sql语句
	 * 
	 * @param object
	 * @return sql
	 * @throws Exception
	 */
	public static String buildSelectSql(Object object) throws Exception {
		if (null == object) {
			throw new RuntimeException(
					"Sorry,I refuse to build sql for a null object!");
		}
		EntityObject em = EntityManager.getInstance()
				.process(object.getClass());
		return em.processSql(em.select_field_sql, object);
	}

}
