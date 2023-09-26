package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.exceptions.DatabaseException;
import com.cerebros.models.Order;
import com.cerebros.models.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository("tradesDaoImpl")
public class TradesDaoImpl implements TradesDao{
    @Autowired
    private TradesMapper mapper;
    public TradesDaoImpl() {

    }

    @Override
    public List<Trade> getTrades(String clientId) throws SQLException, ClientNotFoundException {
        List<Trade> Trades= mapper.getTrades(clientId);
        if(Trades.isEmpty()){
            throw new DatabaseException("Client Invalid or no Trade history");
        }
        return  Trades;

    }
    @Override
    public void addTrade(Trade trade, String clientId) {
        try {
            mapper.insertOrder(trade.getOrder());
            mapper.insertTrade(trade);
        } catch (Exception e) {
            throw new DatabaseException("Failed to add trade", e);
        }
    }

}
