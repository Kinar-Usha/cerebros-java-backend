package com.cerebros.integration;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private DataSource dataSource;

    public TransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void startTransaction() throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException("Unable to begin a transaction", e);
        }

    }

    public void rollbackTransaction() {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            connection.rollback();
            connection.setAutoCommit(true); // restore default behavior in case other code requires it
        } catch (SQLException e) {
            throw new DatabaseException("Unable to rollback transaction", e);
        }
    }

    public void commitTransaction() {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            connection.commit();
            connection.setAutoCommit(true); // restore default behavior in case other code requires it
        } catch (SQLException e) {
            throw new DatabaseException("Unable to rollback transaction", e);
        }
    }
}
