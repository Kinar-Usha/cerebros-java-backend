package com.cerebros.integration.mapper;

import com.cerebros.models.Order;
import com.cerebros.models.Trade;

import java.util.List;

public interface TradesMapper {
    List<Trade> getTrades(String clientId);
    void insertOrder(Order order);
    int insertTrade(Trade trade);
}
