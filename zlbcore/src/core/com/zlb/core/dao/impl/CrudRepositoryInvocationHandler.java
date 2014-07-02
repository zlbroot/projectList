package com.zlb.core.dao.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import com.zlb.core.dao.CrudRepository;

public class CrudRepositoryInvocationHandler<T, ID extends Serializable>
		implements InvocationHandler {
	protected final CrudRepository<T, ID> crudRepository;
	protected final HashSet<String> haset = new HashSet<String>();

	public CrudRepositoryInvocationHandler(CrudRepository<T, ID> crudRepository) {
		this.crudRepository = crudRepository;
		for (Method m : crudRepository.getClass().getMethods()) {// 处理方法
			String key = m.getName() + ""
					+ Arrays.toString(m.getParameterTypes());
			haset.add(key);
		}
	}

	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		String key = m.getName() + "" + Arrays.toString(m.getParameterTypes());
		if (haset.contains(key)) {
			return m.invoke(crudRepository, args);
		}
		return null;
	}

}
