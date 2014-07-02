package com.zlb.core.dao.batis;

import java.sql.SQLException;

public class DataAccessException extends RuntimeException {
	private static final long serialVersionUID = -6918254608293532142L;

	public DataAccessException(String task, String sql, SQLException ex) {
		super(task + " --  " + sql, ex);
	}

	public DataAccessException(RuntimeException e) {
		super(e);
	}

}
