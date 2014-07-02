/*
 *    Copyright 2010-2013 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.zlb.core.dao.batis;

import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;

/**
 * Default exception translator.
 * 
 * Translates MyBatis SqlSession returned exception into a Spring
 * {@code DataAccessException} using Spring's {@code SQLExceptionTranslator} Can
 * load {@code SQLExceptionTranslator} eagerly of when the first exception is
 * translated.
 * 
 * @author Eduardo Macarron
 * 
 * @version $Id$
 */
public class MyBatisExceptionTranslator implements
		PersistenceExceptionTranslator {

	/**
	 * {@inheritDoc}
	 */
	public DataAccessException translateExceptionIfPossible(RuntimeException e) {
		if (e instanceof PersistenceException) {
			if (e.getCause() instanceof PersistenceException) {
				e = (PersistenceException) e.getCause();
			}
			if (e.getCause() instanceof SQLException) {
				return translate(e.getMessage() + "\n", null,
						(SQLException) e.getCause());
			}
			return new DataAccessException(e);
		}
		return null;
	}

	@Override
	public DataAccessException translate(String task, String sql,
			SQLException ex) {
		return new DataAccessException(task, sql, ex);
	}

}
