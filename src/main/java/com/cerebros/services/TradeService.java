package com.cerebros.services;

import com.cerebros.exceptions.NoTradeHistoryFoundException;
import com.cerebros.models.Order;
import com.cerebros.models.Portfolio;
import com.cerebros.models.Trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TradeService {

    private List<Trade> allTrades;
    public TradeService(){
        allTrades= new ArrayList<>();


    }
    public void setupTradeHistory(){
        for(String clientId:new String[]{"bhavesh@gmail.com","john.doe@gmail.com", "jane.doe@gmail.com"}){
            for(int i=0;i<105;i++){
                if(clientId.equals("bhavesh@gmail.com") && i==35){
                    break;
                }
                if(clientId.equals("john.doe@gmail.com") && i==100){
                    break;
                }
                Order order= new Order("orderId_"+clientId+i,new BigDecimal("10"),new BigDecimal("100.0"), "B", clientId,"instrument"+i);
                Trade trade= new Trade("tradeid_"+clientId+i,new BigDecimal("10"), new BigDecimal("100.0"),"B",new BigDecimal("100.0"),clientId,"instrument"+i, order);
                allTrades.add(trade);
            }
        }

    }
    public  List<Trade> getClientTradeHistory(String clientId, Comparator<Trade> sortCriteria) throws NoTradeHistoryFoundException {
        List<Trade> clientTrades= allTrades.stream().
                filter(trade -> trade.getClientid().equals(clientId))
                .sorted(sortCriteria)
                .limit(100)
                .collect(Collectors.toList());
        if (clientTrades.isEmpty()) {
            throw new NoTradeHistoryFoundException("No trade history found for client ID: " + clientId);
        }
        return clientTrades;
    }
    public void updateClientTradeHistory(Trade trade){
        if(trade!=null){
            allTrades.add(trade);
        }
        else{
            throw new IllegalArgumentException("Trade is empty");
        }
    }


public Trade executeSellTrade(Order order) throws Exception{
    	if(order==null) {
    		throw new NullPointerException("Order cannot be null");
    	}
    	PortfolioService portfolioService = new PortfolioService();
    	ClientService clientService = new ClientService();
//    	Price price= mockfmts.getPrice().stream().
//                filter(trade -> trade.getInstrument().equals(clientId))
    	List<Portfolio> portfolio = portfolioService.getPortfolio(clientService.getClient(order.getClientId()).getPerson().getEmail());
    	for(Portfolio p : portfolio) {
    		if(order.getInsturmentId() == p.getInstrumentId() ) {
    			if(order.getQuantity().compareTo( p.getHoldings())<=0) {
    				Trade newTrade= new Trade("tradeid_",new BigDecimal("10"), new BigDecimal("100.0"),"B",new BigDecimal("100.0"),"clientID","instrument", order);
    				return newTrade;
    			}
    			else {
    				throw new Exception("Quantity is greater than the holdings");
    			}

    		}
    		else {
    			throw new Exception("Order not found");
    		}
    	}

    	throw new IllegalStateException("Sell trade cannot be executed for the given order.");


    }



}
