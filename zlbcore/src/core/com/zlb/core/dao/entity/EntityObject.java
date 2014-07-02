package com.zlb.core.dao.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.test.entity.User;
import com.zlb.core.annos.dao.entity.Column;
import com.zlb.core.annos.dao.entity.GeneratedValue;
import com.zlb.core.annos.dao.entity.Id;
import com.zlb.core.consts.GenerationType;
import com.zlb.core.dao.entity.EntityManager.SqlTx;
import com.zlb.core.kit.StrKit;

public class EntityObject {
	protected final String tableName;
	protected final Class<?> clazz;
	public final String insert_entity_sql;
	public final String update_by_id_sql;
	public final String select_field_sql;
	public final String delete_by_id_sql;
	public final String select_one;

	public EntityObject(String tableName, Class<?> clazz) {
		this.tableName = tableName;
		this.clazz = clazz;
		Id id = null;
		String idField = null;
		GeneratedValue gv = null;
		StringBuffer sb_insert_field = new StringBuffer();
		StringBuffer sb_insert_value = new StringBuffer();
		StringBuffer sb_update = new StringBuffer();
		String fieldName = null;
		String columnName = null;
		Column column = null;
		String methodName = null;
		for (Method m : clazz.getMethods()) {
			id = m.getAnnotation(Id.class);
			methodName = m.getName();
			fieldName = null;
			if (!"getClass".equals(methodName) && methodName.startsWith("get")) {
				fieldName = methodName.substring(3);
			} else if (methodName.startsWith("is")) {
				fieldName = methodName.substring(2);
			}
			if (null != fieldName) {
				columnName = fieldName = StrKit.firstCharToLowerCase(fieldName);
				System.err.println(fieldName);
				if (null != id) {
					gv = m.getAnnotation(GeneratedValue.class);
					idField = fieldName;
					if (null != gv
							&& GenerationType.IDENTITY.equals(gv.strategy())) {
						continue;
					}
				}
				column = m.getAnnotation(Column.class);
				if (null != column) {
					if (StrKit.isBlank(column.value())) {
						columnName = column.value();
					}
				}
				sb_insert_field.append(",").append(columnName);
				sb_insert_value.append(",").append("#{").append(fieldName).append("} ");
				sb_update.append(",").append(columnName).append(" = #{")
						.append(fieldName).append("} ");
			}
		}
		delete_by_id_sql = "DELETE FROM #tableName# WHERE " + idField + " = #{"
				+ idField + "} ";
		select_field_sql = "SELECT " + idField + ","
				+ sb_insert_field.substring(1) + " FROM  #tableName# ";
		update_by_id_sql = "UPDATE  #tableName# set " + sb_update.substring(1)
				+ " WHERE " + idField + " = #{" + idField + "} ";
		insert_entity_sql = "insert into #tableName#("
				+ sb_insert_field.substring(1) + ") values("
				+ sb_insert_value.substring(1) + ") ";
		select_one = select_field_sql + " WHERE " + idField + " = #{" + idField + "} ";
	}
	

	
	/**
	 * 处理环境
	 * @param sql
	 * @param params
	 * @return
	 */
	public SqlTx process(String sql ,Map<String,Object> params){
		String tmp = sql;
		tmp.replace("#tableName#", tableName);
		List<Object> lst = new ArrayList<Object>();
		String field = null;
		for (Entry<String, Object> e : params.entrySet()) {
			field = ":" + e.getKey();
			if(tmp.indexOf(field) > -1){
				tmp = tmp.replace(field, "?");
				lst.add(e.getValue());
			}
		}
		SqlTx tx = new SqlTx();
		tx.sql = tmp;
		tx.params = lst.toArray();
		return tx;
	}
	public String getTableName() {
		return tableName;
	}
	
	public String processSql(String sql ,Object object){
		return sql.replace("#tableName#", getTableName());
	}

	public static void main(String[] args) {
		EntityObject e = new EntityObject("test", User.class);
		System.out.println(e.update_by_id_sql);
	}
}
