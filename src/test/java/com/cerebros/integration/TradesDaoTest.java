package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Order;
import com.cerebros.models.Trade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TradesDaoTest {
    private SimpleDataSource dataSource;
    private TradesDaoImpl tradesDao;
    TransactionManager transactionManager;

    private DbTestUtils dbTestUtils;
    private Connection connection;
    private JdbcTemplate jdbcTemplate;

    private boolean codeExecuted = false;
    @AfterEach
    void tearDown() {
//        assertTrue(codeExecuted, "The code was not executed.");

        transactionManager.rollbackTransaction();
        dataSource.shutdown();
    }

    @BeforeEach
    void setUp() throws SQLException {
        dataSource= new SimpleDataSource();
        tradesDao= new TradesDaoImpl(dataSource);
        connection = dataSource.getConnection();

        transactionManager = new TransactionManager(dataSource);
        transactionManager.startTransaction();
//        connection.setAutoCommit(false);
        dbTestUtils = new DbTestUtils(connection);
        jdbcTemplate = dbTestUtils.initJdbcTemplate();
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



    @Test
    void testInsertIntoTrades() throws SQLException {
        int oldSize= JdbcTestUtils.countRowsInTable(jdbcTemplate,"Cerebros_Trades");
        String orderId = "BUY_ORDER_Q123_11";
        String clientId = "YOUR_CLIENTID";
        String instrumentId = "Q123";
        String direction = "B";
        BigDecimal quantity = new BigDecimal("100");
        BigDecimal targetPrice = new BigDecimal("104.75");
        Date placedTimestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
            placedTimestamp = dateFormat.parse("21-AUG-19");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Order order = new Order(orderId, quantity, targetPrice, direction, clientId, instrumentId, placedTimestamp);

        // Create a dummy Trade
        Trade trade = new Trade("11", quantity, targetPrice, direction, targetPrice.multiply(quantity).negate(), clientId, instrumentId, order, placedTimestamp);
        tradesDao.addTrade(trade, clientId);
        assertEquals(oldSize+1, JdbcTestUtils.countRowsInTable(jdbcTemplate,"Cerebros_Trades"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"Cerebros_Trades", "tradeId=11"));
        codeExecuted = true;

    }
    @Test
    void negativeTestInsertIntoTradesInstrumentInvalid(){
        String orderId = "BUY_ORDER_Q123_11";
        String clientId = "YOUR_CLIENTID";
        String instrumentId = "Invalid";
        String direction = "B";
        BigDecimal quantity = new BigDecimal("100");
        BigDecimal targetPrice = new BigDecimal("104.75");
        Date placedTimestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
            placedTimestamp = dateFormat.parse("21-AUG-19");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Order order = new Order(orderId, quantity, targetPrice, direction, clientId, instrumentId, placedTimestamp);

        // Create a dummy Trade
        Trade trade = new Trade("11", quantity, targetPrice, direction, targetPrice.multiply(quantity).negate(), clientId, instrumentId, order, placedTimestamp);
        assertThrows(DatabaseException.class,()->{
            tradesDao.addTrade(trade, clientId);
        });
    }
    @Test
    void negativeTestInsertIntoTradesClientInvalid(){
        String orderId = "BUY_ORDER_Q123_11";
        String clientId = "YOUR_CLIENTID_INVALID";
        String instrumentId = "Q123";
        String direction = "B";
        BigDecimal quantity = new BigDecimal("100");
        BigDecimal targetPrice = new BigDecimal("104.75");
        Date placedTimestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
            placedTimestamp = dateFormat.parse("21-AUG-19");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Order order = new Order(orderId, quantity, targetPrice, direction, clientId, instrumentId, placedTimestamp);

        // Create a dummy Trade
        Trade trade = new Trade("11", quantity, targetPrice, direction, targetPrice.multiply(quantity).negate(), clientId, instrumentId, order, placedTimestamp);
        assertThrows(DatabaseException.class,()->{
            tradesDao.addTrade(trade, clientId);
        });
    }
    @Test
    void testaddTradeWIthNullOrder(){
        String orderId = "BUY_ORDER_Q123_11";
        String clientId = "YOUR_CLIENTID";
        String instrumentId = "Q123";
        String direction = "B";
        BigDecimal quantity = new BigDecimal("100");
        BigDecimal targetPrice = new BigDecimal("104.75");
        Date placedTimestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
            placedTimestamp = dateFormat.parse("21-AUG-19");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create a dummy Trade
        Trade trade = new Trade("11", quantity, targetPrice, direction, targetPrice.multiply(quantity).negate(), clientId, instrumentId,null, placedTimestamp);
        assertThrows(NullPointerException.class,()->{
            tradesDao.addTrade(trade, clientId);
        });

    }



}
