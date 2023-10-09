package com.cerebros.services;

import com.cerebros.exceptions.DatabaseException;
import com.cerebros.integration.doa.TradesDao;
import com.cerebros.models.Preferences;
import com.cerebros.models.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    private final TradesDao tradesDao;

    @Autowired
    public TradeService(TradesDao tradesDao) {
        this.tradesDao = tradesDao;
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
    public List<Trade> getTopBuyAndSellTrades(Preferences preferences, String clientId) {
        List<Trade> allTrades = tradesDao.getTrades(clientId); 
        return calculateTopTradesBasedOnPreferences(allTrades, preferences);
        }
        private List<Trade> calculateTopTradesBasedOnPreferences(List<Trade> trades, Preferences preferences) {
            String riskTolerance = preferences.getRisk();
            String timeHorizon = preferences.getTime();
            String incomeBracket = preferences.getIncome();

            return trades.stream()
                    .filter(trade -> isTradeSuitable(trade, riskTolerance, timeHorizon, incomeBracket))
                    .sorted(Comparator.comparing(Trade::getExecutionPrice))
                    .limit(5) // Get the top 5 trades
                    .collect(Collectors.toList());
        }

        private boolean isTradeSuitable(Trade trade, String riskTolerance, String timeHorizon, String incomeBracket) {
        	 BigDecimal bidPrice = trade.getExecutionPrice();
             if ("HIGH".equalsIgnoreCase(riskTolerance) &&
                     "HIGH".equalsIgnoreCase(timeHorizon) &&
                     "HIGH".equalsIgnoreCase(incomeBracket)) {
                 // High bid price stock
                 return bidPrice.compareTo(BigDecimal.valueOf(100.0)) > 0;
             } else if ("LOW".equalsIgnoreCase(riskTolerance) &&
                     "HIGH".equalsIgnoreCase(timeHorizon)) {
                 // Lower bid price stock
                 return bidPrice.compareTo(BigDecimal.valueOf(50.0)) <= 0;
             }
             return false;
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
