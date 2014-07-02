package com.zlb.core.dao.batis;

import java.sql.SQLException;

public interface PersistenceExceptionTranslator {
	DataAccessException translateExceptionIfPossible(RuntimeException ex);
	DataAccessException translate(String task, String sql, SQLException ex);
}
