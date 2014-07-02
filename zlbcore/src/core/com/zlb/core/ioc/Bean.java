package com.zlb.core.ioc;

import com.zlb.core.logger.Logger;

public class Bean {
	private Logger log = null;
	public String id;
	public String name;
	public Class<?> clazz;
	public Object object;

	public Bean(String id, Class<?> clazz) {
		this(id, id, clazz,null);
	}
	public Bean(String id, Class<?> clazz,Object object) {
		this(id, id, clazz,object);
	}

	public Bean(String id, String name, Class<?> clazz,Object object) {
		this.id = id;
		this.name = name;
		this.clazz = clazz;
		log = Logger.getLogger(clazz);
		if(null == object){
			try {
				object = clazz.newInstance();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		this.object = object;
	}
	public Bean() {
	}
}
