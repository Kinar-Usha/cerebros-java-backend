package com.cerebros.integration;
/*
 * DbTestUtils.java - utility functions for database integration tests.
 */

import java.sql.Connection;

import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;


public class DbTestUtils
{

    private static final String SQL_SCRIPT = "sql/emp_populate.sql";

	private Connection connection;

	public DbTestUtils(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Re-run the database initialization script.
	 * Only pure SQL scripts allowed - PL/SQL statements must be removed
	 *
	 * Alternatively, for simple schemas, we could drop all rows in the required
	 * tables, then insert test data: Statement stmt = connection.createStatement();
	 * stmt.executeUpdate("delete from emp"); stmt.executeUpdate("insert into emp
	 * (empno,ename,job,...) values (7369,'SMITH','CLERK',...)"); ... stmt.close();
	 */
	public void initDb() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

		populator.setContinueOnError(true);
		populator.addScript(new FileSystemResource(SQL_SCRIPT));
		populator.populate(connection);
	}

	public JdbcTemplate initJdbcTemplate() {
		return new JdbcTemplate(new SingleConnectionDataSource(connection, true));
	}

}
