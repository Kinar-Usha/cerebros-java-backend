package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Trade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradesDaoTest {
    private SimpleDataSource dataSource;
    private TradesDaoImpl tradesDao;
    @AfterEach
    void tearDown() {
    dataSource.shutdown();
    }

    @BeforeEach
    void setUp() {
        dataSource= new SimpleDataSource();
        tradesDao= new TradesDaoImpl(dataSource);
    }

    @Test
    void smokeTest() {
        Assertions.assertNotNull(tradesDao);
    }

    @Test
    void testGetTradeHistory() throws ClientNotFoundException, SQLException {
        String testClientId= "YOUR_CLIENTID";
        List<Trade> trades= tradesDao.getTrades(testClientId);
        Assertions.assertFalse(trades.isEmpty());
        assertTrue(trades.get(0).getTradeId().compareTo( trades.get(1).getTradeId())<0);

    }
    @Test
    void testClientHasNoTradeHistory(){
        String testClientId= "YOUR_CLIENTID_WITH_NO_TRADES";
        Assertions.assertThrows(ClientNotFoundException.class,()->{
            tradesDao.getTrades(testClientId);
        });
    }
    @Test
    void testTradeHistoryLessThan100() throws ClientNotFoundException, SQLException {
        String testClientId= "YOUR_CLIENTID";
        List<Trade> trades= tradesDao.getTrades(testClientId);
        assertTrue(trades.size()<=100);
    }

}
