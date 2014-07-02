package com.zlb.core.dao;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.zlb.core.dao.batis.MyBatisExceptionTranslator;
import com.zlb.core.dao.batis.PersistenceExceptionTranslator;

public class TransactionSynchronizationManager {
	private final static ThreadLocal<SqlSession> LOCAL = new ThreadLocal<SqlSession>();
	
	public static SqlSession getSqlSession(SqlSessionFactory sqlSessionFactory) {
		return getSqlSession(sqlSessionFactory, sqlSessionFactory.getConfiguration()
				.getDefaultExecutorType(),  new MyBatisExceptionTranslator());
	}
	public static SqlSession getSqlSession(SqlSessionFactory sqlSessionFactory,
			ExecutorType executorType,
			PersistenceExceptionTranslator exceptionTranslator) {
		SqlSession result =LOCAL.get();
		if(null == result){
			result = sqlSessionFactory.openSession(executorType,false);
			LOCAL.set(result);
		}
		return result;
	}
	public static void closeSqlSession(SqlSession sqlSession,
			SqlSessionFactory sqlSessionFactory) {
		sqlSession.clearCache();
		sqlSession.close();
		LOCAL.remove();
	}

	public static boolean isSqlSessionTransactional(SqlSession sqlSession,
			SqlSessionFactory sqlSessionFactory) {
		return sqlSession == LOCAL.get();
	}
	
}
