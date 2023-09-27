package com.cerebros.services;

import com.cerebros.exceptions.DatabaseException;
import com.cerebros.integration.doa.TradesDao;
import com.cerebros.models.Trade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeServiceTest {

	@Mock
	private TradesDao tradesDao;
	private TradeService tradeService;


	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		tradeService = new TradeService(tradesDao);
//		tradeService.setupTradeHistory();

	}

	@AfterEach
	void tearDown() {

	}

	@Test
	void test() {assertNotNull(tradeService);}

	@Test
	void testInvalidClient() {

		String clientId = "456";
		when(tradesDao.getTrades(clientId)).thenThrow(new DatabaseException("Client Invalid or no Trade history"));
		try {
			tradeService.getClientTradeHistory(clientId);

		} catch (DatabaseException e) {
			assertEquals("Client Invalid or no Trade history", e.getMessage());
		}
		assertThrows(DatabaseException.class, () -> {
			tradeService.getClientTradeHistory("456");
		});
	}
	@Test
	void testNoTradesForClient() {
		String clientId = "456";
		when(tradesDao.getTrades(clientId)).thenThrow(new DatabaseException("Client Invalid or no Trade history"));
		try {
			tradeService.getClientTradeHistory(clientId);

		} catch (DatabaseException e) {
			assertEquals("Client Invalid or no Trade history", e.getMessage());
		}
		assertThrows(DatabaseException.class, () -> {
			tradeService.getClientTradeHistory("456");
		});
	}
	@Test
	void testTradesCorrectlySorted() {
		String clientId = "YOUR_CLIENTID";
		List<Trade> expectedTrades = new ArrayList<>();
		expectedTrades.add(new Trade("1", new BigDecimal("10"), new BigDecimal("10"), "B", new BigDecimal("100.0"), "YOUR_CLIENTID", "instrument", null));
		expectedTrades.add(new Trade("2", new BigDecimal("10"), new BigDecimal("10"), "B", new BigDecimal("100.0"), "YOUR_CLIENTID", "instrument2", null));

		when(tradesDao.getTrades(clientId)).thenReturn(expectedTrades);

		List<Trade> actualTrades = tradeService.getClientTradeHistory(clientId);

		assertEquals(expectedTrades, actualTrades);

		verify(tradesDao, times(1)).getTrades(clientId);
		List<Trade> trades = tradeService.getClientTradeHistory("YOUR_CLIENTID");
		assertTrue(trades.get(0).getTradeId().compareTo(trades.get(1).getTradeId()) < 0);
	}

	@Test
	void testTradeslessThan100Returned() {
		String clientId = "YOUR_CLIENTID";
		List<Trade> expectedTrades = new ArrayList<>();
		expectedTrades.add(new Trade("1", new BigDecimal("10"), new BigDecimal("10"), "B", new BigDecimal("100.0"), "YOUR_CLIENTID", "instrument", null));
		expectedTrades.add(new Trade("2", new BigDecimal("10"), new BigDecimal("10"), "B", new BigDecimal("100.0"), "YOUR_CLIENTID", "instrument2", null));

		when(tradesDao.getTrades(clientId)).thenReturn(expectedTrades);

		List<Trade> actualTrades = tradeService.getClientTradeHistory(clientId);

		assertEquals(expectedTrades, actualTrades);

		verify(tradesDao, times(1)).getTrades(clientId);
		List<Trade> trades = tradeService.getClientTradeHistory("YOUR_CLIENTID");
		assertEquals(2, actualTrades.size());
	}
	@Test
	void testTradesExactly100Returned() {
		String clientId = "YOUR_CLIENTID";
		List<Trade> expectedTrades = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			expectedTrades.add(new Trade(String.valueOf(i), new BigDecimal("10"), new BigDecimal("10"), "B", new BigDecimal("100.0"), "YOUR_CLIENTID", "instrument", null));
		}

		when(tradesDao.getTrades(clientId)).thenReturn(expectedTrades);

		List<Trade> actualTrades = tradeService.getClientTradeHistory(clientId);


		assertEquals(expectedTrades.get(0), actualTrades.get(0));

		verify(tradesDao, times(1)).getTrades(clientId);
		List<Trade> trades = tradeService.getClientTradeHistory("YOUR_CLIENTID");
		assertEquals(100, actualTrades.size());
	}
	@Test
	void testTradesMoreThan100Returned() {
		String clientId = "YOUR_CLIENTID";
		List<Trade> expectedTrades = new ArrayList<>();
		for (int i = 0; i < 105; i++) {
			expectedTrades.add(new Trade(String.valueOf(i), new BigDecimal("10"), new BigDecimal("10"), "B", new BigDecimal("100.0"), "YOUR_CLIENTID", "instrument", null));
		}

		when(tradesDao.getTrades(clientId)).thenReturn(expectedTrades);

		List<Trade> actualTrades = tradeService.getClientTradeHistory(clientId);


		assertEquals(expectedTrades.get(0), actualTrades.get(0));

		verify(tradesDao, times(1)).getTrades(clientId);
		List<Trade> trades = tradeService.getClientTradeHistory("YOUR_CLIENTID");
		assertEquals(100, actualTrades.size());
	}

	@Test
	void testAddToTradesHistory() {
		Trade trade = new Trade("trade123", BigDecimal.ONE, BigDecimal.TEN, "B", BigDecimal.ZERO, "789", "12345", null);

		Mockito.when(tradesDao.addTrade(trade, trade.getClientid())).thenReturn(1);

		int result = tradeService.updateClientTradeHistory(trade);

		assertEquals(1, result);
		Mockito.verify(tradesDao).addTrade(trade, trade.getClientid());


	}
	@Test
	void testAddToTradesHistory_null() {

		assertThrows(IllegalArgumentException.class,()->{
			tradeService.updateClientTradeHistory(null);
		});
	}




}
