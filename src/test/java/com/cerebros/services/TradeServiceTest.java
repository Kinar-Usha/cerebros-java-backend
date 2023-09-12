package com.cerebros.services;

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
	}

	@AfterEach
	void tearDown() throws Exception {

	}

	@Test
	void test() {assertNotNull(tradeService);}

	@Test
	void testInvalidClient(){
		assertTrue(tradeService.getClientTradeHistory("invalidId", Comparator.comparing(Trade::getTradeId)).isEmpty());
	}
	@Test
	void testNoTradesForClient(){
		assertTrue(tradeService.getClientTradeHistory("client4", Comparator.comparing(Trade::getTradeId)).isEmpty());
	}
	@Test
	void testTradesCorrectlySorted(){
		List<Trade> trades=tradeService.getClientTradeHistory("client1", Comparator.comparing(Trade::getTradeId).reversed());
		assertTrue(trades.get(0).getTradeId().compareTo(trades.get(1).getTradeId())>0);
	}

	@Test
	void testTradeslessThan100Returned(){
		List<Trade> trades=tradeService.getClientTradeHistory("client1", Comparator.comparing(Trade::getTradeId));
		System.out.println(trades.size());
		assertEquals(35, trades.size());
	}




}
