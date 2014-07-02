package com.zlb.core.dao.entity;

import java.util.concurrent.ConcurrentHashMap;

import com.zlb.core.annos.dao.entity.Entity;
import com.zlb.core.annos.dao.entity.Table;
import com.zlb.core.kit.StrKit;

public class EntityManager {
	protected final ConcurrentHashMap<String, EntityObject> entityCenter = new ConcurrentHashMap<String, EntityObject>();
	private static final EntityManager instance = new EntityManager();

	private EntityManager() {
	}

	public final static EntityManager getInstance() {
		return instance;
	}

	public EntityObject process(Class<?> clazz) {
		EntityObject result = null;
		if (null != clazz) {
			String className = clazz.getName();
			result = entityCenter.get(className);
			if (null == result) {
				Entity entity = clazz.getAnnotation(Entity.class);
				Table table = clazz.getAnnotation(Table.class);
				String tableName = null;
				if (null != entity || null != table) {
					if (null != table) {
						tableName = table.value();
					}
					if (StrKit.isBlank(tableName)) {
						tableName = StrKit.toCapitalizeCamelCase(clazz
								.getSimpleName());
					}
					result = new EntityObject(tableName, clazz);
					entityCenter.put(clazz.getName(), result);
				}
			}

		}
		return result;
	}

	public static class SqlTx {
		public String sql;
		public Object params;
	}
}
