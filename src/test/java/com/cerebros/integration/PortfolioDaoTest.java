package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Portfolio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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




}
