package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.exceptions.DatabaseException;
import com.cerebros.integration.doa.impl.TradesDaoImpl;
import com.cerebros.models.Order;
import com.cerebros.models.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TradesDaoTest {
    @Autowired
    private TradesDaoImpl tradesDao;



    @Test
    void smokeTest() {
        Assertions.assertNotNull(tradesDao);
    }

    @Test
    void testGetTradeHistory() {
        String testClientId= "YOUR_CLIENTID";
        List<Trade> trades= tradesDao.getTrades(testClientId);
        Assertions.assertFalse(trades.isEmpty());
        assertTrue(trades.get(0).getTradeId().compareTo( trades.get(1).getTradeId())<0);

    }
    @Test
    void testClientHasNoTradeHistory(){
        String testClientId= "YOUR_CLIENTID_WITH_NO_TRADES";
        Assertions.assertThrows(DatabaseException.class,()->{
            tradesDao.getTrades(testClientId);
        });
    }
    @Test
    void testTradeHistoryLessThan100() throws ClientNotFoundException {
        String testClientId= "YOUR_CLIENTID";
        List<Trade> trades= tradesDao.getTrades(testClientId);
        assertTrue(trades.size()<=100);
    }



    @Test
    void testInsertIntoTrades()   {
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
        Trade trade = new Trade("11", quantity, targetPrice, direction, targetPrice.multiply(quantity).negate(), clientId, instrumentId, order,"description", placedTimestamp);
        tradesDao.addTrade(trade, clientId);

    }
    @Test
    void testInsertSellIntoTrades()   {
        String orderId = "BUY_ORDER_Q123_11";
        String clientId = "YOUR_CLIENTID";
        String instrumentId = "Q123";
        String direction = "S";
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
        Trade trade = new Trade("11", quantity, targetPrice, direction, targetPrice.multiply(quantity).negate(), clientId, instrumentId, order,"description", placedTimestamp);
        int rows = tradesDao.addTrade(trade, clientId);
        assertEquals(1, rows);

    }
//    @Test
//    void negativeTestInsertIntoTradesInstrumentInvalid(){
//        String orderId = "BUY_ORDER_Q123_11";
//        String clientId = "YOUR_CLIENTID";
//        String instrumentId = "Invalid";
//        String direction = "B";
//        BigDecimal quantity = new BigDecimal("100");
//        BigDecimal targetPrice = new BigDecimal("104.75");
//        Date placedTimestamp = null;
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
//            placedTimestamp = dateFormat.parse("21-AUG-19");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Order order = new Order(orderId, quantity, targetPrice, direction, clientId, instrumentId, placedTimestamp);
//
//        // Create a dummy Trade
//        Trade trade = new Trade("11", quantity, targetPrice, direction, targetPrice.multiply(quantity).negate(), clientId, instrumentId, order, placedTimestamp);
//        assertThrows(DatabaseException.class,()->{
//            tradesDao.addTrade(trade, clientId);
//        });
//    }
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
//    @Test
//    void testaddTradeWIthNullOrder(){
//        String orderId = "BUY_ORDER_Q123_11";
//        String clientId = "YOUR_CLIENTID";
//        String instrumentId = "Q123";
//        String direction = "B";
//        BigDecimal quantity = new BigDecimal("100");
//        BigDecimal targetPrice = new BigDecimal("104.75");
//        Date placedTimestamp = null;
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
//            placedTimestamp = dateFormat.parse("21-AUG-19");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        // Create a dummy Trade
//        Trade trade = new Trade("11", quantity, targetPrice, direction, targetPrice.multiply(quantity).negate(), clientId, instrumentId,null, placedTimestamp);
//        assertThrows(DatabaseException.class,()->{
//            tradesDao.addTrade(trade, clientId);
//        });
//
//    }



}
