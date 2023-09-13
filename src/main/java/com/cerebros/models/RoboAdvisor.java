package com.cerebros.models;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.cerebros.services.TradeService;
import com.cerebros.exceptions.NoTradeHistoryFoundException;
import com.cerebros.models.Trade;
import java.util.ArrayList;
import java.util.Collections;

public class RoboAdvisor {
	 private TradeService tradeService;

	    public RoboAdvisor(TradeService tradeService) {
	        this.tradeService = tradeService;
	    }

	    public List<Trade> recommendTrades(String clientId, Preferences preferences) throws NoTradeHistoryFoundException {

	        Comparator<Trade> sortCriteria = Comparator.comparing(Trade::getExecutionPrice);
	        List<Trade> clientTradeHistory = tradeService.getClientTradeHistory(clientId, sortCriteria);


	        List<Trade> buyTrades = new ArrayList<>();
	        List<Trade> sellTrades = new ArrayList<>();

	        for (Trade trade : clientTradeHistory) {
	            if ("B".equals(trade.getDirection())) {
	                buyTrades.add(trade);
	            } else if ("S".equals(trade.getDirection())) {
	                sellTrades.add(trade);
	            }
	        }
	        Collections.sort(buyTrades, sortCriteria.reversed());
	        Collections.sort(sellTrades, sortCriteria);

	        List<Trade> recommendedTrades = new ArrayList<>();
	        recommendedTrades.addAll(buyTrades.subList(0, Math.min(5, buyTrades.size())));
	        recommendedTrades.addAll(sellTrades.subList(0, Math.min(5, sellTrades.size())));

	        return recommendedTrades;
	    }
	    
}