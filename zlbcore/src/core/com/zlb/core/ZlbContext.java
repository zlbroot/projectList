package com.zlb.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.zlb.core.dao.TransactionSynchronizationManager;
import com.zlb.core.dao.batis.AutoMapperInterceptor;
import com.zlb.core.dao.batis.SqlSessionTemplate;
import com.zlb.core.handler.Handler;
import com.zlb.core.ioc.ApplicationContext;
import com.zlb.core.kit.ScanKit;
import com.zlb.core.logger.Logger;
import com.zlb.core.mvc.ActionMethodHandler;

public class ZlbContext {
	protected static Logger log = Logger.getLogger(ZlbContext.class);
	/**
	 * 项目名
	 */
	protected String contextPath = null;
	/**
	 * servlet容器上下文环境
	 */
	protected ServletContext servletContext = null;
	/**
	 * 扫描路径
	 */
	public String scan = "com.zlb";
	/**
	 * jdbc配置文件 常规的配置文件
	 */
	public final Properties config = new Properties();

	public String prefix = "/WEB-INF/views/";
	public String suffix = ".jsp";

	private ApplicationContext applicationContext = null;
	protected final LinkedList<Handler> handlers = new LinkedList<Handler>();
	/**
	 * 事务管理
	 */
	protected final TransactionFactory transactionFactory = new JdbcTransactionFactory();
	protected SqlSessionTemplate sqlSessionTemplate;

	private ZlbContext() {
	}

	/**
	 * 初始化
	 * 
	 * @param servletContext
	 * @return
	 * @throws ScriptException
	 * @throws IOException
	 */
	public final static ZlbContext initialize(ServletContext servletContext)
			throws ScriptException, IOException {
		ZlbContext context = new ZlbContext();
		context.servletContext = servletContext;
		context.contextPath = servletContext.getContextPath();

		String zlbJs = servletContext.getInitParameter("zlbJs");
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		Bindings vars = new SimpleBindings();
		vars.put("zlb", context);
		Reader scriptReader = new InputStreamReader(
				ZlbContext.class.getResourceAsStream(zlbJs));
		try {
			engine.eval(scriptReader, vars);
		} finally {
			scriptReader.close();
		}
		servletContext.setAttribute("ctx", context.contextPath);
		context.initAppcationContext();
		return context;
	}

	private void initAppcationContext() {
		ActionMethodHandler actionMethodHandler = new ActionMethodHandler(
				prefix, suffix);
		handlers.add(actionMethodHandler);
		applicationContext = new ApplicationContext(actionMethodHandler);
		initJdbc();
		applicationContext.addBean("sqlSessionTemplate", sqlSessionTemplate);
		Set<String> classLst = ScanKit.getClassNameFromDir(new File(
				ScanKit.APP_CLASS_PATH));
		applicationContext.init(classLst, scan);
	}

	/**
	 * 初始化JDBC dataSource
	 */
	private void initJdbc() {
		if (!config.isEmpty()) {
			String driver = config.getProperty("driver",
					"com.mysql.jdbc.Driver");
			String url = config.getProperty("url",
					"jdbc:mysql://127.0.0.1:3306");
			String username = config.getProperty("username", "root");
			String password = config.getProperty("password", "");
			String environmentName = config.getProperty("environment",
					"development");
			DataSource dataSource = new PooledDataSource(driver, url, username,
					password);
			Environment environment = new Environment(environmentName,
					transactionFactory, dataSource);
			Configuration configuration = new Configuration(environment);
			SqlSource sql = new RawSqlSource(configuration, "select id,name from ${TABLE_NAME} where id = #{id}",HashMap.class);
			MappedStatement m = new MappedStatement.Builder(configuration, "selectOne", sql, SqlCommandType.SELECT).build();
			configuration.addMappedStatement(m);
			configuration.addInterceptor(new AutoMapperInterceptor());
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(configuration);
			sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		}
	}


	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, AtomicBoolean isHandled) {
		Transaction tx = transactionFactory
				.newTransaction(TransactionSynchronizationManager
						.getSqlSession(
								sqlSessionTemplate.getSqlSessionFactory())
						.getConnection());
		try {
			for (Handler handler : handlers) {
				if (!isHandled.get()) {
					handler.handle(target, request, response, isHandled);
				}
			}
			tx.commit();
		} catch (SQLException | RuntimeException e) {
			try {
				tx.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage(), e1);
			}
		} finally {
			try {
				tx.close();
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 销毁
	 */
	public final void destroy() {
		contextPath = null;
		servletContext = null;
	}
}
