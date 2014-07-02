package com.zlb.core.handler;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {
	void handle(String target, HttpServletRequest request,
			HttpServletResponse response, AtomicBoolean isHandled);
}
