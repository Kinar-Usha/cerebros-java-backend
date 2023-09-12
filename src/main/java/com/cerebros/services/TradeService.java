package com.cerebros.services;

import com.cerebros.models.Order;
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
        for(String clientId:new String[]{"client1", "client2", "client3"}){
            for(int i=0;i<35;i++){
                Order order= new Order("orderId_"+clientId+i,new BigDecimal("10"),new BigDecimal("100.0"), "B", clientId,"instrument"+i);
                Trade trade= new Trade("tradeid_"+clientId+i,new BigDecimal("10"), new BigDecimal("100.0"),"B",new BigDecimal("100.0"),clientId,"instrument"+i, order);
                allTrades.add(trade);
            }
        }

    }
    public  List<Trade> getClientTradeHistory(String clientId, Comparator<Trade> sortCriteria){
        return allTrades.stream().
                filter(trade -> trade.getClientid().equals(clientId))
                .sorted(sortCriteria)
                .limit(100)
                .collect(Collectors.toList());
    }
}
