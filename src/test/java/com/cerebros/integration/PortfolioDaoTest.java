package com.cerebros.integration;

import com.cerebros.models.Portfolio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PortfolioDaoTest {
    private SimpleDataSource dataSource;
    private PortfolioDaoImpl portfolioDao;
    @Test
    void smokeTest() {
        Assertions.assertNotNull(portfolioDao);
    }

    @BeforeEach
    void setUp() {
        dataSource= new SimpleDataSource();
        portfolioDao= new PortfolioDaoImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        dataSource.shutdown();
    }

    @Test
    void testGetPortfolio() throws SQLException {
        List<Portfolio> portfolioList= portfolioDao.getPortfolio();
        assertFalse(portfolioList.isEmpty());
    }



}
