package com.cerebros.integration;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private DataSource dataSource;
    private Connection connection;

    public TransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void startTransaction() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

        } else {
            throw new DatabaseException("Transaction already in progress.");
        }
    }

    public void rollbackTransaction() {
        if (connection != null) {
            try {
                connection.rollback();
                System.out.println("in rollback");
            } catch (SQLException e) {
                throw new DatabaseException("Unable to rollback transaction", e);
            } finally {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    throw new DatabaseException("Unable to close connection", e);
                }
                connection = null;
            }
        } else {
            throw new DatabaseException("No transaction in progress.");
        }
    }

    public void commitTransaction() {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                throw new DatabaseException("Unable to commit transaction", e);
            } finally {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    throw new DatabaseException("Unable to close connection", e);
                }
                connection = null;
            }
        } else {
            throw new DatabaseException("No transaction in progress.");
        }
    }
}