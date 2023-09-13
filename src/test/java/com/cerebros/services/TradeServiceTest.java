package com.cerebros.services;

import com.cerebros.exceptions.NoTradeHistoryFoundException;
import com.cerebros.models.Trade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TradeServiceTest {

	private TradeService tradeService;

	@BeforeEach
	void setUp() throws Exception {
		tradeService= new TradeService();
		tradeService.setupTradeHistory();
	}

	@AfterEach
	void tearDown() throws Exception {

	}

	@Test
	void test() {assertNotNull(tradeService);}

	@Test
	void testInvalidClient() throws NoTradeHistoryFoundException {
		assertThrows(NoTradeHistoryFoundException.class,()->{
			tradeService.getClientTradeHistory("invalidId", Comparator.comparing(Trade::getTradeId));
		});
	}
	@Test
	void testNoTradesForClient() throws NoTradeHistoryFoundException {
		assertThrows(NoTradeHistoryFoundException.class,()->{
			tradeService.getClientTradeHistory("client", Comparator.comparing(Trade::getTradeId));
		});
	}
	@Test
	void testTradesCorrectlySorted() throws NoTradeHistoryFoundException {
		List<Trade> trades=tradeService.getClientTradeHistory("bhavesh@gmail.com", Comparator.comparing(Trade::getTradeId).reversed());
		assertTrue(trades.get(0).getTradeId().compareTo(trades.get(1).getTradeId())>0);
	}

	@Test
	void testTradeslessThan100Returned() throws NoTradeHistoryFoundException {
		List<Trade> trades=tradeService.getClientTradeHistory("bhavesh@gmail.com", Comparator.comparing(Trade::getTradeId));
		assertEquals(35, trades.size());
	}
	@Test
	void testTradesExactly100Returned() throws NoTradeHistoryFoundException {
		List<Trade> trades=tradeService.getClientTradeHistory("john.doe@gmail.com", Comparator.comparing(Trade::getTradeId));
		assertEquals(100, trades.size());
	}
	@Test
	void testTradesMoreThan100Returned() throws NoTradeHistoryFoundException {
		List<Trade> trades=tradeService.getClientTradeHistory("jane.doe@gmail.com", Comparator.comparing(Trade::getTradeId));
		assertEquals(100, trades.size());
	}





}
