package com.zlb.core.logger;

/**
 * ILoggerFactory.
 */
public interface ILoggerFactory {
	
	Logger getLogger(Class<?> clazz);
	
	Logger getLogger(String name);
}
