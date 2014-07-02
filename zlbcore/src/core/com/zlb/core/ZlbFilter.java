package com.zlb.core;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zlb.core.logger.Logger;

/**
 * JFinal framework filter
 */
public final class ZlbFilter implements Filter {
	private ZlbContext context = null;
	private String encoding="UTF-8";
	private static Logger log = Logger.getLogger(ZlbFilter.class);
	private int contextPathLength;

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			context = ZlbContext.initialize(filterConfig.getServletContext());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage(), e);
		}
		String contextPath = filterConfig.getServletContext().getContextPath();
		contextPathLength = (contextPath == null || "/".equals(contextPath) ? 0
				: contextPath.length());
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		String target = request.getRequestURI();
		if (contextPathLength != 0)
			target = target.substring(contextPathLength);
		AtomicBoolean isHandled = new AtomicBoolean(false);
		try {
			context.handle(target, request, response, isHandled);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				String qs = request.getQueryString();
				log.error(qs == null ? target : target + "?" + qs, e);
			}
		}
		if (!isHandled.get()) {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
		if (null != context) {
			context.destroy();
		}
		context = null;
	}
}
