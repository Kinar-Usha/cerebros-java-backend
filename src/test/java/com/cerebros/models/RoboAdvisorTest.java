//package com.cerebros.models;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import com.cerebros.exceptions.NoTradeHistoryFoundException;
//import com.cerebros.services.TradeService;
//
//public class RoboAdvisorTest {
//	@Mock
//    private TradeService tradeService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testRecommendTrades() throws NoTradeHistoryFoundException {
//        Preferences clientPreferences = new Preferences("Invest", "Low", "Long-term", "High");
//
//        Trade trade1 = new Trade("trade1", new BigDecimal("10"), new BigDecimal("100"), "B",
//                new BigDecimal("1000"), "client1", "instrument1", new Order("order1", new BigDecimal("10"),
//                new BigDecimal("100"), "B", "client1", "instrument1"));
//
//        Trade trade2 = new Trade("trade2", new BigDecimal("15"), new BigDecimal("90"), "B",
//                new BigDecimal("1350"), "client1", "instrument2", new Order("order2", new BigDecimal("15"),
//                new BigDecimal("90"), "B", "client1", "instrument2"));
//
//        List<Trade> clientTradeHistory = Arrays.asList(trade1, trade2);
//        when(tradeService.getClientTradeHistory(Mockito.anyString(), Mockito.any()))
//                .thenReturn(clientTradeHistory);
//        RoboAdvisor roboAdvisor = new RoboAdvisor(tradeService);
//        List<Trade> recommendedTrades = roboAdvisor.recommendTrades("client1", clientPreferences);
//        assertEquals(2, recommendedTrades.size());
//        assertEquals(trade2, recommendedTrades.get(1));
//        assertEquals(trade1, recommendedTrades.get(0));
//    }
//
//    @Test
//    void testRecommendTradesNoTradeHistory() throws NoTradeHistoryFoundException {
//        Preferences clientPreferences = new Preferences("Invest", "Low", "Long-term", "High");
//        List<Trade> emptyClientTradeHistory = Arrays.asList();
//        when(tradeService.getClientTradeHistory(Mockito.anyString(), Mockito.any()))
//                .thenThrow(new NoTradeHistoryFoundException("No trade history found"));
//        RoboAdvisor roboAdvisor = new RoboAdvisor(tradeService);
//        assertThrows(NoTradeHistoryFoundException.class, () ->
//                roboAdvisor.recommendTrades("client2", clientPreferences));
//    }
//}
