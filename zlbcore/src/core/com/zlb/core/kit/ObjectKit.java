package com.zlb.core.kit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.zlb.core.logger.Logger;

public class ObjectKit {
	private static final Logger logger = Logger.getLogger(ObjectKit.class);

	/**
	 * 返回由对象的属性为key,值为map的value的Map集合
	 * 
	 * @param obj
	 *            Object
	 * @return mapValue Map<String,String>
	 * @throws Exception
	 */
	public static Map<String, Object> toMap(Object obj) {
		Map<String, Object> mapValue = new HashMap<String, Object>();
		if (null != obj) {
			Class<?> clazz = obj.getClass();
			String fieldName = null;
			String methodName = null;
			Object value = null;
			for (Method m : clazz.getMethods()) {
				methodName = m.getName();
				fieldName = null;
				if (!"getClass".equals(methodName)
						&& methodName.startsWith("get")) {
					fieldName = methodName.substring(3);
				} else if (methodName.startsWith("is")) {
					fieldName = methodName.substring(2);
				}
				if (null != fieldName) {
					try {
						value = m.invoke(obj, new Object[0]);
						mapValue.put(fieldName, value);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
		return mapValue;
	}

}