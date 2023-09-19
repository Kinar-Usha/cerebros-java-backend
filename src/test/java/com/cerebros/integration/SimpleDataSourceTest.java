package com.cerebros.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SimpleDataSourceTest defines unit tests for SimpleDataSource.
 *
 * @author ROI Instructor Team
 */
class SimpleDataSourceTest {
    private SimpleDataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new SimpleDataSource();
    }

    @AfterEach
    void tearDown() throws Exception {
        dataSource.shutdown();
    }

    @Test
    void testDataSourceCreated() {
        assertNotNull(dataSource);
    }

    @Test
    void testConnectionCreated() {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

}

