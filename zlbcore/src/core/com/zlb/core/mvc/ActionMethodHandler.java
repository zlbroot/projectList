package com.zlb.core.mvc;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zlb.core.annos.mvc.Action;
import com.zlb.core.annos.mvc.Controller;
import com.zlb.core.annos.mvc.ReqParam;
import com.zlb.core.ex.BizException;
import com.zlb.core.handler.Handler;
import com.zlb.core.ioc.Bean;
import com.zlb.core.ioc.BeanCreatedCallback;
import com.zlb.core.kit.StrKit;
import com.zlb.core.logger.Logger;
import com.zlb.core.mvc.ActionMethod.MethodParam;

public class ActionMethodHandler implements Handler, BeanCreatedCallback {
	private final ConcurrentHashMap<String, ActionMethod> amCenter = new ConcurrentHashMap<String, ActionMethod>();
	Logger logger = Logger.getLogger(getClass());
	private final String prefix;
	private final String suffix;
	public ActionMethodHandler(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, AtomicBoolean isHandled) {
		ActionMethod am = amCenter.get(target);
		if (null != am) {
			int len = am.pp.length;
			Object[] params = new Object[len];
			MethodParam p = null;
			for (int i = 0; i < len; i++) {
				p = am.pp[i];
				if (p.clazz.isAssignableFrom(HttpServletRequest.class)) {
					params[i] = request;
				} else if (p.clazz.isAssignableFrom(HttpServletResponse.class)) {
					params[i] = response;
				} else {
					for (Annotation ann : p.annotations) {
						if (ReqParam.class.equals(ann.annotationType())) {
							ReqParam reqP = (ReqParam) ann;
							String val = request.getParameter(reqP.value());
							if (null == val
									&& !StrKit.isBlank(reqP.defaultValue())) {
								val = reqP.defaultValue();
							}
							if (null == val && reqP.required()) {
								throw new BizException(500, " param["
										+ reqP.value() + " ] required");
							}
							params[i] = val;
						}
					}
				}
			}
			try {
				Object result = am.invoke(params);
				if (null != result) {
					if (result instanceof String) {
						try {
							request.getRequestDispatcher(prefix + (String) result + suffix)
									.forward(request, response);
						} catch (ServletException e) {
							logger.error(e.getMessage(), e);
						} catch (IOException e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				logger.error(e.getMessage(), e);
				try {
					response.sendError(500, e.getMessage());
					e.printStackTrace(response.getWriter());
				} catch (Exception e2) {
					logger.error(e2.getMessage(), e2);
				}
			}
			isHandled.set(true);
		}
	}

	@Override
	public void callback(Bean bean) {
		Class<?> clazz = bean.clazz;
		Action action = clazz.getAnnotation(Action.class);
		Controller controller = clazz.getAnnotation(Controller.class);
		if (null != action || null != controller) {
			registerAction(action, clazz, bean.object);
		}
	}

	private final void registerAction(Action action, Class<?> clazz,
			Object object) {
		String base = null;
		if (null != action) {
			base = action.value();
			if (StrKit.isBlank(base)) {
				base = StrKit.firstCharToLowerCase(clazz.getSimpleName());
				if (base.length() > 6) {
					base.substring(0, base.length() - 6);
				}
			}
		}
		ActionMethod am = null;
		Method[] methods = clazz.getMethods();
		Action tmp = null;
		String path = null;
		String t = null;
		String methodName = null;
		for (Method method : methods) {
			tmp = null;
			path = null;
			methodName = method.getName();
			if (null != action && "execute".equals(methodName)) {
				path = base;
				tmp = action;
			} else {
				tmp = method.getAnnotation(Action.class);
				if (null != tmp) {
					if (null != base) {
						path = base;
					} else {
						path = "";
					}
					t = tmp.value();
					if (StrKit.isBlank(t)) {
						t = methodName;
					}
					path += t;
				}
			}
			if (null != path) {
				if (!path.startsWith("/")) {
					path = "/" + path;
				}
				am = new ActionMethod(path, tmp, object);
				am.setMethod(method);
				amCenter.put(path, am);
			}
		}
	}
}
