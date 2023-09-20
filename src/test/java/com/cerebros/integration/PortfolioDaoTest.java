package com.cerebros.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cerebros.models.Portfolio;

public class PortfolioDaoTest {
	private SimpleDataSource dataSource;
	private PortfolioDaoImpl portfolioDao;

	@BeforeEach
	void setUp() {
		dataSource = new SimpleDataSource();
		portfolioDao = new PortfolioDaoImpl(dataSource);
	}

	@AfterEach
	void tearDown() {
		dataSource.shutdown();
	}

	@Test
	void smokeTest() {
		Assertions.assertNotNull(portfolioDao);
	}

	@Test
	void testGetPortfolio() throws SQLException {
		List<Portfolio> portfolioList = portfolioDao.getPortfolio();
		assertFalse(portfolioList.isEmpty());
	}

}
