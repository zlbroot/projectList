package com.bos.kit;

import com.alibaba.fastjson.JSON;

public class JsonKit {
	public static final String object2Json(Object object) {
		if (null == object) {
			return null;
		}
		return JSON.toJSONString(object);
	}

	public static final <T> T json2Object(String json, Class<T> clazz) {
		if (null == json) {
			return null;
		}
		return JSON.parseObject(json, clazz);
	}
}
