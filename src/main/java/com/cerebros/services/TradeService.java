package com.cerebros.services;

import com.cerebros.exceptions.DatabaseException;
import com.cerebros.integration.doa.TradesDao;
import com.cerebros.models.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//
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
