package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Portfolio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioDaoTest {
    private JdbcTemplate jdbcTemplate;
    private SimpleDataSource dataSource;
    TransactionManager transactionManager;

    private DbTestUtils dbTestUtils;
    private Connection connection;
	private PortfolioDaoImpl portfolioDao;

	@BeforeEach
	void setUp() throws SQLException {
        dataSource= new SimpleDataSource();
        connection = dataSource.getConnection();
        transactionManager = new TransactionManager(dataSource);
        transactionManager.startTransaction();
        dbTestUtils = new DbTestUtils(connection);
        jdbcTemplate = dbTestUtils.initJdbcTemplate();
		portfolioDao = new PortfolioDaoImpl(dataSource);
	}

	@AfterEach
	void tearDown() {
        transactionManager.rollbackTransaction();
        dataSource.shutdown();
	}

    @Test
    void testGetPortfolio() throws SQLException, ClientNotFoundException {
        String testClientId= "YOUR_CLIENTID";
        List<Portfolio> portfolioList= portfolioDao.getPortfolio(testClientId);
        assertFalse(portfolioList.isEmpty());
        assertTrue(portfolioList.get(0).getHoldings().compareTo( portfolioList.get(1).getHoldings())>0);

    }
    @Test
    void testClientNotFoundGetPortfolio(){
        String testClientId= "YOUR_CLIENTID_WITH_NO_PORTFOLIO";
        assertThrows(ClientNotFoundException.class,()->{
            List<Portfolio> portfolioList= portfolioDao.getPortfolio(testClientId);

        });
    }
    @Test
    void testAddToPortfolio(){
        String testClientId= "YOUR_CLIENTID";
        Portfolio dummyPortfolio = new Portfolio("T67878", "Dummy Portfolio", "Category123", new BigDecimal("100"), new BigDecimal("50.00"));
        int oldSize= JdbcTestUtils.countRowsInTable(jdbcTemplate,"Cerebros_Portfolio");
        portfolioDao.addToPortfolio(dummyPortfolio, testClientId);
        assertEquals(oldSize+1, JdbcTestUtils.countRowsInTable(jdbcTemplate,"Cerebros_Portfolio"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"Cerebros_Portfolio", "instrumentId='T67878'"));
    }
    @Test
    void testAddInvalidInstrument(){
        String testClientId= "YOUR_CLIENTID";
        Portfolio dummyPortfolio = new Portfolio("123", "Dummy Portfolio", "Category123", new BigDecimal("100"), new BigDecimal("50.00"));
        assertThrows(DatabaseException.class,()->{
            portfolioDao.addToPortfolio(dummyPortfolio, testClientId);
        });
    }
    @Test
    void testAddInvalidClient(){
        String testClientId= "Invalid";
        Portfolio dummyPortfolio = new Portfolio("123", "Dummy Portfolio", "Category123", new BigDecimal("100"), new BigDecimal("50.00"));
        assertThrows(DatabaseException.class,()->{
            portfolioDao.addToPortfolio(dummyPortfolio, testClientId);
        });
    }


    @Test
    void testUpdatePortfolio(){
        String testClientId= "YOUR_CLIENTID";
        Portfolio dummyPortfolio = new Portfolio("Q123", "Dummy Portfolio", "Category123", new BigDecimal("190"), new BigDecimal("50.00"));
        assertDoesNotThrow(()->{
            portfolioDao.addToPortfolio(dummyPortfolio, testClientId);

        });

    }
    @Test
    void testNegativeUpdateInstrument(){
        String testClientId= "YOUR_CLIENTID";
        Portfolio dummyPortfolio = new Portfolio("kinar", "Dummy Portfolio", "Category123", new BigDecimal("190"), new BigDecimal("50.00"));
        assertThrows(DatabaseException.class, ()->{
            portfolioDao.addToPortfolio(dummyPortfolio, testClientId);

        });
    }


    @Test
    void testNegativeUpdateClient(){
        String testClientId= "Invalid";
        Portfolio dummyPortfolio = new Portfolio("Q123", "Dummy Portfolio", "Category123", new BigDecimal("190"), new BigDecimal("50.00"));
        assertThrows(DatabaseException.class, ()->{
            portfolioDao.addToPortfolio(dummyPortfolio, testClientId);

        });
    }





}
