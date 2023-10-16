package com.cerebros.services;
import org.slf4j.Logger;
import com.cerebros.exceptions.DatabaseException;
import com.cerebros.integration.doa.TradesDao;
import com.cerebros.models.Instrument;
import com.cerebros.models.Preferences;
import com.cerebros.models.Price;
import com.cerebros.models.Trade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.stream.Collectors;

@Service
public class TradeService {

    private final TradesDao tradesDao;
    @Value("${fmts.api.url}")
    private String fmtsApiUrl="http://localhost:3000";
    private final RestTemplate restTemplate;
    
    @Autowired
    private Logger logger;

    @Autowired
    public TradeService(TradesDao tradesDao, RestTemplate restTemplate) {
        this.tradesDao = tradesDao;
        this.restTemplate = restTemplate;
    }

    public List<Trade> getClientTradeHistory(String clientId) throws DatabaseException {
        return tradesDao.getTrades(clientId).stream()
                .sorted(Comparator.comparing(Trade::getTradeId))
                .limit(100)
                .collect(Collectors.toList());
    }

    public int updateClientTradeHistory(Trade trade) {
        if(trade!=null){
            return tradesDao.addTrade(trade, trade.getClientid());
        }
        else{
            throw new IllegalArgumentException("Trade is empty");
        }

    }
    public List<Price> getTopBuyAndSellTrades(Preferences preferences, String clientId) {
        String instrumentsApiUrl = fmtsApiUrl + "/fmts/trades/instruments";
        String pricesApiUrl = fmtsApiUrl + "/fmts/trades/prices";

        // Fetch instruments data
        String instrumentsJsonResponse = restTemplate.getForObject(instrumentsApiUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        // Fetch prices data
        String pricesJsonResponse = restTemplate.getForObject(pricesApiUrl, String.class);
        List<Price> prices;
        try {
            prices = objectMapper.readValue(pricesJsonResponse, new TypeReference<List<Price>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
        }

        return calculateTopTradesBasedOnPreferences(prices, preferences);
    }

    private List<Price> calculateTopTradesBasedOnPreferences(List<Price> prices, Preferences preferences) {
        BigDecimal riskToleranceValue = getPreferenceValue(preferences.getRisk());
        BigDecimal timeHorizonValue = getPreferenceValue(preferences.getTime());
        BigDecimal incomeBracketValue = getPreferenceValue(preferences.getIncome());
        logger.debug("{}", preferences.getRisk());
        return prices.stream()
                .map(price -> calculateTradeSuitability(price, riskToleranceValue, timeHorizonValue, incomeBracketValue))
                .sorted(Comparator.comparing(Price::getSuitabilityScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private BigDecimal getPreferenceValue(String preference) {
        logger.debug("{}", preference);
        switch (preference.toUpperCase()) {
            case "LOW":
                return BigDecimal.valueOf(0.2);
            case "MEDIUM":
                return BigDecimal.valueOf(0.5);
            case "HIGH":
                return BigDecimal.valueOf(0.8);
            default:
                return BigDecimal.valueOf(0.5);
        }
    }

    private Price calculateTradeSuitability(Price price, BigDecimal riskTolerance, BigDecimal timeHorizon, BigDecimal incomeBracket) {
        BigDecimal randomFactor = BigDecimal.valueOf(new Random().nextDouble());
        BigDecimal suitabilityScore = riskTolerance.multiply(randomFactor)
                .multiply(timeHorizon)
                .multiply(incomeBracket);

        if (riskTolerance.compareTo(BigDecimal.valueOf(0.2)) == 0) {
            // Low risk preference, suggest bonds and cash deposits
            if (price.getInstrument().getCategoryId().equalsIgnoreCase("Bonds") || price.getInstrument().getCategoryId().equalsIgnoreCase("Cash")) {
                suitabilityScore = suitabilityScore.multiply(BigDecimal.valueOf(1.2));
            }
            
        } else if (riskTolerance.compareTo(BigDecimal.valueOf(0.8)) == 0) {
            // High risk preference, suggest stocks with higher minimum quantities and higher prices
            if (price.getInstrument().getCategoryId().equalsIgnoreCase("Stocks")) {
                suitabilityScore = suitabilityScore.multiply(BigDecimal.valueOf(1.5));
            }
        }

        if (incomeBracket.compareTo(BigDecimal.valueOf(0.8)) == 0) {
            // High income bracket, suggest stocks with higher minimum quantities and higher prices
            if (price.getInstrument().getCategoryId().equalsIgnoreCase("Stocks")) {
                suitabilityScore = suitabilityScore.multiply(BigDecimal.valueOf(1.5));
            }
        }

        price.setSuitabilityScore(suitabilityScore);
        return price;
    }

//public Trade executeSellTrade(Order order) throws Exception{
//
//
//    	if(order==null) {
//    		throw new NullPointerException("Order cannot be null");
//    	}
////    	PortfolioService portfolioService = new PortfolioService(portfolioDao);
//    	ClientService clientService = new ClientService();
//
////    	Price price= mockfmts.getPrice().stream().
////                filter(trade -> trade.getInstrument().equals(clientId))
//    	List<Portfolio> portfolio = portfolioService.getPortfolio(clientService.getClient(order.getClientId()).getPerson().getEmail());
//    	for(Portfolio p : portfolio) {
//    		if(Objects.equals(order.getInsturmentId(), p.getInstrumentId())) {
//    			if(order.getQuantity().compareTo( p.getHoldings())<=0) {
//    				Trade newTrade= new Trade("tradeid_",new BigDecimal("10"), new BigDecimal("100.0"),"B",new BigDecimal("100.0"),"clientID","instrument", order);
//    				return newTrade;
//    			}
//    			else {
//    				throw new Exception("Quantity is greater than the holdings");
//    			}
//
//    		}
//    		else {
//    			throw new Exception("Order not found");
//    		}
//    	}
//
//    	throw new IllegalStateException("Sell trade cannot be executed for the given order.");
//
//
//    }
//
//

}
