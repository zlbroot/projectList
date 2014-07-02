package com.zlb.core.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.zlb.core.annos.mvc.Action;

public class ActionMethod {
	public String path;
	public Action action;
	public Method method;
	public Object object;
	public MethodParam[] pp;
	public Class<?> resultClazz;

	public ActionMethod(String path, Action action, Object object) {
		this.path = path;
		this.action = action;
		this.object = object;
	}

	public void setMethod(Method method) {
		this.method = method;
		Class<?>[] pps = method.getParameterTypes();
		Annotation[][] annos = method.getParameterAnnotations();
		if (null != pps) {
			pp = new MethodParam[pps.length];
			for (int i = 0; i < pps.length; i++) {
				pp[i] = new MethodParam(pps[i], annos[i]);
			}
		}else{
			pp = new MethodParam[0];;
		}
		resultClazz = method.getReturnType();
	}

	final static class MethodParam {
		public Class<?> clazz;
		public Annotation[] annotations;
		public MethodParam next;

		public MethodParam(Class<?> clazz, Annotation[] annotations) {
			this.clazz = clazz;
			this.annotations = annotations;
		}
	}

	@Override
	public String toString() {
		return "ActionMethod [path=" + path + ", action=" + action
				+ ", method=" + method + ", object=" + object + ", pp=" + pp
				+ "]";
	}

	public Object invoke(Object[] params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return method.invoke(object, params);
	}
}
