package com.zlb.core.logger;

/**
 * The five logging levels used by Log are (in order): 1. DEBUG (the least
 * serious) 2. INFO 3. WARN 4. ERROR 5. FATAL (the most serious)
 */
public abstract class Logger {

	private static ILoggerFactory factory;

	static {
		init();
	}

	public static void setLoggerFactory(ILoggerFactory loggerFactory) {
		if (loggerFactory != null)
			Logger.factory = loggerFactory;
	}

	public static Logger getLogger(Class<?> clazz) {
		return factory.getLogger(clazz);
	}

	public static Logger getLogger(String name) {
		return factory.getLogger(name);
	}

	public static void init() {
		if (factory != null)
			return;
		try {
			Class.forName("org.apache.log4j.Logger");
			factory = new Log4jLoggerFactory();
		} catch (Exception e) {
			factory = new JdkLoggerFactory();
		}
	}

	public abstract void debug(String message);

	public abstract void debug(String message, Throwable t);

	public abstract void info(String message);

	public abstract void info(String message, Throwable t);

	public abstract void warn(String message);

	public abstract void warn(String message, Throwable t);

	public abstract void error(String message);

	public abstract void error(String message, Throwable t);

	public abstract void fatal(String message);

	public abstract void fatal(String message, Throwable t);

	public abstract boolean isDebugEnabled();

	public abstract boolean isInfoEnabled();

	public abstract boolean isWarnEnabled();

	public abstract boolean isErrorEnabled();

	public abstract boolean isFatalEnabled();
}
